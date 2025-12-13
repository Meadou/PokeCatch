package ui;
import javax.swing.*;

import Logic.Logic;
import Logic.Stage;
import Logic.Util;
import pkmn.Pokemon;

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
    
    public PokeGamePanel(Stage stage) {
        this.setTitle("Pokemon Clicker Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            String imagePath = "firered-leafgreen/" + formattedID + ".png";

            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));

            button.addActionListener(e -> {
            this.remove(button);
            this.repaint();

            logic.ListPokemon(pokemon);
            logic.displayList();

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
        System.exit(0);
    }

    
    private String getBackgroundPath(String stageName) {
        switch (stageName.toLowerCase()) {
            case "grass":
                return "backgrounds/grass.png";
            case "rock":
                return "backgrounds/rock.png";
            case "ocean":
                return "backgrounds/ocean.png";
            case "snow":
                return "backgrounds/snow.png";
            case "swamp":
                return "backgrounds/swamp.png";
            case "lava":
                return "backgrounds/lava.png";
            default:
                return "backgrounds/default.png";
        }
    }

    class BackgroundPanel extends JPanel {

    private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            this.backgroundImage = new ImageIcon(imagePath).getImage();
            this.setLayout(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
