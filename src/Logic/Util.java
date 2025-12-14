package Logic;

import java.util.ArrayList;
import pkmn.Pokemon;

public class Util {

    // 🌿 Stage 1: Grass / Forest / Bug / Early Normal/Flying
    public ArrayList<Pokemon> initializeStage1Pokemon() {
        ArrayList<Pokemon> list = new ArrayList<>();

        // Grass starters
        list.add(new Pokemon(1,"Bulbasaur",Types.GRASS,Types.POISON));
        list.add(new Pokemon(2,"Ivysaur",Types.GRASS,Types.POISON));
        list.add(new Pokemon(3,"Venusaur",Types.GRASS,Types.POISON));

        // Bug Pokémon
        list.add(new Pokemon(10,"Caterpie",Types.BUG));
        list.add(new Pokemon(11,"Metapod",Types.BUG));
        list.add(new Pokemon(12,"Butterfree",Types.BUG,Types.FLYING));
        list.add(new Pokemon(13,"Weedle",Types.BUG,Types.POISON));
        list.add(new Pokemon(14,"Kakuna",Types.BUG,Types.POISON));
        list.add(new Pokemon(15,"Beedrill",Types.BUG,Types.POISON));
        list.add(new Pokemon(46,"Paras",Types.BUG,Types.GRASS));
        list.add(new Pokemon(47,"Parasect",Types.BUG,Types.GRASS));
        list.add(new Pokemon(48,"Venonat",Types.BUG,Types.POISON));
        list.add(new Pokemon(49,"Venomoth",Types.BUG,Types.POISON));
        list.add(new Pokemon(123,"Scyther",Types.BUG,Types.FLYING));

        // Normal / Flying
        list.add(new Pokemon(16,"Pidgey",Types.NORMAL,Types.FLYING));
        list.add(new Pokemon(17,"Pidgeotto",Types.NORMAL,Types.FLYING));
        list.add(new Pokemon(18,"Pidgeot",Types.NORMAL,Types.FLYING));
        list.add(new Pokemon(19,"Rattata",Types.NORMAL));
        list.add(new Pokemon(20,"Raticate",Types.NORMAL));
        list.add(new Pokemon(21,"Spearow",Types.NORMAL,Types.FLYING));
        list.add(new Pokemon(22,"Fearow",Types.NORMAL,Types.FLYING));

        // Grass / Poison wilds
        list.add(new Pokemon(43,"Oddish",Types.GRASS,Types.POISON));
        list.add(new Pokemon(44,"Gloom",Types.GRASS,Types.POISON));
        list.add(new Pokemon(45,"Vileplume",Types.GRASS,Types.POISON));
        list.add(new Pokemon(69,"Bellsprout",Types.GRASS,Types.POISON));
        list.add(new Pokemon(70,"Weepinbell",Types.GRASS,Types.POISON));
        list.add(new Pokemon(71,"Victreebel",Types.GRASS,Types.POISON));
        list.add(new Pokemon(102,"Exeggcute",Types.GRASS,Types.PSYCHIC));
        list.add(new Pokemon(103,"Exeggutor",Types.GRASS,Types.PSYCHIC));

        // Normal / Fairy
        list.add(new Pokemon(35,"Clefairy",Types.FAIRY));
        list.add(new Pokemon(36,"Clefable",Types.FAIRY));
        list.add(new Pokemon(39,"Jigglypuff",Types.NORMAL,Types.FAIRY));
        list.add(new Pokemon(40,"Wigglytuff",Types.NORMAL,Types.FAIRY));
        list.add(new Pokemon(108,"Lickitung",Types.NORMAL));

        return list;
    }

