package nepic;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    public void playMusic(String filePath) {
        File file = new File(filePath);
        
        if (!file.exists()) {  // Check if the file exists
            System.out.println("File not found: " + filePath);
            return;
        }

        String uri = file.toURI().toString(); // Convert to URI format
        System.out.println("Playing file: " + uri);  // Debugging message

        try {
            Media media = new Media(uri);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
