package Game;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {
    private final Image backgroundImage;
    public BackgroundPanel(String imageName) {
       URL imageURL = BackgroundPanel.class.getResource(imageName);
        System.out.println("Image URL: " + imageURL);
        if (imageURL == null) {
            throw new RuntimeException("Image not found");
        }
        backgroundImage = new ImageIcon(imageURL).getImage();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
