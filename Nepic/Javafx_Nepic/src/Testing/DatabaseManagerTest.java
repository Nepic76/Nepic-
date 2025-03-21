package Testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import nepic.DatabaseManager;

public class DatabaseManagerTest {
    @Test
    public void testDatabaseConnection() {
        DatabaseManager dbManager = new DatabaseManager();
        assertTrue(dbManager.connect(), "Database connection failed!");
    }
}
