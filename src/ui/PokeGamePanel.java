package ui;
import Logic.FileHandler;
import Logic.GameState;
import Logic.Logic;
import Logic.PlayerDataManager;
import Logic.Stage;
import Logic.Util;
import Music.MusicPlayer;
import View.EndingFrame;
import View.Loading_Screen;
import View.PokedexFrame;
import View.intro_GUI.MainFrame;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import pkmn.Pokemon;


public class PokeGamePanel extends JFrame {
    JLabel scoreLabel;
    GameState gameState = GameState.getInstance();

    Util util = new Util();
    Logic logic = new Logic();
    int numTracker = 0;

    JButton pokemonButton;
    int gameTime = 15; 
    JLabel timerLabel;
    Timer gameTimer;

    private JButton pauseButton;
    private JButton resumeButton;
    private boolean isPaused = false;
    private Timer spawnTimer; 

    private final ArrayList<JButton> activeButtons = new ArrayList<>();
    private final ArrayList<Timer> despawnTimers = new ArrayList<>();

    private double bombChance;
    private boolean stageCompleted = false;
    private boolean showStageClear = false;
    private String stageClearMessage = "";
    private int caughtCountThisStage = 0;
    private boolean isFinalStage = false;
    private Stage currentStage;
    private JButton nextStageBtn;
    private JButton pokedexBtn;
    private JButton exitBtn;
    private Music.MusicPlayer stageMusic;
    private int countdown = 3;
    private boolean isCountdownActive = true;
    private Timer countdownTimer;


    public PokeGamePanel(Stage stage) {
        this.currentStage = stage;
        this.setTitle("Pokemon Clicker Game - Stage: " + stage.stageName);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null);

        bombChance = switch (stage.stageName) {
            case "grass" -> 0.15;
            case "cave" -> 0.25;
            case "ocean" -> 0.35;
            case "lava" -> 0.45;
            default -> 0.2;
        };


        String bgPath = getBackgroundPath(stage.stageName);
        BackgroundPanel bgPanel = new BackgroundPanel(bgPath);
        this.setContentPane(bgPanel);

        initPauseResumeButtons();

        timerLabel = new JLabel("Time: " + gameTime);
        timerLabel.setBounds(1080, 10, 180, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.WHITE);
        bgPanel.add(timerLabel);

        scoreLabel = new JLabel("Score: " + gameState.getGlobalScore());
        scoreLabel.setBounds(10, 10, 250, 30);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        bgPanel.add(scoreLabel);



        try {
            stageMusic = new Music.MusicPlayer();
            //stageMusic.playLoop("/Music/surf_theme.wav");
        } catch (Exception ignored) {
        }

        gameTimer = new Timer(1000, e -> {  
            gameTime--;
            timerLabel.setText("Time: " + gameTime);

            if (gameTime <= 0) {
                gameTimer.stop();
                endStage(stage); 
            }
        });


