package Logic;

import pkmn.Pokemon;

public class PokeTreeNode {
    public Pokemon pokemon;
    public PokeTreeNode left;
    public PokeTreeNode right;
    // position for rendering
    public int x;
    public int y;

    public PokeTreeNode(Pokemon pokemon) {
        this.pokemon = pokemon;
        this.left = null;
        this.right = null;
    }
}
