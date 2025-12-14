package View;

import Logic.GameState;
import Music.MusicPlayer;
import ui.Ending;

import javax.swing.*;

public class EndingFrame extends JFrame {
    private final GameState state;
    private final MusicPlayer music;

    public EndingFrame(GameState state, MusicPlayer music) {
        super("Ending");
        this.state = state;
        this.music = music;

        Ending view = new Ending(state);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(view);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void showEnding() {
        if (state.isUnlocked()) return;
        music.playLoop("/music/ending_theme.wav");
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public void dispose() {
        super.dispose();
        music.stop();
    }
}
