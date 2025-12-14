package Logic;

import java.util.ArrayList;
import java.util.Random;

import Model.Pokemon;

public class Logic {
    ArrayList<Pokemon> list = new ArrayList<>();

    public Pokemon randomizer(ArrayList<Pokemon> list) {
        Random rand = new Random();
        int index = rand.nextInt(list.size());
        return list.get(index);
    }

    public void ListPokemon(Pokemon pokemon) {
    boolean duplicateFound = false;

    for (Pokemon p : list) {
        if (p.pokemonID == pokemon.pokemonID) {  
            p.duplicates++; 
            duplicateFound = true;
            break;           
        }
    }

    if (!duplicateFound) {
        list.add(pokemon);
    }
}
    public void displayList() {
        for (Pokemon pokemon2 : list) {
            System.out.println(pokemon2.name + ": "+ pokemon2.duplicates);
        }
        System.out.println();
    }
}
