import com.example.electroniccemetery.model.Deceased;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DeceasedTest {

    //проверка формирования ФИО
    @Test
    void testGetFullName_WithAllParts() {
        Deceased deceased = new Deceased(
                1, 1, "Иванов", "Иван", "Иванович",
                null, null, null, null
        );

        String result = deceased.getFullName();

        assertEquals("Иванов Иван Иванович", result);
    }

    //проверка формирования ФИ без отчества
    @Test
    void testGetFullName_WithoutMiddleName() {
        Deceased deceased = new Deceased(
                1, 1, "Петров", "Петр", null,
                null, null, null, null
        );

        String result = deceased.getFullName();

        assertEquals("Петров Петр", result);
    }

    //тестирование геттеров (правильность сохранения данных в полях)
    @Test
    void testGetters() {
        LocalDate birth = LocalDate.of(1950, 3, 12);
        LocalDate death = LocalDate.of(2020, 1, 23);

        Deceased deceased = new Deceased(
                5, 10, "Соколова", "Ирина", "Олеговна",
                birth, death, "Инженер-конструктор", "photo.jpg"
        );

        assertEquals(5, deceased.getId());
        assertEquals(10, deceased.getGraveId());
        assertEquals("Соколова", deceased.getLastName());
        assertEquals("Ирина", deceased.getFirstName());
        assertEquals("Олеговна", deceased.getOthestvo());
        assertEquals(birth, deceased.getBirthDate());
        assertEquals(death, deceased.getDeathDate());
        assertEquals("Инженер-конструктор", deceased.getDescription());
        assertEquals("photo.jpg", deceased.getPhotoUrl());
    }
}