import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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



                ImageIcon play = new ImageIcon(getClass().getResource("button.png"));
                Image play1 = play.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                ImageIcon play2 = new ImageIcon(play1);
                JButton playBtn = new JButton("Play",play2);
                playBtn.setHorizontalTextPosition(SwingConstants.CENTER);
                playBtn.setVerticalTextPosition(SwingConstants.CENTER);
                playBtn.setFont(new Font("Press Start 2P",Font.BOLD,20));
                playBtn.setForeground(Color.WHITE);
                playBtn.setBounds(480, 425, 300, 60);
                playBtn.setBorderPainted(false);
                playBtn.setContentAreaFilled(false);
                playBtn.setFocusPainted(false);
                playBtn.setOpaque(false);
                playBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ImageIcon hover = new ImageIcon(getClass().getResource("hover.png"));
                        Image hover1 = hover.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                        ImageIcon hover2 = new ImageIcon(hover1);
                        playBtn.setIcon(new ImageIcon(hover2.getImage()));
                    }
                    public void mouseExited(MouseEvent e){
                        playBtn.setIcon(new ImageIcon(play2.getImage()));
                    }
                });
                playBtn.addActionListener(e -> frame.showPanel("Play"));



                ImageIcon lead = new ImageIcon(getClass().getResource("button.png"));
                Image lead1 = lead.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                ImageIcon lead2 = new ImageIcon(lead1);
                JButton leadBtn = new JButton("Leaderboards",lead2);
                leadBtn.setBounds(480, 505, 300, 60);
                leadBtn.setHorizontalTextPosition(SwingConstants.CENTER);
                leadBtn.setVerticalTextPosition(SwingConstants.CENTER);
                leadBtn.setFont(new Font("Press Start 2P",Font.BOLD,20));
                leadBtn.setForeground(Color.WHITE);
                leadBtn.setBorderPainted(false);
                leadBtn.setContentAreaFilled(false);
                leadBtn.setFocusPainted(false);
                leadBtn.setOpaque(false);
                leadBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ImageIcon hover = new ImageIcon(getClass().getResource("hover.png"));
                        Image hover1 = hover.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                        ImageIcon hover2 = new ImageIcon(hover1);
                        leadBtn.setIcon(new ImageIcon(hover2.getImage()));
                    }
                    public void mouseExited(MouseEvent e){
                        leadBtn.setIcon(new ImageIcon(lead2.getImage()));
                    }

                });
                leadBtn.addActionListener(e -> frame.showPanel("Leaderboards"));


                ImageIcon quit = new ImageIcon(getClass().getResource("button.png"));
                Image quit1 = quit.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                ImageIcon quit2 = new ImageIcon(quit1);
                JButton quitBtn = new JButton("Quit", quit2);
                quitBtn.setBounds(480, 585, 300, 60);
                quitBtn.setHorizontalTextPosition(SwingConstants.CENTER);
                quitBtn.setVerticalTextPosition(SwingConstants.CENTER);
                quitBtn.setFont(new Font("Press Start 2P",Font.BOLD,20));
                quitBtn.setForeground(Color.WHITE);
                quitBtn.setBorderPainted(false);
                quitBtn.setContentAreaFilled(false);
                quitBtn.setFocusPainted(false);
                quitBtn.setOpaque(false);
                quitBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ImageIcon hover = new ImageIcon(getClass().getResource("hover.png"));
                        Image hover1 = hover.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                        ImageIcon hover2 = new ImageIcon(hover1);
                        quitBtn.setIcon(new ImageIcon(hover2.getImage()));
                    }
                    public void mouseExited(MouseEvent e){
                        quitBtn.setIcon(new ImageIcon(quit2.getImage()));
                    }

                });
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

                ImageIcon newGame = new ImageIcon(getClass().getResource("button.png"));
                Image newGame1 = newGame.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                ImageIcon newGame2 = new ImageIcon(newGame1);
                JButton newGBtn = new JButton("New Game",newGame2);
                newGBtn.setBounds(480, 425, 300, 60);
                newGBtn.setHorizontalTextPosition(SwingConstants.CENTER);
                newGBtn.setVerticalTextPosition(SwingConstants.CENTER);
                newGBtn.setFont(new Font("Press Start 2P",Font.BOLD,20));
                newGBtn.setForeground(Color.WHITE);
                newGBtn.setBorderPainted(false);
                newGBtn.setContentAreaFilled(false);
                newGBtn.setFocusPainted(false);
                newGBtn.setOpaque(false);
                newGBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ImageIcon hover = new ImageIcon(getClass().getResource("hover.png"));
                        Image hover1 = hover.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                        ImageIcon hover2 = new ImageIcon(hover1);
                        newGBtn.setIcon(new ImageIcon(hover2.getImage()));
                    }
                    public void mouseExited(MouseEvent e){
                        newGBtn.setIcon(new ImageIcon(newGame2.getImage()));
                    }

                });
                newGBtn.addActionListener(e -> frame.showPanel("New Game"));


                ImageIcon cont = new ImageIcon(getClass().getResource("button.png"));
                Image cont1 = cont.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                ImageIcon cont2 = new ImageIcon(cont1);
                JButton contBtn = new JButton("Continue",cont2);
                contBtn.setBounds(480, 505, 300, 60);
                contBtn.setHorizontalTextPosition(SwingConstants.CENTER);
                contBtn.setVerticalTextPosition(SwingConstants.CENTER);
                contBtn.setFont(new Font("Press Start 2P",Font.BOLD,20));
                contBtn.setForeground(Color.WHITE);
                contBtn.setBorderPainted(false);
                contBtn.setContentAreaFilled(false);
                contBtn.setFocusPainted(false);
                contBtn.setOpaque(false);
                contBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ImageIcon hover = new ImageIcon(getClass().getResource("hover.png"));
                        Image hover1 = hover.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                        ImageIcon hover2 = new ImageIcon(hover1);
                        contBtn.setIcon(new ImageIcon(hover2.getImage()));
                    }
                    public void mouseExited(MouseEvent e){
                        contBtn.setIcon(new ImageIcon(cont2.getImage()));
                    }

                });
                contBtn.addActionListener(e -> frame.showPanel("Continue"));


                ImageIcon back = new ImageIcon(getClass().getResource("button.png"));
                Image back1 = back.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                ImageIcon back2 = new ImageIcon(back1);
                JButton backBtn = new JButton("Back",back2);
                backBtn.setBounds(480, 585, 300, 60);
                backBtn.setHorizontalTextPosition(SwingConstants.CENTER);
                backBtn.setVerticalTextPosition(SwingConstants.CENTER);
                backBtn.setFont(new Font("Press Start 2P",Font.BOLD,20));
                backBtn.setForeground(Color.WHITE);
                backBtn.setBorderPainted(false);
                backBtn.setContentAreaFilled(false);
                backBtn.setFocusPainted(false);
                backBtn.setOpaque(false);
                backBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ImageIcon hover = new ImageIcon(getClass().getResource("hover.png"));
                        Image hover1 = hover.getImage().getScaledInstance(300,60,Image.SCALE_SMOOTH);
                        ImageIcon hover2 = new ImageIcon(hover1);
                        backBtn.setIcon(new ImageIcon(hover2.getImage()));
                    }
                    public void mouseExited(MouseEvent e){
                        backBtn.setIcon(new ImageIcon(back2.getImage()));
                    }

                });
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




