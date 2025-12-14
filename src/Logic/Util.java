package Logic;

import java.util.ArrayList;

import Model.Pokemon;

public class Util {
    
    public ArrayList<Pokemon> initializeStage1Pokemon() {
        ArrayList<Pokemon> list = new ArrayList<>();

        list.add(new Pokemon(1,"Bulbasaur",Types.GRASS,Types.POISON));
        list.add(new Pokemon(2,"Ivysaur",Types.GRASS,Types.POISON));
        list.add(new Pokemon(3,"Venusaur",Types.GRASS,Types.POISON));

        list.add(new Pokemon(10,"Caterpie",Types.BUG));
        list.add(new Pokemon(11,"Metapod",Types.BUG));
        list.add(new Pokemon(12,"Butterfree",Types.BUG,Types.FLYING));

        list.add(new Pokemon(13,"Weedle",Types.BUG,Types.POISON));
        list.add(new Pokemon(14,"Kakuna",Types.BUG,Types.POISON));
        list.add(new Pokemon(15,"Beedrill",Types.BUG,Types.POISON));

        list.add(new Pokemon(16,"Pidgey",Types.NORMAL,Types.FLYING));
        list.add(new Pokemon(17,"Pidgeotto",Types.NORMAL,Types.FLYING));
        list.add(new Pokemon(18,"Pidgeot",Types.NORMAL,Types.FLYING));

        list.add(new Pokemon(43,"Oddish",Types.GRASS,Types.POISON));
        list.add(new Pokemon(44,"Gloom",Types.GRASS,Types.POISON));
        list.add(new Pokemon(45,"Vileplume",Types.GRASS,Types.POISON));
        list.add(new Pokemon(182,"Bellossom",Types.GRASS));

        list.add(new Pokemon(69,"Bellsprout",Types.GRASS,Types.POISON));
        list.add(new Pokemon(70,"Weepinbell",Types.GRASS,Types.POISON));
        list.add(new Pokemon(71,"Victreebel",Types.GRASS,Types.POISON));

        return list;
    }

    public ArrayList<Pokemon> initializeStage2Pokemon() {
        ArrayList<Pokemon> list = new ArrayList<>();

        list.add(new Pokemon(27,"Sandshrew",Types.GROUND));
        list.add(new Pokemon(28,"Sandslash",Types.GROUND));

        list.add(new Pokemon(74,"Geodude",Types.ROCK,Types.GROUND));
        list.add(new Pokemon(75,"Graveler",Types.ROCK,Types.GROUND));
        list.add(new Pokemon(76,"Golem",Types.ROCK,Types.GROUND));

        list.add(new Pokemon(95,"Onix",Types.ROCK,Types.GROUND));
        list.add(new Pokemon(208,"Steelix",Types.STEEL,Types.GROUND));

        list.add(new Pokemon(104,"Cubone",Types.GROUND));
        list.add(new Pokemon(105,"Marowak",Types.GROUND));

        list.add(new Pokemon(111,"Rhyhorn",Types.GROUND,Types.ROCK));
        list.add(new Pokemon(112,"Rhydon",Types.GROUND,Types.ROCK));

        return list;
    }

    public ArrayList<Pokemon> initializeStage3Pokemon() {
        ArrayList<Pokemon> list = new ArrayList<>();

        list.add(new Pokemon(7,"Squirtle",Types.WATER));
        list.add(new Pokemon(8,"Wartortle",Types.WATER));
        list.add(new Pokemon(9,"Blastoise",Types.WATER));

        list.add(new Pokemon(54,"Psyduck",Types.WATER));
        list.add(new Pokemon(55,"Golduck",Types.WATER));

        list.add(new Pokemon(60,"Poliwag",Types.WATER));
        list.add(new Pokemon(61,"Poliwhirl",Types.WATER));
        list.add(new Pokemon(62,"Poliwrath",Types.WATER,Types.FIGHTING));
        list.add(new Pokemon(186,"Politoed",Types.WATER));

        list.add(new Pokemon(72,"Tentacool",Types.WATER,Types.POISON));
        list.add(new Pokemon(73,"Tentacruel",Types.WATER,Types.POISON));

        list.add(new Pokemon(116,"Horsea",Types.WATER));
        list.add(new Pokemon(117,"Seadra",Types.WATER));
        list.add(new Pokemon(230,"Kingdra",Types.WATER,Types.DRAGON));

        list.add(new Pokemon(129,"Magikarp",Types.WATER));
        list.add(new Pokemon(130,"Gyarados",Types.WATER,Types.FLYING));

        return list;
    }

