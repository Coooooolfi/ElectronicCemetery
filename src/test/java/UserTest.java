import com.example.electroniccemetery.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGetFullName() {
        User user = new User(
                1, 1, "Павлеченко", "Ирина", "Ивановна",
                "admin1", "12345", 1, "Администратор"
        );

        assertEquals("Павлеченко Ирина Ивановна", user.getFullName());
    }

    @Test
    void testGetFullName_WithoutMiddleName() {
        User user = new User(
                2, 1, "Ковалев", "Николай", null,
                "admin2", "admin123", 2, "Администратор"
        );

        assertEquals("Ковалев Николай", user.getFullName());
    }

    @Test
    void testUserWithNullCemetery() {
        User user = new User(
                3, 1, "Львицин", "Никита", "Александрович",
                "admin3", "addmin", null, "Администратор"
        );

        assertNull(user.getCemeteryId());
        assertEquals("Львицин Никита Александрович", user.getFullName());
    }
}