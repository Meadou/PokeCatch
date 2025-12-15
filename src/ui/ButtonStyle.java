package ui;

import Music.MusicPlayer;
import java.awt.*;
import java.io.InputStream;
import javax.swing.*;

// Simple helper class for button style - matches Ending.java
public class ButtonStyle {
    private static Font eightBitFont;
    
    static {
        loadFont();
    }
    
    private static void loadFont() {
        try {
            InputStream is = ButtonStyle.class.getResourceAsStream("/fonts/8bit.ttf");
            if (is != null) {
                eightBitFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f);
            } else {
                eightBitFont = new Font("Monospaced", Font.BOLD, 18);
            }
        } catch (Exception e) {
            eightBitFont = new Font("Monospaced", Font.BOLD, 18);
        }
    }
    
    // Create a button with the standard style
    public static JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(new Color(0xD32F2F));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(eightBitFont.deriveFont(14f));
        // Add simple hover effect: brighten background on hover
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            Color original = b.getBackground();
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(original.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(original);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                // play click sound effect (non-blocking)
                try {
                    MusicPlayer.playOnce("/Music/assets_audio_sfx_select.wav");
                } catch (Exception ex) {
                    // ignore sound errors to avoid breaking UI
                }
            }
        });
        return b;
    }
    
    // Get the font for custom use
    public static Font getFont() {
        return eightBitFont;
    }
}

