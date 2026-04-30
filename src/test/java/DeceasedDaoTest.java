import com.example.electroniccemetery.dao.DeceasedDao;

import com.example.electroniccemetery.model.Deceased;
import com.example.electroniccemetery.util.DbConnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeceasedDaoTest {

    // функция списка захороненных на кладбище
    @Test
    void testFindByCemeteryIdReturnsList() throws Exception {
        // Mock-объекты
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        // иммитация двух строк в ResultSet
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("ID_Deceased")).thenReturn(1, 2);
        when(mockRs.getString("LastName")).thenReturn("Иванов", "Петров");
        when(mockRs.getString("FirstName")).thenReturn("Иван", "Петр");
        when(mockRs.getString("Othestvo")).thenReturn("Иванович", null);
        when(mockRs.getString("Description")).thenReturn("Ветеран", null);
        when(mockRs.getString("Path_File")).thenReturn("photo1.jpg", null);
        when(mockRs.getDate("Birth_Date")).thenReturn(null, null);
        when(mockRs.getDate("Death_Date")).thenReturn(null, null);

        try (MockedStatic<DbConnection> mockedDb = mockStatic(DbConnection.class)) {
            mockedDb.when(DbConnection::getConnection).thenReturn(mockConn);
            DeceasedDao dao = new DeceasedDao();
            List<Deceased> list = dao.findByCemeteryId(1);
            assertEquals(2, list.size());
            assertEquals("Иванов", list.get(0).getLastName());
            assertEquals("Петров", list.get(1).getLastName());
        }
    }

    //тест для "пустого" кладбища
    @Test
    void testFindByCemeteryIdReturnsEmptyList() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // пустой результат

        try (MockedStatic<DbConnection> mockedDb = mockStatic(DbConnection.class)) {
            mockedDb.when(DbConnection::getConnection).thenReturn(mockConn);
            DeceasedDao dao = new DeceasedDao();
            List<Deceased> list = dao.findByCemeteryId(999);
            assertTrue(list.isEmpty());
        }
    }
}