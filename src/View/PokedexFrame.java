package View;

import java.awt.*;
import javax.swing.*;

import Logic.PokeTreeNode;
import Logic.PokemonBST;
import Logic.Util;
import Model.Pokemon;

public class PokedexFrame extends JFrame {
    private PokemonBST originalBst;
    private PokemonBST currentBst;
    private TreePanel treePanel;
    private DetailPanel detailPanel;
    private JLabel rightImageLabel;
    private JLabel statusLabel;
    private JTextField searchField;
    private JComboBox<String> typeFilter;
    private java.util.List<PokeTreeNode> traversalList = new java.util.ArrayList<>();
    private int traversalIndex = 0;

    public PokedexFrame() {
        super("Pokemon Pokedex");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        
        originalBst = new PokemonBST();
        loadSampleData();
        currentBst = originalBst;
        
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(0xFF4646));

        // Create left and right panels
        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        // Center container with two equal columns so both sides get equal space
        JPanel centerContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        centerContainer.setOpaque(false);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerContainer.add(leftPanel);
        centerContainer.add(rightPanel);

        // Place the center container in the center of the frame so it's balanced and centered
        add(centerContainer, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel left = new JPanel(new BorderLayout(5, 5));
        left.setBackground(new Color(0x8BCFD9));
        left.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Back button (top)
        JButton backBtn = new JButton("back");
        backBtn.setBackground(new Color(0xFF4646));
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(e -> dispose());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0x8BCFD9));
        topPanel.add(backBtn, BorderLayout.WEST);
        left.add(topPanel, BorderLayout.NORTH);

        // Search and filter panel
        JPanel searchPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        searchPanel.setBackground(new Color(0x8BCFD9));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));

        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> onSearch());
        JPanel searchRow = new JPanel(new BorderLayout(5, 0));
        searchRow.setBackground(new Color(0x8BCFD9));
        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(searchBtn, BorderLayout.EAST);
        searchPanel.add(searchRow);

        // Type filter
        String[] types = {"All", "NORMAL", "FIRE", "WATER", "GRASS", "ELECTRIC", "ICE", "FIGHTING", "POISON", "GROUND", "FLYING", "PSYCHIC", "BUG", "ROCK", "GHOST", "DRAGON", "STEEL", "FAIRY"};
        typeFilter = new JComboBox<>(types);
        typeFilter.addActionListener(e -> onTypeFilterChanged());
        searchPanel.add(new JLabel("Filter by Type:"));
        searchPanel.add(typeFilter);

        left.add(searchPanel, BorderLayout.NORTH);

        // Tree visualization panel
        treePanel = new TreePanel(currentBst);
        left.add(treePanel, BorderLayout.CENTER);

        // Bottom panel with balance button and status
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBackground(new Color(0x8BCFD9));
        JButton balanceBtn = new JButton("Balance Tree");
        balanceBtn.addActionListener(e -> onBalance());
        statusLabel = new JLabel("Ready");
        bottomPanel.add(balanceBtn, BorderLayout.WEST);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        left.add(bottomPanel, BorderLayout.SOUTH);

        return left;
    }

    private JPanel createRightPanel() {
        JPanel right = new JPanel(new BorderLayout(5, 5));
        right.setBackground(new Color(0xFF4646));
        right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        right.setPreferredSize(new Dimension(300, 0));

        // Image placeholder (use a JLabel so we can set an icon)
        rightImageLabel = new JLabel("(no image)", SwingConstants.CENTER);
        rightImageLabel.setOpaque(true);
        rightImageLabel.setBackground(Color.GRAY);
        rightImageLabel.setForeground(Color.WHITE);
        rightImageLabel.setPreferredSize(new Dimension(280, 200));
        rightImageLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        right.add(rightImageLabel, BorderLayout.NORTH);

        // Detail panel
        detailPanel = new DetailPanel();
        right.add(detailPanel, BorderLayout.CENTER);

        // Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        navPanel.setBackground(new Color(0xFF4646));
        JButton prevBtn = new JButton("prev");
        JButton nextBtn = new JButton("next");
        prevBtn.setPreferredSize(new Dimension(60, 30));
        nextBtn.setPreferredSize(new Dimension(60, 30));
        prevBtn.addActionListener(e -> traversePrev());
        nextBtn.addActionListener(e -> traverseNext());
        navPanel.add(prevBtn);
        navPanel.add(nextBtn);
        right.add(navPanel, BorderLayout.SOUTH);

        return right;
    }

    private void onSearch() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            statusLabel.setText("Enter a Pokemon ID");
            return;
        }
        try {
            int id = Integer.parseInt(text);
            Pokemon p = currentBst.search(id);
            if (p != null) {
                PokeTreeNode node = findNodeById(currentBst.getRoot(), id);
                if (node != null) {
                    treePanel.highlightNode = node;
                    detailPanel.updateDetails(p);
                    statusLabel.setText("Found: " + p.getName());
                    treePanel.repaint();
                }
            } else {
                statusLabel.setText("Pokemon ID " + id + " not found");
            }
        } catch (NumberFormatException ex) {
            statusLabel.setText("Invalid ID format");
        }
    }

    private void onTypeFilterChanged() {
        String selectedType = (String) typeFilter.getSelectedItem();
        currentBst = originalBst.filterByType(selectedType);
        traversalList.clear();
        traversalIndex = 0;
        treePanel.setBst(currentBst);
        statusLabel.setText("Filtered by: " + selectedType);
        treePanel.repaint();
    }

    private void onBalance() {
        if (currentBst.isEmpty()) {
            statusLabel.setText("Tree is empty");
            return;
        }
        int oldDepth = getDepth(currentBst.getRoot());
        currentBst.balance();
        int newDepth = getDepth(currentBst.getRoot());
        traversalList.clear();
        traversalIndex = 0;
        statusLabel.setText("Balanced! Depth: " + oldDepth + " -> " + newDepth);
        treePanel.repaint();
    }

    private void traverseNext() {
        if (traversalList.isEmpty()) {
            buildPreorderList(currentBst.getRoot());
            if (traversalList.isEmpty()) {
                statusLabel.setText("Tree is empty");
                return;
            }
            traversalIndex = 0;
        }

        if (traversalIndex >= traversalList.size()) {
            statusLabel.setText("End of traversal");
            return;
        }

        PokeTreeNode current = traversalList.get(traversalIndex);
        treePanel.highlightNode = current;
        detailPanel.updateDetails(current.pokemon);
        statusLabel.setText("Step " + (traversalIndex + 1) + "/" + traversalList.size() + ": " + current.pokemon.getName());
        traversalIndex++;
        treePanel.repaint();
    }

    private void traversePrev() {
        if (traversalList.isEmpty()) {
            buildPreorderList(currentBst.getRoot());
            traversalIndex = 0;
        }

        if (traversalIndex <= 1) {
            statusLabel.setText("Beginning of traversal");
            if (traversalIndex == 0) return;
        }

        traversalIndex = Math.max(0, traversalIndex - 2);
        PokeTreeNode current = traversalList.get(traversalIndex);
        treePanel.highlightNode = current;
        detailPanel.updateDetails(current.pokemon);
        statusLabel.setText("Step " + (traversalIndex + 1) + "/" + traversalList.size() + ": " + current.pokemon.getName());
        traversalIndex++;
        treePanel.repaint();
    }

    private void buildPreorderList(PokeTreeNode node) {
        if (node == null) return;
        traversalList.add(node);
        buildPreorderList(node.left);
        buildPreorderList(node.right);
    }

    private PokeTreeNode findNodeById(PokeTreeNode node, int id) {
        if (node == null) return null;
        if (node.pokemon.pokemonID == id) return node;
        PokeTreeNode left = findNodeById(node.left, id);
        if (left != null) return left;
        return findNodeById(node.right, id);
    }

    private int getDepth(PokeTreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(getDepth(node.left), getDepth(node.right));
    }

    private void loadSampleData() {
        Util util = new Util();
        java.util.ArrayList<Pokemon> allPokemon = util.initializeStage1Pokemon();
        
        // caught Pokemon IDs stored here in array (not sorted)
        int[] caughtIds = {1, 4, 7, 25, 6, 12, 15};
        for (int id : caughtIds) {
            for (Pokemon p : allPokemon) {
                if (p.pokemonID == id) {
                    originalBst.insert(p);
                    break;
                }
            }
        }
    }

    // Inner class: TreePanel for visualization
    static class TreePanel extends JPanel {
        private PokemonBST bst;
        public PokeTreeNode highlightNode = null;

        public TreePanel(PokemonBST bst) {
            this.bst = bst;
            setBackground(new Color(0x8BCFD9));
            setPreferredSize(new Dimension(400, 400));
        }

        public void setBst(PokemonBST bst) {
            this.bst = bst;
            highlightNode = null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (bst.getRoot() != null) {
                calculatePositions(getWidth(), getHeight());
                drawLines(g2, bst.getRoot());
                drawNodes(g2, bst.getRoot());
            }
        }

        private void calculatePositions(int width, int height) {
            PokeTreeNode root = bst.getRoot();
            if (root == null) return;
            calculatePos(root, width / 2, 30, width / 4, 0);
        }

        private void calculatePos(PokeTreeNode node, int x, int y, int xOffset, int depth) {
            if (node == null) return;
            node.x = x;
            node.y = y;
            int nextY = y + 80;
            int nextOffset = Math.max(20, xOffset / 2);
            if (node.left != null) {
                calculatePos(node.left, x - xOffset, nextY, nextOffset, depth + 1);
            }
            if (node.right != null) {
                calculatePos(node.right, x + xOffset, nextY, nextOffset, depth + 1);
            }
        }

        private void drawLines(Graphics2D g, PokeTreeNode node) {
            if (node == null) return;
            g.setColor(new Color(80, 120, 180));
            g.setStroke(new BasicStroke(3));
            if (node.left != null) {
                g.drawLine(node.x, node.y, node.left.x, node.left.y);
                drawLines(g, node.left);
            }
            if (node.right != null) {
                g.drawLine(node.x, node.y, node.right.x, node.right.y);
                drawLines(g, node.right);
            }
        }

        private void drawNodes(Graphics2D g, PokeTreeNode node) {
            if (node == null) return;
            int radius = 25;
            Color nodeColor = (node == highlightNode) ? new Color(0xFF6464) : new Color(0x96B4DC);
            g.setColor(nodeColor);
            g.fillOval(node.x - radius, node.y - radius, radius * 2, radius * 2);
            g.setColor(new Color(0x325078));
            g.setStroke(new BasicStroke(2));
            g.drawOval(node.x - radius, node.y - radius, radius * 2, radius * 2);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 9));
            String idText = String.valueOf(node.pokemon.pokemonID);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(idText);
            g.drawString(idText, node.x - textWidth / 2, node.y + 3);

            drawNodes(g, node.left);
            drawNodes(g, node.right);
        }
    }

    // Inner class: DetailPanel for Pokemon information
    class DetailPanel extends JPanel {
        private JLabel idLabel, nameLabel, typeLabel, caughtLabel, dupLabel;

        public DetailPanel() {
            setLayout(new BorderLayout(5,5));
            setBackground(new Color(0xFF4646));
            setBorder(BorderFactory.createTitledBorder("Pokemon Details"));

            JPanel info = new JPanel(new GridLayout(5, 1, 5, 5));
            info.setOpaque(false);
            idLabel = new JLabel("ID: --");
            nameLabel = new JLabel("Name: --");
            typeLabel = new JLabel("Type: --");
            caughtLabel = new JLabel("First Caught: --");
            dupLabel = new JLabel("Duplicates: --");
            for (JLabel lbl : new JLabel[]{idLabel, nameLabel, typeLabel, caughtLabel, dupLabel}) {
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Arial", Font.PLAIN, 12));
                info.add(lbl);
            }
            add(info, BorderLayout.CENTER);
        }

        public void updateDetails(Pokemon p) {
            idLabel.setText("ID: " + String.format("%06d", p.pokemonID));
            nameLabel.setText("Name: " + p.getName());
            // Show both types if present; print blank if both are null
            StringBuilder typeSb = new StringBuilder();
            if (p.type != null) typeSb.append(p.type.toString());
            if (p.type2 != null) {
                if (typeSb.length() > 0) typeSb.append(" / ");
                typeSb.append(p.type2.toString());
            }
            String typeText = typeSb.toString();
            typeLabel.setText("Type: " + (typeText.isEmpty() ? "" : typeText));
            caughtLabel.setText("First Caught: --");
            dupLabel.setText("Duplicates: " + p.getDuplicates());

            // Load sprite image
            String fileName = String.format("%04d.png", p.pokemonID);
            java.io.File file = new java.io.File("firered-leafgreen/" + fileName);
            if (file.exists()) {
                ImageIcon ic = new ImageIcon(file.getAbsolutePath());
                Image img = ic.getImage().getScaledInstance(280, 200, Image.SCALE_SMOOTH);
                rightImageLabel.setIcon(new ImageIcon(img));
                rightImageLabel.setText("");
            } else {
                rightImageLabel.setIcon(null);
                rightImageLabel.setText("(image not found)");
                rightImageLabel.setForeground(Color.WHITE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PokedexFrame());
    }
}
