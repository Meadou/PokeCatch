package View;
import javax.swing.*;

import Model.*;

import java.awt.*;

public class Gui extends JFrame {

    Util util = new Util();
    Logic logic = new Logic();
    int numTracker = 0;

    JButton pokemonButton;
    int gameTime = 30; 
    JLabel timerLabel;
    Timer gameTimer;


    public Gui() {
        this.setTitle("Pokemon Clicker Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(832, 480);
        this.setLocationRelativeTo(null);
        this.setLayout(null); 
        timerLabel = new JLabel("Time: " + gameTime);
        timerLabel.setBounds(10, 10, 100, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        


        buttonSpawner();
        buttonSpawner();
        this.setVisible(true);
        this.add(timerLabel);

        gameTimer = new Timer(1000, e -> {  
        gameTime--;
        timerLabel.setText("Time: " + gameTime);

        if (gameTime <= 0) {
            gameTimer.stop();  
            endGame();          
        }
    });

    gameTimer.start();

    }

    public void buttonSpawner() {
        Timer spawnTimer = new Timer(500, e -> spawnPokemonButton()); 
        spawnTimer.setRepeats(true);
        spawnTimer.start();
    }


    public void spawnPokemonButton() {
        Pokemon pokemon = logic.randomizer(util.initializePokemon());

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

}
