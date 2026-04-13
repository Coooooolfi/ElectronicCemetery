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
}