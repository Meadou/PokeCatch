package Model;

public class Pokemon {
    public int pokemonID;
    String name;
    Types type;
    Types type2;
    int duplicates;

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
    }
}

enum Types {
    NORMAL,
    FIRE,
    WATER,
    GRASS,
    ELECTRIC,
    ICE,
    FIGHTING,
    POISON,
    GROUND,
    FLYING,
    PSYCHIC,
    BUG,
    ROCK,
    GHOST,
    DRAGON,
    STEEL,
}

