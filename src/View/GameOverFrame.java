package View;

import java.awt.*;
import javax.swing.*;
import ui.ButtonStyle;

public class GameOverFrame extends JFrame {
    public GameOverFrame() {
        super("Game Over");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600,300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lbl = new JLabel("Game Over", SwingConstants.CENTER);
        lbl.setFont(ButtonStyle.getFont().deriveFont(28f));
        lbl.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));
        add(lbl, BorderLayout.NORTH);

        JLabel info = new JLabel("What would you like to do?", SwingConstants.CENTER);
        info.setFont(ButtonStyle.getFont().deriveFont(16f));
        add(info, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        JButton pokedex = ButtonStyle.createButton("VIEW POKEDEX");
        JButton mainmenu = ButtonStyle.createButton("MAIN MENU");
        JButton exit = ButtonStyle.createButton("EXIT");

        pokedex.addActionListener(e -> {
            new PokedexFrame();
        });

        mainmenu.addActionListener(e -> {
            new intro_GUI.MainFrame();
            dispose();
        });

        exit.addActionListener(e -> System.exit(0));

        buttons.add(pokedex);
        buttons.add(mainmenu);
        buttons.add(exit);

        add(buttons, BorderLayout.SOUTH);

        setVisible(true);
    }
}
