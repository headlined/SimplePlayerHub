import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Starting Up...");
        Data.loadData();

        boolean loop = true;
        while (loop) {
            System.out.println("\nWelcome to the Nutshell Player Hub!\nPlease choose between these options:\n1. Manage Players\n2. Add Event\n3. Add Standalone Match\n4. View Ratings Leaderboard\n5. Save Data\n0. Exit");

            int choice = -1;
            while (choice == -1) {
                try {
                    choice = SCANNER.nextInt();
                    SCANNER.nextLine();
                } catch (Exception e) {
                    System.out.println("Invalid Input! Please try again:");
                    SCANNER.nextLine();
                }
                if (choice > 5 || choice < 0) {
                    System.out.println("Invalid Selection! Please try again:");
                    choice = -1;
                }
            }

            System.out.println("");

            switch (choice) {
                case 1 -> {
                    boolean listing = true;

                    while (listing) {
                        Player chosenPlayer = (Player) scrollList(Data.players);
                        if (chosenPlayer != null) {selectPlayer(chosenPlayer);}
                        else {listing = false;}
                    }
                }
                case 2 -> {
                    System.out.println("Select series:");
                    Series series = (Series) scrollList(Data.serieses);

                    if (series != null) {
                        Data.createEvent(series);
                    } else {
                        System.out.println("What is the name of this event?");
                        String name = SCANNER.nextLine();

                        int mt = -1;

                        System.out.println("What is the format?\n1. 1v1s\n2. 2v2s");
                        while (mt == -1) {
                            try {
                                mt = SCANNER.nextInt();
                                SCANNER.nextLine();
                            } catch (Exception e) {
                                System.out.print("Invalid Input! Please try again: ");
                                SCANNER.nextLine();
                            }

                            if (mt != 1 && mt != 2) {
                                System.out.print("Invalid Input! Please try again: ");
                                mt = -1;
                            }
                        }

                        Data.createEvent(name, mt);
                    }

                    Event event = Data.events.get(Data.events.size() - 1);
                    event.addGroupMatches();
                    event.addBracketMatches();
                    Data.saveData();
                }
                case 3 -> {
                    int mt = -1;
                    int sets = -1;
                    boolean cancel = false;

                    System.out.println("What is the format?\n1. 1v1s\n2. 2v2s");
                    while (mt == -1) {
                        try {
                            mt = SCANNER.nextInt();
                            SCANNER.nextLine();
                        } catch (Exception e) {
                            System.out.print("Invalid Input! Please try again: ");
                            SCANNER.nextLine();
                        }

                        if (mt != 1 && mt != 2) {
                            System.out.print("Invalid Input! Please try again: ");
                            mt = -1;
                        }
                    }

                    System.out.print("How many sets were played? ");
                    while (sets == -1) {
                        try {
                            sets = SCANNER.nextInt();
                            SCANNER.nextLine();
                        } catch (Exception e) {
                            System.out.print("Invalid Input! Please try again: ");
                            SCANNER.nextLine();
                        }
                    }

                    ArrayList<Player> players = new ArrayList<>();
                    for (int i = 0; i < 2 * mt; i++) {
                        Player player = Event.askForPlayer(players, "Input Player name/id (or -1 to go back): ");

                        if (player == null) {
                            cancel = true;
                            break;
                        } else {
                            players.add(player);
                        }
                    }

                    if (!cancel) {
                        ArrayList<Integer> teams = new ArrayList<>();

                        for (int i = 0; i < players.size(); i++) {teams.add(((double) i / mt < 1) ? 0 : 1);}

                        for (int i = 0; i < sets; i++) {
                            ArrayList<Integer> scores = new ArrayList<>();

                            for (Player player : players) {
                                int score = -1;

                                System.out.print(String.format("Input %1$s's score: ", player.getName()));
                                while (score == -1) {
                                    try {
                                        score = SCANNER.nextInt();
                                        SCANNER.nextLine();
                                    } catch (Exception e) {
                                        System.out.print("Invalid Input! Please try again: ");
                                        SCANNER.nextLine();
                                    }

                                    if (score < 0) {
                                        System.out.print("Score cannot be less than 0! Please try again: ");
                                        score = -1;
                                    }
                                }

                                scores.add(score);
                            }

                            Data.createSet(players, teams, scores);
                        }

                        Data.createMatch(mt);
                    }
                }
                case 4 -> {
                    ArrayList<Player> sortedPlayers = Data.players;
                    Collections.sort(sortedPlayers, (plr1, plr2) -> {
                        Player a = (Player) plr1;
                        Player b = (Player) plr2;
                        if (a.getRating() < b.getRating()) {return 1;}
                        if (a.getRating() > b.getRating()) {return -1;}
                        return 0;
                    });

                    System.out.println("Ratings Leaderboard:");
                    for (int i = 0; i < sortedPlayers.size(); i++) {System.out.println(String.format("%1$d. %2$s", i + 1, sortedPlayers.get(i)));}
                }
                case 5 -> Data.saveData();
                case 0 -> {
                    loop = false;
                    Data.saveData();
                    System.out.println("Goodbye! :)");
                }
                default -> throw new AssertionError();
            }
        }
    }

    private static Object scrollList(ArrayList<?> list) {
        int page = 1;
        int maxPage = (int) Math.abs(list.size()/9 - 0.1) + 1;
        int itemsOnPage;
        while (true) {
            itemsOnPage = (page < maxPage) ? 9 : (list.size() % 9 == 0 && !list.isEmpty()) ? 9 : list.size() % 9;

            System.out.println("\nPage " + page + " of " + maxPage);
            System.out.println("Items on page: " + itemsOnPage);
            for (int i = 0; i < 9; i++) {
                if (list.size() > i + (page - 1) * 9) {
                    System.out.println((i+1) + ". " + list.get(i + (page - 1) * 9));
                }
            }
            System.out.println("\nOptions:\n-1. Next Page\n-2. Previous Page");
            if (list.equals(Data.players)) {System.out.println("-3. Back\n-4. Create New Player");}
            else if (list.equals(Data.serieses)) {System.out.println("-3. None");}

            int option = 0;
            while (option == 0) {
                try {
                    option = SCANNER.nextInt();
                    SCANNER.nextLine();
                } catch (Exception e) {
                    System.out.print("Invalid Input! Please try again: ");
                    SCANNER.nextLine();
                }

                if (option >= 1) {
                    if (page < maxPage) {
                        return list.get(option - 1);
                    } else if (option <= itemsOnPage) {
                        return list.get(option - 1);
                    } else {
                        System.out.print("Invalid Option \"" + option + "\" Please try again: ");
                        option = 0;
                    }
                } else if (option == -1) {
                    if (page < maxPage) {page++;}
                    else {System.out.println("You are already at the last page!");}
                } else if (option == -2) {
                    if (page > 1) {page--;}
                    else {System.out.println("You are already at the first page!");}
                } else if (option == -3) {
                    return null;
                } else if (option == -4) {
                    System.out.println("Input new player's name:");
                    String name = SCANNER.nextLine();
                    Data.createNewPlayer(name);
                } else {
                    System.out.print("Invalid Option \"" + option + "\" Please try again: ");
                    option = 0;
                }
            }
        }
    }

    private static void selectPlayer(Player player) {
        int option = -1;

        while (option != 0) {
            System.out.println("\nCurrently selected: " + player.getName() + "\n\nPlease select an option:\n1. View Full Profile\n2. Delete Player\n0. Back");

            try  {
                option = SCANNER.nextInt();
                SCANNER.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid Input! Please try again:");
                SCANNER.nextLine();
            }

            switch (option) {
                case 1 -> System.out.println(player.fullProfile());
                case 2 -> {
                    Data.players.remove(player);
                    option = 0;
                }
                case 0 -> {}
                default -> System.out.print("Invalid input! Please try again: ");
            }
        }
    }
}