    // 🪨 Stage 2: Cave / Rock / Ground / Poison / Fighting
    public ArrayList<Pokemon> initializeStage2Pokemon() {
        ArrayList<Pokemon> list = new ArrayList<>();

        // Ground / Rock
        list.add(new Pokemon(27,"Sandshrew",Types.GROUND));
        list.add(new Pokemon(28,"Sandslash",Types.GROUND));
        list.add(new Pokemon(74,"Geodude",Types.ROCK,Types.GROUND));
        list.add(new Pokemon(75,"Graveler",Types.ROCK,Types.GROUND));
        list.add(new Pokemon(76,"Golem",Types.ROCK,Types.GROUND));
        list.add(new Pokemon(95,"Onix",Types.ROCK,Types.GROUND));
        list.add(new Pokemon(111,"Rhyhorn",Types.GROUND,Types.ROCK));
        list.add(new Pokemon(112,"Rhydon",Types.GROUND,Types.ROCK));
        list.add(new Pokemon(104,"Cubone",Types.GROUND));
        list.add(new Pokemon(105,"Marowak",Types.GROUND));

        // Poison
        list.add(new Pokemon(23,"Ekans",Types.POISON));
        list.add(new Pokemon(24,"Arbok",Types.POISON));
        list.add(new Pokemon(88,"Grimer",Types.POISON));
        list.add(new Pokemon(89,"Muk",Types.POISON));
        list.add(new Pokemon(109,"Koffing",Types.POISON));
        list.add(new Pokemon(110,"Weezing",Types.POISON));

        // Fighting
        list.add(new Pokemon(56,"Mankey",Types.FIGHTING));
        list.add(new Pokemon(57,"Primeape",Types.FIGHTING));
        list.add(new Pokemon(66,"Machop",Types.FIGHTING));
        list.add(new Pokemon(67,"Machoke",Types.FIGHTING));
        list.add(new Pokemon(68,"Machamp",Types.FIGHTING));
        list.add(new Pokemon(106,"Hitmonlee",Types.FIGHTING));
        list.add(new Pokemon(107,"Hitmonchan",Types.FIGHTING));

        // Psychic / Cave misc
        list.add(new Pokemon(96,"Drowzee",Types.PSYCHIC));
        list.add(new Pokemon(97,"Hypno",Types.PSYCHIC));
        list.add(new Pokemon(122,"Mr. Mime",Types.PSYCHIC,Types.FAIRY));

        // Water (cave/wet)
        list.add(new Pokemon(86,"Seel",Types.WATER));
        list.add(new Pokemon(87,"Dewgong",Types.WATER,Types.ICE));

        // Others
        list.add(new Pokemon(84,"Doduo",Types.NORMAL,Types.FLYING));
        list.add(new Pokemon(85,"Dodrio",Types.NORMAL,Types.FLYING));
        list.add(new Pokemon(83,"Farfetch'd",Types.NORMAL,Types.FLYING));

        return list;
    }

    // 💧 Stage 3: Water / Ice / Coastal / Sea routes
    public ArrayList<Pokemon> initializeStage3Pokemon() {
        ArrayList<Pokemon> list = new ArrayList<>();

        // Squirtle line
        list.add(new Pokemon(7,"Squirtle",Types.WATER));
        list.add(new Pokemon(8,"Wartortle",Types.WATER));
        list.add(new Pokemon(9,"Blastoise",Types.WATER));

        // Psyduck line
        list.add(new Pokemon(54,"Psyduck",Types.WATER));
        list.add(new Pokemon(55,"Golduck",Types.WATER));

        // Poliwag line
        list.add(new Pokemon(60,"Poliwag",Types.WATER));
        list.add(new Pokemon(61,"Poliwhirl",Types.WATER));
        list.add(new Pokemon(62,"Poliwrath",Types.WATER,Types.FIGHTING));

        // Tentacool line
        list.add(new Pokemon(72,"Tentacool",Types.WATER,Types.POISON));
        list.add(new Pokemon(73,"Tentacruel",Types.WATER,Types.POISON));

        // Horsea line
        list.add(new Pokemon(116,"Horsea",Types.WATER));
        list.add(new Pokemon(117,"Seadra",Types.WATER));
        list.add(new Pokemon(129,"Magikarp",Types.WATER));
        list.add(new Pokemon(130,"Gyarados",Types.WATER,Types.FLYING));

        // Krabby line
        list.add(new Pokemon(98,"Krabby",Types.WATER));
        list.add(new Pokemon(99,"Kingler",Types.WATER));

        // Goldeen line
        list.add(new Pokemon(118,"Goldeen",Types.WATER));
        list.add(new Pokemon(119,"Seaking",Types.WATER));

        // Staryu line
        list.add(new Pokemon(120,"Staryu",Types.WATER));
        list.add(new Pokemon(121,"Starmie",Types.WATER,Types.PSYCHIC));

        // Omanyte / Kabuto
        list.add(new Pokemon(138,"Omanyte",Types.ROCK,Types.WATER));
        list.add(new Pokemon(139,"Omastar",Types.ROCK,Types.WATER));
        list.add(new Pokemon(140,"Kabuto",Types.ROCK,Types.WATER));
        list.add(new Pokemon(141,"Kabutops",Types.ROCK,Types.WATER));

        // Lapras
        list.add(new Pokemon(131,"Lapras",Types.WATER,Types.ICE));

        return list;
    }

