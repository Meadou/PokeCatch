package Model;

import java.util.ArrayList;


public class Util {
    
    public ArrayList<Pokemon> initializePokemon() {
        ArrayList<Pokemon> pokemonList = new ArrayList<>();
            pokemonList.add(new Pokemon(1, "Bulbasaur", Types.GRASS, Types.POISON));
            pokemonList.add(new Pokemon(2, "Ivysaur", Types.GRASS, Types.POISON));
            pokemonList.add(new Pokemon(3, "Venusaur", Types.GRASS, Types.POISON));
            pokemonList.add(new Pokemon(4, "Charmander", Types.FIRE));
            pokemonList.add(new Pokemon(5, "Charmeleon", Types.FIRE));
            pokemonList.add(new Pokemon(6, "Charizard", Types.FIRE, Types.FLYING));
            pokemonList.add(new Pokemon(7, "Squirtle", Types.WATER));
            pokemonList.add(new Pokemon(8, "Wartortle", Types.WATER));
            pokemonList.add(new Pokemon(9, "Blastoise", Types.WATER));
            pokemonList.add(new Pokemon(10, "Caterpie", Types.BUG));
            pokemonList.add(new Pokemon(11, "Metapod", Types.BUG));
            pokemonList.add(new Pokemon(12, "Butterfree", Types.BUG, Types.FLYING));
            pokemonList.add(new Pokemon(13, "Weedle", Types.BUG, Types.POISON));
            pokemonList.add(new Pokemon(14, "Kakuna", Types.BUG, Types.POISON));
            pokemonList.add(new Pokemon(15, "Beedrill", Types.BUG, Types.POISON));
            pokemonList.add(new Pokemon(16, "Pidgey", Types.NORMAL, Types.FLYING));
            pokemonList.add(new Pokemon(17, "Pidgeotto", Types.NORMAL, Types.FLYING));
            pokemonList.add(new Pokemon(18, "Pidgeot", Types.NORMAL, Types.FLYING));
            pokemonList.add(new Pokemon(19, "Rattata", Types.NORMAL));
            pokemonList.add(new Pokemon(20, "Raticate", Types.NORMAL));
            pokemonList.add(new Pokemon(21, "Spearow", Types.NORMAL, Types.FLYING));
            pokemonList.add(new Pokemon(22, "Fearow", Types.NORMAL, Types.FLYING));
            pokemonList.add(new Pokemon(23, "Ekans", Types.POISON));
            pokemonList.add(new Pokemon(24, "Arbok", Types.POISON));
            pokemonList.add(new Pokemon(25, "Pikachu", Types.ELECTRIC));
            pokemonList.add(new Pokemon(26, "Raichu", Types.ELECTRIC));
            pokemonList.add(new Pokemon(27, "Sandshrew", Types.GROUND));
            pokemonList.add(new Pokemon(28, "Sandslash", Types.GROUND));
            pokemonList.add(new Pokemon(29, "Nidoran♀", Types.POISON));
            pokemonList.add(new Pokemon(30, "Nidorina", Types.POISON));
            pokemonList.add(new Pokemon(31, "Nidoqueen", Types.POISON, Types.GROUND));
            pokemonList.add(new Pokemon(32, "Nidoran♂", Types.POISON));
            pokemonList.add(new Pokemon(33, "Nidorino", Types.POISON));
            pokemonList.add(new Pokemon(34, "Nidoking", Types.POISON, Types.GROUND));
            pokemonList.add(new Pokemon(35, "Clefairy", Types.FAIRY));
            pokemonList.add(new Pokemon(36, "Clefable", Types.FAIRY));
            pokemonList.add(new Pokemon(37, "Vulpix", Types.FIRE));
            pokemonList.add(new Pokemon(38, "Ninetales", Types.FIRE));
            pokemonList.add(new Pokemon(39, "Jigglypuff", Types.NORMAL, Types.FAIRY));
            pokemonList.add(new Pokemon(40, "Wigglytuff", Types.NORMAL, Types.FAIRY));
            pokemonList.add(new Pokemon(41, "Zubat", Types.POISON, Types.FLYING));
            pokemonList.add(new Pokemon(42, "Golbat", Types.POISON, Types.FLYING));
            pokemonList.add(new Pokemon(43, "Oddish", Types.GRASS, Types.POISON));
            pokemonList.add(new Pokemon(44, "Gloom", Types.GRASS, Types.POISON));
            pokemonList.add(new Pokemon(45, "Vileplume", Types.GRASS, Types.POISON));
            pokemonList.add(new Pokemon(46, "Paras", Types.BUG, Types.GRASS));
            pokemonList.add(new Pokemon(47, "Parasect", Types.BUG, Types.GRASS));
            pokemonList.add(new Pokemon(48, "Venonat", Types.BUG, Types.POISON));
            pokemonList.add(new Pokemon(49, "Venomoth", Types.BUG, Types.POISON));

        return pokemonList;
    }
}
