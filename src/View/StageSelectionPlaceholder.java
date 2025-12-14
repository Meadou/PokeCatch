package View;

import javax.swing.*;
import java.awt.*;
import View.*;

public class StageSelectionPlaceholder extends JFrame {

    private JButton grassButton;
    private JButton caveButton;
    private JButton oceanButton;
    private JButton lavaButton;
    static int choice;

    public StageSelectionPlaceholder() {
        choice = 0;
        setTitle("Stage Selection");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();

        setVisible(true);
    }

    public int initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(30, 30, 30));
        setContentPane(mainPanel);

        JLabel titleLabel = new JLabel("Select a Stage");
        titleLabel.setBounds(0, 40, 1280, 60);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel);

        grassButton = createStageButton("Grass Stage", 240);
        caveButton  = createStageButton("Cave Stage", 330);
        oceanButton = createStageButton("Ocean Stage", 420);
        lavaButton  = createStageButton("Lava Stage", 510);

        mainPanel.add(grassButton);
        mainPanel.add(caveButton);
        mainPanel.add(oceanButton);
        mainPanel.add(lavaButton);
        
        grassButton.addActionListener(e -> {
            this.dispose();
            StageWindow.stageSelector(1);
        });

        caveButton.addActionListener(e -> {
            this.dispose();
            StageWindow.stageSelector(2);
        });

        oceanButton.addActionListener(e -> {
            this.dispose();
            StageWindow.stageSelector(3);
        });

        lavaButton.addActionListener(e -> {
            this.dispose();
            StageWindow.stageSelector(4);
        });

        return 1;
    }

    private JButton createStageButton(String text, int y) {
        JButton button = new JButton(text);
        button.setBounds(490, y, 300, 60);
        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setFocusPainted(false);
        return button;
    }

    public static void main(String[] args) {
        new StageSelectionPlaceholder();
    }
}
