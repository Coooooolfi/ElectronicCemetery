package com.example.electroniccemetery.dao;

import com.example.electroniccemetery.model.Cemetery;
import com.example.electroniccemetery.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CemeteryDao {

    public List<Cemetery> findAll() {
        List<Cemetery> list = new ArrayList<>();
        String sql = "SELECT ID_Cemetery, Name_Cemetery, Address FROM Cemeteries ORDER BY Name_Cemetery";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Cemetery(
                        rs.getInt("ID_Cemetery"),
                        rs.getString("Name_Cemetery"),
                        rs.getString("Address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insertCemetery(String name, String address) {
        String sql = "INSERT INTO Cemeteries (Name_Cemetery, Address) VALUES (?, ?) RETURNING ID_Cemetery";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, address);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID_Cemetery");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Сносим все, перестройка (Каскадное удаление кладбища)
    public boolean deleteCascadeCemetery(int cemeteryId) {
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Удаление фоток усопшего
                String deletePhotoDeceased = "DELETE FROM Photo_Deceased WHERE ID_Deceased IN " +
                        "(SELECT d.ID_Deceased FROM Deceased d " +
                        "JOIN Graves g ON d.ID_Grave = g.ID_Grave " +
                        "JOIN Plots p ON g.ID_Plot = p.ID_Plot " +
                        "JOIN Cemeteries_Sections s ON p.ID_Section = s.ID_Section " +
                        "WHERE s.ID_Cemetery = ?)";
                try (PreparedStatement ps = conn.prepareStatement(deletePhotoDeceased)) {
                    ps.setInt(1, cemeteryId);
                    ps.executeUpdate();
                }

                // Удаление усопших
                String deleteDeceased = "DELETE FROM Deceased WHERE ID_Grave IN " +
                        "(SELECT g.ID_Grave FROM Graves g " +
                        "JOIN Plots p ON g.ID_Plot = p.ID_Plot " +
                        "JOIN Cemeteries_Sections s ON p.ID_Section = s.ID_Section " +
                        "WHERE s.ID_Cemetery = ?)";
                try (PreparedStatement ps = conn.prepareStatement(deleteDeceased)) {
                    ps.setInt(1, cemeteryId);
                    ps.executeUpdate();
                }

                // Удаление фото захоронений
                String deletePhotoBurial = "DELETE FROM Photo_Burial WHERE ID_Grave IN " +
                        "(SELECT g.ID_Grave FROM Graves g " +
                        "JOIN Plots p ON g.ID_Plot = p.ID_Plot " +
                        "JOIN Cemeteries_Sections s ON p.ID_Section = s.ID_Section " +
                        "WHERE s.ID_Cemetery = ?)";
                try (PreparedStatement ps = conn.prepareStatement(deletePhotoBurial)) {
                    ps.setInt(1, cemeteryId);
                    ps.executeUpdate();
                }

                // Сами захоронения сносим
                String deleteGraves = "DELETE FROM Graves WHERE ID_Plot IN " +
                        "(SELECT p.ID_Plot FROM Plots p " +
                        "JOIN Cemeteries_Sections s ON p.ID_Section = s.ID_Section " +
                        "WHERE s.ID_Cemetery = ?)";
                try (PreparedStatement ps = conn.prepareStatement(deleteGraves)) {
                    ps.setInt(1, cemeteryId);
                    ps.executeUpdate();
                }

                // - Участки
                String deletePlots = "DELETE FROM Plots WHERE ID_Section IN " +
                        "(SELECT ID_Section FROM Cemeteries_Sections WHERE ID_Cemetery = ?)";
                try (PreparedStatement ps = conn.prepareStatement(deletePlots)) {
                    ps.setInt(1, cemeteryId);
                    ps.executeUpdate();
                }

                // - сектора
                String deleteSections = "DELETE FROM Cemeteries_Sections WHERE ID_Cemetery = ?";
                try (PreparedStatement ps = conn.prepareStatement(deleteSections)) {
                    ps.setInt(1, cemeteryId);
                    ps.executeUpdate();
                }

                // - связи с администраторами
                String updateUsers = "UPDATE Users SET ID_Cemetery = NULL WHERE ID_Cemetery = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateUsers)) {
                    ps.setInt(1, cemeteryId);
                    ps.executeUpdate();
                }

                // - кладбище (снесли) :(
                String deleteCemetery = "DELETE FROM Cemeteries WHERE ID_Cemetery = ?";
                try (PreparedStatement ps = conn.prepareStatement(deleteCemetery)) {
                    ps.setInt(1, cemeteryId);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateCemetery(int id, String name, String address) {
        String sql = "UPDATE Cemeteries SET Name_Cemetery = ?, Address = ? WHERE ID_Cemetery = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}