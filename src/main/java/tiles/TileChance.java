package tiles;

import tiles.data.ChanceData;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TileChance extends Tile {
    public TileChance(int step) {
        super(step, TileType.CARD, loadImage(), new ChanceData());
    }

    public static Image loadImage() {
        URL url = TileChance.class.getResource("null");
        if (url == null) {
            System.err.println("TileTax icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
