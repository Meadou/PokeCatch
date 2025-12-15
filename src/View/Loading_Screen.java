package View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Loading_Screen extends JPanel {


    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private Timer timer;
    private int elapsedTicks = 0;
    private int animationDuration; // Duration in milliseconds
    private Color backgroundColor;

    private BufferedImage pokeballImage;

    private class Pokeball {
        double bounceOffset;
        boolean bouncingUp;
        int delayCount;
        boolean active;
        int size;
        int x, y;

        public Pokeball(int delayCount, int x, int y, int size) {
            this.bounceOffset = 0;
            this.bouncingUp = true;
            this.delayCount = delayCount;
            this.active = false;
            this.size = size;
            this.x = x;
            this.y = y;
        }

        public void update() {
            if (delayCount > 0) {
                delayCount--;
                return;
            }
            active = true;

            // Bounce animation
            if (bouncingUp) {
                bounceOffset -= 1.2;
                if (bounceOffset <= -12) bouncingUp = false;
            } else {
                bounceOffset += 1.2;
                if (bounceOffset >= 0) bouncingUp = true;
            }
        }

        public void draw(Graphics2D g2d) {
            if (!active) return;

            int drawX = x;
            int drawY = y + (int) bounceOffset;

            g2d.drawImage(
                    pokeballImage,
                    drawX,
                    drawY,
                    size,
                    size,
                    null
            );
        }
    }

    private Pokeball[] pokeballs;

    //Constructor with default values
    public Loading_Screen() {
        this(1500, 0x000000); // Default: 1.5 seconds, black background
    }
    
    //Constructor with custom duration and color
    public Loading_Screen(int durationMs, int colorHex) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.animationDuration = durationMs;
        this.backgroundColor = new Color(colorHex);
        setBackground(backgroundColor);

        try {
            pokeballImage = ImageIO.read(new java.io.File("assets/images/pokeball.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Pokeball image not found!");
        }

        int size = 60;
        int centerY = HEIGHT / 2 - size / 2;

        pokeballs = new Pokeball[]{
                new Pokeball(0, WIDTH / 2 - 120, centerY, size),
                new Pokeball(10, WIDTH / 2 - 30, centerY, size),
                new Pokeball(20, WIDTH / 2 + 60, centerY, size)
        };

        // Calculate number of ticks based on duration (30ms per tick)
        int totalTicks = animationDuration / 30;
        
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTicks++;

                for (Pokeball p : pokeballs) {
                    p.update();
                }

                // Stop after specified duration
                if (elapsedTicks >= totalTicks) {
                    timer.stop();

                    Window window = SwingUtilities.getWindowAncestor(Loading_Screen.this);
                    if (window != null) {
                        window.dispose();
                    }
                }

                repaint();
            }
        });

        timer.start();

    }

    // ================= DRAW =================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        for (Pokeball p : pokeballs) {
            p.draw(g2d);
        }

        String loadingText = "LOADING...";
        Font font = new Font("Press Start 2P", Font.PLAIN, 12);

        g2d.setFont(font);
        // Use white text for good contrast against the Pokedex blue
        g2d.setColor(Color.WHITE);

        FontMetrics fm = g2d.getFontMetrics(font);
        int textWidth = fm.stringWidth(loadingText);

        int x = (getWidth() - textWidth) / 2;
        int y = getHeight() - 40; // bottom spacing

        g2d.drawString(loadingText, x, y);

    }

    // ================= MAIN =================
    public static void main(String[] args) {
        JFrame frame = new JFrame("Loading...");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.add(new Loading_Screen());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
