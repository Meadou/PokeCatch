package Logic;

import java.util.ArrayList;
import java.util.List;
import Model.Pokemon;

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
            // duplicate ID: increase duplicates counter if available
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
            // found node to delete
            // if duplicates > 1, just decrement
            if (node.pokemon.getDuplicates() > 1) {
                node.pokemon.decrementDuplicates();
                return node;
            }

            size--;

            // node with only one child or no child
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // node with two children: get inorder successor (smallest in right)
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

    private void inOrderRecursive(PokeTreeNode node, List<Model.Pokemon> out) {
        if (node == null) return;
        inOrderRecursive(node.left, out);
        out.add(node.pokemon);
        inOrderRecursive(node.right, out);
    }

    // helper to print tree contents
    public void printInOrder() {
        for (Model.Pokemon p : inOrder()) {
            System.out.println(p.pokemonID + ": " + p.getName() + " (dup=" + p.getDuplicates() + ")");
        }
    }

    // expose root for visualization
    public PokeTreeNode getRoot() {
        return root;
    }

    // Balance the tree using median-insertion method
    public void balance() {
        if (root == null) return;

        List<Pokemon> sortedList = inOrder();

        // Clear the tree
        root = null;
        size = 0;

        // Rebuild balanced
        buildBalanced(sortedList, 0, sortedList.size() - 1);
    }

    private void buildBalanced(List<Pokemon> list, int start, int end) {
        if (start > end) return;
        int mid = start + (end - start) / 2;
        insert(list.get(mid));
        buildBalanced(list, start, mid - 1);
        buildBalanced(list, mid + 1, end);
    }

    // Filter tree by Pokemon type
    public PokemonBST filterByType(String type) {
        if (type.equals("All")) {
            return this;
        }
        
        PokemonBST filtered = new PokemonBST();
        List<Pokemon> allPokemon = inOrder();
        
        for (Pokemon p : allPokemon) {
            // Check if type matches (handle null type2)
            if (p.type != null && p.type.toString().equals(type)) {
                filtered.insert(p);
            }
        }
        
        return filtered;
    }
}
