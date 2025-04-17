package nepic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;
import javazoom.jl.decoder.JavaLayerException;
import javafx.scene.layout.VBox;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
// For working with file paths
import java.nio.file.Paths;               // For creating Path objects from file paths
// For handling IOExceptions
import java.io.InputStream;               // For general input stream handling


public class MusicController {

    @FXML
    private ImageView volumeIcon;
    @FXML
    private ImageView nepicLogo;
    @FXML
    private Button musicButton;
    @FXML
    private Button favoritesButton;
    @FXML
    private Button uploadButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchIcon;
    @FXML
    private ListView<String> songList;
    @FXML
    private ImageView artistImage1;
    @FXML
    private Label artistName1;
    @FXML
    private Label artistFans1;
    @FXML
    private ImageView artistImage2;
    @FXML
    private Label artistName2;
    @FXML
    private Label artistFans2;
    @FXML
    private ImageView artistImage3;
    @FXML
    private Label artistName3;
    @FXML
    private Label artistFans3;
    @FXML
    private ImageView playlistImage1;
    @FXML
    private Label playlistTitle1;
    @FXML
    private ImageView playlistImage2;
    @FXML
    private Label playlistTitle2;
    @FXML
    private ImageView playlistImage3;
    @FXML
    private Label playlistTitle3;
    @FXML
    private Button prevButton;
    @FXML
    private Button playButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button nextButton;
    @FXML
    private Slider volumeSlider;
    @FXML
    private VBox uploadLayout;
    @FXML
    private Label usernameLabel;  // Added for username display
    @FXML
    private Button logoutButton;    // Added for logout functionality

    private AdvancedPlayer player;
    private List<String> songFilePaths = new ArrayList<>();
    private int currentSongIndex = 0;
    private boolean isPlaying = false;
    private float volume = 1.0f;

    private TextField songTitleField;
    private TextField artistNameField;
    private TextField genreField;
    private Label coverArtPathLabel;
    private Label audioFilePathLabel;
    private File coverArtFile;
    private File audioFile;

    @FXML
    private void initialize() {
        loadLogo();
        loadImages();
        setupSongList();
        setupPlayerControls();
        setupUploadUI();
        setupUserLogout(); //added
    }

    private void loadLogo() {
        InputStream logoStream = getClass().getResourceAsStream("nepic_logo.png");
        if (logoStream != null) {
            Platform.runLater(() -> nepicLogo.setImage(new Image(logoStream)));
        } else {
            showError("Failed to load NEPIC logo.");
        }
    }

    private void loadImages() {
        loadImage(artistImage1, "artist1.jpg");
        loadImage(artistImage2, "artist2.jpg");
        loadImage(artistImage3, "artist3.jpg");
        loadImage(playlistImage1, "playlist1.jpg");
        loadImage(playlistImage2, "playlist2.jpg");
        loadImage(playlistImage3, "playlist3.jpg");
    }

    private void loadImage(ImageView imageView, String imageFileName) {
        InputStream imageStream = getClass().getResourceAsStream(imageFileName);
        if (imageStream != null) {
            Platform.runLater(() -> imageView.setImage(new Image(imageStream)));
        } else {
            showError("Failed to load image: " + imageFileName);
        }
    }

