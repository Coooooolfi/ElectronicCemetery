import com.example.electroniccemetery.dao.UserDao;
import com.example.electroniccemetery.util.DbConnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDaoTest {
    //тестирование получения роли
    @Test
    void testCheckAndGetRole_ReturnsRole() throws Exception {
        // создание моков
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // настройка поведения
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("Name_Role")).thenReturn("Администратор");

        // подмена метода DbConnection.getConnection()
        try (MockedStatic<DbConnection> mockedDb = mockStatic(DbConnection.class)) {
            mockedDb.when(DbConnection::getConnection).thenReturn(mockConnection);

            UserDao userDao = new UserDao();
            String role = userDao.checkAndGetRole("admin1", "12345");

            assertEquals("Администратор", role);
        }
    }


    //  функция редактирования администратора
    @Test
    void testUpdateAdminSuccess() throws Exception {
        // Mock-объекты
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1); // одна строка обновлена

        try (MockedStatic<DbConnection> mockedDb = mockStatic(DbConnection.class)) {
            mockedDb.when(DbConnection::getConnection).thenReturn(mockConn);
            UserDao userDao = new UserDao();
            boolean result = userDao.updateAdmin(1, "Иванов", "Иван", "Иванович", "ivan", "pass");
            assertTrue(result);
            // проверка, что параметры SQL установлены корректно
            verify(mockStmt).setString(1, "Иванов");
            verify(mockStmt).setInt(6, 1);
        }
    }

    //проверка дублиования данных
    @Test
    void testUpdateAdminDuplicateLogin() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0); // ничего не обновлено

        try (MockedStatic<DbConnection> mockedDb = mockStatic(DbConnection.class)) {
            mockedDb.when(DbConnection::getConnection).thenReturn(mockConn);
            UserDao userDao = new UserDao();
            boolean result = userDao.updateAdmin(2, "Петров", "Петр", null, "admin1", "123");
            assertFalse(result);
        }
    }

    // функция переназначения кладбища для администратора
    @Test
    void testAssignAdminToCemeterySuccess() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DbConnection> mockedDb = mockStatic(DbConnection.class)) {
            mockedDb.when(DbConnection::getConnection).thenReturn(mockConn);
            UserDao userDao = new UserDao();
            boolean result = userDao.assignAdminToCemetery(1, 3);
            assertTrue(result);
        }
    }

    //ложное срабатывание назначения
    @Test
    void testAssignAdminToCemeteryFail() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DbConnection> mockedDb = mockStatic(DbConnection.class)) {
            mockedDb.when(DbConnection::getConnection).thenReturn(mockConn);
            UserDao userDao = new UserDao();
            boolean result = userDao.assignAdminToCemetery(999, 999);
            assertFalse(result);
        }
    }
}