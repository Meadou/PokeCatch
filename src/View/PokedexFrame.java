package View;

import Logic.GameState;
import Logic.PokeTreeNode;
import Logic.PokemonBST;
import java.awt.*;
import javax.swing.*;
import pkmn.Pokemon;
import ui.ButtonStyle;
import ui.PokeGamePanel;

public class PokedexFrame extends JFrame {
    private PokemonBST originalBst;
    private PokemonBST currentBst;
    private TreePanel treePanel;
    private JScrollPane treeScrollPane;
    private DetailPanel detailPanel;
    private JLabel rightImageLabel;
    private JLabel statusLabel;
    private JTextField searchField;
    private JComboBox<String> typeFilter;
    private java.util.List<PokeTreeNode> traversalList = new java.util.ArrayList<>();
    private int traversalIndex = 0;
    private PokeGamePanel stageClearPanel = null;

    public PokedexFrame() {
        super("Pokemon Pokedex");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        
        GameState gameState = GameState.getInstance();
        originalBst = gameState.getGlobalPokemonBST();
        currentBst = originalBst;
        
        initUI();
    }
    
    public void setStageClearPanel(PokeGamePanel panel) {
        this.stageClearPanel = panel;
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(0xFF4646));

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        JPanel centerContainer = new JPanel(new BorderLayout(20, 0));
        centerContainer.setOpaque(false);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        rightPanel.setPreferredSize(new Dimension(385, 0));
        centerContainer.add(rightPanel, BorderLayout.EAST);
        
        centerContainer.add(leftPanel, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        setVisible(true);
        
        setTraversalToRoot();
    }
    
    private void setTraversalToRoot() {
        if (currentBst != null && currentBst.getRoot() != null) {
            buildPreorderList(currentBst.getRoot());
            
            if (!traversalList.isEmpty()) {
                traversalIndex = 0;
                PokeTreeNode rootNode = traversalList.get(0);
                
                treePanel.highlightNode = rootNode;
                
                detailPanel.updateDetails(rootNode.pokemon);
                
                statusLabel.setText("Step 1/" + traversalList.size() + ": " + rootNode.pokemon.getName());
                
                treePanel.repaint();
                scrollToNode(rootNode);
            }
        }
    }

    private JPanel createLeftPanel() {
        JPanel left = new JPanel(new BorderLayout(5, 5));
        left.setBackground(new Color(0x8BCFD9));
        left.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backBtn = ButtonStyle.createButton("BACK");
        backBtn.addActionListener(e -> {
            dispose();
            if (stageClearPanel != null) {
                stageClearPanel.showStageClearOverlay();
            }
        });
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0x8BCFD9));
        topPanel.add(backBtn, BorderLayout.WEST);
        left.add(topPanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        searchPanel.setBackground(new Color(0x8BCFD9));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));

        searchField = new JTextField(10);
        JButton searchBtn = ButtonStyle.createButton("SEARCH");
        searchBtn.addActionListener(e -> onSearch());
        JPanel searchRow = new JPanel(new BorderLayout(5, 0));
        searchRow.setBackground(new Color(0x8BCFD9));
        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(searchBtn, BorderLayout.EAST);
        searchPanel.add(searchRow);

        String[] types = {"All", "NORMAL", "FIRE", "WATER", "GRASS", "ELECTRIC", "ICE", "FIGHTING", "POISON", "GROUND", "FLYING", "PSYCHIC", "BUG", "ROCK", "GHOST", "DRAGON", "STEEL", "FAIRY"};
        typeFilter = new JComboBox<>(types);
        typeFilter.addActionListener(e -> onTypeFilterChanged());
        searchPanel.add(new JLabel("Filter by Type:"));
        searchPanel.add(typeFilter);

        left.add(searchPanel, BorderLayout.NORTH);

        treePanel = new TreePanel(currentBst);
        treeScrollPane = new JScrollPane(treePanel);
        treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        treeScrollPane.setBorder(BorderFactory.createEmptyBorder());
        treeScrollPane.getViewport().setBackground(new Color(0x8BCFD9));
        left.add(treeScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBackground(new Color(0x8BCFD9));
        JButton balanceBtn = ButtonStyle.createButton("BALANCE TREE");
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

        rightImageLabel = new JLabel("(no image)", SwingConstants.CENTER);
        rightImageLabel.setOpaque(true);
        rightImageLabel.setBackground(Color.GRAY);
        rightImageLabel.setForeground(Color.WHITE);
        rightImageLabel.setPreferredSize(new Dimension(280, 200));
        rightImageLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        right.add(rightImageLabel, BorderLayout.NORTH);

        detailPanel = new DetailPanel();
        right.add(detailPanel, BorderLayout.CENTER);

      JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); 
