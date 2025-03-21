package Testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import nepic.Song;
import nepic.PlayerController;
public class PlayerControllerTest {
    @Test
    public void testPlaySong() {
        Song song = new Song("Imagine", "John Lennon", 3.45);
        PlayerController player = new PlayerController();

        // Capture system output to check printed message
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        player.play(song);
        assertTrue(outContent.toString().contains("Playing: 3:27"), "Play method output incorrect!");
    }
}
