package Game;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

/**
 * Entry point for Menu UI.
 * You can also run server.GameServer and client.SwingGameClient directly.
 */
public class main {

    public static void main(String[] args) {
        // FlatLaf (modern look)
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ignored) { }

        // Splash (safe even without images)
        SplashScreen splash = new SplashScreen("/images/Start.png");
        splash.showSplash(1200);

        SwingUtilities.invokeLater(Menu::new);
    }
}
