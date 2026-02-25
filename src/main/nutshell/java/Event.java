import java.util.ArrayList;
import java.util.Scanner;

public class Event {
    private static final Scanner SCANNER = new Scanner(System.in);
    private final String name;
    private ArrayList<Integer> matches;
    private ArrayList<Integer> placements;
    final private int matchType;
    // 1 - 1v1
    // 2 - 2v2
    final private Series series;

    public Event(String name, ArrayList<Integer> matches, ArrayList<Integer> placements, int matchType, Series series) {
        this.name = name;
        this.matches = matches;
        this.placements = placements;
        this.matchType = matchType;
        this.series = series;
    }

    public void addBracketMatches() {
        int bracketSize = askForScore("Bracket Size: ", true);
        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < bracketSize; i++) {
            players.add(askForPlayer(players, "Please enter the name/id of Player " + (i% (2 * matchType) + 1) + " of match " + ((int) (i / (2 * matchType)) + 1) + ": "));
        }

        for (int i = bracketSize; i > 1 ; i/=2) {
            WLwrapper results = runBracketRound(i, players, false);

            for (Player loser : results.losers) {
                placements.addFirst(loser.getId());
                players.remove(loser);
            }

            if (i == 4 * matchType) {
                WLwrapper tplace = runBracketRound(2 * matchType, results.losers, true);

                if (matchType == 2) {
                    placements.set(0, tplace.winners.get(0).getId());
                    placements.set(1, tplace.winners.get(1).getId());
                    placements.set(2, tplace.losers.get(0).getId());
                    placements.set(3, tplace.losers.get(1).getId());
                } else {
                    placements.set(0, tplace.winners.get(0).getId());
                    placements.set(1, tplace.losers.get(0).getId());
                }
            } else if (i == 2 * matchType) {
                for (Player winner : results.winners) {
                    placements.addFirst(winner.getId());
                    players.remove(winner);
                }
            }
        }

