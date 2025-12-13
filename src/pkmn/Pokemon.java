package pkmn;

public class Pokemon {
    public int pokemonID;
    public String name;
    public Types type;
    public Types type2;
    public int duplicates;
    
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


