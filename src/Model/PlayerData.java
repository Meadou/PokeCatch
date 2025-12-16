package Model;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    public String playerName;
    public int starterPokemonId;
    public int currentStage;
    public List<Integer> unlockedStages;
    public List<Integer> caughtPokemonIds; 
    public int score;

    public PlayerData() {
        this.playerName = "";
        this.starterPokemonId = -1;
        this.currentStage = 1;
        this.unlockedStages = new ArrayList<>();
        this.unlockedStages.add(1);
        this.caughtPokemonIds = new ArrayList<>();
        this.score = 0;
    }

    public PlayerData(String name, int starterId) {
        this.playerName = name;
        this.starterPokemonId = starterId;
        this.currentStage = 1;
        this.unlockedStages = new ArrayList<>();
        this.unlockedStages.add(1); 
        this.caughtPokemonIds = new ArrayList<>();
        this.score = 0;
    }

    public void calculateScore() {
        int uniquePokemonCount = caughtPokemonIds.size();
        int stagesCompleted = unlockedStages.size();
        this.score = uniquePokemonCount + stagesCompleted;
    }

    public void addCaughtPokemon(int pokemonId) {
        if (!caughtPokemonIds.contains(pokemonId)) {
            caughtPokemonIds.add(pokemonId);
        }
    }

    public void unlockStage(int stageNumber) {
        if (!unlockedStages.contains(stageNumber)) {
            unlockedStages.add(stageNumber);
        }
    }
}

