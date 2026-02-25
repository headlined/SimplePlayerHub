import java.util.ArrayList;

public class Player {
    private int id;
    private String name;
    private int rating;
    private int matchesPlayed;
    private int matchesWon;
    private ArrayList<TitleWrapper> titles;

    public Player(int id, String name, int rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.matchesPlayed = 0;
        this.matchesWon = 0;
        this.titles = new ArrayList<>();
    }

    public Player(int id, String name) {this(id, name, 1500);}

    public int getId() {return id;}
    public String getName() {return name;}
    public int getRating() {return rating;}
    public int getMatchesPlayed() {return matchesPlayed;}
    public int getMatchesWon() {return matchesWon;}
    public ArrayList<TitleWrapper> getTitles() {return titles;}

    public void setRating(int rating) {this.rating = rating;}

    public void changeRating(int delta, boolean display) {
        if (display) {
            System.out.println(this.name + ": " + this.rating + " -> " + (this.rating + delta) + " (" + ((delta >= 0) ? "+" : "") + delta + ")");
        }
        this.rating += delta;
    }

    public void changeRating(int delta) {changeRating(delta, true);}

    public void recordMatch(boolean won) {
        matchesPlayed++;
        if (won) {
            matchesWon++;
        }
    }

    public void setMatchesPlayed(int matchesPlayed) { this.matchesPlayed = matchesPlayed; }

    public void setMatchesWon(int matchesWon) { this.matchesWon = matchesWon; }

    public void addTitle(TitleWrapper title) {
        titles.add(title);
    }

    public void setTitle(int index, TitleWrapper title) {
        titles.set(index, title);
    }

    public String fullProfile() {
        String profile = String.format("%n%1$s (Rating: %2$d)%n", name, rating);

        profile += String.format("Matches Played: %1$d%n", matchesPlayed);
        profile += String.format("Matches Won: %1$d (%%%2$f)%n", matchesWon, (double) matchesWon / (double) matchesPlayed * 100);

        profile += "\nTitles";
        profile += "\n--------\n";

        if (titles.isEmpty()) {
            profile += "None!\n";
        } else {
            for (TitleWrapper title : titles) {
                profile += title.series;
                profile += String.format("%nWins: %1$d%n", title.wins);
                profile += String.format("Runner-Ups: %1$d%n", title.runnerUps);
                profile += String.format("Third Places: %1$d%n", title.thirds);
                profile += String.format("Most Recent Placement: %1$s%n", title.recentPlacement);
            }
        }

        return profile;
    }

    @Override
    public String toString() {
        return String.format("%1$s (Rating: %2$d)", name, rating);
    }
}