package ui;

import Logic.GameState;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

public class Ending extends JPanel implements ActionListener {
    private final GameState gameState;

    private float titleAlpha = 0f;
    private float titleScale = 0.9f;
    private int step = 0;
    private int ticks = 0;

    private final String[] credits = {"credits", "Raven Villarante", "Marjhun Pamilar", "Emman Pedoroso", "Joshua Bontuyan", "Vincent Añonuevo"};
    private int creditsY;
    private boolean finalButtonsVisible = false;

    private final JButton statsButton;
    private final JButton menuButton;
    private Font eightBit;

    public Ending(GameState gameState) {
        this.gameState = gameState;
        setBackground(Color.BLACK);
        setLayout(null);

        typeFont();

        statsButton = makeButton("PLAYER STATS");
        menuButton = makeButton("BACK TO MAIN MENU");

        statsButton.setVisible(false);
        menuButton.setVisible(false);

        add(statsButton);
        add(menuButton);

        Timer timer = new Timer(30, this);
        timer.start();
    }

    private void typeFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/8bit.ttf");
            if (is != null) {
                eightBit = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f);
            } else {
                eightBit = new Font("Monospaced", Font.BOLD, 18);
            }
        } catch (Exception e) {
            eightBit = new Font("Monospaced", Font.BOLD, 18);
        }
    }

    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(new Color(0xD32F2F));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(eightBit.deriveFont(14f));
        b.addActionListener(_ -> System.out.println("Button clicked: " + text));
        return b;
    }

    public void addNotify() {
        super.addNotify();
        creditsY = getHeight() + 20;
        layoutButtons();
    }

    private void layoutButtons() {
        int btnW = 220;
        int btnH = 40;
        int gap = 20;
        int cx = (getWidth() - btnW * 2 - gap) / 2;
        int y = (int)(getHeight() * 0.65);
        statsButton.setBounds(cx, y, btnW, btnH);
        menuButton.setBounds(cx + btnW + gap, y, btnW, btnH);
    }

    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g.setFont(eightBit.deriveFont(Font.BOLD, 60f * titleScale));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, titleAlpha));
        g.setColor(Color.WHITE);
        String title = "THANK YOU FOR PLAYING";
        FontMetrics fm = g.getFontMetrics();
        int tx = (w - fm.stringWidth(title)) / 2;
        int ty = (int)(h * 0.5);
        g.drawString(title, tx, ty);

        if (step >= 3) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g.setFont(eightBit.deriveFont(Font.PLAIN, 20f));
            g.setColor(Color.LIGHT_GRAY);
            int cy = creditsY;
            for (String line : credits) {
                FontMetrics cfm = g.getFontMetrics();
                int cx = (w - cfm.stringWidth(line)) / 2;
                g.drawString(line, cx, cy);
                cy += cfm.getHeight() + 10;
            }
        }

        if (step >= 4) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g.setFont(eightBit.deriveFont(Font.BOLD, 64f));
            g.setColor(Color.WHITE);
            String poke = "PokeCatch";
            FontMetrics pfm = g.getFontMetrics();
            int px = (w - pfm.stringWidth(poke)) / 2;
            int py = (int)(h * 0.45);
            g.drawString(poke, px, py);
        }
        if (finalButtonsVisible) {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect(statsButton.getX() - 5, statsButton.getY() - 5,
                    statsButton.getWidth() + 10, statsButton.getHeight() + 10);
            g.fillRect(menuButton.getX() - 5, menuButton.getY() - 5,
                    menuButton.getWidth() + 10, menuButton.getHeight() + 10);
        }

        g.dispose();
    }

    public void actionPerformed(ActionEvent e) {

        ticks++;
        switch (step) {
            case 0:
                titleAlpha += 0.05f;
                titleScale += 0.01f;
                if (titleAlpha >= 1f) {
                    titleAlpha = 1f;
                    step = 1;
                    ticks = 0;
                }
                break;
            case 1:
                if (ticks > 40) {
                    step = 2;
                    ticks = 0;
                }
                break;
            case 2:
                titleAlpha -= 0.04f;
                titleScale -= 0.01f;
                if (titleAlpha <= 0f) {
                    titleAlpha = 0f;
                    step = 3;
                    creditsY = getHeight() + 20;
                }
                break;
            case 3:
                creditsY -= (int) 1.5;
                int totalCreditsHeight = credits.length * 50;
                int creditsBottomY = creditsY + totalCreditsHeight;
                if (creditsBottomY < 0) {
                    step = 4;
                    SwingUtilities.invokeLater(() -> {
                        finalButtonsVisible = true;
                        statsButton.setVisible(true);
                        menuButton.setVisible(true);
                        layoutButtons();
                        repaint();
                    });
                }
                break;
            case 4:
                break;
        }
        repaint();
    }
}