    // 🔥 Stage 4: Fire / Electric / Special / Legendary / Endgame
    public ArrayList<Pokemon> initializeStage4Pokemon() {
        ArrayList<Pokemon> list = new ArrayList<>();

        // Charmander line
        list.add(new Pokemon(4,"Charmander",Types.FIRE));
        list.add(new Pokemon(5,"Charmeleon",Types.FIRE));
        list.add(new Pokemon(6,"Charizard",Types.FIRE,Types.FLYING));

        // Vulpix line
        list.add(new Pokemon(37,"Vulpix",Types.FIRE));
        list.add(new Pokemon(38,"Ninetales",Types.FIRE));

        // Growlithe line
        list.add(new Pokemon(58,"Growlithe",Types.FIRE));
        list.add(new Pokemon(59,"Arcanine",Types.FIRE));

        // Pikachu line
        list.add(new Pokemon(25,"Pikachu",Types.ELECTRIC));
        list.add(new Pokemon(26,"Raichu",Types.ELECTRIC));
        list.add(new Pokemon(100,"Voltorb",Types.ELECTRIC));
        list.add(new Pokemon(101,"Electrode",Types.ELECTRIC));
        list.add(new Pokemon(125,"Electabuzz",Types.ELECTRIC));

        // Legendary Birds
        list.add(new Pokemon(144,"Articuno",Types.ICE,Types.FLYING));
        list.add(new Pokemon(145,"Zapdos",Types.ELECTRIC,Types.FLYING));
        list.add(new Pokemon(146,"Moltres",Types.FIRE,Types.FLYING));

        // Dragon line
        list.add(new Pokemon(147,"Dratini",Types.DRAGON));
        list.add(new Pokemon(148,"Dragonair",Types.DRAGON));
        list.add(new Pokemon(149,"Dragonite",Types.DRAGON,Types.FLYING));

        // Mewtwo / Mew
        list.add(new Pokemon(150,"Mewtwo",Types.PSYCHIC));
        list.add(new Pokemon(151,"Mew",Types.PSYCHIC));

        // Others: Eevee evolutions
        list.add(new Pokemon(133,"Eevee",Types.NORMAL));
        list.add(new Pokemon(134,"Vaporeon",Types.WATER));
        list.add(new Pokemon(135,"Jolteon",Types.ELECTRIC));
        list.add(new Pokemon(136,"Flareon",Types.FIRE));

        // Magmar, Pinsir, Tauros
        list.add(new Pokemon(126,"Magmar",Types.FIRE));
        list.add(new Pokemon(127,"Pinsir",Types.BUG));
        list.add(new Pokemon(128,"Tauros",Types.NORMAL));

        // Snorlax
        list.add(new Pokemon(143,"Snorlax",Types.NORMAL));

        // Aerodactyl
        list.add(new Pokemon(142,"Aerodactyl",Types.ROCK,Types.FLYING));

        // Others
        list.add(new Pokemon(132,"Ditto",Types.NORMAL));
        list.add(new Pokemon(124,"Jynx",Types.ICE,Types.PSYCHIC));

        return list;
    }

    // Helper to get Pokémon by ID
    public Pokemon getPokemonById(int id) {
        @SuppressWarnings("unchecked")
        ArrayList<Pokemon>[] allStages = new ArrayList[]{
            initializeStage1Pokemon(),
            initializeStage2Pokemon(),
            initializeStage3Pokemon(),
            initializeStage4Pokemon()
        };

        for (ArrayList<Pokemon> stageList : allStages) {
            for (Pokemon p : stageList) {
                if (p.pokemonID == id) {
                    if (p.type2 != null)
                        return new Pokemon(p.pokemonID,p.name,p.type,p.type2);
                    else
                        return new Pokemon(p.pokemonID,p.name,p.type);
                }
            }
        }
        return null;
    }
}
