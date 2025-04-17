package nepic;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();  // Create an instance

        if (dbManager.connect()) {  // Establish a connection
            System.out.println("Connected to the database!");

            Connection conn = dbManager.getConnection(); // Get connection
            if (conn != null) {
                System.out.println("Successfully retrieved connection.");
            }
        } else {
            System.out.println("Database connection failed.");
        }
    }
}
