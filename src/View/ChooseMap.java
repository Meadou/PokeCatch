package View;

import javax.swing.*;

import View.intro_GUI.MainFrame;

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

    public ChooseMap() {
        setTitle("PokeCatch - Choose Map");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel content =
                new BackgroundPanel("/ChooseMapResources/background.png");
        setContentPane(content);

        /* ================= BACK BUTTON ================= */
        try {
            ImageIcon backIcon = new ImageIcon(Objects.requireNonNull(
                    getClass().getResource("/ChooseMapResources/back_button.png")));
            JLabel backButton = new JLabel(backIcon);
            backButton.setBounds(20, 20,
                    backIcon.getIconWidth(),
                    backIcon.getIconHeight());
            content.add(backButton);

            backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            backButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    dispose();
                    new MainFrame();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* ================= TITLE ================= */
        JLabel titleLabel = new JLabel("Choose Map");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, 1280, 60);
        content.add(titleLabel);

        /* ================= MAP PANELS ================= */
        int marginX = 100, marginTop = 100, marginBottom = 200, gap = 50;
        int imageW = (1280 - marginX * 2 - gap) / 2;
        int imageH = (720 - marginTop - marginBottom - gap) / 2;

        ImagePanel grass = new ImagePanel(
                "/ChooseMapResources/grass.png", "grass");
        grass.setBounds(marginX, marginTop, imageW, imageH);

        ImagePanel cave = new ImagePanel(
                "/ChooseMapResources/cave.png", "cave");
        cave.setBounds(marginX + imageW + gap, marginTop, imageW, imageH);

        ImagePanel ocean = new ImagePanel(
                "/ChooseMapResources/ocean.png", "ocean");
        ocean.setBounds(marginX, marginTop + imageH + gap, imageW, imageH);

        ImagePanel lava = new ImagePanel(
                "/ChooseMapResources/lava.png", "lava");
        lava.setBounds(marginX + imageW + gap,
                marginTop + imageH + gap, imageW, imageH);

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

        /* ================= CHOOSE BUTTON ================= */
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

            if (selected == null) {
                JOptionPane.showMessageDialog(
                        this, "Please select a map!");
                return;
            }

            dispose();

            switch (selected.getName()) {
                case "grass" -> StageManager.stageSelector(1);
                case "cave"  -> StageManager.stageSelector(2);
                case "ocean" -> StageManager.stageSelector(3);
                case "lava"  -> StageManager.stageSelector(4);
            }
        });

        /* ================= APPLY UNLOCK STATES ================= */
        updateMapsFromStageManager();

        setVisible(true);
    }

    /* ================= UNLOCK SYNC ================= */
    private void updateMapsFromStageManager() {
        for (ImagePanel panel : mapPanels) {
            switch (panel.getName()) {
                case "grass" -> panel.setUnlocked(StageManager.stage1.isUnlocked);
                case "cave"  -> panel.setUnlocked(StageManager.stage2.isUnlocked);
                case "ocean" -> panel.setUnlocked(StageManager.stage3.isUnlocked);
                case "lava"  -> panel.setUnlocked(StageManager.stage4.isUnlocked);
            }
        }
    }

    private void selectPanel(ImagePanel selectedPanel) {
        for (ImagePanel panel : mapPanels) {
            panel.setSelected(panel == selectedPanel);
        }
    }
}

/* ================= BACKGROUND PANEL ================= */
class BackgroundPanel extends JPanel {

    private BufferedImage background;

    public BackgroundPanel(String resourcePath) {
        try {
            background = ImageIO.read(
                    Objects.requireNonNull(getClass().getResource(resourcePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0,
                    getWidth(), getHeight(), null);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 0, 0, 230));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}

/* ================= IMAGE PANEL ================= */
class ImagePanel extends JPanel {

    private BufferedImage image;
    private boolean selected = false;
    private boolean isUnlocked = false;
    private boolean isEnlarged = false;
    private final String name;
    private Rectangle originalBounds;

    public ImagePanel(String resourcePath, String name) {
        this.name = name;
        try {
            image = ImageIO.read(
                    Objects.requireNonNull(getClass().getResource(resourcePath)));
            setOpaque(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUnlocked() { return isUnlocked; }
    public void setUnlocked(boolean unlocked) {
        this.isUnlocked = unlocked;
        repaint();
    }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) {
        if (isUnlocked) {
            this.selected = selected;
            repaint();
        }
    }

    public String getName() { return name; }

    public boolean isEnlarged() { return isEnlarged; }
    public void setEnlarged(boolean enlarged) { this.isEnlarged = enlarged; }

    public void setOriginalBounds(Rectangle bounds) {
        this.originalBounds = bounds;
    }

    public Rectangle getOriginalBounds() { return originalBounds; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;

        int pw = getWidth();
        int ph = getHeight();

        double imgAspect = (double) image.getWidth() / image.getHeight();
        double panelAspect = (double) pw / ph;

        int dw, dh;
        if (imgAspect > panelAspect) {
            dw = pw;
            dh = (int) (pw / imgAspect);
        } else {
            dh = ph;
            dw = (int) (ph * imgAspect);
        }

        int x = (pw - dw) / 2;
        int y = (ph - dh) / 2;

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
            g2.drawString(
                    lockedText,
                    x + (dw - fm.stringWidth(lockedText)) / 2,
                    y + (dh + fm.getAscent()) / 2
            );
        }

        g2.dispose();
    }
}
