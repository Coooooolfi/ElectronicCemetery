package com.example.electroniccemetery.dao;

import com.example.electroniccemetery.model.Deceased;
import com.example.electroniccemetery.util.DbConnection;
import com.example.electroniccemetery.util.LoggerUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelativeDao {

    // Добавление записи о родственнике пользователя в профиль
    public boolean addRelative(int userId, int deceasedId) {
        String sql = "INSERT INTO User_Relatives (ID_User, ID_Deceased) VALUES (?, ?) " +
                "ON CONFLICT DO NOTHING";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, deceasedId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Удаление записи о родственнике пользователя из профиля
    public boolean removeRelative(int userId, int deceasedId) {
        String sql = "DELETE FROM User_Relatives WHERE ID_User = ? AND ID_Deceased = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, deceasedId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Проверка на повторное добавление
    public boolean isRelativeAdded(int userId, int deceasedId) {
        String sql = "SELECT 1 FROM User_Relatives WHERE ID_User = ? AND ID_Deceased = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, deceasedId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Получение списка родственников пользователя
    public List<Deceased> getRelativesByUserId(int userId) {
        List<Deceased> list = new ArrayList<>();
        String sql = "SELECT d.ID_Deceased, d.ID_Grave, d.LastName, d.FirstName, d.Othestvo, " +
                "d.Birth_Date, d.Death_Date, d.Description, pd.Path_File " +
                "FROM Deceased d " +
                "JOIN User_Relatives ur ON ur.ID_Deceased = d.ID_Deceased " +
                "LEFT JOIN Photo_Deceased pd ON pd.ID_Deceased = d.ID_Deceased " +
                "WHERE ur.ID_User = ? " +
                "ORDER BY d.LastName, d.FirstName";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
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
            LoggerUtil.error("SQL ошибка в CemeteryDao.findAll", e);
        }
        return list;
    }
}