        startCountdown(stage);
        this.setVisible(true);

    }

    public boolean isStageCompleted() {
        return stageCompleted;
    }

    private void startCountdown(Stage stage) {

        countdownTimer = new Timer(1000, e -> {
            countdown--;
            repaint();

            if (countdown <= 0) {
                countdownTimer.stop();
                isCountdownActive = false;

                spawner(stage.stagePokemon);

                gameTimer.start();

                try {
                    stageMusic = new Music.MusicPlayer();
                    stageMusic.playLoop("/Music/surf_theme.wav");
                } catch (Exception ignored) {}

                repaint();
            }
        });

        countdownTimer.start();
    }


    public void spawner(ArrayList<Pokemon> pokemonList) {
        spawnTimer = new Timer(500, e -> spawnPokemonButton(pokemonList)); 
        spawnTimer.setRepeats(true);
        spawnTimer.start();
    }

    private void initPauseResumeButtons() {
        pauseButton = ui.ButtonStyle.createButton("PAUSE");
        pauseButton.setBounds(this.getWidth()/2 - 50, 10, 100, 30);
        pauseButton.addActionListener(e -> pauseGame());
        this.getContentPane().add(pauseButton);

        resumeButton = ui.ButtonStyle.createButton("RESUME");
        resumeButton.setBounds(this.getWidth()/2 - 50, 10, 100, 30);
        resumeButton.setVisible(false);
        resumeButton.addActionListener(e -> resumeGame());
        this.getContentPane().add(resumeButton);
    }

    private void pauseGame() {
        if (!isPaused) {
            isPaused = true;

            gameTimer.stop();
            if (spawnTimer != null) spawnTimer.stop();

            for (Timer t : despawnTimers) {
                t.stop();
            }

            for (JButton btn : activeButtons) {
                btn.setEnabled(false);
            }

            pauseButton.setVisible(false);
            resumeButton.setVisible(true);
        }
    }


    private void resumeGame() {
        if (isPaused) {
            isPaused = false;

            gameTimer.start();
            if (spawnTimer != null) spawnTimer.start();

            for (Timer t : despawnTimers) {
                t.start();
            }

            for (JButton btn : activeButtons) {
                btn.setEnabled(true);
            }

            pauseButton.setVisible(true);
            resumeButton.setVisible(false);
        }
    }



    public void spawnPokemonButton(ArrayList<Pokemon> pokemonList) {
        if (isCountdownActive) return;

        int buttonSize = 170;

        int maxX = this.getWidth() - buttonSize - 16;
        int maxY = this.getHeight() - buttonSize - 39;

        int x = (int) (Math.random() * maxX);
        int y = (int) (Math.random() * maxY);

        JButton button = new JButton();
        button.setBounds(x, y, buttonSize, buttonSize);
        button.setBorder(null);
        button.setContentAreaFilled(false);

        boolean isBomb = Math.random() < bombChance;

        if (isBomb) {
            ImageIcon bombIcon = new ImageIcon("assets/images/bomb.png");

        Image bombImg = bombIcon.getImage().getScaledInstance(
            buttonSize,
            buttonSize,
            Image.SCALE_SMOOTH
        );

        bombIcon = new ImageIcon(bombImg);
        button.setIcon(bombIcon);


            button.addActionListener(e -> {
                gameTimer.stop();
                endGame();
            });

        } else {
            Pokemon pokemon = logic.randomizer(pokemonList);
            String formattedID = String.format("%04d", pokemon.pokemonID);
            String fileName = formattedID + ".png";
            java.io.File file = new java.io.File("firered-leafgreen/" + fileName);
            
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(img));
            }

            button.addActionListener(e -> {
                ImageIcon icon = (ImageIcon) button.getIcon();
                if (icon != null) {
                    Image img = icon.getImage();
                    Image whiteImg = createWhiteSilhouette(img, button.getWidth(), button.getHeight());
                    button.setIcon(new ImageIcon(whiteImg));
                    
                }

                button.setEnabled(false);

                Timer effectTimer = new Timer(300, ev -> {
                    this.remove(button);
                    activeButtons.remove(button);
                    this.repaint();
                });
                effectTimer.setRepeats(false);
                effectTimer.start();


                gameState.addCaughtPokemon(pokemon);
                gameState.addScore(100);
                scoreLabel.setText("Score: " + gameState.getGlobalScore());
                try {
                    MusicPlayer.playOnce("/Music/catch1.wav");
                } catch (Exception ignore) {
                }
            });
            
        this.add(button);
        this.repaint();

        Timer despawnTimer = new Timer(1000, e -> {
            Timer t = (Timer) e.getSource();

            t.stop();
            this.remove(button);
            activeButtons.remove(button);
            despawnTimers.remove(t);
            this.repaint();
        });

        despawnTimer.setRepeats(false);
        despawnTimers.add(despawnTimer);
        despawnTimer.start();

        }

        this.add(button);
        this.repaint();

        Timer timer = new Timer(1000, e -> {
            this.remove(button);
            this.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void endGame() {
    try {
        if (stageMusic != null) stageMusic.stop();
    } catch (Exception ignored) {}
    
    if (gameTimer != null) gameTimer.stop();
    if (spawnTimer != null) spawnTimer.stop();
    for (Timer t : despawnTimers) t.stop();
    
    for (Component comp : this.getContentPane().getComponents()) {
        if (comp instanceof JButton) {
            comp.setEnabled(false);
        }
    }
    
    gameState.transferCaughtPokemonToBST();
    
    try {
        Model.PlayerData currentPlayer = PlayerDataManager.getCurrentPlayerData();
        if (currentPlayer != null && currentPlayer.playerName != null && !currentPlayer.playerName.isEmpty()) {
            PlayerDataManager.syncFromGameState(
                gameState, 
                currentPlayer.playerName,
                currentPlayer.starterPokemonId
            );
            
            FileHandler.saveToLeaderboards(currentPlayer);
            
            FileHandler.deleteSaveFile();
        }
    } catch (Exception ex) {
        System.err.println("Error saving to leaderboards on game over: " + ex.getMessage());
    }
    
    try {
        MusicPlayer.playOnce("/Music/GameOver.wav");
    } catch (Exception ignored) {}

    SwingUtilities.invokeLater(() -> {
        new View.GameOverFrame();
    });
    GameState.setCurrenStage(null);
    this.dispose();
}

   private void endStage(Stage currStage) {

        try {
            if (stageMusic != null) stageMusic.stop();
        } catch (Exception ignored) {}

        if (gameTimer != null) gameTimer.stop();
        if (spawnTimer != null) spawnTimer.stop();
        for (Timer t : despawnTimers) t.stop();

        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JButton && comp != pauseButton && comp != resumeButton) {
                comp.setEnabled(false);
            }
        }

        try {
            MusicPlayer.playOnce("/Music/StageClear.wav");
        } catch (Exception ignored) {}


        caughtCountThisStage = gameState.getCaughtPokemonArray().size();
        gameState.transferCaughtPokemonToBST();

        unlockNextStage(currStage);
        stageClearMessage =
                "Stage Completed!\nScore: " + gameState.getGlobalScore() +
                "\nCaught Pokémon this stage: " + caughtCountThisStage;

        isFinalStage = currStage.stageName.equalsIgnoreCase("lava");

        int btnWidth = 200;
        int btnHeight = 40;
        int centerX = 640;
        int btnY = 500;
        int spacing = 50;

        nextStageBtn = ButtonStyle.createButton(isFinalStage ? "CONTINUE" : "NEXT STAGE");
        nextStageBtn.setBounds(centerX - btnWidth - 10, btnY, btnWidth, btnHeight);
        nextStageBtn.addActionListener(e -> {
            stageCompleted = true;
            try {
                int next = getNextStageNumber(currStage);
                PlayerDataManager.saveProgress(gameState, next);
            } catch (Exception ex) {
                System.err.println("Error saving progress: " + ex.getMessage());
            }

            if(isFinalStage) {
                new EndingFrame(gameState, stageMusic);
            }

            showLoadingScreenOnFrame(() -> {
                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    View.StageManager.nextStage(currStage);
                });
            });
        });
        this.getContentPane().add(nextStageBtn);

        pokedexBtn = ButtonStyle.createButton("OPEN POKEDEX");
        pokedexBtn.setBounds(centerX - btnWidth / 2, btnY + spacing, btnWidth, btnHeight);
        pokedexBtn.addActionListener(e -> {
            PokedexFrame pokedex = new PokedexFrame();
            pokedex.setStageClearPanel(this);
        });
        this.getContentPane().add(pokedexBtn);

        exitBtn = ButtonStyle.createButton("EXIT");
        exitBtn.setBounds(centerX + 10, btnY, btnWidth, btnHeight);
        exitBtn.addActionListener(e -> {
            stageCompleted = true;
            try {
                int next = getNextStageNumber(currStage);
                PlayerDataManager.saveProgress(gameState, next);
            } catch (Exception ex) {
                System.err.println("Error saving progress on exit: " + ex.getMessage());
            }

            this.dispose();
            new MainFrame();
        });
        this.getContentPane().add(exitBtn);

        showStageClear = true;
        repaint();
    }


    private int getNextStageNumber(Stage currStage) {
        String name = currStage.stageName.toLowerCase();
        if (name.equals("grass")) return 2;
        if (name.equals("cave")) return 3;
        if (name.equals("ocean")) return 4;
        if (name.equals("lava")) return 4;  
        return 1;
    }

    public void showStageClearOverlay() {
        showStageClear = true;
        repaint();
    }

    public void showLoadingScreenOnFrame(Runnable onComplete) {
        if (this == null) return;
        
        this.getContentPane().removeAll();
        
        JPanel blackPanel = new JPanel(new GridBagLayout());
        blackPanel.setBackground(Color.BLACK);
        blackPanel.setOpaque(true);
        
        Loading_Screen loadingScreen = new Loading_Screen(2000, 0x000000);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        blackPanel.add(loadingScreen, gbc);
        
        this.setContentPane(blackPanel);
        
        this.revalidate();
        this.repaint();
        
        Timer completeTimer = new Timer(2500, e -> {
            if (onComplete != null) {
                onComplete.run();
            }
        });
        completeTimer.setRepeats(false);
        completeTimer.start();
    }

    private void unlockNextStage(Stage currStage) {
        switch (currStage.stageName.toLowerCase()) {
            case "grass" -> View.StageManager.stage2.isUnlocked = true;
            case "cave"  -> View.StageManager.stage3.isUnlocked = true;
            case "ocean" -> View.StageManager.stage4.isUnlocked = true;
            case "lava"  -> {
                // Final stage
            }
        }
    }

    
    private String getBackgroundPath(String stageName) {
        switch (stageName.toLowerCase()) {
            case "grass":
                return "Backgrounds/grass.png";
            case "cave":
                return "Backgrounds/cave.png";
            case "ocean":
                return "Backgrounds/ocean.png";
            case "lava":
                return "Backgrounds/lava.png";
            default:
                return "Backgrounds/grass.png";
        }
    }


    class BackgroundPanel extends JPanel {

    private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            java.io.File file = new java.io.File(imagePath);
            if (file.exists()) {
                this.backgroundImage = new ImageIcon(file.getAbsolutePath()).getImage();
            } else {
                this.backgroundImage = null;
            }
            this.setLayout(null);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
            if (backgroundImage != null) {
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g2d.setColor(new Color(50, 50, 50));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            
            if (showStageClear) {
                g2d.setColor(new Color(0, 0, 0, 200));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                int boxWidth = 500;
                int boxHeight = 200;
                int boxX = (getWidth() - boxWidth) / 2;
                int boxY = (getHeight() - boxHeight) / 2;
                
                g2d.setColor(new Color(30, 30, 30));
                g2d.fillRect(boxX, boxY, boxWidth, boxHeight);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRect(boxX, boxY, boxWidth, boxHeight);
                
                Font font = ButtonStyle.getFont().deriveFont(Font.BOLD, 24f);
                g2d.setFont(font);
                g2d.setColor(Color.WHITE);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                String[] lines = stageClearMessage.split("\n");
                int textY = boxY + 50;
                for (String line : lines) {
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = boxX + (boxWidth - fm.stringWidth(line)) / 2;
                    g2d.drawString(line, textX, textY);
                    textY += fm.getHeight() + 10;
                }
            }

        if (isCountdownActive) {

            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 140));
            g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            String text = String.valueOf(countdown);
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() + fm.getAscent()) / 2;

            g2d.drawString(text, x, y);
        }

                    
            g2d.dispose();
        }

    }

    private Image createWhiteSilhouette(Image original, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(original, 0, 0, width, height, null);

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int alpha = (img.getRGB(x, y) >> 24) & 0xff;
                if (alpha != 0) {
                    img.setRGB(x, y, (alpha << 24) | 0xFFFFFF);
                }
            }
        }

        g2d.dispose();
        return img;
    }
}