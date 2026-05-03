package com.example.electroniccemetery.dao;

import com.example.electroniccemetery.model.Grave;
import com.example.electroniccemetery.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GraveDao {

    public List<Grave> findByPlotId(int plotId) {
        List<Grave> list = new ArrayList<>();
        String sql = "SELECT g.ID_Grave, g.ID_Plot, g.Number_Grave, pb.Path_File " +
                "FROM Graves g " +
                "LEFT JOIN Photo_Burial pb ON pb.ID_Grave = g.ID_Grave " +
                "WHERE g.ID_Plot = ? ORDER BY g.Number_Grave";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, plotId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Grave(
                            rs.getInt("ID_Grave"),
                            rs.getInt("ID_Plot"),
                            rs.getInt("Number_Grave"),
                            rs.getString("Path_File")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Grave findById(int graveId) {
        String sql = "SELECT g.ID_Grave, g.ID_Plot, g.Number_Grave, pb.Path_File " +
                "FROM Graves g " +
                "LEFT JOIN Photo_Burial pb ON g.ID_Grave = pb.ID_Grave " +
                "WHERE g.ID_Grave = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, graveId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Grave(
                            rs.getInt("ID_Grave"),
                            rs.getInt("ID_Plot"),
                            rs.getInt("Number_Grave"),
                            rs.getString("Path_File")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
