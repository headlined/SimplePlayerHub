import java.util.ArrayList;

public class Set {
    public ArrayList<Integer> players;
    public ArrayList<Integer> teams;
    public ArrayList<Integer> scores;
    public int winner;

    public Set(ArrayList<Integer> players, ArrayList<Integer> teams, ArrayList<Integer> scores) {
        this.players = players;
        this.teams = teams;
        this.scores = scores;
        determineWinner();
    }

    private void determineWinner() {
        ArrayList<Integer> teamScores = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            int team = teams.get(i);
            int score = scores.get(i);
            
            if (teamScores.size() < team + 1) {
                teamScores.add(score);
            } else {
                teamScores.set(team, teamScores.get(team) + score);
            }
        }

        int maxScore = -1;
        for (int i = 0; i < teamScores.size(); i++) {
            if (teamScores.get(i) > maxScore) {
                maxScore = teamScores.get(i);
                winner = i;
            }
        }
    }
}