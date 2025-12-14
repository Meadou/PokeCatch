import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class intro_GUI {

    public static class MainFrame extends JFrame {
        CardLayout cardLayout;
        JPanel mainPanel;

        public MainFrame() {
            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);

            StartScreen screen = new StartScreen(this);
            PlayPanel playPanel = new PlayPanel(this);
            LeaderboardsPanel leaderboardsPanel = new LeaderboardsPanel(this);
            GamePanel gamePanel = new GamePanel(this);
            CurrentStagePanel currentStagePanel = new CurrentStagePanel(this);

            mainPanel.add(screen, "Screen");
            mainPanel.add(playPanel, "Play");
            mainPanel.add(leaderboardsPanel, "Leaderboards");
            mainPanel.add(gamePanel,"New Game");
            mainPanel.add(currentStagePanel,"Continue");

            add(mainPanel);
            setSize(1280, 720);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);

        }

        public void showPanel(String panelName) {
            cardLayout.show(mainPanel, panelName);
        }

        public class StartScreen extends JPanel {
            BufferedImage screen;

            public StartScreen(MainFrame frame) {
                try {
                    screen = ImageIO.read(getClass().getResource("Start screen.png"));
                } catch (IOException e) {
                    System.out.println("Cannot load image.");
                }
                setLayout(null);


                ImageIcon play = new ImageIcon(getClass().getResource("play.png"));
                Image play1 = play.getImage();
                Image play2 = play1.getScaledInstance(429, 65, Image.SCALE_SMOOTH);
                JButton playBtn = new JButton(new ImageIcon(play2));
                playBtn.setBounds(413, 410, 429, 65);
                playBtn.setContentAreaFilled(false);
                playBtn.setFocusPainted(false);
                playBtn.setOpaque(false);
                playBtn.addActionListener(e -> frame.showPanel("Play"));

                ImageIcon lead = new ImageIcon(getClass().getResource("leaderboards.png"));
                Image lead1 = lead.getImage();
                Image lead2 = lead1.getScaledInstance(429, 65, Image.SCALE_SMOOTH);
                JButton leadBtn = new JButton(new ImageIcon(lead2));
                leadBtn.setBounds(413, 490, 429, 65);
                leadBtn.setContentAreaFilled(false);
                leadBtn.setFocusPainted(false);
                leadBtn.setOpaque(false);
                leadBtn.addActionListener(e -> frame.showPanel("Leaderboards"));

                ImageIcon quit = new ImageIcon(getClass().getResource("quit.png"));
                Image quit1 = quit.getImage();
                Image quit2 = quit1.getScaledInstance(429, 65, Image.SCALE_SMOOTH);
                JButton quitBtn = new JButton(new ImageIcon(quit2));
                quitBtn.setBounds(413, 570, 429, 65);
                quitBtn.setContentAreaFilled(false);
                quitBtn.setFocusPainted(false);
                quitBtn.setOpaque(false);
                quitBtn.addActionListener(e -> System.exit(0));

                add(playBtn);
                add(leadBtn);
                add(quitBtn);


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
                    screen = ImageIO.read(getClass().getResource("Start screen.png"));
                } catch (IOException e) {
                    System.out.println("Cannot load image.");
                }
                setLayout(null);


                ImageIcon newGame = new ImageIcon(getClass().getResource("new game.png"));
                Image newGame1 = newGame.getImage();
                Image newGame2 = newGame1.getScaledInstance(429, 65, Image.SCALE_SMOOTH);
                JButton newGBtn = new JButton(new ImageIcon(newGame2));
                newGBtn.setBounds(413, 410, 429, 65);
                newGBtn.setContentAreaFilled(false);
                newGBtn.setFocusPainted(false);
                newGBtn.setOpaque(false);
                newGBtn.addActionListener(e -> frame.showPanel("New Game"));

                ImageIcon cont = new ImageIcon(getClass().getResource("continue.png"));
                Image cont1 = cont.getImage();
                Image cont2 = cont1.getScaledInstance(429, 65, Image.SCALE_SMOOTH);
                JButton contBtn = new JButton(new ImageIcon(cont2));
                contBtn.setBounds(413, 490, 429, 65);
                contBtn.setContentAreaFilled(false);
                contBtn.setFocusPainted(false);
                contBtn.setOpaque(false);
                contBtn.addActionListener(e -> frame.showPanel("Continue"));

                ImageIcon back = new ImageIcon(getClass().getResource("back.png"));
                Image back1 = back.getImage();
                Image back2 = back1.getScaledInstance(429, 65, Image.SCALE_SMOOTH);
                JButton backBtn = new JButton(new ImageIcon(back2));
                backBtn.setBounds(413, 570, 429, 65);
                backBtn.setContentAreaFilled(false);
                backBtn.setFocusPainted(false);
                backBtn.setOpaque(false);
                backBtn.addActionListener(e -> showPanel("Screen"));

                add(newGBtn);
                add(contBtn);
                add(backBtn);


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
                    screen = ImageIO.read(getClass().getResource("Leaderboards Screen.png"));
                } catch (IOException e) {
                    System.out.println("Cannot load image.");
                }
                setLayout(null);

                ImageIcon back = new ImageIcon(getClass().getResource("bck t menu.png"));
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




