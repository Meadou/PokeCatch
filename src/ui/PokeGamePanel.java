package ui;
import javax.swing.*;

import Logic.Logic;
import Logic.Stage;
import Logic.Util;
import View.PokedexFrame;
import View.StageWindow;
import pkmn.Pokemon;
import View.*;
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

    private JButton pauseButton;
    private JButton resumeButton;
    private boolean isPaused = false;
    private Timer spawnTimer; 

    private final ArrayList<JButton> activeButtons = new ArrayList<>();
    private final ArrayList<Timer> despawnTimers = new ArrayList<>();

    private double bombChance;

    public PokeGamePanel(Stage stage) {
        
        this.setTitle("Pokemon Clicker Game - Stage: " + stage.stageName);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null);

        bombChance = switch (stage.stageName) {
            case "grass" -> 0.15;
            case "cave" -> 0.25;
            case "ocean" -> 0.35;
            case "lava" -> 0.45;
            default -> 0.2;
        };


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
                endStage(stage); 
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

            // Freeze all despawn timers
            for (Timer t : despawnTimers) {
                t.stop();
            }

            // Disable all active buttons
            for (JButton btn : activeButtons) {
                btn.setEnabled(false);
            }

            pauseButton.setVisible(false);
            resumeButton.setVisible(true);
        }
    }


    private void resumeGame() {
        if (isPaused) {
            isPaused = false;

            gameTimer.start();
            if (spawnTimer != null) spawnTimer.start();

            // Resume despawn timers
            for (Timer t : despawnTimers) {
                t.start();
            }

            // Enable buttons again
            for (JButton btn : activeButtons) {
                btn.setEnabled(true);
            }

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

        boolean isBomb = Math.random() < bombChance;

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

                Timer effectTimer = new Timer(300, ev -> {
                    this.remove(button);
                    activeButtons.remove(button);
                    this.repaint();
                });
                effectTimer.setRepeats(false);
                effectTimer.start();


                // Add caught Pokémon & update score
                gameState.addCaughtPokemon(pokemon);
                gameState.addScore(100);
                scoreLabel.setText("Score: " + gameState.getGlobalScore());
            });
            
        this.add(button);
        this.repaint();

        Timer despawnTimer = new Timer(1000, e -> {
            Timer t = (Timer) e.getSource();

            t.stop();
            this.remove(button);
            activeButtons.remove(button);
            despawnTimers.remove(t);
            this.repaint();
        });

        despawnTimer.setRepeats(false);
        despawnTimers.add(despawnTimer);
        despawnTimer.start();

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
        this.dispose();
    }

   private void endStage(Stage currStage) {
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

        boolean isFinalStage = currStage.stageName.equalsIgnoreCase("lava");

        Object[] options;
        if (isFinalStage) {
            options = new Object[]{"Back", "Open Pokédex", "Exit"};
        } else {
            options = new Object[]{"Next Stage", "Open Pokédex", "Exit"};
        }

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
            case 0 -> {
                this.dispose();
                if (!isFinalStage) {
                    StageWindow.nextStage(currStage);
                }
            }
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
