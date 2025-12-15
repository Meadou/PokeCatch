package Logic;

import Model.PlayerData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {
    private static final String SAVES_DIR = "saves";
    private static final String PLAYER_SAVE_FILE = SAVES_DIR + "/player_save.txt";
    private static final String LEADERBOARDS_FILE = SAVES_DIR + "/leaderboards.txt";

    // Ensure saves directory exists
    private static void ensureSavesDirectory() {
        File dir = new File(SAVES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Save current player progress
    public static void savePlayerData(PlayerData playerData) {
        ensureSavesDirectory();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(PLAYER_SAVE_FILE))) {
            writer.println("PLAYER_NAME:" + playerData.playerName);
            writer.println("STARTER_ID:" + playerData.starterPokemonId);
            writer.println("CURRENT_STAGE:" + playerData.currentStage);
            
            // Write unlocked stages
            writer.print("UNLOCKED_STAGES:");
            for (int i = 0; i < playerData.unlockedStages.size(); i++) {
                writer.print(playerData.unlockedStages.get(i));
                if (i < playerData.unlockedStages.size() - 1) {
                    writer.print(",");
                }
            }
            writer.println();
            
            // Write caught pokemon IDs
            writer.print("CAUGHT_POKEMON:");
            for (int i = 0; i < playerData.caughtPokemonIds.size(); i++) {
                writer.print(playerData.caughtPokemonIds.get(i));
                if (i < playerData.caughtPokemonIds.size() - 1) {
                    writer.print(",");
                }
            }
            writer.println();
            
            writer.println("SCORE:" + playerData.score);
            
        } catch (IOException e) {
            System.err.println("Error saving player data: " + e.getMessage());
        }
    }

    // Load current player progress
    public static PlayerData loadPlayerData() {
        ensureSavesDirectory();
        File file = new File(PLAYER_SAVE_FILE);
        
        if (!file.exists()) {
            return null; // No save file exists
        }

        try (Scanner scanner = new Scanner(file)) {
            PlayerData playerData = new PlayerData();
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(":", 2);
                if (parts.length != 2) continue;
                
                String key = parts[0];
                String value = parts[1];
                
                switch (key) {
                    case "PLAYER_NAME":
                        playerData.playerName = value;
                        break;
                    case "STARTER_ID":
                        playerData.starterPokemonId = Integer.parseInt(value);
                        break;
                    case "CURRENT_STAGE":
                        playerData.currentStage = Integer.parseInt(value);
                        break;
                    case "UNLOCKED_STAGES":
                        if (!value.isEmpty()) {
                            String[] stages = value.split(",");
                            playerData.unlockedStages = new ArrayList<>();
                            for (String stage : stages) {
                                playerData.unlockedStages.add(Integer.parseInt(stage.trim()));
                            }
                        }
                        break;
                    case "CAUGHT_POKEMON":
                        if (!value.isEmpty()) {
                            String[] pokemonIds = value.split(",");
                            playerData.caughtPokemonIds = new ArrayList<>();
                            for (String id : pokemonIds) {
                                playerData.caughtPokemonIds.add(Integer.parseInt(id.trim()));
                            }
                        }
                        break;
                    case "SCORE":
                        playerData.score = Integer.parseInt(value);
                        break;
                }
            }
            
            return playerData;
            
        } catch (FileNotFoundException e) {
            System.err.println("Save file not found: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error loading player data: " + e.getMessage());
            return null;
        }
    }

    // Save to leaderboards (append mode)
    public static void saveToLeaderboards(PlayerData playerData) {
        ensureSavesDirectory();
        
        // Use playerData.score (already set from GameState) for leaderboards
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEADERBOARDS_FILE, true))) {
            // Format: NAME|STARTER_ID|SCORE|UNIQUE_POKEMON|STAGES_COMPLETED
            int uniquePokemon = playerData.caughtPokemonIds.size();
            int stagesCompleted = playerData.unlockedStages.size();
            
            writer.println(playerData.playerName + "|" + 
                          playerData.starterPokemonId + "|" + 
                          playerData.score + "|" + 
                          uniquePokemon + "|" + 
                          stagesCompleted);
            
        } catch (IOException e) {
            System.err.println("Error saving to leaderboards: " + e.getMessage());
        }
    }

    // Load leaderboards
    public static List<LeaderboardEntry> loadLeaderboards() {
        ensureSavesDirectory();
        File file = new File(LEADERBOARDS_FILE);
        
        List<LeaderboardEntry> entries = new ArrayList<>();
        
        if (!file.exists()) {
            return entries; // Return empty list if no leaderboards file
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    LeaderboardEntry entry = new LeaderboardEntry();
                    entry.name = parts[0];
                    entry.starterId = Integer.parseInt(parts[1]);
                    entry.score = Integer.parseInt(parts[2]);
                    entry.uniquePokemon = Integer.parseInt(parts[3]);
                    entry.stagesCompleted = Integer.parseInt(parts[4]);
                    entries.add(entry);
                }
            }
            
            // Sort by score (descending)
            entries.sort((a, b) -> Integer.compare(b.score, a.score));
            
        } catch (FileNotFoundException e) {
            System.err.println("Leaderboards file not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error loading leaderboards: " + e.getMessage());
        }
        
        return entries;
    }

    // Helper class for leaderboard entries
    public static class LeaderboardEntry {
        public String name;
        public int starterId;
        public int score;
        public int uniquePokemon;
        public int stagesCompleted;
    }

    // Delete save file (for new game)
    public static void deleteSaveFile() {
        File file = new File(PLAYER_SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}

