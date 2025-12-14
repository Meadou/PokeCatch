package pkmn;


import Logic.Types;

public class Pokemon {
    public int pokemonID;
    public String name;
    public Types type;
    public Types type2;
    public int duplicates;
    private String imagePath;

    public Pokemon(int id, String name, Types type, Types type2) {
        this.pokemonID = id;
        this.name = name;
        this.type = type;
        this.type2 = type2;
        this.duplicates = 1;
    }

    public Pokemon(int id, String name, Types type) {
        this.pokemonID = id;
        this.name = name;
        this.type = type;
        this.duplicates = 1;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return this.name;
    }

    public int getDuplicates() {
        return this.duplicates;
    }

    public void incrementDuplicates() {
        this.duplicates++;
    }

    public void decrementDuplicates() {
        if (this.duplicates > 0) this.duplicates--;
    }
}

