package Logic;

import java.util.ArrayList;
import java.util.List;
import pkmn.Pokemon;

public class PokemonBST {
    private PokeTreeNode root;
    private int size;

    public PokemonBST() {
        this.root = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(Pokemon p) {
        if (p == null) return;
        root = insertRecursive(root, p);
    }

    private PokeTreeNode insertRecursive(PokeTreeNode node, Pokemon p) {
        if (node == null) {
            size++;
            return new PokeTreeNode(p);
        }

        if (p.pokemonID < node.pokemon.pokemonID) {
            node.left = insertRecursive(node.left, p);
        } else if (p.pokemonID > node.pokemon.pokemonID) {
            node.right = insertRecursive(node.right, p);
        } else {
            node.pokemon.incrementDuplicates();
        }

        return node;
    }

    public Pokemon search(int id) {
        PokeTreeNode node = searchNode(root, id);
        return node == null ? null : node.pokemon;
    }

    private PokeTreeNode searchNode(PokeTreeNode node, int id) {
        if (node == null) return null;
        if (id == node.pokemon.pokemonID) return node;
        if (id < node.pokemon.pokemonID) return searchNode(node.left, id);
        return searchNode(node.right, id);
    }

    public boolean delete(int id) {
        int oldSize = size;
        root = deleteRecursive(root, id);
        return size < oldSize;
    }

    private PokeTreeNode deleteRecursive(PokeTreeNode node, int id) {
        if (node == null) return null;

        if (id < node.pokemon.pokemonID) {
            node.left = deleteRecursive(node.left, id);
        } else if (id > node.pokemon.pokemonID) {
            node.right = deleteRecursive(node.right, id);
        } else {
            if (node.pokemon.getDuplicates() > 1) {
                node.pokemon.decrementDuplicates();
                return node;
            }

            size--;

            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            PokeTreeNode successor = findMin(node.right);
            node.pokemon = successor.pokemon;
            node.right = deleteRecursive(node.right, successor.pokemon.pokemonID);
        }

        return node;
    }

    private PokeTreeNode findMin(PokeTreeNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public List<Pokemon> inOrder() {
        List<Pokemon> list = new ArrayList<>();
        inOrderRecursive(root, list);
        return list;
    }

    private void inOrderRecursive(PokeTreeNode node, List<Pokemon> out) {
        if (node == null) return;
        inOrderRecursive(node.left, out);
        out.add(node.pokemon);
        inOrderRecursive(node.right, out);
    }

    public void printInOrder() {
        for (Pokemon p : inOrder()) {
            System.out.println(p.pokemonID + ": " + p.getName() + " (dup=" + p.getDuplicates() + ")");
        }
    }

    public PokeTreeNode getRoot() {
        return root;
    }

    public void balance() {
        if (root == null) return;

        List<Pokemon> sortedList = inOrder();

        root = null;
        size = 0;

        buildBalanced(sortedList, 0, sortedList.size() - 1);
    }

    private void buildBalanced(List<Pokemon> list, int start, int end) {
        if (start > end) return;
        int mid = start + (end - start) / 2;
        insert(list.get(mid));
        buildBalanced(list, start, mid - 1);
        buildBalanced(list, mid + 1, end);
    }

    public PokemonBST filterByType(String type) {
        if (type.equals("All")) {
            return this;
        }
        
        PokemonBST filtered = new PokemonBST();
        List<Pokemon> allPokemon = inOrder();
        
        for (Pokemon p : allPokemon) {
            if ((p.type != null && p.type.toString().equals(type)) || 
                (p.type2 != null && p.type2.toString().equals(type))) {
                filtered.insert(p);
            }
        }
        
        return filtered;
    }
}
