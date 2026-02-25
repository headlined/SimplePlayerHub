import java.util.ArrayList;

public class Match {
    public int matchID;
    public int matchType;
    // 1 - Duel
    // 2 - Team Duel
    // 3 - FFA
    public ArrayList<Set> sets;
    public ArrayList<Integer> players;
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

    private void determineWinner() {
        ArrayList<Integer> teamWins = new ArrayList<>();
        teamWins.add(0);
        teamWins.add(0);

        for (Set set : sets) {
            int winningTeam = set.winner;
            teamWins.set(winningTeam, teamWins.get(winningTeam) + 1);
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
            ArrayList<Integer> playersInSet = set.players;
            ArrayList<Integer> teamsInSet = set.teams;
            int winningTeam = set.winner;

            for (int player : playersInSet) {
                int playerTeam = teamsInSet.get(playersInSet.indexOf(player));
                int won = (playerTeam == winningTeam) ? 1 : 0;

                int averageOpponentElo = 0;
                int averageTeamElo = 0;
                int teamCount = 0;
                int opponentCount = 0;
                for (int player2 : playersInSet) {
                    int opponentTeam = teamsInSet.get(playersInSet.indexOf(player2));
                    if (opponentTeam != playerTeam) {
                        averageOpponentElo += Data.getPlayerById(player2).getRating();
                        opponentCount++;
                    } else {
                        averageTeamElo += Data.getPlayerById(player2).getRating();
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

                if (playersInSet.indexOf(player) < ratingChanges.size()) {
                    ratingChanges.set(playersInSet.indexOf(player), ratingChanges.get(playersInSet.indexOf(player)) + ratingChange);
                } else {
                    ratingChanges.add(ratingChange);
                }
            }
        }

        for (int i = 0; i < players.size(); i++) {
            Data.getPlayerById(players.get(i)).changeRating(ratingChanges.get(i));
        }
    }

    private void giveMatchWins() {
        for (int player : players) {
            boolean won = (teams.get(players.indexOf(player)) == winner);
            Data.getPlayerById(player).recordMatch(won);
        }
    }
}
