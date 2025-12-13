package ui;
import javax.swing.*;

import Logic.Logic;
import Logic.Util;
import pkmn.Pokemon;
import pkmn.Stage;

import java.awt.*;
import java.util.ArrayList;

public class PokeGamePanel extends JFrame {

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
        timerLabel.setBounds(10, 10, 200, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.WHITE);
        bgPanel.add(timerLabel);

        spawner(stage.stagePokemon);
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
        Pokemon pokemon = logic.randomizer(pokemonList);

        int buttonSize = 120;

        int maxX = this.getWidth() - buttonSize - 16;
        int maxY = this.getHeight() - buttonSize - 39;

        int x = (int) (Math.random() * maxX);
        int y = (int) (Math.random() * maxY);

        String formattedID = String.format("%04d", pokemon.pokemonID);
        String imagePath = "firered-leafgreen/" + formattedID + ".png";

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JButton newPokemonButton = new JButton();
        newPokemonButton.setIcon(icon);
        newPokemonButton.setBounds(x, y, buttonSize, buttonSize);
        newPokemonButton.setBorder(null);
        newPokemonButton.setContentAreaFilled(false);

        newPokemonButton.addActionListener(e -> {
            this.remove(newPokemonButton);
            this.repaint();
            logic.ListPokemon(pokemon);
            logic.displayList();
            System.out.println();
        });

        this.add(newPokemonButton);
        this.repaint();


        Timer timer = new Timer(1000, e -> {
            this.remove(newPokemonButton);
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
