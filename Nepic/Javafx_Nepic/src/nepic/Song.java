package nepic;

public class Song {
    private String title;
    private String artist;
    private double duration;

    public Song(String title, String artist, double duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    public String getFormattedDuration() {
        int minutes = (int) duration;
        int seconds = (int) ((duration - minutes) * 60);
        return String.format("%d:%02d", minutes, seconds);
    }
}
