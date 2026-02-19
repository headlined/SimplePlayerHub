import java.util.ArrayList;

public class Series {
    private final String name;
    private final int namingScheme;
    // 1 - 1st, 2nd, 3rd
    // 2 - #1, #2, #3
    private final int titles;
    // 1 - Champion, Runner-Up, 3rd Place
    // 2 - 1st Place, 2nd Place, 3rd Place
    // 3 - Winner, Eliminated in Finals, Winner of Third Place Match
    // 4 - 1st Place, 2nd Place, 3rd Place, 4th Place, 5th-8th Place
    private final int matchType;
    private ArrayList<Integer> events;

    public Series(String name, int namingScheme, int titles, int matchType, ArrayList<Integer> events) {
        this.name = name;
        this.namingScheme = namingScheme;
        this.matchType = matchType;
        this.titles = titles;
    }

    public void updateTitles() {
        ArrayList<Player> placements = Data.events.get(events.size() - 1).getPlacements();
        for (int i = 0; i < placements.size(); i++) {
            int titleIndex = -1;
            for (TitleWrapper title : placements.get(i).getTitles()) {
                if (title.series.equals(name)) {
                    titleIndex = placements.get(i).getTitles().indexOf(title);

                    String recentPlacement = getTitleFromPlacement(i + 1, placements.size()) + " (" + addPrefix(events.size()) + ")";
                    TitleWrapper newTitle = new TitleWrapper(name, title.wins, title.runnerUps, title.thirds, recentPlacement);

                    switch (i) {
                        case 0 -> newTitle.wins++;
                        case 1 -> newTitle.runnerUps++;
                        case 2 -> newTitle.thirds++;
                        default -> {}
                    }

                    placements.get(i).setTitle(titleIndex, newTitle);
                    break;
                }
            }

            if (titleIndex == -1) {
                String recentPlacement = getTitleFromPlacement(i + 1, placements.size()) + " (" + addPrefix(events.size()) + ")";
                TitleWrapper newTitle = new TitleWrapper(name, 0, 0, 0, recentPlacement);

                switch (i) {
                    case 0 -> newTitle.wins++;
                    case 1 -> newTitle.runnerUps++;
                    case 2 -> newTitle.thirds++;
                    default -> {
                    }
                }

                placements.get(i).addTitle(newTitle);
            }
        }
    }

    public String getTitleFromPlacement(int placement, int size) {
        String title = "";

        switch (titles) {
            case 1 -> title = switch (placement) {
                    case 1 -> "Champion";
                    case 2 -> "Runner-Up";
                    default -> addPrefix(placement) + " Place";
                };
            case 2 -> title = addPrefix(placement) + " Place";
            case 3 -> title = switch (placement) {
                    case 1 -> "Winner";
                    case 3 -> "Third Place Winner";
                    default -> "Eliminated in " + placementToRound(placement, size);
                };
            case 4 -> title = switch (placement) {
                    case 1 -> "1st Place";
                    case 2 -> "2nd Place";
                    case 3 -> "3rd Place";
                    case 4 -> "4th Place";
                    default -> assortedPlacement(placement, size);
                };
            default -> throw new AssertionError();
        }

        return title;
    }

    private String addPrefix(int placement) {
        String prefix = "";
        int baseNum = Math.abs(placement) % 10;

        if (baseNum == 1 && placement != 11) {prefix = "st";}
        if (baseNum == 2 && placement != 12) {prefix = "nd";}
        if (baseNum == 3 && placement != 13) {prefix = "rd";}

        return placement + prefix;
    }

    private String placementToRound(int placement, int size) {
        String round = "";

        for (int i = 2; i < placement; i*=2) {
            if (i >= placement) {
                switch (i) {
                    case 2 -> round = "Finals";
                    case 4 -> round = "Third Place Match";
                    case 8 -> round = "Quarter-Finals";
                    default -> {
                        if (i > size) {round = "Group Stage";}
                        else {round = "Round of " + i;}
                    }
                }
            }
        }

        return round;
    }

    private String assortedPlacement(int placement, int size) {
        String range = "";

        for (int i = 4; i < placement; i*=2) {
            if (i >= placement) {
                if (i > size) {
                    range = "Group Stage";
                } else {
                    range = addPrefix(i/2 + 1) + "-" + addPrefix(i);
                }
            }
        }

        return range;
    }

    public void addEvent(int eventIndex) {events.add(eventIndex);}

    public String getName() {return name;}

    public String newEventName() {
        return switch (namingScheme) {
            case 1 -> addPrefix(events.size() + 1) + name;
            case 2 -> name + " #" + (events.size() + 1);
            default -> throw new AssertionError();
        };
    }

    public int getMatchType() {return matchType;}
}