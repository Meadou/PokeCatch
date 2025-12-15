package Logic;

import Model.PlayerData;
import java.util.List;
import pkmn.Pokemon;

public class PlayerDataManager {
    private static PlayerData currentPlayerData = null;

    // Initialize player data from GameState
    public static void syncFromGameState(GameState gameState, String playerName, int starterId) {
        if (currentPlayerData == null) {
            currentPlayerData = new PlayerData(playerName, starterId);
        }
        
        // Update player name and starter ID if provided
        if (playerName != null && !playerName.isEmpty()) {
            currentPlayerData.playerName = playerName;
        }
        if (starterId > 0) {
            currentPlayerData.starterPokemonId = starterId;
        }
        
        // Update caught pokemon from BST
        currentPlayerData.caughtPokemonIds.clear();
        List<Pokemon> allPokemon = gameState.getGlobalPokemonBST().inOrder();
        for (Pokemon p : allPokemon) {
            currentPlayerData.addCaughtPokemon(p.pokemonID);
        }
        
        // Update score from game state's global score (preserve actual gameplay score)
        currentPlayerData.score = gameState.getGlobalScore();
    }

    // Load player data and sync to GameState
    public static PlayerData loadAndSyncToGameState(GameState gameState, Util util) {
        currentPlayerData = FileHandler.loadPlayerData();
        
        if (currentPlayerData == null) {
            return null; // No save file
        }
        
        // Restore caught pokemon to BST
        gameState.reset();
        for (int pokemonId : currentPlayerData.caughtPokemonIds) {
            Pokemon p = util.getPokemonById(pokemonId);
            if (p != null) {
                gameState.getGlobalPokemonBST().insert(p);
            }
        }
        
        // Restore score
        gameState.addScore(currentPlayerData.score);
        
        return currentPlayerData;
    }

    // Save current progress
    public static void saveProgress(GameState gameState, int currentStage) {
        if (currentPlayerData == null) {
            return;
        }
        
        // Update current stage
        currentPlayerData.currentStage = currentStage;
        
        // Sync caught pokemon
        syncFromGameState(gameState, currentPlayerData.playerName, currentPlayerData.starterPokemonId);
        
        // Unlock the current stage if not already unlocked
        currentPlayerData.unlockStage(currentStage);
        
        // Save to file
        FileHandler.savePlayerData(currentPlayerData);
    }

    // Start new game: save current run to leaderboards, then reset
    public static PlayerData startNewGame(GameState gameState, String playerName, int starterId) {
        // If there's existing progress, save it to leaderboards first
        if (currentPlayerData != null && !currentPlayerData.playerName.isEmpty()) {
            // Sync latest data before saving to leaderboards
            syncFromGameState(gameState, currentPlayerData.playerName, currentPlayerData.starterPokemonId);
            FileHandler.saveToLeaderboards(currentPlayerData);
        }
        
        // Delete old save file
        FileHandler.deleteSaveFile();
        
        // Create new player data
        currentPlayerData = new PlayerData(playerName, starterId);
        
        // Reset game state
        gameState.reset();
        
        return currentPlayerData;
    }

    // Get current player data
    public static PlayerData getCurrentPlayerData() {
        return currentPlayerData;
    }

    // Set current player data
    public static void setCurrentPlayerData(PlayerData data) {
        currentPlayerData = data;
    }
}

