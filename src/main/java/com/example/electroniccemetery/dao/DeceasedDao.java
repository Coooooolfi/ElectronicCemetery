package com.example.electroniccemetery.dao;

import com.example.electroniccemetery.model.Deceased;
import com.example.electroniccemetery.util.DbConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeceasedDao {

    public List<Deceased> findByGraveId(int graveId) {
        List<Deceased> list = new ArrayList<>();
        String sql = "SELECT d.ID_Deceased, d.ID_Grave, d.LastName, d.FirstName, d.Othestvo, " +
                "d.Birth_Date, d.Death_Date, d.Description, pd.Path_File " +
                "FROM Deceased d " +
                "LEFT JOIN Photo_Deceased pd ON pd.ID_Deceased = d.ID_Deceased " +
                "WHERE d.ID_Grave = ? ORDER BY d.LastName, d.FirstName";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, graveId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate birth = rs.getDate("Birth_Date") != null
                            ? rs.getDate("Birth_Date").toLocalDate() : null;
                    LocalDate death = rs.getDate("Death_Date") != null
                            ? rs.getDate("Death_Date").toLocalDate() : null;
                    list.add(new Deceased(
                            rs.getInt("ID_Deceased"),
                            rs.getInt("ID_Grave"),
                            rs.getString("LastName"),
                            rs.getString("FirstName"),
                            rs.getString("Othestvo"),
                            birth,
                            death,
                            rs.getString("Description"),
                            rs.getString("Path_File")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Deceased> searchByFio(String query) {
        List<Deceased> list = new ArrayList<>();
        String sql = "SELECT d.ID_Deceased, d.ID_Grave, d.LastName, d.FirstName, d.Othestvo, " +
                "d.Birth_Date, d.Death_Date, d.Description, pd.Path_File " +
                "FROM Deceased d " +
                "LEFT JOIN Photo_Deceased pd ON pd.ID_Deceased = d.ID_Deceased " +
                "WHERE (d.LastName || ' ' || d.FirstName || ' ' || COALESCE(d.Othestvo, '')) ILIKE ? " +
                "ORDER BY d.LastName, d.FirstName";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate birth = rs.getDate("Birth_Date") != null
                            ? rs.getDate("Birth_Date").toLocalDate() : null;
                    LocalDate death = rs.getDate("Death_Date") != null
                            ? rs.getDate("Death_Date").toLocalDate() : null;
                    list.add(new Deceased(
                            rs.getInt("ID_Deceased"),
                            rs.getInt("ID_Grave"),
                            rs.getString("LastName"),
                            rs.getString("FirstName"),
                            rs.getString("Othestvo"),
                            birth,
                            death,
                            rs.getString("Description"),
                            rs.getString("Path_File")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public String getBurialPathByDeceasedId(int deceasedId) {
        String sql =
                "SELECT c.Name_Cemetery || ', сектор ' || s.Number_Section || " +
                        "       ', участок ' || p.Number_Plot || ', захоронение №' || g.Number_Grave AS path " +
                        "FROM Deceased d " +
                        "JOIN Graves g ON d.ID_Grave = g.ID_Grave " +
                        "JOIN Plots p ON g.ID_Plot = p.ID_Plot " +
                        "JOIN Cemeteries_Sections s ON p.ID_Section = s.ID_Section " +
                        "JOIN Cemeteries c ON s.ID_Cemetery = c.ID_Cemetery " +
                        "WHERE d.ID_Deceased = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deceasedId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("path");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // вывод списка усопших определенного кладбища
    public List<Deceased> findByCemeteryId(int cemeteryId) {
        List<Deceased> list = new ArrayList<>();
        String sql = "SELECT d.ID_Deceased, d.ID_Grave, d.LastName, d.FirstName, d.Othestvo, " +
                "d.Birth_Date, d.Death_Date, d.Description, pd.Path_File " +
                "FROM Deceased d " +
                "JOIN Graves g ON d.ID_Grave = g.ID_Grave " +
                "JOIN Plots p ON g.ID_Plot = p.ID_Plot " +
                "JOIN Cemeteries_Sections s ON p.ID_Section = s.ID_Section " +
                "LEFT JOIN Photo_Deceased pd ON d.ID_Deceased = pd.ID_Deceased " +
                "WHERE s.ID_Cemetery = ? " +
                "ORDER BY d.LastName, d.FirstName";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cemeteryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate birth = rs.getDate("Birth_Date") != null ? rs.getDate("Birth_Date").toLocalDate() : null;
                    LocalDate death = rs.getDate("Death_Date") != null ? rs.getDate("Death_Date").toLocalDate() : null;
                    Deceased d = new Deceased(
                            rs.getInt("ID_Deceased"),
                            rs.getInt("ID_Grave"),
                            rs.getString("LastName"),
                            rs.getString("FirstName"),
                            rs.getString("Othestvo"),
                            birth, death,
                            rs.getString("Description"),
                            rs.getString("Path_File")
                    );
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
