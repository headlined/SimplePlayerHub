import java.util.ArrayList;

public class Match {
    public int matchID;
    public int matchType;
    // 1 - Duel
    // 2 - Team Duel
    // 3 - FFA
    public ArrayList<Set> sets;
    public ArrayList<Player> players;
    public ArrayList<Integer> teams;
    public int winner;

    public Match(int matchID, int matchType, ArrayList<Set> sets) {
        this.matchID = matchID;
        this.matchType = matchType;
        this.sets = sets;
        this.players = sets.get(0).players;
        this.teams = sets.get(0).teams;
        determineWinner();
        calculateEloChanges();
        giveMatchWins();
    }

    public Match(int matchID, int matchType, ArrayList<Set> sets, boolean skipProcessing) {
        this.matchID = matchID;
        this.matchType = matchType;
        this.sets = sets;
        this.players = sets.get(0).players;
        this.teams = sets.get(0).teams;
        if (!skipProcessing) {
            determineWinner();
            calculateEloChanges();
            giveMatchWins();
        }
    }

    private void determineWinner() {
        ArrayList<Integer> teamWins = new ArrayList<>();
        for (Set set : sets) {
            set.determineWinner();
            int winningTeam = set.winner;

            if (teamWins.size() < winningTeam + 1) {
                teamWins.add(1);
            } else {
                teamWins.set(winningTeam, teamWins.get(winningTeam) + 1);
            }
        }

        int maxWins = -1;
        for (int i = 0; i < teamWins.size(); i++) {
            if (teamWins.get(i) > maxWins) {
                maxWins = teamWins.get(i);
                winner = i;
            }
        }
    }

    private void calculateEloChanges() {
        ArrayList<Integer> ratingChanges = new ArrayList<>();

        for (Set set : sets) {
            ArrayList<Player> playersInSet = set.players;
            ArrayList<Integer> teamsInSet = set.teams;
            int winningTeam = set.winner;

            for (Player player : playersInSet) {
                int playerTeam = teamsInSet.get(playersInSet.indexOf(player));
                int won = (playerTeam == winningTeam) ? 1 : 0;

                int averageOpponentElo = 0;
                int averageTeamElo = 0;
                int teamCount = 0;
                int opponentCount = 0;
                for (Player player2 : playersInSet) {
                    int opponentTeam = teamsInSet.get(playersInSet.indexOf(player2));
                    if (opponentTeam != playerTeam) {
                        averageOpponentElo += player2.getRating();
                        opponentCount++;
                    } else {
                        averageTeamElo += player2.getRating();
                        teamCount++;
                    }
                }

                if (opponentCount > 0) {
                    averageOpponentElo /= opponentCount;
                }

                if (teamCount > 0) {
                    averageTeamElo /= teamCount;
                }

                double winProbability = 1 / (1 + Math.pow(10, (averageOpponentElo - averageTeamElo) / 1200.0));
                int ratingChange = (int) Math.round(32 * (won - winProbability));

                //System.out.println(player.getName() + ": " + "Team " + playerTeam + ", " + (won == 1 ? "Won" : "Lost") + " (Winning Team: " + winningTeam + "), Elo Change: " + ratingChange);

                if (playersInSet.indexOf(player) < ratingChanges.size()) {
                    ratingChanges.set(playersInSet.indexOf(player), ratingChanges.get(playersInSet.indexOf(player)) + ratingChange);
                } else {
                    ratingChanges.add(ratingChange);
                }
            }
        }

        for (int i = 0; i < players.size(); i++) {
            players.get(i).changeRating(ratingChanges.get(i));
        }
    }

    private void giveMatchWins() {
        for (Player player : players) {
            boolean won = (teams.get(players.indexOf(player)) == winner);
            player.recordMatch(won);
        }
    }
}
