import com.example.electroniccemetery.model.Cemetery;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CemeteryTest {

    //проверка создания объекта через конструктор
    @Test
    void testCemeteryConstructor() {
        Cemetery cemetery = new Cemetery(1, "Северное кладбище", "ул.Лесная, 15");

        assertEquals(1, cemetery.getId());
        assertEquals("Северное кладбище", cemetery.getName());
        assertEquals("ул.Лесная, 15", cemetery.getAddress());
    }

    //проверка создания объекта без адреса
    @Test
    void testCemeteryWithEmptyAddress() {
        Cemetery cemetery = new Cemetery(2, "Восточное кладбище", "");

        assertEquals(2, cemetery.getId());
        assertEquals("Восточное кладбище", cemetery.getName());
        assertEquals("", cemetery.getAddress());
    }
}