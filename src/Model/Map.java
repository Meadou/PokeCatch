package Model;
import java.util.ArrayList;

public class Map {

    public static final ArrayList<Pokemon> GRASS   = new ArrayList<>();
    public static final ArrayList<Pokemon> CAVE    = new ArrayList<>();
    public static final ArrayList<Pokemon> BEACH   = new ArrayList<>();
    public static final ArrayList<Pokemon> VOLCANO = new ArrayList<>();
    public static final ArrayList<Pokemon> SWAMP   = new ArrayList<>();
    public static final ArrayList<Pokemon> SNOW = new ArrayList<>();

    static {
        ArrayList<Pokemon> all = new Util().initializePokemon();
        for (Pokemon p : all) {
            Types t1 = p.type;
            Types t2 = p.type2;

            if (hasType(t1, t2, Types.GRASS, Types.BUG, Types.NORMAL))GRASS.add(p);
            if (hasType(t1, t2, Types.ROCK, Types.GROUND, Types.POISON, Types.STEEL, Types.FIGHTING, Types.PSYCHIC, Types.ELECTRIC))CAVE.add(p);
            if (hasType(t1, t2, Types.WATER, Types.GROUND, Types.ROCK))BEACH.add(p);
            if (hasType(t1, t2, Types.FIRE, Types.ROCK, Types.GROUND, Types.DRAGON))VOLCANO.add(p);
            if (hasType(t1, t2, Types.WATER, Types.GRASS, Types.POISON, Types.BUG))SWAMP.add(p);
            if (hasType(t1, t2, Types.ICE, Types.STEEL)) SNOW.add(p);
        }
    }

    private static boolean hasType(Types type1, Types type2, Types... allowed) {
        for (Types t : allowed) {
            if (type1 == t || (type2 != null && type2 == t)) {
                return true;
            }
        }
        return false;
    }
}