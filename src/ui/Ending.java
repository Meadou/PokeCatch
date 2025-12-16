package ui;

import Logic.FileHandler;
import Logic.GameState;
import Logic.PlayerDataManager;
import Model.PlayerData;
import View.intro_GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import javax.swing.*;

public class Ending extends JPanel implements ActionListener {
    private final GameState gameState;
    private final JFrame parentFrame;

    private float titleAlpha = 0f;
    private float titleScale = 0.9f;
    private int step = 0;
    private int ticks = 0;

    private final String[] credits = {"credits", "Raven Villarante", "Marjhun Pamilar", "Emman Pedoroso", "Joshua Algene Bontuyan", "Vincent Añonuevo"};
    private int creditsY;
    private boolean finalButtonsVisible = false;

    private final JButton statsButton;
    private final JButton menuButton;
    private Font eightBit;

    public Ending(GameState gameState, JFrame parentFrame) {
        this.gameState = gameState;
        this.parentFrame = parentFrame;
        setBackground(Color.BLACK);
        setLayout(null);

        typeFont();

        statsButton = makeButton("PLAYER STATS");
        menuButton = makeButton("BACK TO MAIN MENU");

        statsButton.setVisible(false);
        menuButton.setVisible(false);

        add(statsButton);
        add(menuButton);

        statsButton.addActionListener(e -> showPlayerStats());
        menuButton.addActionListener(e -> backToMenu());

        Timer timer = new Timer(30, this);
        timer.start();
    }

    private void showPlayerStats() {
        if (parentFrame == null) return;

        PlayerData playerData = FileHandler.loadPlayerData();
        if (playerData == null) {
            playerData = new PlayerData();
            playerData.playerName = "No Save Data";
            playerData.starterPokemonId = 0;
            playerData.currentStage = 0;
        }

        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(new Color(0x1a1a2e));

        JLabel title = new JLabel("PLAYER STATS", SwingConstants.CENTER);
        title.setFont(eightBit.deriveFont(Font.BOLD, 48f));
        title.setForeground(new Color(0x00d4ff));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        statsPanel.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 16, 8, 16);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        Font labelFont = eightBit.deriveFont(Font.BOLD, 16f);
        Font valueFont = eightBit.deriveFont(Font.PLAIN, 18f);

        addStatRow(center, c, "PLAYER NAME", playerData.playerName, labelFont, valueFont);
        addStatRow(center, c, "STARTER POKEMON", getStarterName(playerData.starterPokemonId), labelFont, valueFont);
        addStatRow(center, c, "CURRENT STAGE", String.valueOf(playerData.currentStage), labelFont, valueFont);
        addStatRow(center, c, "UNLOCKED STAGES", String.valueOf(playerData.unlockedStages == null ? 0 : playerData.unlockedStages.size()), labelFont, valueFont);
        addStatRow(center, c, "UNIQUE POKEMON CAUGHT", String.valueOf(playerData.caughtPokemonIds == null ? 0 : playerData.caughtPokemonIds.size()), labelFont, valueFont);
        addStatRow(center, c, "TOTAL SCORE", String.valueOf(playerData.score), labelFont, valueFont);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        statsPanel.add(scroll, BorderLayout.CENTER);

        JButton back = makeButton("BACK");
        back.addActionListener(e -> {
            parentFrame.setContentPane(this);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(back);
        statsPanel.add(bottom, BorderLayout.SOUTH);

        parentFrame.setContentPane(statsPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void addStatRow(JPanel panel, GridBagConstraints c, String label, String value, Font labelFont, Font valueFont) {
        JLabel l = new JLabel(label);
        l.setFont(labelFont);
        l.setForeground(new Color(0x00d4ff));
        c.gridx = 0;
        panel.add(l, c);

        JLabel v = new JLabel(value);
        v.setFont(valueFont);
        v.setForeground(Color.WHITE);
        c.gridx = 1;
        panel.add(v, c);

        c.gridy++;
    }

    private String getStarterName(int id) {
        switch (id) {
            case 1: return "Bulbasaur";
            case 4: return "Charmander";
            case 7: return "Squirtle";
            case 25: return "Pikachu";
            default: return id == 0 ? "None" : "Pokemon #" + id;
        }
    }

    private void backToMenu() {
        if (parentFrame == null) return;

        try {
            Model.PlayerData current = PlayerDataManager.getCurrentPlayerData();
            if (current == null) {
                current = FileHandler.loadPlayerData();
                if (current != null) {
                    PlayerDataManager.setCurrentPlayerData(current);
                }
            }

            if (PlayerDataManager.getCurrentPlayerData() != null) {
                PlayerDataManager.syncFromGameState(gameState, PlayerDataManager.getCurrentPlayerData().playerName, PlayerDataManager.getCurrentPlayerData().starterPokemonId);
            } else {
                PlayerData p = FileHandler.loadPlayerData();
                if (p == null) p = new PlayerData();
                PlayerDataManager.setCurrentPlayerData(p);
                PlayerDataManager.syncFromGameState(gameState, p.playerName, p.starterPokemonId);
            }

            FileHandler.saveToLeaderboards(PlayerDataManager.getCurrentPlayerData());

        } catch (Exception ex) {
            System.err.println("Error saving to leaderboards: " + ex.getMessage());
        }

        try {
            parentFrame.dispose();
        } catch (Exception ignore) {}

        try {
            new intro_GUI.MainFrame();
        } catch (Exception ex) {
            System.err.println("Error launching main menu: " + ex.getMessage());
        }
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