    private void setupSongList() {
        refreshSongList();
        songList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                currentSongIndex = songList.getSelectionModel().getSelectedIndex();
                playSelectedSong();
            }
        });
    }

    private void setupPlayerControls() {
        playButton.setOnAction(e -> togglePlayPause());
        nextButton.setOnAction(e -> playNextSong());
        prevButton.setOnAction(e -> playPreviousSong());
        pauseButton.setOnAction(e -> pauseSong());
        searchIcon.setOnAction(e -> searchSong());
        musicButton.setOnAction(e -> showMusic());
        favoritesButton.setOnAction(e -> showFavorites());
        uploadButton.setOnAction(e -> upload());
        volumeSlider.valueProperty().addListener(observable -> {
            volume = (float) (volumeSlider.getValue() / 100);
            if (player != null) {
                //JLayer does not have direct volume control, you may need to implement a custom volume solution or find another library.
            }
        });
    }

    private void playSelectedSong() {
        stopMp3();
        if (!songFilePaths.isEmpty() && currentSongIndex >= 0 && currentSongIndex < songFilePaths.size()) {
            String filePath = songFilePaths.get(currentSongIndex);
            playMp3(filePath);
            isPlaying = true;
            updatePlayButton();
        }
    }

    private void togglePlayPause() {
        if (player == null) {
            playSelectedSong();
        } else if (isPlaying) {
            stopMp3();
            isPlaying = false;
        } else {
            if (!songFilePaths.isEmpty() && currentSongIndex >= 0 && currentSongIndex < songFilePaths.size()) {
                playMp3(songFilePaths.get(currentSongIndex));
                isPlaying = true;
            }
        }
        updatePlayButton();
    }

    @FXML
    protected void browseAudioFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.ogg"));
        audioFile = fileChooser.showOpenDialog(null);
        if (audioFile != null) {
            audioFilePathLabel.setText(audioFile.getAbsolutePath());
        }
    }

    private void pauseSong() {
        stopMp3();
        isPlaying = false;
        updatePlayButton();
    }

    private void playNextSong() {
        currentSongIndex++;
        if (currentSongIndex >= songFilePaths.size()) {
            currentSongIndex = 0;
        }
        playSelectedSong();
        songList.getSelectionModel().select(currentSongIndex);
    }

    private void playPreviousSong() {
        currentSongIndex--;
        if (currentSongIndex < 0) {
            currentSongIndex = songFilePaths.size() - 1;
        }
        playSelectedSong();
        songList.getSelectionModel().select(currentSongIndex);
    }

    private void updatePlayButton() {
        playButton.setText(isPlaying ? "⏸️" : "▶️");
    }

    @FXML
    private void searchSong() {
        String searchText = searchField.getText().toLowerCase();
        songList.getItems().clear();

        if (searchText.isEmpty()) {
            refreshSongList();
        } else {
            String url = "jdbc:mysql://localhost:3306/nepic";
            String user = "root";
            String password = "mandip123";

            try (Connection connection = DriverManager.getConnection(url, user, password);
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "SELECT title FROM songs WHERE title LIKE ?")) {
                preparedStatement.setString(1, "%" + searchText + "%");
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String songTitle = resultSet.getString("title");
                    songList.getItems().add(songTitle);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showError("Database error: " + e.getMessage());
            }
        }
    }

    @FXML
    private void showMusic() {
        System.out.println("Music clicked");
        refreshSongList();
    }

    @FXML
    private void showFavorites() {
        System.out.println("Favorites clicked");
    }

    @FXML
    private void upload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Audio Files");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.ogg")
        );

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(uploadButton.getScene().getWindow());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            for (File file : selectedFiles) {
                uploadFile(file);
            }
            refreshSongList();
        }
    }

    private void uploadFile(File file) {
        String destinationDirectory = "uploads";
        Paths.get(destinationDirectory).toFile().mkdirs();
        Paths.get(file.toPath().toString()).toFile().renameTo(Paths.get(destinationDirectory, file.getName()).toFile());
        songFilePaths.add(Paths.get(destinationDirectory, file.getName()).toString());
    }

    private void refreshSongList() {
        System.out.println("refreshSongList called");

        List<String> fetchedSongs = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/nepic";
        String user = "root";
        String password = "mandip123";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT title, url FROM songs")) {

            while (resultSet.next()) {
                String songTitle = resultSet.getString("title");
                String songURL = resultSet.getString("url");
                System.out.println("Fetched song: " + songTitle);
                fetchedSongs.add(songTitle);
                songFilePaths.add(songURL);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error: " + e.getMessage());
        }

        Platform.runLater(() -> {
            System.out.println("Clearing song list UI");
            songList.getItems().clear();
            for (String song : fetchedSongs) {
                System.out.println("Adding song to UI: " + song);
                songList.getItems().add(song);
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void playMp3(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new AdvancedPlayer(bis);
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(javazoom.jl.player.advanced.PlaybackEvent playbackEvent) {
                    Platform.runLater(() -> {
                        playNextSong();
                    });
                }
            });
            new Thread(() -> {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                    showError("Error playing MP3: " + e.getMessage());
                }
            }).start();
        } catch (IOException | JavaLayerException e) {
            e.printStackTrace();
            showError("Error playing MP3: " + e.getMessage());
        }
    }

    private void stopMp3() {
        if (player != null) {
            player.close();
            player = null;
        }
    }

    private void setupUploadUI() {
        songTitleField = new TextField();
        songTitleField.setPromptText("Song Title");
        artistNameField = new TextField();
        artistNameField.setPromptText("Artist Name");
        genreField = new TextField();
        genreField.setPromptText("Genre");
        coverArtPathLabel = new Label();
        audioFilePathLabel = new Label();

        Button coverArtButton = new Button("Browse Cover Art");
        coverArtButton.setOnAction(e -> browseCoverArt());
        Button audioFileButton = new Button("Browse Audio File");
        audioFileButton.setOnAction(e -> browseAudioFile());
        Button uploadSongButton = new Button("Upload Song");
        uploadSongButton.setOnAction(e -> uploadSong());

        uploadLayout.getChildren().addAll(
                songTitleField, artistNameField, genreField,
                coverArtButton, coverArtPathLabel,
                audioFileButton, audioFilePathLabel,
                uploadSongButton
        );
    }

    @FXML
    protected void browseCoverArt() {
        FileChooser fileChooser = new FileChooser();
        coverArtFile = fileChooser.showOpenDialog(null);
        if (coverArtFile != null) {
            coverArtPathLabel.setText(coverArtFile.getAbsolutePath());
        }
    }

    @FXML
    protected void uploadSong() {
        String songTitle = songTitleField.getText();
        String artistName = artistNameField.getText();
        String genre = genreField.getText();

        if (coverArtFile == null || audioFile == null) {
            showError("Please select both cover art and audio file.");
            return;
        }

        String coverArtPath = coverArtFile.getAbsolutePath();
        String audioFilePath = audioFile.getAbsolutePath();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nepic", "root", "mandip123")) {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO songs (title, artist, genre, cover_art, url) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, songTitle);
            ps.setString(2, artistName);
            ps.setString(3, genre);
            ps.setString(4, coverArtPath);
            ps.setString(5, audioFilePath);
            ps.executeUpdate();

            showInfo("Song uploaded successfully!");
            refreshSongList();
        } catch (SQLException e) {
            showError("Error uploading song: " + e.getMessage());
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // New methods for username and logout
    private void setupUserLogout() {
        // Set the username.  Replace "CurrentUser" with your actual user data.
        usernameLabel.setText("Nepic"); //  set the actual username
        logoutButton.setOnAction(event -> logout()); // Changed to logout
    }

    @FXML
    private void logout() {  // Changed method name to logout
        //  implement your logout logic here.
        System.out.println("User logged out.");
        //  Add code to clear session data, redirect to login, etc.
    }
}
