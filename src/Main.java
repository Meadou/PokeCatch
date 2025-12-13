import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to PokeCatch!");
        System.out.println("marjhun gaming");

        // Launch intro and then run stage flow. All UI runs on EDT.
        Intro introScreen = new Intro();
        MusicPlayer music = new MusicPlayer();

        music.playLoop("/Music/pallet_town_theme.wav");

        introScreen.setStarterSelectionListener(starterId -> {
            // called on EDT when player selects starter
            System.out.println("Starter ID selected after intro: " + starterId);
            music.stop();

            // start first stage sequence
            SwingUtilities.invokeLater(() -> startStageSequence(1));
        });

        introScreen.launchIntro();

        // task 1 marjhun pamilar 
        // task 1 marjhun
        // task 2 Charmander Shiny
        // task 3 Shiny Magicarp
        // task 4 bulbasaur
        System.out.println("Welcome to PokeCatch!");
        System.out.println("marjhun gaming");
    }

    // Orchestrates stages and pokedex viewing
    private static void startStageSequence(int startStage) {
        runStageAndOptions(startStage);
    }

    private static void runStageAndOptions(int stageNumber) {
        // show the stage window
        View.StageWindow stage = new View.StageWindow(stageNumber, clearedStage -> {
            // when cleared, show options dialog on EDT
            SwingUtilities.invokeLater(() -> showPostStageOptions(clearedStage));
        });
    }

    private static void showPostStageOptions(int lastStage) {
        String[] options = {"Next Stage", "View Pokedex", "Exit"};
        int choice = JOptionPane.showOptionDialog(null,
                "Stage " + lastStage + " cleared. What would you like to do next?",
                "Stage Complete",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            // Next Stage
            runStageAndOptions(lastStage + 1);
        } else if (choice == 1) {
            // View Pokedex - open pokedex frame and wait for it to close, then show options again
            View.PokedexFrame pokedex = new View.PokedexFrame();
            pokedex.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    // when pokedex closes, return to options
                    SwingUtilities.invokeLater(() -> showPostStageOptions(lastStage));
                }
            });
        } else {
            // Exit
            System.exit(0);
        }
    }
}