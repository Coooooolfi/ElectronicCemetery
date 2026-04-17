package com.example.electroniccemetery.dao;

import com.example.electroniccemetery.model.Section;
import com.example.electroniccemetery.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SectionDao {

    public List<Section> findByCemeteryId(int cemeteryId) {
        List<Section> list = new ArrayList<>();
        String sql = "SELECT ID_Section, ID_Cemetery, Number_Section " +
                "FROM Cemeteries_Sections WHERE ID_Cemetery = ? ORDER BY Number_Section";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cemeteryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Section(
                            rs.getInt("ID_Section"),
                            rs.getInt("ID_Cemetery"),
                            rs.getInt("Number_Section")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int insertSection(int cemeteryId, int number) {
        String sql = "INSERT INTO Cemeteries_Sections (ID_Cemetery, Number_Section) " +
                "VALUES (?, ?) RETURNING ID_Section";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cemeteryId);
            ps.setInt(2, number);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID_Section");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean existsByCemeteryAndNumber(int cemeteryId, int number) {
        String sql = "SELECT 1 FROM Cemeteries_Sections " +
                "WHERE ID_Cemetery = ? AND Number_Section = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cemeteryId);
            ps.setInt(2, number);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateSectionNumber(int sectionId, int newNumber) {
        String sql = "UPDATE Cemeteries_Sections SET Number_Section = ? WHERE ID_Section = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newNumber);
            ps.setInt(2, sectionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // - Сектор
    public boolean deleteSection(int sectionId) {
        String sql = "DELETE FROM Cemeteries_Sections WHERE ID_Section = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // поиск по ID (сектор)
    public Section findById(int sectionId) {
        String sql = "SELECT ID_Section, ID_Cemetery, Number_Section FROM Cemeteries_Sections WHERE ID_Section = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Section(
                            rs.getInt("ID_Section"),
                            rs.getInt("ID_Cemetery"),
                            rs.getInt("Number_Section")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
