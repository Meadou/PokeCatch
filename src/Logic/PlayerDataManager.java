package Logic;

import Model.PlayerData;
import java.util.List;
import pkmn.Pokemon;

public class PlayerDataManager {
    private static PlayerData currentPlayerData = null;

    public static void syncFromGameState(GameState gameState, String playerName, int starterId) {
        if (currentPlayerData == null) {
            currentPlayerData = new PlayerData(playerName, starterId);
        }
        
        if (playerName != null && !playerName.isEmpty()) {
            currentPlayerData.playerName = playerName;
        }
        if (starterId > 0) {
            currentPlayerData.starterPokemonId = starterId;
        }
        
        currentPlayerData.caughtPokemonIds.clear();
        List<Pokemon> allPokemon = gameState.getGlobalPokemonBST().inOrder();
        for (Pokemon p : allPokemon) {
            currentPlayerData.addCaughtPokemon(p.pokemonID);
        }
        
        currentPlayerData.score = gameState.getGlobalScore();
    }

    public static PlayerData loadAndSyncToGameState(GameState gameState, Util util) {
        currentPlayerData = FileHandler.loadPlayerData();
        
        if (currentPlayerData == null) {
            return null;
        }
        
        gameState.reset();
        for (int pokemonId : currentPlayerData.caughtPokemonIds) {
            Pokemon p = util.getPokemonById(pokemonId);
            if (p != null) {
                gameState.getGlobalPokemonBST().insert(p);
            }
        }
        
        gameState.addScore(currentPlayerData.score);
        
        return currentPlayerData;
    }

    public static void saveProgress(GameState gameState, int currentStage) {
        if (currentPlayerData == null) {
            return;
        }
        
        currentPlayerData.currentStage = currentStage;
        
        syncFromGameState(gameState, currentPlayerData.playerName, currentPlayerData.starterPokemonId);
        
        currentPlayerData.unlockStage(currentStage);
        
        FileHandler.savePlayerData(currentPlayerData);
    }

    public static PlayerData startNewGame(GameState gameState, String playerName, int starterId) {
        if (currentPlayerData != null && !currentPlayerData.playerName.isEmpty()) {
            syncFromGameState(gameState, currentPlayerData.playerName, currentPlayerData.starterPokemonId);
            FileHandler.saveToLeaderboards(currentPlayerData);
        }
        
        FileHandler.deleteSaveFile();
        
        currentPlayerData = new PlayerData(playerName, starterId);
        
        gameState.reset();
        
        return currentPlayerData;
    }

    public static PlayerData getCurrentPlayerData() {
        return currentPlayerData;
    }

    public static void setCurrentPlayerData(PlayerData data) {
        currentPlayerData = data;
    }
}

