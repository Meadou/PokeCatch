package View;

import java.awt.*;
import javax.swing.*;
import ui.ButtonStyle;

public class InfoDialog extends JDialog {
    public InfoDialog(Frame owner, String title, String message) {
        super(owner, title, true);
        initUI(message);
    }

    private void initUI(String message) {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel(message, SwingConstants.CENTER);
        lbl.setFont(ButtonStyle.getFont().deriveFont(16f));
        lbl.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        add(lbl, BorderLayout.CENTER);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton ok = ButtonStyle.createButton("OK");
        ok.addActionListener(e -> dispose());
        p.add(ok);
        add(p, BorderLayout.SOUTH);

        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }

    public static void showMessage(Frame owner, String title, String message) {
        InfoDialog d = new InfoDialog(owner, title, message);
        d.setVisible(true);
    }
}
