package com.example.electroniccemetery.dao;

import com.example.electroniccemetery.model.Cemetery;
import com.example.electroniccemetery.model.User;
import com.example.electroniccemetery.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // проверка и получение роли
    public String checkAndGetRole(String login, String password) {
        String sql = "SELECT r.Name_Role, u.ID_Cemetery FROM Users u " +
                "JOIN C_Role r ON u.ID_Role = r.ID_Role " +
                "WHERE u.Login = ? AND u.U_Password = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Name_Role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // id кладбища
    public Integer getUserCemeteryId(String login) {
        String sql = "SELECT u.ID_Cemetery FROM Users u WHERE u.Login = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID_Cemetery");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // все админы
    public List<User> findAllAdmins() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.ID_User, u.ID_Role, u.LastName, u.FirstName, u.Othestvo, " +
                "u.Login, u.U_Password, u.ID_Cemetery, r.Name_Role " +
                "FROM Users u JOIN C_Role r ON u.ID_Role = r.ID_Role " +
                "WHERE r.Name_Role = 'Администратор' ORDER BY u.LastName";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("ID_User"),
                        rs.getInt("ID_Role"),
                        rs.getString("LastName"),
                        rs.getString("FirstName"),
                        rs.getString("Othestvo"),
                        rs.getString("Login"),
                        rs.getString("U_Password"),
                        rs.getInt("ID_Cemetery"),
                        rs.getString("Name_Role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // все кладбища
    public List<Cemetery> findAllCemeteries() {
        CemeteryDao cemeteryDao = new CemeteryDao();
        return cemeteryDao.findAll();
    }

    // +новый админ
    public int createAdmin(String lastName, String firstName, String othestvo,
                           String login, String password, Integer cemeteryId) {
        String sql = "INSERT INTO Users (ID_Role, LastName, FirstName, Othestvo, Login, U_Password, ID_Cemetery) " +
                "VALUES (1, ?, ?, ?, ?, ?, ?) RETURNING ID_User";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lastName);
            ps.setString(2, firstName);
            ps.setString(3, othestvo);
            ps.setString(4, login);
            ps.setString(5, password);
            if (cemeteryId != null) {
                ps.setInt(6, cemeteryId);
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID_User");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // закрепление админа за кладбищем
    public boolean assignAdminToCemetery(int userId, int cemeteryId) {
        String sql = "UPDATE Users SET ID_Cemetery = ? WHERE ID_User = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cemeteryId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -админ
    public boolean deleteAdmin(int userId) {
        String sql = "DELETE FROM Users WHERE ID_User = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}