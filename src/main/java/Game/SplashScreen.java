package Game;

import javax.swing.*;
import java.awt.*;

/**
 * Simple splash screen.
 * Tries to use BackgroundPanel image if available; otherwise shows a gradient + title.
 */
public class SplashScreen extends JWindow {

    public SplashScreen(String imageResource) {
        BackgroundPanel bg = new BackgroundPanel(imageResource);
        bg.setLayout(new BorderLayout());

        JLabel title = new JLabel("MONOPOLY HUB", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        bg.add(title, BorderLayout.CENTER);

        setContentPane(bg);
        setSize(520, 320);
        setLocationRelativeTo(null);
    }

    public void showSplash(int durationMs) {
        setVisible(true);
        try {
            Thread.sleep(durationMs);
        } catch (InterruptedException ignored) {
        }
        setVisible(false);
        dispose();
    }
}
