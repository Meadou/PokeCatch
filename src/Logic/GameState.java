package Logic;

import java.util.ArrayList;

import pkmn.Pokemon;

public class GameState {

    private static GameState instance;
    private static Stage currentStage;
    private int globalScore;
    private ArrayList<Pokemon> caughtPokemonArray; // Temporary array for caught pokemon during stage
    private PokemonBST globalPokemonBST; // Global BST storing all caught pokemon

    public GameState() {
        globalScore = 0;
        caughtPokemonArray = new ArrayList<>();
        globalPokemonBST = new PokemonBST();
        currentStage = null;
    }

    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static void setCurrenStage(Stage stage) {
        currentStage = stage;
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void addScore(int points) {
        globalScore += points;
    }

    public int getGlobalScore() {
        return globalScore;
    }

    public void reset() {
        globalScore = 0;
        caughtPokemonArray.clear();
        globalPokemonBST = new PokemonBST();
    }

    // Add pokemon to the temporary caught array (during stage play)
    public void addCaughtPokemon(Pokemon pokemon) {
        if (pokemon != null) {
            caughtPokemonArray.add(pokemon);
        }
    }

    // Transfer all pokemon from array to BST, then clear array
    // This is called after stage ends (game over or time up)
    public void transferCaughtPokemonToBST() {
        for (Pokemon pokemon : caughtPokemonArray) {
            // BST insert handles duplicates by incrementing counter
            globalPokemonBST.insert(pokemon);
        }
        // Clear the array after transferring
        caughtPokemonArray.clear();
    }

    // Get the global BST (for Pokedex)
    public PokemonBST getGlobalPokemonBST() {
        return globalPokemonBST;
    }

    // Get current caught pokemon array (for debugging/testing)
    public ArrayList<Pokemon> getCaughtPokemonArray() {
        return caughtPokemonArray;
    }
    // example condition for unlocking ending
    public boolean isUnlocked() {
        return globalScore >= 5000; // Example threshold for unlocking ending
    }

    
}

