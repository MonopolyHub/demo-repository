package tiles;
import model.Player;
import javax.swing.*;
import java.awt.*;

public abstract class Tile extends JButton {
    private final int step;
    private final TileType tileType;
    private final Image icon;
    private Player player;
    protected Tile(int step, TileType tileType, Image icon , Player player) {
        this.step = step;
        this.tileType = tileType;
        this.icon = icon;
        updateIcon();
        this.player = player;
    }
    public int getStep() {
        return step;
    }
    public void updateIcon(){
        if (icon != null) {
            setIcon(new ImageIcon(icon));
        }
        else  {
            setIcon(null);
        }
    }

    public TileType getTileType() {
        return tileType;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
