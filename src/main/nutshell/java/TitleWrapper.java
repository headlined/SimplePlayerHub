public class TitleWrapper {
    public String series;
    public int wins;
    public int runnerUps;
    public int thirds;
    public String recentPlacement;

    public TitleWrapper(String series, int wins, int runnerUps, int thirds, String recentPlacement) {
        this.series = series;
        this.wins = wins;
        this.runnerUps = runnerUps;
        this.thirds = thirds;
        this.recentPlacement = recentPlacement;
    }
}