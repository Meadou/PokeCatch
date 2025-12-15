package View;
import Music.MusicPlayer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import ui.ButtonStyle;

public class intro_GUI {

    public static class MainFrame extends JFrame {
        CardLayout cardLayout;
        JPanel mainPanel;



        public MainFrame() {
            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);
            MusicPlayer mainMenuMusic = new Music.MusicPlayer();
            
            StartScreen screen = new StartScreen(this);
            PlayPanel playPanel = new PlayPanel(this);
            LeaderboardsPanel leaderboardsPanel = new LeaderboardsPanel(this);
            GamePanel gamePanel = new GamePanel(this);
            CurrentStagePanel currentStagePanel = new CurrentStagePanel(this);
            MusicPlayer.playOnce(getName());
            mainPanel.add(screen, "Screen");
            mainPanel.add(playPanel, "Play");
            mainPanel.add(leaderboardsPanel, "Leaderboards");
            mainPanel.add(gamePanel,"New Game");
            mainPanel.add(currentStagePanel,"Continue");
            mainMenuMusic.playLoop("Music/MainMenu.wav");
            add(mainPanel);
            setSize(1280, 720);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);
            // start menu music
            menuMusic = new MusicPlayer();
            menuMusic.playLoop("/Music/title_screen.wav");

            // ensure music stops when window closes
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    if (menuMusic != null) menuMusic.stop();
                }
            });

            setVisible(true);

        }

        private MusicPlayer menuMusic;

        public void showPanel(String panelName) {
            cardLayout.show(mainPanel, panelName);
        }

        public class StartScreen extends JPanel {
            BufferedImage screen;

            public StartScreen(MainFrame frame) {
                try {
                    screen = ImageIO.read(new File("assets/images/Start screen.png"));
                } catch (IOException e) {
                    System.out.println("Cannot load image.");
                }
                setLayout(new BorderLayout());

                // bottom panel with centered buttons anchored to bottom
                JPanel bottom = new JPanel();
                bottom.setOpaque(false);
                bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

                Dimension btnSize = new Dimension(300, 50);
                int spacing = 12;

                JButton startBtn = ButtonStyle.createButton("START");
                startBtn.setMaximumSize(btnSize);
                startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                startBtn.addActionListener(e -> frame.showPanel("Play"));

                JButton leadBtn = ButtonStyle.createButton("LEADERBOARDS");
                leadBtn.setMaximumSize(btnSize);
                leadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                leadBtn.addActionListener(e -> frame.showPanel("Leaderboards"));

                JButton quitBtn = ButtonStyle.createButton("QUIT");
                quitBtn.setMaximumSize(btnSize);
                quitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                quitBtn.addActionListener(e -> System.exit(0));

                bottom.add(Box.createVerticalGlue());
                bottom.add(startBtn);
                bottom.add(Box.createVerticalStrut(spacing));
                bottom.add(leadBtn);
                bottom.add(Box.createVerticalStrut(spacing));
                bottom.add(quitBtn);
                bottom.add(Box.createVerticalStrut(40));

                add(bottom, BorderLayout.SOUTH);
            }

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (screen != null) {
                    g.drawImage(screen, 0, 0, getWidth(), getHeight(), this);
                }
            }
        }

        public class PlayPanel extends JPanel {
            BufferedImage screen;

            public PlayPanel(MainFrame frame) {
                try {
                    screen = ImageIO.read(new File("assets/images/Start screen.png"));
                } catch (IOException e) {
                    System.out.println("Cannot load image.");
                }
                setLayout(new BorderLayout());

                // bottom area with vertical buttons
                JPanel bottom = new JPanel();
                bottom.setOpaque(false);
                bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
                Dimension btnSize = new Dimension(300, 60);

                JButton newGBtn = ButtonStyle.createButton("NEW GAME");
                newGBtn.setMaximumSize(btnSize);
                newGBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                newGBtn.addActionListener(e -> {
                    
                    try {
                        if (menuMusic != null) menuMusic.stop();
                    } catch (Exception ignore) {}

                    try {
                        Class<?> mainClass = Class.forName("Main");
                        mainClass.getMethod("startNewGame", java.awt.Frame.class).invoke(null, frame);
                    } catch (NoSuchMethodException nsme) {
                        // fallback to parameterless startNewGame
                        try {
                            Class<?> mainClass = Class.forName("Main");
                            mainClass.getMethod("startNewGame").invoke(null);
                        } catch (Exception ex) {
                            System.out.println("Error starting game: " + ex.getMessage());
                        }
                    } catch (Exception ex) {
                        System.out.println("Error starting game: " + ex.getMessage());
                    }
                });

                JButton contBtn = ButtonStyle.createButton("CONTINUE");
                contBtn.setMaximumSize(btnSize);
                contBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                contBtn.addActionListener(e -> {
                    // Placeholder for continue
                    System.out.println("Continue button clicked (placeholder). Implement load/save handling later.");
                });

                JButton backBtn = ButtonStyle.createButton("BACK");
                backBtn.setMaximumSize(btnSize);
                backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                backBtn.addActionListener(e -> showPanel("Screen"));

                bottom.add(Box.createVerticalGlue());
                bottom.add(newGBtn);
                bottom.add(Box.createVerticalStrut(12));
                bottom.add(contBtn);
                bottom.add(Box.createVerticalStrut(12));
                bottom.add(backBtn);
                bottom.add(Box.createVerticalStrut(30));

                add(bottom, BorderLayout.SOUTH);


            }

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (screen != null) {
                    g.drawImage(screen, 0, 0, getWidth(), getHeight(), this);
                }
            }
        }

        public class LeaderboardsPanel extends JPanel {
            BufferedImage screen;
            public LeaderboardsPanel(MainFrame frame) {

                try {
                    screen = ImageIO.read(new File("assets/images/Leaderboards Screen.png"));
                } catch (IOException e) {
                    System.out.println("Cannot load image.");
                }
                setLayout(null);

                ImageIcon back = new ImageIcon("assets/images/bck t menu.png");
                Image back1 = back.getImage();
                Image back2 = back1.getScaledInstance(200, 50, Image.SCALE_SMOOTH);
                JButton backBtn = new JButton(new ImageIcon(back2));
                backBtn.setBounds(1000, 10, 200, 50);
                backBtn.setContentAreaFilled(false);
                backBtn.setFocusPainted(false);
                backBtn.setOpaque(false);
                backBtn.addActionListener(e -> frame.showPanel("Screen"));

                add(backBtn);

            }
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (screen != null) {
                    g.drawImage(screen, 0, 0, getWidth(), getHeight(), this);
                }
            }
        }

    }

    public static class GamePanel extends JPanel{
        public GamePanel (MainFrame frame){

        }
    }

    public static class CurrentStagePanel extends JPanel{
        public CurrentStagePanel(MainFrame frame){

        }
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}




