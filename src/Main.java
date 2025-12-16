import Logic.GameState;
import Logic.PlayerDataManager;
import Logic.Util;
import Model.PlayerData;
import Music.MusicPlayer;
import View.ChooseMap;
import View.Loading_Screen;
import View.NameDialog;
import View.StageManager;
import View.intro_GUI;
import java.awt.*;
import java.util.Scanner;
import javax.swing.*;
import pkmn.Pokemon;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("PokeCatch Game Starting...");
        System.out.println("Pokecatch is running....");
        
        SwingUtilities.invokeLater(() -> {
            new intro_GUI.MainFrame();
        });
    }

    private static void launchGame(int starterId, String playerName, int startStage, boolean skipIntro, JFrame menuFrame) {
        GameState gameState = GameState.getInstance();
        Util util = new Util();
        MusicPlayer music = new MusicPlayer();
        
        PlayerData currentData = PlayerDataManager.getCurrentPlayerData();
        if (currentData == null) {
            currentData = new PlayerData(playerName, starterId);
            PlayerDataManager.setCurrentPlayerData(currentData);
        }

        if (skipIntro) {
            System.out.println("Continuing from stage " + startStage);
            SwingUtilities.invokeLater(() -> startStageSequence(startStage));
        } else {
            if (menuFrame != null) {
                showLoadingOnFrame(menuFrame, () -> {
                    menuFrame.setVisible(false);
                    
                    SwingUtilities.invokeLater(() -> {
                        music.playLoop("/Music/pallet_town_theme.wav");
                        Intro introScreen = new Intro();

                        introScreen.setStarterSelectionListener(selectedStarterId -> {
                            System.out.println("Starter ID selected: " + selectedStarterId);
                            
                            Pokemon starter = util.getPokemonById(selectedStarterId);
                            if (starter != null) {
                                gameState.addCaughtPokemon(starter);
                                System.out.println("Starter " + starter.name + " added!");
                                
                                gameState.transferCaughtPokemonToBST();
                                PlayerDataManager.syncFromGameState(gameState, playerName, selectedStarterId);
                                PlayerDataManager.saveProgress(gameState, startStage);
                            }
                            
                            music.stop();
                            
                            Timer stageTimer = new Timer(2500, ev -> {
                                startStageSequence(startStage);
                            });
                            stageTimer.setRepeats(false);
                            stageTimer.start();
                        });

                        introScreen.launchIntro();
                    });
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    music.playLoop("/Music/pallet_town_theme.wav");
                    Intro introScreen = new Intro();

                    introScreen.setStarterSelectionListener(selectedStarterId -> {
                        System.out.println("Starter ID selected: " + selectedStarterId);
                        
                        Pokemon starter = util.getPokemonById(selectedStarterId);
                        if (starter != null) {
                            gameState.addCaughtPokemon(starter);
                            System.out.println("Starter " + starter.name + " added!");
                            
                            gameState.transferCaughtPokemonToBST();
                            PlayerDataManager.syncFromGameState(gameState, playerName, selectedStarterId);
                            PlayerDataManager.saveProgress(gameState, startStage);
                        }
                        
                        music.stop();
                        
                        Timer stageTimer = new Timer(2500, ev -> {
                            startStageSequence(startStage);
                        });
                        stageTimer.setRepeats(false);
                        stageTimer.start();
                    });

                    introScreen.launchIntro();
                });
            }
        }
    }

    public static void startStageWithLoading(int nextStage, java.awt.Component parent) {
        SwingUtilities.invokeLater(() -> startStageSequence(nextStage));
    }
    
    public static void startNewGame() {
        startNewGame(null);
    }

    public static void startNewGame(java.awt.Frame owner) {
        String playerName = NameDialog.showDialog(owner);
        if (playerName == null) {
            System.out.println("New game cancelled.");
            return;
        }

        System.out.println("\nStarting new game...");
        System.out.println("Player: " + playerName);

        GameState gameState = GameState.getInstance();
        PlayerDataManager.startNewGame(gameState, playerName, -1);

        JFrame menuFrame = null;
        if (owner instanceof JFrame) {
            menuFrame = (JFrame) owner;
        }
        
        launchGame(-1, playerName, 1, false, menuFrame);
    }
    
    public static void continueGame() {
        GameState gameState = GameState.getInstance();
        Util util = new Util();
        
        PlayerData playerData = PlayerDataManager.loadAndSyncToGameState(gameState, util);
        
        if (playerData == null) {
            View.InfoDialog.showMessage(null, "No Save File", "No save file found. Please start a new game.");
            return;
        }
        
        System.out.println("\nLoading saved game...");
        System.out.println("Player: " + playerData.playerName);
        System.out.println("Current Stage: " + playerData.currentStage);
        System.out.println("Score: " + playerData.score);
        System.out.println("Pokemon Caught: " + playerData.caughtPokemonIds.size());
        
        new ChooseMap();
    }


    private static void showLoadingOnFrame(JFrame frame, Runnable onComplete) {
        if (frame == null) return;
        
        frame.getContentPane().removeAll();
        
        JPanel blackPanel = new JPanel(new GridBagLayout());
        blackPanel.setBackground(Color.BLACK);
        blackPanel.setOpaque(true);
        
        Loading_Screen loadingScreen = new Loading_Screen(2000, 0x000000);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        blackPanel.add(loadingScreen, gbc);
        
        frame.setContentPane(blackPanel);
        frame.revalidate();
        frame.repaint();
        
        Timer completeTimer = new Timer(2500, e -> {
            if (onComplete != null) {
                onComplete.run();
            }
        });
        completeTimer.setRepeats(false);
        completeTimer.start();
    }

    private static void startStageSequence(int startStage) {
        StageManager.stageSelector(startStage);
    }
}