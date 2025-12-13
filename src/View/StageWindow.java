package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// placeholder for stage window
public class StageWindow extends JFrame {

    public interface StageCompleteListener {
        void onStageCleared(int stageNumber);
    }

    private int stageNumber;
    private StageCompleteListener listener;

    public StageWindow(int stageNumber, StageCompleteListener listener) {
        super("Stage " + stageNumber);
        this.stageNumber = stageNumber;
        this.listener = listener;
        initUI();
    }

    private void initUI() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lbl = new JLabel("Stage " + stageNumber + " - Catch the Pokemon!", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        add(lbl, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton clearBtn = new JButton("Clear Stage");
        JButton giveUpBtn = new JButton("Give Up");
        bottom.add(clearBtn);
        bottom.add(giveUpBtn);
        add(bottom, BorderLayout.SOUTH);

        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // simulate clearing the stage
                dispose();
                if (listener != null) listener.onStageCleared(stageNumber);
            }
        });

        giveUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // close without calling listener (treated as cleared)
                dispose();
                if (listener != null) listener.onStageCleared(stageNumber);
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