    public ArrayList<Pokemon> initializeStage4Pokemon() {
        ArrayList<Pokemon> list = new ArrayList<>();

        list.add(new Pokemon(35,"Clefairy",Types.FAIRY));
        list.add(new Pokemon(36,"Clefable",Types.FAIRY));
        list.add(new Pokemon(173,"Cleffa",Types.FAIRY));

        list.add(new Pokemon(39,"Jigglypuff",Types.NORMAL,Types.FAIRY));
        list.add(new Pokemon(40,"Wigglytuff",Types.NORMAL,Types.FAIRY));
        list.add(new Pokemon(174,"Igglybuff",Types.FAIRY));

        list.add(new Pokemon(96,"Drowzee",Types.PSYCHIC));
        list.add(new Pokemon(97,"Hypno",Types.PSYCHIC));

        list.add(new Pokemon(124,"Jynx",Types.ICE,Types.PSYCHIC));

        list.add(new Pokemon(144,"Articuno",Types.ICE,Types.FLYING));

        return list;
    }

    public ArrayList<Pokemon> initializeStage5Pokemon() {
        ArrayList<Pokemon> list = new ArrayList<>();

        list.add(new Pokemon(23,"Ekans",Types.POISON));
        list.add(new Pokemon(24,"Arbok",Types.POISON));

        list.add(new Pokemon(29,"Nidoran♀",Types.POISON));
        list.add(new Pokemon(30,"Nidorina",Types.POISON));
        list.add(new Pokemon(31,"Nidoqueen",Types.POISON,Types.GROUND));

        list.add(new Pokemon(32,"Nidoran♂",Types.POISON));
        list.add(new Pokemon(33,"Nidorino",Types.POISON));
        list.add(new Pokemon(34,"Nidoking",Types.POISON,Types.GROUND));

        list.add(new Pokemon(41,"Zubat",Types.POISON,Types.FLYING));
        list.add(new Pokemon(42,"Golbat",Types.POISON,Types.FLYING));
        list.add(new Pokemon(169,"Crobat",Types.POISON,Types.FLYING));

        list.add(new Pokemon(88,"Grimer",Types.POISON));
        list.add(new Pokemon(89,"Muk",Types.POISON));

        list.add(new Pokemon(109,"Koffing",Types.POISON));
        list.add(new Pokemon(110,"Weezing",Types.POISON));

        return list;
    }

    public ArrayList<Pokemon> initializeStage6Pokemon() {
        ArrayList<Pokemon> list = new ArrayList<>();

        list.add(new Pokemon(4,"Charmander",Types.FIRE));
        list.add(new Pokemon(5,"Charmeleon",Types.FIRE));
        list.add(new Pokemon(6,"Charizard",Types.FIRE,Types.FLYING));

        list.add(new Pokemon(37,"Vulpix",Types.FIRE));
        list.add(new Pokemon(38,"Ninetales",Types.FIRE));
        list.add(new Pokemon(58,"Growlithe",Types.FIRE));
        list.add(new Pokemon(59,"Arcanine",Types.FIRE));
        list.add(new Pokemon(126,"Magmar",Types.FIRE));
        list.add(new Pokemon(240,"Magby",Types.FIRE));

        list.add(new Pokemon(25,"Pikachu",Types.ELECTRIC));
        list.add(new Pokemon(26,"Raichu",Types.ELECTRIC));
        list.add(new Pokemon(172,"Pichu",Types.ELECTRIC));

        list.add(new Pokemon(125,"Electabuzz",Types.ELECTRIC));
        list.add(new Pokemon(239,"Elekid",Types.ELECTRIC));

        list.add(new Pokemon(145,"Zapdos",Types.ELECTRIC,Types.FLYING));
        list.add(new Pokemon(146,"Moltres",Types.FIRE,Types.FLYING));

        return list;
    }

    // Helper method to get a Pokemon by ID
    // Searches through all stage pokemon lists
    @SuppressWarnings("unchecked")
    public Pokemon getPokemonById(int id) {
        // Check all stages
        ArrayList<Pokemon>[] allStages = (ArrayList<Pokemon>[]) new ArrayList[]{
            initializeStage1Pokemon(),
            initializeStage2Pokemon(),
            initializeStage3Pokemon(),
            initializeStage4Pokemon(),
            initializeStage5Pokemon(),
            initializeStage6Pokemon()
        };

        for (ArrayList<Pokemon> stageList : allStages) {
            for (Pokemon p : stageList) {
                if (p.pokemonID == id) {
                    // Return a new instance with duplicates = 1
                    Pokemon result;
                    if (p.type2 != null) {
                        result = new Pokemon(p.pokemonID, p.name, p.type, p.type2);
                    } else {
                        result = new Pokemon(p.pokemonID, p.name, p.type);
                    }
                    return result;
                }
            }
        }
        return null;
    }
}
