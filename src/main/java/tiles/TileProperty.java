package tiles;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TileProperty extends Tile {
    public TileProperty(int step) {
        super(step, TileType.PROPERTY, loadImage(), null);
    }

    public static Image loadImage() {
        URL url = TileProperty.class.getResource("null");
        if (url == null) {
            System.err.println("TileProperty icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
