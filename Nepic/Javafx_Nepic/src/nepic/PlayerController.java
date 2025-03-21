package nepic;

public class PlayerController {
    private Song currentSong;

    public void play(Song song) {
        this.currentSong = song;
        System.out.println("Playing: " + song.getFormattedDuration());
    }

    public void pause() {
        System.out.println("Paused: " + currentSong.getFormattedDuration());
    }

    public void stop() {
        System.out.println("Stopped.");
    }
}
