package Game;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Background panel that tries to load an image from resources.
 * If the image is not found, it uses a simple gradient background (safe fallback).
 */
public class BackgroundPanel extends JPanel {

    private Image backgroundImage;

    public BackgroundPanel(String imageName) {
        if (imageName != null) {
            URL imageURL = BackgroundPanel.class.getResource(imageName);
            if (imageURL != null) {
                backgroundImage = new ImageIcon(imageURL).getImage();
            }
        }
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        try {
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Fallback gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 50, 70),
                        0, getHeight(), new Color(10, 20, 30));
                g.setPaint(gp);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        } finally {
            g.dispose();
        }
    }
}
