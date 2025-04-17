package nepic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MusicDAO {

    // Method to add a song to the database
    public static void addSong(String title, int albumId, double duration, String genre, String url, String songImageUrl) {
        Connection conn = DatabaseManager.getConnection(); // Use your DatabaseManager
        String query = "INSERT INTO songs (album_id, title, duration, genre, url, song_image_url) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, albumId); // Use album_id
            stmt.setString(2, title);
            stmt.setDouble(3, duration);
            stmt.setString(4, genre);
            stmt.setString(5, url); // Use url instead of file_path
            stmt.setString(6, songImageUrl);
            stmt.executeUpdate();
            System.out.println("Song added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Example: Method to get a song's URL by song ID
    public static String getSongUrlById(int songId) {
        Connection conn = DatabaseManager.getConnection();
        String query = "SELECT url FROM songs WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, songId);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}