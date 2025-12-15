package View;

import ui.ButtonStyle;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NameDialog extends JDialog {
    private String playerName = null;

    public NameDialog(Frame owner) {
        super(owner, "Enter Player Name", true);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel content = new JPanel(new BorderLayout(8,8));
        content.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel lbl = new JLabel("Please enter your name:");
        lbl.setFont(ButtonStyle.getFont().deriveFont(16f));
        content.add(lbl, BorderLayout.NORTH);

        JTextField tf = new JTextField();
        tf.setFont(ButtonStyle.getFont().deriveFont(16f));
        content.add(tf, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = ButtonStyle.createButton("OK");
        JButton cancel = ButtonStyle.createButton("CANCEL");
        buttons.add(cancel);
        buttons.add(ok);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String val = tf.getText();
                if (val == null || val.trim().isEmpty()) {
                    playerName = "Player";
                } else {
                    playerName = val.trim();
                }
                dispose();
            }
        });

        cancel.addActionListener(e -> {
            playerName = null;
            dispose();
        });

        add(content, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }

    public static String showDialog(Frame owner) {
        NameDialog d = new NameDialog(owner);
        d.setVisible(true);
        return d.playerName;
    }
}
