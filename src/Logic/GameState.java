package Logic;

import java.util.ArrayList;
import pkmn.Pokemon;

public class GameState {

    private static GameState instance;
    private static Stage currentStage;
    private int globalScore;
    private ArrayList<Pokemon> caughtPokemonArray; 
    private PokemonBST globalPokemonBST;

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

    public void addCaughtPokemon(Pokemon pokemon) {
        if (pokemon != null) {
            caughtPokemonArray.add(pokemon);
        }
    }

    public void transferCaughtPokemonToBST() {
        for (Pokemon pokemon : caughtPokemonArray) {
            globalPokemonBST.insert(pokemon);
        }
        caughtPokemonArray.clear();
    }

    public PokemonBST getGlobalPokemonBST() {
        return globalPokemonBST;
    }

    public ArrayList<Pokemon> getCaughtPokemonArray() {
        return caughtPokemonArray;
    }
    public boolean isUnlocked() {
        return globalScore >= 5000;
    }

    
}