        if (series != null) {
            series.updateTitles();
        }
    }

    private WLwrapper runBracketRound(int bracketSize, ArrayList<Player> players, boolean thirdPlace) {
        ArrayList<Player> victors = new ArrayList<>();
        ArrayList<Player> defeated = new ArrayList<>();

        String roundName = switch (bracketSize / matchType) {
            case 2 -> thirdPlace ? "Third-Place Match" : "Finals";
            case 4 -> "Semi-Finals";
            case 8 -> "Quarter Finals";
            default -> "Round of " + bracketSize / matchType;
        };
        int setCount = (bracketSize == 2 * matchType) ? 5 : 3;

        System.out.println(roundName);
        System.out.println("-------------------------------");

        for (int i = 0; i < bracketSize / (2 * matchType); i++) {
            ArrayList<Player> matchPlayers = new ArrayList<>();
            ArrayList<Integer> teams = new ArrayList<>();
            for (int j = 0; j < (2 * matchType); j++) {
                matchPlayers.add(players.get((2 * matchType) * i + j));
                teams.add((j < matchType) ? 0 : 1);
            }

            if (matchPlayers.size() == 2) {
                System.out.println(matchPlayers.get(0).getName() + " vs. " + matchPlayers.get(1).getName());
            } else {
                System.out.println(matchPlayers.get(0).getName() + " and " + matchPlayers.get(1).getName() + " vs. " + matchPlayers.get(2).getName() + " and " + matchPlayers.get(3).getName());
            }

            int team0wins = 0;
            for (int j = 1; j <= setCount; j++) {
                ArrayList<Integer> scores = new ArrayList<>();
                for (Player competitor : matchPlayers) {
                    scores.add(askForScore("Input " + competitor.getName() + "\'s KOs: ", false));
                }

                Data.createSet(matchPlayers, teams, scores);
                //System.out.println("Set winner: " + Data.tempSets.get(Data.tempSets.size() - 1).winner);

                if (Data.tempSets.get(Data.tempSets.size() - 1).winner == 0) {
                    team0wins++;
                }

                if (setCount == 3) {
                    if (team0wins == 2 || team0wins == 0 && j == 2) {
                        break;
                    }
                } else {
                    if (team0wins == 3 || (team0wins == 0 && j == 3) || (team0wins == 1 && j == 4)) {
                        break;
                    }
                }
            }
            Data.createMatch(matchType);
            matches.add(Data.matches.size() - 1);
            System.out.println("Match winner: Team " + (Data.matches.get(Data.matches.size() - 1).winner + 1) + "\n");

            if (Data.matches.get(Data.matches.size() - 1).winner == 1) {
                if (matchPlayers.size() == 2) {
                    victors.add(matchPlayers.get(1));
                    defeated.add(matchPlayers.get(0));
                } else {
                    victors.add(matchPlayers.get(2));
                    victors.add(matchPlayers.get(3));
                    defeated.add(matchPlayers.get(0));
                    defeated.add(matchPlayers.get(1));
                }
            } else {
                if (matchPlayers.size() == 2) {
                    victors.add(matchPlayers.get(0));
                    defeated.add(matchPlayers.get(1));
                } else {
                    victors.add(matchPlayers.get(0));
                    victors.add(matchPlayers.get(1));
                    defeated.add(matchPlayers.get(2));
                    defeated.add(matchPlayers.get(3));
                }
            }
        }

        WLwrapper wrapper = new WLwrapper(victors, defeated);
        return wrapper;
    }

    public void addGroupMatches() {
        boolean fin = false;

        while (!fin) {
            ArrayList<Player> players = new ArrayList<>();

            if (matchType == 1) {
                Player p1 = askForPlayer(players, "Please enter the first player\'s name/id (-1 to finish): ");
                if (p1 == null) {return;}
                players.add(p1);

                Player p2 = (askForPlayer(players, "Please enter the second player\'s name/id (-1 to finish): "));
                if (p2 == null) {return;}
                players.add(p2);
            } else {
                Player p1 = (askForPlayer(players, "Please enter the first player of Team 1\'s name/id (-1 to finish): "));
                if (p1 == null) {return;}
                players.add(p1);

                Player p2 = (askForPlayer(players, "Please enter the second player of Team 1\'s name/id (-1 to finish): "));
                if (p2 == null) {return;}
                players.add(p2);

                Player p3 = (askForPlayer(players, "Please enter the first player of Team 2\'s name/id (-1 to finish): "));
                if (p3 == null) {return;}
                players.add(p3);

                Player p4 = (askForPlayer(players, "Please enter the second player of Team 2\'s name/id (-1 to finish): "));
                if (p4 == null) {return;}
                players.add(p4);
            }

            for (int i = 1; i < 4; i++) {
                ArrayList<Integer> scores = new ArrayList<>();
                for (Player player : players) {
                    scores.add(askForScore("Input " + player.getName() + "\'s KOs: ", false));
                }

                ArrayList<Integer> teams = new ArrayList<>();
                for (int j = 0; j < players.size(); j++) {
                    teams.add((j < players.size() / 2) ? 0 : 1);
                }

                Data.createSet(players, teams, scores);

                if (i == 2 && Data.tempSets.get(Data.tempSets.size() - 2).winner == Data.tempSets.get(Data.tempSets.size() - 1).winner) {break;}
            }

            Data.createMatch(matchType);
            matches.add(Data.matches.size() - 1);
        }
    }

    public static Player askForPlayer(ArrayList<Player> exclusions, String text) {
        System.out.print(text);

        Player player = null;
        while (player == null) {
            try {
                String name = SCANNER.nextLine();

                player = Data.getPlayerByName(name);
                
                if (player == null) {
                    try {
                        player = Data.getPlayerById(Integer.parseInt(name));
                        if (Integer.parseInt(name) == -1) {return null;}
                    } catch (NumberFormatException e) {}
                }
                
                if (player == null) {
                    System.out.print("Player with name/id \"" + name + "\" does not exist!\nPlease try again: ");
                }

                if (exclusions.contains(player)) {
                    System.out.print("Player \"" + player.getName() + "\" has already been added to this match!\nPlease try again: ");
                    player = null;
                }
            } catch (Exception e) {
                System.out.println("ERROR: error while locating player");
            }
        }
        return player;
    }

    public int askForScore(String text, boolean sq) {
        System.out.print(text);
        int score = -1;

        while (score < 0) {
            try {
                score = SCANNER.nextInt();

                if (score < 0) {
                    System.out.print("Invalid number! Please try again: ");
                }

                if (sq) {
                    for (int i = 2; i <= score; i*= (2 * matchType)) {
                        if (i > score) {
                            System.out.print("Invalid bracket size! Please try again: ");
                            score = -1;
                        }
                    }
                }
                SCANNER.nextLine();
            } catch (Exception e) {
                System.out.print("ERROR: Input is NOT an integer! Please try again: ");
                SCANNER.nextLine();
            }
        }

        return score;
    }

    public String getName() {return this.name;}
    public ArrayList<Integer> getMatches() {return this.matches;}
    public ArrayList<Integer> getPlacements() {return this.placements;}
    public int getMatchType() {return this.matchType;}
}

class WLwrapper {
    public ArrayList<Player> winners;
    public ArrayList<Player> losers;

    public WLwrapper(ArrayList<Player> winners, ArrayList<Player> losers) {
        this.winners = winners;
        this.losers = losers;
    }
}