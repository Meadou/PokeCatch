package maps;

import View.PokedexFrame;
import View.intro_GUI;
import Logic.GameState;
import Logic.Stage;
import Logic.Util;
import Music.MusicPlayer;
import ui.PokeGamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Objects;

public class ChooseMap extends JFrame {


    private final ArrayList<ImagePanel> mapPanels = new ArrayList<>();

    public ArrayList<ImagePanel> getMapPanels() {
        return mapPanels;
    }

    // Stages
    public static Stage stage1 = new Stage("grass", new Logic.Util().initializeStage1Pokemon(), true);
    public static Stage stage2 = new Stage("cave", new Logic.Util().initializeStage2Pokemon(), false);
    public static Stage stage3 = new Stage("ocean", new Logic.Util().initializeStage3Pokemon(), false);
    public static Stage stage4 = new Stage("lava", new Logic.Util().initializeStage4Pokemon(), false);

    public ChooseMap() {
        setTitle("PokeCatch - Choose Map");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel content = new BackgroundPanel("/ChooseMapResources/background.png");
        setContentPane(content);

        // Title
        JLabel titleLabel = new JLabel("Choose Map");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, 1280, 60);
        content.add(titleLabel);

        int marginX = 100, marginTop = 100, marginBottom = 200, gap = 50;
        int imageW = (1280 - marginX * 2 - gap) / 2;
        int imageH = (720 - marginTop - marginBottom - gap) / 2;

        // Map panels
        ImagePanel grass = new ImagePanel("/ChooseMapResources/grass.png", "grass", stage1.isUnlocked);
        grass.setBounds(marginX, marginTop, imageW, imageH);

        ImagePanel cave = new ImagePanel("/ChooseMapResources/cave.png", "cave", stage2.isUnlocked);
        cave.setBounds(marginX + imageW + gap, marginTop, imageW, imageH);

        ImagePanel ocean = new ImagePanel("/ChooseMapResources/ocean.png", "ocean", stage3.isUnlocked);
        ocean.setBounds(marginX, marginTop + imageH + gap, imageW, imageH);

        ImagePanel lava = new ImagePanel("/ChooseMapResources/lava.png", "lava", stage4.isUnlocked);
        lava.setBounds(marginX + imageW + gap, marginTop + imageH + gap, imageW, imageH);

        mapPanels.add(grass);
        mapPanels.add(cave);
        mapPanels.add(ocean);
        mapPanels.add(lava);

