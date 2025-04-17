package nepic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static boolean login(String username, String password) {
        // Use DatabaseManager.getConnection() instead of DatabaseConnection.connect()
        Connection conn = DatabaseManager.getConnection(); 

        // SQL query to check for matching username and password
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try {
            // Prepare the statement with query parameters
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query and check if a result is returned
            ResultSet rs = stmt.executeQuery();
            return rs.next();  // If result exists, login is successful

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // Return false if no matching user is found or an error occurs
    }
}
