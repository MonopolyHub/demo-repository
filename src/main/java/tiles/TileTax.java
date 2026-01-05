package tiles;

import tiles.data.JailData;
import tiles.data.TAXData;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TileTax extends Tile {
    public TileTax(int step){
        super(step,TileType.TAX,loadImage(),new TAXData());
    }
    public static Image loadImage() {
        URL url = TileGo.class.getResource("null");
        if (url == null) {
            System.err.println("TileTax icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
