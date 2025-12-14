import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import Logic.Util;
import pkmn.Pokemon;
import View.Loading_Screen;

public class Intro {

    private int currentIndex = 0;
    private BufferedImage[] storyImages;
    private BufferedImage[] pokemonStarterImages;
    private JLabel imageLabel;
    private JPanel captionPanel;
    private JFrame frame;
    private JLabel topLabel;
    private String[] labelTexts;
    private int[] chooseStarter;
    private int chosenStarterId = -1;
    private JPanel skipPanel;
    private Timer slideshowTimer;
    private boolean isTransitioning = false;

    private final String[] captions = {
            "We are going on a field trip around the world, and all kinds of Pokemon out there awaits to be caught.",
            "We need to be prepared for our trip. Fortunately for us, I know just where we can get some help!",
            "This is Professor Oak’s laboratory. We can pick a Pokemon for us to start with. I wonder which Pokemon is it?"
    };

    public interface StarterSelectionListener {
        void onStarterChosen(int starterId);
    }

    private StarterSelectionListener selectionListener;

    public void setStarterSelectionListener(StarterSelectionListener listener) {
        this.selectionListener = listener;
    }

    public void launchIntro() {
        // Hardcode starter Pokemon IDs: 1 (Bulbasaur), 4 (Charmander), 7 (Squirtle)
        setStarters();
        SwingUtilities.invokeLater(this::GUI);
    }
    
    // Set hardcoded starter Pokemon IDs
    private void setStarters() {
        Util util = new Util();
        
        // Hardcode the three classic starters
        int[] starterIds = {1, 4, 7}; // Bulbasaur, Charmander, Squirtle
        
        labelTexts = new String[3];
        chooseStarter = new int[3];
        
        for (int i = 0; i < 3; i++) {
            Pokemon p = util.getPokemonById(starterIds[i]);
            if (p != null) {
                labelTexts[i] = p.name.toUpperCase();
                chooseStarter[i] = p.pokemonID;
            } else {
                // Fallback if Pokemon not found
                labelTexts[i] = "POKEMON " + starterIds[i];
                chooseStarter[i] = starterIds[i];
            }
        }
    }

    private void GUI() {
        frame = new JFrame("PokeCatch Intro");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new BorderLayout());

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        imageLabel.setOpaque(false);
        frame.add(imageLabel, BorderLayout.CENTER);

        captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.Y_AXIS));
        captionPanel.setOpaque(false);
        captionPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 100, 20));
        frame.add(captionPanel, BorderLayout.SOUTH);

        String[] storyImagesArr = {
                "/introResources/pallet_town.png",
                "/introResources/walk.jpg",
                "/introResources/starter.png",
                "/introResources/transparent.png"
        };
        storyImages = new BufferedImage[storyImagesArr.length];
        for (int i = 0; i < storyImagesArr.length; i++) {
            storyImages[i] = loadAndScale(Objects.requireNonNull(getClass().getResource(storyImagesArr[i])), 800, 500);
        }

        // Load Pokemon images based on selected starter IDs (smaller size to prevent blurring)
        pokemonStarterImages = new BufferedImage[3];
        for (int i = 0; i < 3 && i < chooseStarter.length; i++) {
            pokemonStarterImages[i] = loadPokemonImage(chooseStarter[i], 300, 300);
        }

        frame.setVisible(true);

        startSlideshow();
        showSkipButton();
    }

    private void startSlideshow() {
        currentIndex = 0;
        imageLabel.setIcon(new ImageIcon(storyImages[currentIndex]));
        updateCaption(currentIndex);

        slideshowTimer = new Timer(7000, null);
        slideshowTimer.addActionListener(e -> {
            int nextIndex = currentIndex + 1;
            if (nextIndex < storyImages.length) {
                fadeTransition(storyImages[currentIndex], storyImages[nextIndex], () -> {
                    currentIndex = nextIndex;
                    if (currentIndex < captions.length) {
                        updateCaption(currentIndex);
                    }
                    if (currentIndex < storyImages.length - 1) {
                        slideshowTimer.restart();
                    } else {
                        stopSlideshowAndCaptions();
                        showPokemonStarterImages();
                    }
                });
            }
            slideshowTimer.stop();
        });
        slideshowTimer.setRepeats(false);
        slideshowTimer.start();
    }

    private void stopSlideshowAndCaptions() {
        if (slideshowTimer != null) {
            slideshowTimer.stop();
        }
        imageLabel.setIcon(null);
        captionPanel.removeAll();
        captionPanel.revalidate();
        captionPanel.repaint();

        if (skipPanel != null) {
          frame.remove(skipPanel);
          skipPanel = null;
          frame.revalidate();
          frame.repaint();
        }
    }

    
    private void updateCaption(int index) {
        captionPanel.removeAll();
        if (index < captions.length) {
            JLabel label = new JLabel(captions[index]);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 20));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            captionPanel.add(label);
        }
        captionPanel.revalidate();
        captionPanel.repaint();
    }

    private void fadeTransition(BufferedImage from, BufferedImage to, Runnable onComplete) {
        Timer fadeTimer = new Timer(50, null);
        final float[] alpha = {0f};
        fadeTimer.addActionListener(e -> {
            alpha[0] += 0.025f;
            if (alpha[0] >= 1f) {
                fadeTimer.stop();
                imageLabel.setIcon(new ImageIcon(to));
                if (onComplete != null) onComplete.run();
            } else {
                imageLabel.setIcon(new ImageIcon(blendImages(from, to, alpha[0])));
            }
        });
        fadeTimer.start();
    }

    private void showPokemonStarterImages() {
        currentIndex = 0;
        imageLabel.setIcon(new ImageIcon(pokemonStarterImages[currentIndex]));

        topLabel = new JLabel(labelTexts[currentIndex], SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topLabel.setForeground(Color.YELLOW);
        topLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        frame.add(topLabel, BorderLayout.NORTH);

        showNavigationButtons();
        showChosenPokemonIdButton();

        frame.revalidate();
        frame.repaint();
    }

    private void showNavigationButtons() {
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");

        prevButton.setFont(new Font("Arial", Font.BOLD, 40));
        nextButton.setFont(new Font("Arial", Font.BOLD, 40));
        prevButton.setBackground(Color.BLACK);
        prevButton.setForeground(Color.WHITE);
        nextButton.setBackground(Color.BLACK);
        nextButton.setForeground(Color.WHITE);
        prevButton.setFocusPainted(false);
        nextButton.setFocusPainted(false);

        prevButton.addActionListener(e -> showPreviousManual());
        nextButton.addActionListener(e -> showNextManual());

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        leftPanel.add(prevButton);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(nextButton);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.revalidate();
        frame.repaint();
    }

    private void showChosenPokemonIdButton() {
        JButton indexButton = new JButton("I CHOOSE YOU!");
        indexButton.setFont(new Font("Arial", Font.BOLD, 20));
        indexButton.setBackground(Color.DARK_GRAY);
        indexButton.setForeground(Color.WHITE);
        indexButton.setFocusPainted(false);

        indexButton.addActionListener(e -> {
            if (currentIndex >= 0 && currentIndex < chooseStarter.length) {
                chosenStarterId = chooseStarter[currentIndex];
                if (selectionListener != null) {
                    selectionListener.onStarterChosen(chosenStarterId);
                }
                // Show loading screen on intro frame before disposing
                showLoadingScreenOnFrame();
            }
        });
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        bottomPanel.add(indexButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    private void showPreviousManual() {
        if (currentIndex > 0) {
            currentIndex--;
            imageLabel.setIcon(new ImageIcon(pokemonStarterImages[currentIndex]));
            topLabel.setText(labelTexts[currentIndex]);
        }
    }

    private void showNextManual() {
        if (currentIndex < pokemonStarterImages.length - 1) {
            currentIndex++;
            imageLabel.setIcon(new ImageIcon(pokemonStarterImages[currentIndex]));
            topLabel.setText(labelTexts[currentIndex]);
        }
    }

    // Floating skip button
    private void showSkipButton() {
        JButton skipButton = new JButton("Skip");
        skipButton.setFont(new Font("Arial", Font.BOLD, 16));
        skipButton.setBackground(Color.DARK_GRAY);
        skipButton.setForeground(Color.WHITE);
        skipButton.setFocusPainted(false);

        skipButton.addActionListener(e -> {
            if (isTransitioning) return;
            int nextIndex = currentIndex + 1;
            if (nextIndex >= storyImages.length) return;
            isTransitioning = true;
            if (slideshowTimer != null) {
                slideshowTimer.stop();
            }

            fadeTransition(storyImages[currentIndex], storyImages[nextIndex], () -> {
                currentIndex = nextIndex;
                if (currentIndex < captions.length) {
                    updateCaption(currentIndex);
                }
                if (currentIndex < storyImages.length - 1) {
                    isTransitioning = false;
                    slideshowTimer.restart();
                } else {
                    stopSlideshowAndCaptions();
                    showPokemonStarterImages();
                }
            });
        });

        skipPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        skipPanel.setOpaque(false);
        skipPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));
        skipPanel.add(skipButton);

        frame.add(skipPanel, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    private BufferedImage blendImages(BufferedImage img1, BufferedImage img2, float alpha) {
        BufferedImage blend = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = blend.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, blend.getWidth(), blend.getHeight());
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g.drawImage(img1, 0, 0, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.drawImage(img2, 0, 0, null);
        g.dispose();
        return blend;
    }

    private BufferedImage loadAndScale(java.net.URL path, int width, int height) {
        Image img = new ImageIcon(path).getImage();
        BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = b.createGraphics();
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
        return b;
    }

    // Show loading screen on the intro frame
    public void showLoadingScreenOnFrame() {
        if (frame == null) return;
        
        frame.getContentPane().removeAll();
        
        Loading_Screen loadingScreen = new Loading_Screen(2000, 0x000000);
        frame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.getContentPane().add(loadingScreen, gbc);
        frame.getContentPane().setBackground(Color.BLACK);
        
        frame.revalidate();
        frame.repaint();
        
        // Dispose frame after loading screen completes
        Timer disposeTimer = new Timer(2500, e -> {
            if (frame != null) {
                frame.dispose();
            }
        });
        disposeTimer.setRepeats(false);
        disposeTimer.start();
    }

    private BufferedImage loadPokemonImage(int pokemonId, int width, int height) {
        // Format Pokemon ID to 4 digits (e.g., 1 -> 0001.png)
        String fileName = String.format("%04d.png", pokemonId);
        String imagePath = "firered-leafgreen/" + fileName;
        
        // Try to load the image
        java.io.File imageFile = new java.io.File(imagePath);
        
        if (imageFile.exists()) {
            // Load image from file
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage();
            
            // Create buffered image and scale it
            BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = b.createGraphics();
            
            // Draw the image scaled to the target size
            g.drawImage(img, 0, 0, width, height, null);
            g.dispose();
            
            return b;
        } else {
            // If image not found, create a black image with error message
            BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = b.createGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String text = "Image not found: " + pokemonId;
            FontMetrics fm = g.getFontMetrics();
            int x = (width - fm.stringWidth(text)) / 2;
            int y = (height + fm.getAscent()) / 2;
            g.drawString(text, x, y);
            g.dispose();
            return b;
        }
    }
}