import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Leaderboards extends JFrame {

    private JPanel listPanel;
    private ArrayList<Trainer> trainers = new ArrayList<>();

    public Leaderboards() {
        setTitle("Trainer Leaderboards");
        setSize(750, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //---HEADER TITLE---//
        JPanel header = new JPanel();
        header.setBackground(new Color(220, 20, 60));
        header.setPreferredSize(new Dimension(750, 120));
        header.setLayout(null);

        JLabel title = new JLabel("TRAINER LEADERBOARDS");
        title.setFont(new Font("Press Start 2P", Font.BOLD, 28));
        title.setForeground(Color.BLACK);
        title.setBounds(20, 25, 700, 50);

        JButton backBtn = new JButton("BACK");
        backBtn.setBounds(600, 20, 120, 60);

        header.add(title);
        header.add(backBtn);
        add(header, BorderLayout.NORTH);

        // ===== SCROLLABLE CENTERED LIST ===== //
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(Color.WHITE);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        wrapper.add(listPanel);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);

        add(scroll, BorderLayout.CENTER);

        // Sample trainers
        addTrainer("ASH_KETCHUM", 147, 151);
        addTrainer("GARY_OAK", 145, 151);
        addTrainer("MISTY_WHITE", 138, 151);
        addTrainer("BROCK_GYM", 132, 151);
        addTrainer("LANCE_DRAGON", 128, 151);
        addTrainer("SABRINA_PSY", 121, 151);
        addTrainer("ERIKA_LEAF", 115, 151);
        addTrainer("LORELEI_ICE", 108, 151);
    }

    public void addTrainer(String name, int caught, int total) {
        trainers.add(new Trainer(name, caught, total));
        refreshLeaderboard();
    }

    // Rebuilds leaderboard after sorting
    private void refreshLeaderboard() {
        listPanel.removeAll();

        trainers.sort(Comparator.comparingInt(Trainer::getCaught).reversed());

        int rank = 1;
        for (Trainer t : trainers) {
            addRow(t, rank);
            rank++;
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // Creates leaderboard row
    private void addRow(Trainer t, int rank) {
        JPanel row = new JPanel();
        row.setLayout(null);
        row.setPreferredSize(new Dimension(1400, 90));  // smaller width to center nicely
        row.setMaximumSize(new Dimension(1400, 90));
        row.setBorder(BorderFactory.createLineBorder(new Color(20, 60, 100), 3));
        row.setBackground(Color.WHITE);

        // Rank badge
        JLabel rankBadge = new JLabel("#" + rank, SwingConstants.CENTER);
        rankBadge.setOpaque(true);
        rankBadge.setFont(new Font("Press Start 2P", Font.BOLD, 14));

        if (rank == 1) rankBadge.setBackground(new Color(250, 200, 70));
        else if (rank == 2) rankBadge.setBackground(Color.LIGHT_GRAY);
        else if (rank == 3) rankBadge.setBackground(new Color(255, 150, 80));
        else rankBadge.setBackground(new Color(140, 140, 140));

        rankBadge.setBounds(20, 25, 50, 40);
        row.add(rankBadge);

        // Trainer Name
        JLabel nameLabel = new JLabel(t.name);
        nameLabel.setFont(new Font("Press Start 2P", Font.BOLD, 20));
        nameLabel.setBounds(100, 30, 350, 30);
        row.add(nameLabel);

        // Score badge
        JPanel scoreBadge = new JPanel();
        scoreBadge.setBackground(new Color(220, 20, 60));
        scoreBadge.setLayout(new GridBagLayout());

        JLabel scoreLabel = new JLabel(t.caught + " / " + t.total + " CAUGHT");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Press Start 2P", Font.BOLD, 14));
        scoreBadge.add(scoreLabel);

        scoreBadge.setBounds(480, 25, 180, 40);
        row.add(scoreBadge);

        listPanel.add(Box.createVerticalStrut(12));
        listPanel.add(row);
    }

    // Trainer Data Model
    class Trainer {
        String name;
        int caught;
        int total;

        Trainer(String name, int caught, int total) {
            this.name = name;
            this.caught = caught;
            this.total = total;
        }

        int getCaught() {
            return caught;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Leaderboards().setVisible(true));
    }
}
