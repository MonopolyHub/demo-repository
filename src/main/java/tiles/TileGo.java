package tiles;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TileGo extends Tile {
    public TileGo(int step) {
        super(step, TileType.Go, loadImage(), null);
    }

    public static Image loadImage() {
        URL url = TileGo.class.getResource("null");
        if (url == null) {
            System.err.println("TileGo icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
