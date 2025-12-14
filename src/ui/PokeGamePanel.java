package ui;
import javax.swing.*;

import Logic.Logic;
import Logic.Stage;
import Logic.Util;
import View.PokedexFrame;
import pkmn.Pokemon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import Logic.GameState;


public class PokeGamePanel extends JFrame {
    JLabel scoreLabel;
    GameState gameState = GameState.getInstance();

    Util util = new Util();
    Logic logic = new Logic();
    int numTracker = 0;

    JButton pokemonButton;
    int gameTime = 15; 
    JLabel timerLabel;
    Timer gameTimer;
    private Runnable onGameEndCallback;

    private JButton pauseButton;
    private JButton resumeButton;
    private boolean isPaused = false;
    private Timer spawnTimer; 

    
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

        initPauseResumeButtons();

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
                endStage(); 
            }
        });
        gameTimer.start();


        this.setVisible(true);
    }


    public void spawner(ArrayList<Pokemon> pokemonList) {
        spawnTimer = new Timer(500, e -> spawnPokemonButton(pokemonList)); 
        spawnTimer.setRepeats(true);
        spawnTimer.start();
    }

    private void initPauseResumeButtons() {
        pauseButton = new JButton("Pause");
        pauseButton.setBounds(this.getWidth()/2 - 50, 10, 100, 30);
        pauseButton.setFont(new Font("Arial", Font.BOLD, 16));
        pauseButton.addActionListener(e -> pauseGame());
        this.getContentPane().add(pauseButton);

        resumeButton = new JButton("Resume");
        resumeButton.setBounds(this.getWidth()/2 - 50, 10, 100, 30);
        resumeButton.setFont(new Font("Arial", Font.BOLD, 16));
        resumeButton.setVisible(false);
        resumeButton.addActionListener(e -> resumeGame());
        this.getContentPane().add(resumeButton);
    }

    private void pauseGame() {
        if (!isPaused) {
            isPaused = true;
            gameTimer.stop();
            if (spawnTimer != null) spawnTimer.stop();
            pauseButton.setVisible(false);
            resumeButton.setVisible(true);
        }
    }

    private void resumeGame() {
        if (isPaused) {
            isPaused = false;
            gameTimer.start();
            if (spawnTimer != null) spawnTimer.start();
            pauseButton.setVisible(true);
            resumeButton.setVisible(false);
        }
    }


    public void spawnPokemonButton(ArrayList<Pokemon> pokemonList) {

        int buttonSize = 170;

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
                ImageIcon icon = (ImageIcon) button.getIcon();
                if (icon != null) {
                    // Convert image to white silhouette
                    Image img = icon.getImage();
                    Image whiteImg = createWhiteSilhouette(img, button.getWidth(), button.getHeight());
                    button.setIcon(new ImageIcon(whiteImg));
                }

                // Disable the button so it can't be clicked again
                button.setEnabled(false);

                // Remove after short delay
                Timer effectTimer = new Timer(300, ev -> {
                    this.remove(button);
                    this.repaint();
                });
                effectTimer.setRepeats(false);
                effectTimer.start();

                // Add caught Pokémon & update score
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
        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            }
        }

        JOptionPane.showMessageDialog(this, "Game Over!");
    }

    private void endStage() {
        if (gameTimer != null) {
            gameTimer.stop();
        }

        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            }
        }

        int caughtCount = gameState.getCaughtPokemonArray().size();
        gameState.transferCaughtPokemonToBST();

        String message = "Stage Completed!\nScore: " + gameState.getGlobalScore() +
                        "\nCaught Pokémon this stage: " + caughtCount;

        Object[] options = {"Next Stage", "Open Pokédex", "Exit"};

        int choice = JOptionPane.showOptionDialog(
            this,
            message,
            "Stage Complete",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0 -> { /* Next Stage: empty */ }
            case 1 -> {
                this.dispose();
                new PokedexFrame();
            }
            case 2 -> System.exit(0);
        }

        dispose();
    }

    
    private String getBackgroundPath(String stageName) {
        switch (stageName.toLowerCase()) {
            case "grass":
                return "backgrounds/grass.png";
            case "cave":
                return "backgrounds/cave.png";
            case "ocean":
                return "backgrounds/ocean.png";
            case "lava":
                return "backgrounds/lava.png";
            default:
                return "backgrounds/default.png";
        }
    }


    class BackgroundPanel extends JPanel {

    private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            java.io.File file = new java.io.File(imagePath);
            if (file.exists()) {
                this.backgroundImage = new ImageIcon(file.getAbsolutePath()).getImage();
            } else {

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

    private Image createWhiteSilhouette(Image original, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(original, 0, 0, width, height, null);

        // Set all non-transparent pixels to white
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int alpha = (img.getRGB(x, y) >> 24) & 0xff;
                if (alpha != 0) {
                    img.setRGB(x, y, (alpha << 24) | 0xFFFFFF);
                }
            }
        }

        g2d.dispose();
        return img;
    }
}
