package pkmn;

import java.util.ArrayList;

public class Stage {
    public int score;
    public String stageName;
    public ArrayList<Pokemon> stagePokemon;
    public boolean isUnlocked;

    public Stage(String name, ArrayList<Pokemon> stagePokemon, boolean isUnlocked) {
        this.stageName = name;
        this.stagePokemon = stagePokemon;
        this.isUnlocked = isUnlocked;
    }
}
