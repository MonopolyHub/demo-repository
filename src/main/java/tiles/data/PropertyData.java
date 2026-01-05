package tiles.data;

import model.Player;

public class PropertyData implements TileData {

    private String name;
    private int price;
    private int rent;
    private Player owner;

    public PropertyData(String name, int price, int rent) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owner = null;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getRent() {
        return rent;
    }

    public int getPrice() {
        return price;
    }
}