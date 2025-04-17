package Testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import nepic.Song;

public class SongTest {
    @Test
    public void testFormattedDuration() {
        Song song = new Song("Imagine", "John Lennon", 3.45);
        assertEquals("3:27", song.getFormattedDuration(), "Incorrect song duration format!");
    }
}
