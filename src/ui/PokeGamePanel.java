package ui;
import javax.swing.*;

import Logic.Logic;
import Logic.Stage;
import Logic.Util;
import Model.Pokemon;

import java.awt.*;
import java.util.ArrayList;
import Logic.GameState;


public class PokeGamePanel extends JFrame {
    JLabel scoreLabel;
    GameState gameState = GameState.getInstance();

    Util util = new Util();
    Logic logic = new Logic();
    int numTracker = 0;

    JButton pokemonButton;
    int gameTime = 30; 
    JLabel timerLabel;
    Timer gameTimer;
    private Runnable onGameEndCallback;
    
    public PokeGamePanel(Stage stage) {
        this(stage, null);
    }
    
    public PokeGamePanel(Stage stage, Runnable onGameEndCallback) {
        this.onGameEndCallback = onGameEndCallback;
        this.setTitle("Pokemon Clicker Game - Stage: " + stage.stageName);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null);

        String bgPath = getBackgroundPath(stage.stageName);
        BackgroundPanel bgPanel = new BackgroundPanel(bgPath);
        this.setContentPane(bgPanel);

        timerLabel = new JLabel("Time: " + gameTime);
        timerLabel.setBounds(1080, 10, 180, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.WHITE);
        bgPanel.add(timerLabel);

        scoreLabel = new JLabel("Score: " + gameState.getGlobalScore());
        scoreLabel.setBounds(10, 10, 250, 30);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        bgPanel.add(scoreLabel);


        spawner(stage.stagePokemon);

        gameTimer = new Timer(1000, e -> {
            gameTime--;
            timerLabel.setText("Time: " + gameTime);

            if (gameTime <= 0) {
                gameTimer.stop();
                endGame();
            }
        });
        gameTimer.start();

        this.setVisible(true);
    }


    public void spawner(ArrayList<Pokemon> pokemonList) {
        Timer spawnTimer = new Timer(500, e -> spawnPokemonButton(pokemonList)); 
        spawnTimer.setRepeats(true);
        spawnTimer.start();
    }


    public void spawnPokemonButton(ArrayList<Pokemon> pokemonList) {

        int buttonSize = 120;

        int maxX = this.getWidth() - buttonSize - 16;
        int maxY = this.getHeight() - buttonSize - 39;

        int x = (int) (Math.random() * maxX);
        int y = (int) (Math.random() * maxY);

        JButton button = new JButton();
        button.setBounds(x, y, buttonSize, buttonSize);
        button.setBorder(null);
        button.setContentAreaFilled(false);

        boolean isBomb = Math.random() < 0.2; 

        if (isBomb) {
            ImageIcon bombIcon = new ImageIcon(
            getClass().getResource("bomb.png")
        );

        Image bombImg = bombIcon.getImage().getScaledInstance(
            buttonSize,
            buttonSize,
            Image.SCALE_SMOOTH
        );

        bombIcon = new ImageIcon(bombImg);
        button.setIcon(bombIcon);


            button.addActionListener(e -> {
                gameTimer.stop();
                endGame();
            });

        } else {
            Pokemon pokemon = logic.randomizer(pokemonList);
            String formattedID = String.format("%04d", pokemon.pokemonID);
            String fileName = formattedID + ".png";
            java.io.File file = new java.io.File("firered-leafgreen/" + fileName);
            
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(img));
            }

            button.addActionListener(e -> {
            this.remove(button);
            this.repaint();

            // Add caught pokemon to GameState array
            gameState.addCaughtPokemon(pokemon);

            gameState.addScore(100);
            scoreLabel.setText("Score: " + gameState.getGlobalScore());
        });

        }

        this.add(button);
        this.repaint();

        Timer timer = new Timer(1000, e -> {
            this.remove(button);
            this.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void endGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            }
        }

        // Transfer all caught pokemon from array to BST
        int caughtCount = gameState.getCaughtPokemonArray().size();
        gameState.transferCaughtPokemonToBST();
        
        String message = "Game Over! Final Score: " + gameState.getGlobalScore();
        if (caughtCount > 0) {
            message += "\nCaught " + caughtCount + " pokemon this stage!";
        }
        JOptionPane.showMessageDialog(this, message);
        
        if (onGameEndCallback != null) {
            onGameEndCallback.run();
        }
        dispose();
    }

    
    private String getBackgroundPath(String stageName) {
        switch (stageName.toLowerCase()) {
            case "grass":
                return "Backgrounds/grass.png";
            case "rock":
                return "Backgrounds/rock.png";
            case "ocean":
                return "Backgrounds/ocean.png";
            case "snow":
                return "Backgrounds/snow.png";
            case "swamp":
                return "Backgrounds/swamp.png";
            case "lava":
                return "Backgrounds/lava.png";
            default:
                return "Backgrounds/grass.png";
        }
    }

    class BackgroundPanel extends JPanel {

    private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            java.io.File file = new java.io.File(imagePath);
            if (file.exists()) {
                this.backgroundImage = new ImageIcon(file.getAbsolutePath()).getImage();
            } else {
                // Fallback to a solid color if image not found
                this.backgroundImage = null;
            }
            this.setLayout(null);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Fallback background color
                g.setColor(new Color(50, 50, 50));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }

    }
}
