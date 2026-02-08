import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Data {
    public static final String FILEPATH = "src/main/nutshell/data/data.json";
    public static final File DATAFILE = getDataFile();
    public static ArrayList<Player> players = new ArrayList<>();
    public static ArrayList<Match> matches = new ArrayList<>();
    public static ArrayList<Set> tempSets = new ArrayList<>();

    public static void createNewPlayer(String name) {
        int newId = players.size() + 1;
        Player newPlayer = new Player(newId, name);
        players.add(newPlayer);
    }

    public static Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    public static Player getPlayerByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public static void createSet(ArrayList<Player> players, ArrayList<Integer> teams, ArrayList<Integer> scores) {
        Set newSet = new Set(players, teams, scores);
        tempSets.add(newSet);
    }

    public static void createMatch(int matchType) {
        int newMatchId = matches.size() + 1;
        Match newMatch = new Match(newMatchId, matchType, new ArrayList<>(tempSets));
        matches.add(newMatch);
        tempSets.clear();
    }

    public static File getDataFile() {
        try {
            File dataFile = new File(FILEPATH);
            System.out.println("Checking for data.json at " + FILEPATH + "...");
            if (!dataFile.exists()) {
                System.out.println("data.json not found, creating new file...");
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            } else {
                System.out.println("data.json found");
            }
        return dataFile;
        } catch (IOException e) {
            System.out.println("An error occurred while accessing data.json");
            return null;
        }
    }

    public static void saveData() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try (FileWriter writer = new FileWriter(DATAFILE)) {
                String jsonData = gson.toJson(DataToWrapper());
                writer.write(jsonData);
            }

            System.out.println("Data saved to data.json");
        } catch (IOException e) {
            System.out.println("An error occurred while saving data to data.json");
        }
    }

    public static void loadData() {
        try {
            Gson gson = new Gson();
            String jsonData = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(FILEPATH)));
            DataWrapper wrapper = gson.fromJson(jsonData, DataWrapper.class);
            if (wrapper != null) {
                players = wrapper.players != null ? wrapper.players : new ArrayList<>();
                matches = wrapper.matches != null ? wrapper.matches : new ArrayList<>();
                tempSets = wrapper.tempSets != null ? wrapper.tempSets : new ArrayList<>();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading data from data.json");
        }
    }

    private static DataWrapper DataToWrapper() {
        DataWrapper wrapper = new DataWrapper();
        wrapper.players = players;
        wrapper.matches = matches;
        wrapper.tempSets = tempSets;

        return wrapper;
    }

    private static class DataWrapper {
        ArrayList<Player> players;
        ArrayList<Match> matches;
        ArrayList<Set> tempSets;
    }
}