package tiles.block;

import model.Player;
import tiles.TileType;
import tiles.data.TileData;

import javax.swing.*;
import java.awt.*;

public abstract class Tile extends JButton {

    private final int index;          // شماره خانه
    private final TileType tileType;
    private Image icon;               // اگر بعداً بخوای عوضش کنی
    private TileData data;// به جای Object
    private Player player=null;


    protected Tile(int index, TileType tileType, Image icon, TileData data) {
        this.index = index;
        this.tileType = tileType;
        this.icon = icon;
        this.data = data;

        setFocusPainted(false);
        setContentAreaFilled(false);
        updateIcon();
    }


    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getIndex() {
        return index;
    }

    public TileType getTileType() {
        return tileType;
    }

    public TileData getData() {
        return data;
    }

    public void setData(TileData data) {
        this.data = data;
    }

    public void setIconImage(Image icon) {
        this.icon = icon;
        updateIcon();
    }

    private void updateIcon() {
        setIcon(icon != null ? new ImageIcon(icon) : null);
    }
}
