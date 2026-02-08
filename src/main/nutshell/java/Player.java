import java.util.ArrayList;

public class Player {
    private int id;
    private String name;
    private int rating;
    private int matchesPlayed;
    private int matchesWon;
    private ArrayList<String> titles;

    public Player(int id, String name, int rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.matchesPlayed = 0;
        this.matchesWon = 0;
    }

    public Player(int id, String name) {this(id, name, 1500);}

    public int getId() {return id;}
    public String getName() {return name;}
    public int getRating() {return rating;}
    public int getMatchesPlayed() {return matchesPlayed;}
    public int getMatchesWon() {return matchesWon;}
    public ArrayList<String> getTitles() {return titles;}

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

    public void addTitle(String title) {
        if (titles == null) {
            titles = new ArrayList<>();
        }
        titles.add(title);
    }

    public void removeTitle(String title) {
        if (titles != null) {
            titles.remove(title);
        }
    }

    public void removeTitle(int index) {
        if (titles != null && index >= 0 && index < titles.size()) {
            titles.remove(index);
        }
    }

    public void clearTitles() {
        if (titles != null) {
            titles.clear();
        }
    }
}
