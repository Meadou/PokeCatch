import javax.swing.*;
import Logic.GameState;
import Logic.Util;
import Logic.FileHandler;
import Logic.PlayerDataManager;
import Model.Pokemon;
import Model.PlayerData;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static boolean isDialogShowing = false; // Prevent multiple dialogs
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("PokeCatch Game Starting...");
        
        // Show CLI menu first
        showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== POKECATCH MAIN MENU ===");
            System.out.println("1. Play Game");
            System.out.println("2. Leaderboards");
            System.out.println("3. Quit");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    showPlayGameMenu();
                    break;
                case 2:
                    showLeaderboards();
                    break;
                case 3:
                    System.out.println("Thanks for playing! Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showPlayGameMenu() {
        while (true) {
            System.out.println("\n=== PLAY GAME ===");
            System.out.println("1. New Game");
            System.out.println("2. Continue");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    startNewGame();
                    return; // Return to main menu after game
                case 2:
                    continueGame();
                    return; // Return to main menu after game
                case 3:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void startNewGame() {
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine().trim();
        
        if (playerName.isEmpty()) {
            playerName = "Player";
        }
        
        System.out.println("\nStarting new game...");
        System.out.println("Player: " + playerName);
        
        GameState gameState = GameState.getInstance();
        // Start new game with placeholder starter ID (will be set when starter is selected in intro)
        PlayerDataManager.startNewGame(gameState, playerName, -1);
        
        // Launch intro immediately - starter selection will happen in the intro GUI
        launchGame(-1, playerName);
    }

    private static void continueGame() {
        GameState gameState = GameState.getInstance();
        Util util = new Util();
        
        PlayerData playerData = PlayerDataManager.loadAndSyncToGameState(gameState, util);
        
        if (playerData == null) {
            System.out.println("No save file found. Please start a new game.");
            return;
        }
        
        System.out.println("\nLoading saved game...");
        System.out.println("Player: " + playerData.playerName);
        System.out.println("Current Stage: " + playerData.currentStage);
        System.out.println("Score: " + playerData.score);
        System.out.println("Pokemon Caught: " + playerData.caughtPokemonIds.size());
        
        // Launch game GUI from saved stage (skip intro)
        launchGame(playerData.starterPokemonId, playerData.playerName, playerData.currentStage, true);
    }

    private static void launchGame(int starterId, String playerName) {
        launchGame(starterId, playerName, 1, false);
    }

    private static void launchGame(int starterId, String playerName, int startStage, boolean skipIntro) {
        GameState gameState = GameState.getInstance();
        Util util = new Util();
        MusicPlayer music = new MusicPlayer();
        
        // Set player name and starter in PlayerDataManager
        PlayerData currentData = PlayerDataManager.getCurrentPlayerData();
        if (currentData == null) {
            currentData = new PlayerData(playerName, starterId);
            PlayerDataManager.setCurrentPlayerData(currentData);
        }

        if (skipIntro) {
            // Skip intro for continue games - go straight to stage
            System.out.println("Continuing from stage " + startStage);
            SwingUtilities.invokeLater(() -> startStageSequence(startStage));
        } else {
            // Launch intro for new games
            music.playLoop("/Music/pallet_town_theme.wav");
            Intro introScreen = new Intro();

            introScreen.setStarterSelectionListener(selectedStarterId -> {
                // called on EDT when player selects starter
                System.out.println("Starter ID selected after intro: " + selectedStarterId);
                
                // Add starter to caught pokemon array
                Pokemon starter = util.getPokemonById(selectedStarterId);
                if (starter != null) {
                    gameState.addCaughtPokemon(starter);
                    System.out.println("Starter " + starter.name + " added to caught pokemon!");
                    
                    // Transfer to BST and save
                    gameState.transferCaughtPokemonToBST();
                    PlayerDataManager.syncFromGameState(gameState, playerName, selectedStarterId);
                    PlayerDataManager.saveProgress(gameState, startStage);
                }
                
                music.stop();
                
                // start stage sequence from saved stage or stage 1
                SwingUtilities.invokeLater(() -> startStageSequence(startStage));
            });

            introScreen.launchIntro();
        }
    }

    private static void showLeaderboards() {
        System.out.println("\n=== LEADERBOARDS ===");
        List<FileHandler.LeaderboardEntry> entries = FileHandler.loadLeaderboards();
        
        if (entries.isEmpty()) {
            System.out.println("No leaderboard entries yet. Play the game to see scores!");
            return;
        }
        
        System.out.printf("%-20s %-15s %-10s %-15s %-15s%n", 
                         "Name", "Starter ID", "Score", "Unique Pokemon", "Stages");
        System.out.println("--------------------------------------------------------------------------------");
        
        int rank = 1;
        for (FileHandler.LeaderboardEntry entry : entries) {
            System.out.printf("%-20s %-15d %-10d %-15d %-15d%n",
                            entry.name, entry.starterId, entry.score, 
                            entry.uniquePokemon, entry.stagesCompleted);
            rank++;
            if (rank > 10) break; // Show top 10
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static int getIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    // Orchestrates stages and pokedex viewing
    private static void startStageSequence(int startStage) {
        // Create and show stage, passing callback for when stage ends
        new View.StageWindow(startStage, clearedStage -> {
            // Show options dialog on EDT after stage clears
            SwingUtilities.invokeLater(() -> showPostStageOptions(clearedStage));
        });
    }

    private static void showPostStageOptions(int lastStage) {
        // Prevent multiple dialogs from showing at once
        if (isDialogShowing) {
            System.out.println("Dialog already showing, ignoring duplicate call");
            return;
        }
        
        isDialogShowing = true;
        System.out.println("showPostStageOptions called for stage: " + lastStage);
        
        // Save progress after stage completion
        GameState gameState = GameState.getInstance();
        // Transfer caught pokemon from array to BST
        gameState.transferCaughtPokemonToBST();
        
        // Unlock next stage
        int nextStage = lastStage + 1;
        PlayerDataManager.saveProgress(gameState, nextStage);
        
        // Update unlocked stages
        PlayerData currentData = PlayerDataManager.getCurrentPlayerData();
        if (currentData != null) {
            currentData.unlockStage(lastStage);
            currentData.unlockStage(nextStage);
            PlayerDataManager.saveProgress(gameState, nextStage);
        }
        
        String[] options = {"Next Stage", "View Pokedex", "Exit"};
        
        int choice = JOptionPane.showOptionDialog(null,
                "Stage " + lastStage + " cleared. What would you like to do next?",
                "Stage Complete",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        System.out.println("User selected choice: " + choice);
        isDialogShowing = false; // Reset flag after dialog closes

        // Handle choice based on result
        if (choice == 0) {
            // Next Stage
            startStageSequence(nextStage);
        } else if (choice == 1) {
            // View Pokedex
            System.out.println("Opening Pokedex...");
            View.PokedexFrame pokedexFrame = new View.PokedexFrame();
            
            pokedexFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    System.out.println("Pokedex closed, showing options again...");
                    SwingUtilities.invokeLater(() -> showPostStageOptions(lastStage));
                }
            });
        } else {
            // Exit - save progress before exiting
            gameState.transferCaughtPokemonToBST();
            PlayerDataManager.saveProgress(gameState, lastStage);
            System.out.println("Progress saved. Goodbye!");
            System.exit(0);
        }
    }
}