        for (ImagePanel panel : mapPanels) {
            content.add(panel);
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (!panel.isUnlocked()) return;

                    if (panel.isEnlarged()) {
                        panel.setBounds(panel.getOriginalBounds());
                        panel.setEnlarged(false);
                    } else {
                        for (ImagePanel p : mapPanels) {
                            if (p.isEnlarged()) {
                                p.setBounds(p.getOriginalBounds());
                                p.setEnlarged(false);
                            }
                        }

                        panel.setOriginalBounds(panel.getBounds());
                        int newW = 600;
                        int newH = 400;
                        int centerX = (1280 - newW) / 2;
                        int centerY = (720 - newH) / 2;
                        panel.setBounds(centerX, centerY, newW, newH);
                        panel.setEnlarged(true);

                        panel.getParent().setComponentZOrder(panel, 0);
                    }

                    selectPanel(panel);
                    panel.getParent().repaint();
                }
            });
        }

        // Choose button
        JButton chooseButton = new JButton("Choose");
        chooseButton.setBackground(Color.RED);
        chooseButton.setForeground(Color.WHITE);
        chooseButton.setFont(new Font("Arial", Font.BOLD, 24));
        chooseButton.setBounds(540, 620, 200, 50);
        content.add(chooseButton);

        chooseButton.addActionListener(e -> {
            ImagePanel selected = null;
            for (ImagePanel panel : mapPanels) {
                if (panel.isSelected()) {
                    selected = panel;
                    break;
                }
            }

            if (selected != null) {
                dispose();
                startStage(selected.getName());
            } else {
                System.out.println("No map selected!");
            }
        });

        // Back button
        try {
            ImageIcon backIcon = new ImageIcon(Objects.requireNonNull(
                    getClass().getResource("/ChooseMapResources/back_button.png")));
            JLabel backButton = new JLabel(backIcon);
            backButton.setBounds(20, 20, backIcon.getIconWidth(), backIcon.getIconHeight());
            content.add(backButton);

            backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            backButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    new intro_GUI.MainFrame();
                    dispose();
                }
            });
        } catch (Exception e) { e.printStackTrace(); }

        // View Pokedex button
        JButton pokedexButton = new JButton("View Pokedex");
        pokedexButton.setBounds(20, 620, 180, 50);
        pokedexButton.setFont(new Font("Arial", Font.BOLD, 18));
        pokedexButton.setBackground(Color.BLUE);
        pokedexButton.setForeground(Color.WHITE);
        content.add(pokedexButton);

        pokedexButton.addActionListener(e -> new PokedexFrame());
    }

    public static void stageSelector(int stageNum) {
        Stage selectedStage;
        switch(stageNum) {
            case 1 -> selectedStage = stage1;
            case 2 -> selectedStage = stage2;
            case 3 -> selectedStage = stage3;
            case 4 -> selectedStage = stage4;
            default -> selectedStage = stage1;
        }
        GameState.setCurrenStage(selectedStage);
        new PokeGamePanel(selectedStage);
    }

    private void selectPanel(ImagePanel selectedPanel) {
        for (ImagePanel panel : mapPanels) {
            panel.setSelected(panel == selectedPanel);
        }
    }

    private void startStage(String mapName) {
        Stage selectedStage;
        switch (mapName) {
            case "grass" -> selectedStage = stage1;
            case "cave"  -> selectedStage = stage2;
            case "ocean" -> selectedStage = stage3;
            case "lava"  -> selectedStage = stage4;
            default -> selectedStage = stage1;
        }

        GameState.setCurrenStage(selectedStage);
        new PokeGamePanel(selectedStage);
    }

    // Background panel
    static class BackgroundPanel extends JPanel {
        private BufferedImage background;
        public BackgroundPanel(String resourcePath) {
            try { background = ImageIO.read(Objects.requireNonNull(getClass().getResource(resourcePath))); }
            catch (Exception e) { e.printStackTrace(); }
            setLayout(null);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (background != null) g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }
    }

    // Image panel
    static class ImagePanel extends JPanel {
        private BufferedImage image;
        private boolean selected = false;
        private boolean isUnlocked = false;
        private boolean isEnlarged = false;
        private final String name;
        private Rectangle originalBounds;

        public ImagePanel(String resourcePath, String name, boolean isUnlocked) {
            this.name = name;
            this.isUnlocked = isUnlocked;
            try { image = ImageIO.read(Objects.requireNonNull(getClass().getResource(resourcePath))); setOpaque(false); }
            catch (Exception e) { e.printStackTrace(); }
        }

        public boolean isUnlocked() { return isUnlocked; }
        public void setUnlocked(boolean unlocked) { this.isUnlocked = unlocked; repaint(); }
        public boolean isSelected() { return selected; }
        public void setSelected(boolean selected) { if (isUnlocked) { this.selected = selected; repaint(); } }
        public String getName() { return name; }
        public boolean isEnlarged() { return isEnlarged; }
        public void setEnlarged(boolean enlarged) { this.isEnlarged = enlarged; }
        public void setOriginalBounds(Rectangle bounds) { this.originalBounds = bounds; }
        public Rectangle getOriginalBounds() { return originalBounds; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null) return;

            int pw = getWidth(), ph = getHeight();
            double imgAspect = (double) image.getWidth() / image.getHeight();
            double panelAspect = (double) pw / ph;
            int dw = imgAspect > panelAspect ? pw : (int) (ph * imgAspect);
            int dh = imgAspect > panelAspect ? (int) (pw / imgAspect) : ph;
            int x = (pw - dw) / 2, y = (ph - dh) / 2;
            g.drawImage(image, x, y, dw, dh, null);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(selected ? Color.RED : Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(x, y, dw - 1, dh - 1);

            if (!isUnlocked) {
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect(x, y, dw, dh);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                String lockedText = "Locked";
                int textWidth = fm.stringWidth(lockedText);
                int textHeight = fm.getAscent();
                g2.drawString(lockedText, x + (dw - textWidth) / 2, y + (dh + textHeight) / 2);
            }

            g2.dispose();
        }
    }
}
