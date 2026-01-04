package tiles;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TileJail extends Tile {
    public TileJail(int step) {
        super(step, TileType.JAIL, loadImage(), null);
    }

    public static Image loadImage() {
        URL url = TileGo.class.getResource("null");
        if (url == null) {
            System.err.println("TileJail icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
