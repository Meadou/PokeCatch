import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import Logic.FileHandler;

public class Leaderboards extends JFrame {

    private JPanel rowsPanel;
    private final ArrayList<Trainer> trainers = new ArrayList<>();

    public Leaderboards() {
        setTitle("Leaderboards");

        setSize(600, 500);

        setResizable(false);
        setMaximumSize(new Dimension(600, 500));
        setMinimumSize(new Dimension(600, 500));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(600, 70));
        header.setBackground(new Color(220, 20, 60));

        JButton back = new JButton("BACK");
        back.setFont(new Font("Press Start 2P", Font.PLAIN, 14));
        back.setBounds(10, 15, 100, 40);
        back.setFocusPainted(false);

        JLabel title = new JLabel("LEADERBOARDS", SwingConstants.CENTER);
        title.setFont(new Font("Press Start 2P", Font.PLAIN, 18));
        title.setBounds(0, 20, 600, 30);

        header.add(back);
        header.add(title);
        add(header, BorderLayout.NORTH);

        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);

        JPanel tableBox = new JPanel(new BorderLayout());
        tableBox.setBounds(50, 40, 500, 320);
        tableBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        tableBox.setBackground(Color.WHITE);

        // --------COLUMN HEADER-------
        JPanel headerRow = new JPanel(new GridBagLayout());
        headerRow.setPreferredSize(new Dimension(500, 40));
        headerRow.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, Color.BLACK));
        headerRow.setBackground(Color.WHITE);

        GridBagConstraints hgbc = new GridBagConstraints();
        hgbc.gridy = 0;
        hgbc.fill = GridBagConstraints.BOTH;

        hgbc.gridx = 0;
        hgbc.weightx = 0;
        hgbc.ipadx = 100;
        headerRow.add(makeHeader("RANK"), hgbc);

        hgbc.gridx = 1;
        hgbc.ipadx = 50;
        headerRow.add(makeHeader("STARTER"), hgbc);

        hgbc.gridx = 2;
        hgbc.ipadx = 120;
        headerRow.add(makeHeader("NAME"), hgbc);

        hgbc.gridx = 3;
        hgbc.ipadx = 160;
        headerRow.add(makeHeader("SCORE"), hgbc);


        tableBox.add(headerRow, BorderLayout.NORTH);

        // ---------- SCROLLABLE ROWS ----------
        rowsPanel = new JPanel();
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));
        rowsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(rowsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );


        tableBox.add(scrollPane, BorderLayout.CENTER);
        main.add(tableBox);

        add(main, BorderLayout.CENTER);
        
        // Add action listener for back button
        back.addActionListener(e -> dispose());

        // Load leaderboards from file
        loadLeaderboardsFromFile();
    }
    
    // Load leaderboards data from file
    private void loadLeaderboardsFromFile() {
        List<FileHandler.LeaderboardEntry> entries = FileHandler.loadLeaderboards();
        
        for (FileHandler.LeaderboardEntry entry : entries) {
            addTrainer(entry.name, entry.score, entry.starterId);
        }
    }


    private JLabel makeHeader(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Press Start 2P", Font.PLAIN, 11));
        return lbl;
    }

    private JLabel makeTextCell(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Press Start 2P", Font.PLAIN, 11));
        return lbl;
    }

    private JLabel makeImageCell(int starterId) {
        // Load image from Pokemon ID
        String fileName = String.format("%04d.png", starterId);
        String userDir = System.getProperty("user.dir");
        String[] candidates = new String[] {
            "firered-leafgreen/" + fileName,
            "../firered-leafgreen/" + fileName,
            userDir + "/firered-leafgreen/" + fileName,
            userDir + "/../firered-leafgreen/" + fileName
        };
        
        java.io.File found = null;
        for (String path : candidates) {
            java.io.File f = new java.io.File(path);
            if (f.exists() && f.isFile()) {
                found = f;
                break;
            }
        }
        
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        if (found != null) {
            ImageIcon icon = new ImageIcon(found.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
        } else {
            // If image not found, show ID as text
            lbl.setText(String.valueOf(starterId));
            lbl.setFont(new Font("Press Start 2P", Font.PLAIN, 8));
        }
        return lbl;
    }

    // Add trainer with starter ID instead of image path
    public void addTrainer(String name, int score, int starterId) {
        trainers.add(new Trainer(name, score, starterId));
        refresh();
    }

    private void refresh() {
        rowsPanel.removeAll();
        trainers.sort(Comparator.comparingInt(t -> -t.score));

        int rank = 1;
        for (Trainer t : trainers) {

            JPanel row = new JPanel(new GridBagLayout());
            row.setPreferredSize(new Dimension(500, 40));
            row.setMaximumSize(new Dimension(500, 40));
            row.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1;

            //RANK COLUMN
            gbc.gridx = 0;
            gbc.weightx = 0;
            gbc.ipadx = 50;
            row.add(makeTextCell(String.valueOf(rank)), gbc);

            //STARTER ICON COLUMN
            gbc.gridx = 1;
            gbc.ipadx = 80;
            row.add(makeImageCell(t.starterId), gbc);

            //NAME COLUMN
            gbc.gridx = 2;
            gbc.weightx = 1;
            gbc.ipadx = 200;
            row.add(makeTextCell(t.name), gbc);

            //SCORE COLUMN
            gbc.gridx = 3;
            gbc.weightx = 0;
            gbc.ipadx = 120;
            row.add(makeTextCell(String.valueOf(t.score)), gbc);

            rowsPanel.add(row);
            rank++;
        }

        rowsPanel.revalidate();
        rowsPanel.repaint();
    }


    static class Trainer {
        String name;
        int score;
        int starterId;

        Trainer(String name, int score, int starterId) {
            this.name = name;
            this.score = score;
            this.starterId = starterId;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Leaderboards().setVisible(true));
    }
}
