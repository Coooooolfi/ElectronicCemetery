package com.example.electroniccemetery.dao;

import com.example.electroniccemetery.model.Plot;
import com.example.electroniccemetery.model.Section;
import com.example.electroniccemetery.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlotDao {

    public List<Plot> findBySectionId(int sectionId) {
        List<Plot> list = new ArrayList<>();
        String sql = "SELECT ID_Plot, ID_Section, Number_Plot " +
                "FROM Plots WHERE ID_Section = ? ORDER BY Number_Plot";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Plot(
                            rs.getInt("ID_Plot"),
                            rs.getInt("ID_Section"),
                            rs.getInt("Number_Plot")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

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

    public boolean existsBySectionAndNumber(int sectionId, int number) {
        String sql = "SELECT 1 FROM Plots WHERE ID_Section = ? AND Number_Plot = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionId);
            ps.setInt(2, number);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int insertPlot(int sectionId, int number) {
        String sql = "INSERT INTO Plots (ID_Section, Number_Plot) VALUES (?, ?) RETURNING ID_Plot";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionId);
            ps.setInt(2, number);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID_Plot");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean deletePlot(int plotId) {
        String sql = "DELETE FROM Plots WHERE ID_Plot = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, plotId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Plot findPlotById(int plotId) {
        String sql = "SELECT ID_Plot, ID_Section, Number_Plot FROM Plots WHERE ID_Plot = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, plotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Plot(
                            rs.getInt("ID_Plot"),
                            rs.getInt("ID_Section"),
                            rs.getInt("Number_Plot")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //обновление
    public boolean updatePlotNumber(int plotId, int newNumber) {
        String sql = "UPDATE Plots SET Number_Plot = ? WHERE ID_Plot = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newNumber);
            ps.setInt(2, plotId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Plot> findAll() {
        List<Plot> list = new ArrayList<>();
        String sql = "SELECT ID_Plot, ID_Section, Number_Plot FROM Plots ORDER BY ID_Section, Number_Plot";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Plot(
                        rs.getInt("ID_Plot"),
                        rs.getInt("ID_Section"),
                        rs.getInt("Number_Plot")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getPlotCountBySection(int sectionId) {
        String sql = "SELECT COUNT(*) FROM Plots WHERE ID_Section = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}