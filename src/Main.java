import javax.swing.*;
import Logic.GameState;
import Logic.Util;
import View.StageWindow;
import pkmn.Pokemon;
import ui.PokeGamePanel;
import Logic.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import ui.*;
public class Main {
    private static boolean isDialogShowing = false; // Prevent multiple dialogs
    
    public static void main(String[] args) {
        System.out.println("PokeCatch Game Starting...");

        // Launch intro and then run stage flow. All UI runs on EDT.
        Intro introScreen = new Intro();
        MusicPlayer music = new MusicPlayer();
        GameState gameState = GameState.getInstance();
        Util util = new Util();
        music.playLoop("/Music/pallet_town_theme.wav");

        introScreen.setStarterSelectionListener(starterId -> {
            // called on EDT when player selects starter
            System.out.println("Starter ID selected after intro: " + starterId);
            
            // Add starter to caught pokemon array
            Pokemon starter = util.getPokemonById(starterId);
            if (starter != null) {
                gameState.addCaughtPokemon(starter);
                System.out.println("Starter " + starter.name + " added to caught pokemon!");
            }
            
            music.stop();
            
            new PokeGamePanel(StageWindow.stage1);
        });

        introScreen.launchIntro();
    }
    
}
