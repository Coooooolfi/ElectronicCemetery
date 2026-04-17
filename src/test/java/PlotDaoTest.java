import com.example.electroniccemetery.dao.PlotDao;
import com.example.electroniccemetery.util.DbConnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlotDaoTest {

    //проверка логики существования участка
    @Test
    void testExistsBySectionAndNumber_ReturnsTrue() throws Exception {
        //поддельное подключение
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        //настройка цепочки вызовов
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // запись существует

        //подмена стат. метода подключения к БД своим моком
        try (MockedStatic<DbConnection> mockedDb = mockStatic(DbConnection.class)) {
            mockedDb.when(DbConnection::getConnection).thenReturn(mockConnection);
            PlotDao plotDao = new PlotDao();
            boolean exists = plotDao.existsBySectionAndNumber(1, 5);
            assertTrue(exists);
        }
    }
}