navPanel.setBackground(new Color(0xFF4646));

JButton prevBtn = ButtonStyle.createButton("PREV");
JButton nextBtn = ButtonStyle.createButton("NEXT");

prevBtn.setPreferredSize(new Dimension(80, 40));
nextBtn.setPreferredSize(new Dimension(80, 40));

prevBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
nextBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

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
                    scrollToNode(node);
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
        
        final int oldDepth = getDepth(currentBst.getRoot());
        
        treePanel = new TreePanel(currentBst);
        
        Loading_Screen loadingScreen = new Loading_Screen(1500, 0x8BCFD9);
        treeScrollPane.setViewportView(loadingScreen);
        
        SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() throws Exception {
                currentBst.balance();
                
                int newDepth = getDepth(currentBst.getRoot());
                
                Thread.sleep(1500);
                
                return newDepth;
            }
            
            @Override
            protected void done() {
                try {
                    int newDepth = get();
                    
                    treePanel = new TreePanel(currentBst);
                    treeScrollPane.setViewportView(treePanel);
                    
                    traversalList.clear();
                    traversalIndex = 0;
                    statusLabel.setText("Balanced! Depth: " + oldDepth + " -> " + newDepth);
                    treePanel.repaint();
                    
                } catch (Exception ex) {
                    treePanel = new TreePanel(currentBst);
                    treeScrollPane.setViewportView(treePanel);
                    treePanel.repaint();
                    
                    statusLabel.setText("Error balancing tree");
                    ex.printStackTrace();
                }
            }
        };
        
        worker.execute();
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
        scrollToNode(current);
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
        scrollToNode(current);
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

    private void scrollToNode(PokeTreeNode node) {
        if (node == null || treeScrollPane == null) return;
        
        treePanel.repaint();
        
        SwingUtilities.invokeLater(() -> {
            int padding = 50;
            Rectangle nodeRect = new Rectangle(
                node.x - padding, 
                node.y - padding, 
                padding * 2 + 50, 
                padding * 2 + 50
            );
            
            Rectangle viewRect = treeScrollPane.getViewport().getViewRect();
            int x = nodeRect.x - (viewRect.width / 2) + (nodeRect.width / 2);
            int y = nodeRect.y - (viewRect.height / 2) + (nodeRect.height / 2);
            
            Dimension prefSize = treePanel.getPreferredSize();
            x = Math.max(0, Math.min(x, prefSize.width - viewRect.width));
            y = Math.max(0, Math.min(y, prefSize.height - viewRect.height));
            
            treeScrollPane.getViewport().setViewPosition(new Point(x, y));
        });
    }

    static class TreePanel extends JPanel {
        private PokemonBST bst;
        public PokeTreeNode highlightNode = null;

        private int minX = Integer.MAX_VALUE;
        private int maxX = Integer.MIN_VALUE;
        private int minY = Integer.MAX_VALUE;
        private int maxY = Integer.MIN_VALUE;

        public TreePanel(PokemonBST bst) {
            this.bst = bst;
            setBackground(new Color(0x8BCFD9));
            preloadAllImages();
        }

        public void setBst(PokemonBST bst) {
            this.bst = bst;
            highlightNode = null;
            preloadAllImages();
            revalidate();
            repaint();
        }

        private void preloadAllImages() {
            if (bst == null || bst.getRoot() == null) {
                return;
            }
            int diameter = 50;
            preloadImagesFromNode(bst.getRoot(), diameter - 6, diameter - 6);
        }

        private void preloadImagesFromNode(PokeTreeNode node, int w, int h) {
            if (node == null) return;
            loadImageForId(node.pokemon.pokemonID, w, h);
            preloadImagesFromNode(node.left, w, h);
            preloadImagesFromNode(node.right, w, h);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (bst != null && bst.getRoot() != null) {
                int baseWidth = Math.max(600, getWidth());
                int baseHeight = calculateTreeHeight();
                calculatePositions(baseWidth, baseHeight);
                
                calculateTreeBounds(bst.getRoot());
                
                int padding = 100;
                int prefWidth = Math.max(getWidth(), maxX - minX + padding);
                int prefHeight = Math.max(getHeight(), maxY - minY + padding);
                setPreferredSize(new Dimension(prefWidth, prefHeight));
                
                drawLines(g2, bst.getRoot());    
                drawNodes(g2, bst.getRoot());   
            }
        }

        private int calculateTreeHeight() {
            return getDepth(bst.getRoot()) * 80 + 100;
        }
        
        private int getDepth(PokeTreeNode node) {
            if (node == null) return 0;
            return 1 + Math.max(getDepth(node.left), getDepth(node.right));
        }
        
        private void calculateTreeBounds(PokeTreeNode node) {
            if (node == null) return;
            int radius = 30;
            minX = Math.min(minX, node.x - radius);
            maxX = Math.max(maxX, node.x + radius);
            minY = Math.min(minY, node.y - radius);
            maxY = Math.max(maxY, node.y + radius);
            calculateTreeBounds(node.left);
            calculateTreeBounds(node.right);
        }

        private void calculatePositions(int width, int height) {
            PokeTreeNode root = bst.getRoot();
            if (root == null) return;
            minX = Integer.MAX_VALUE;
            maxX = Integer.MIN_VALUE;
            minY = Integer.MAX_VALUE;
            maxY = Integer.MIN_VALUE;
            calculatePos(root, width / 2, 30, Math.max(40, width / 4), 0);
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
            g.setColor(new Color(0x5078B4));
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
            int diameter = radius * 2;
            Color nodeColor = (node == highlightNode) ? new Color(0xFF6464) : new Color(0x96B4DC);

            g.setColor(nodeColor);
            g.fillOval(node.x - radius, node.y - radius, diameter, diameter);

            Image img = loadImageForId(node.pokemon.pokemonID, diameter - 6, diameter - 6);
            if (img != null) {
                int iw = img.getWidth(null);
                int ih = img.getHeight(null);
                if (iw > 0 && ih > 0) {
                    int ix = node.x - iw / 2;
                    int iy = node.y - ih / 2;
                    g.drawImage(img, ix, iy, null);
                }
            }

            g.setColor(new Color(0x325078));
            g.setStroke(new BasicStroke(2));
            g.drawOval(node.x - radius, node.y - radius, diameter, diameter);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 9));
            String idText = String.valueOf(node.pokemon.pokemonID);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(idText);
            g.drawString(idText, node.x - textWidth / 2, node.y + radius - 4);

            drawNodes(g, node.left);
            drawNodes(g, node.right);
        }

        private Image loadImageForId(int id, int targetW, int targetH) {
            String fileName = String.format("%04d.png", id);
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

            if (found != null) {
                ImageIcon ic = new ImageIcon(found.getAbsolutePath());
                
                if (ic.getIconWidth() > 0 && ic.getIconHeight() > 0) {
                    Image img = ic.getImage();
                    int iw = ic.getIconWidth();
                    int ih = ic.getIconHeight();
                    
                    double aspect = (double) iw / ih;
                    int w = targetW;
                    int h = (int) (w / aspect);
                    if (h > targetH) {
                        h = targetH;
                        w = (int) (h * aspect);
                    }
                    
                    Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    
                    ImageIcon scaledIcon = new ImageIcon(scaled);
                    return scaledIcon.getImage();
                }
            }
            
            return null;
        }
    }

    class DetailPanel extends JPanel {
        private JLabel idLabel, nameLabel, typeLabel, dupLabel;

        public DetailPanel() {
            setLayout(new BorderLayout(5,5));
            setBackground(new Color(0xFF4646));
            setBorder(BorderFactory.createTitledBorder("Pokemon Details"));

            JPanel info = new JPanel(new GridLayout(4, 1, 5, 5));
            info.setOpaque(false);
            idLabel = new JLabel("ID: --");
            nameLabel = new JLabel("Name: --");
            typeLabel = new JLabel("Type: --");
            dupLabel = new JLabel("Duplicates: --");
            for (JLabel lbl : new JLabel[]{idLabel, nameLabel, typeLabel, dupLabel}) {
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Arial", Font.PLAIN, 12));
                info.add(lbl);
            }
            add(info, BorderLayout.CENTER);
        }

        public void updateDetails(Pokemon p) {
            idLabel.setText("ID: " + String.format("%06d", p.pokemonID));
            nameLabel.setText("Name: " + p.getName());
            StringBuilder typeSb = new StringBuilder();
            if (p.type != null) typeSb.append(p.type.toString());
            if (p.type2 != null) {
                if (typeSb.length() > 0) typeSb.append(" / ");
                typeSb.append(p.type2.toString());
            }
            String typeText = typeSb.toString();
            typeLabel.setText("Type: " + (typeText.isEmpty() ? "" : typeText));
            dupLabel.setText("Duplicates: " + p.getDuplicates());

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