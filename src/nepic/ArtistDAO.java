package nepic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ArtistDAO {

    public ObservableList<Artist> getAllArtists() throws SQLException {
        ObservableList<Artist> artists = FXCollections.observableArrayList();
        String query = "SELECT * FROM artists";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Artist artist = new Artist(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("image_url"),
                        resultSet.getInt("fan_count"),
                        resultSet.getString("bio")
                );
                artists.add(artist);
            }
        }
        return artists;
    }

    // Add other methods (getArtistById, addArtist, updateArtist, deleteArtist) as needed
}