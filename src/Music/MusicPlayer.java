package Music;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public class MusicPlayer {
    private Clip clip;

    public void playLoop(String resourcePath) {
        try {
            if (resourcePath == null || resourcePath.isEmpty()) return;
            URL soundURL = getClass().getResource(resourcePath);
            if (soundURL == null) {
                System.err.println("Audio file not found: " + resourcePath);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null) {
            if (clip.isRunning()) clip.stop();
            clip.close();
            clip = null;
        }
    }

    public static void playOnce(String resourcePath) {
        new Thread(() -> {
            try {
                if (resourcePath == null || resourcePath.isEmpty()) return;
                URL soundURL = MusicPlayer.class.getResource(resourcePath);
                if (soundURL == null) {
                    System.err.println("Audio file not found: " + resourcePath);
                    return;
                }
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                Clip sfxClip = AudioSystem.getClip();
                sfxClip.open(audioIn);
                sfxClip.start();
                sfxClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        sfxClip.close();
                    }
                });
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
