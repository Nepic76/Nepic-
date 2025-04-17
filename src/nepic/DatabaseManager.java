package nepic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection;  // static so it's shared across the class

    // Connects to the database
    public static boolean connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nepic", "root", "mandip123");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Returns the connection object
    public static Connection getConnection() {
        if (connection == null) {
            connect();
        }
        return connection;
    }

    // Saves a user in the database
    public static boolean saveUser(User user) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
