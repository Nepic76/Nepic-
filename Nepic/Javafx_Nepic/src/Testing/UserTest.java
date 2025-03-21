package Testing;  // Ensure this matches your actual package name

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import nepic.User;  // Import the User class

public class UserTest {

    @Test
    public void testValidateCorrectPassword() {
        User user = new User("testuser", "test@example.com", "password123");
        boolean result = user.validatePassword("passwor123");
        assertTrue(result, "Password validation failed! Expected true but got false.");
    }
}
