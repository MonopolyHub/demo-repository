package tiles.data;

public class PropertyData extends TileData {

    private String name;
    private int price;
    private int rent;
    private int upgradeLevel;

    public PropertyData() {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.upgradeLevel = 0;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent + (upgradeLevel * 50);
    }

    public void upgrade() {
        upgradeLevel++;
    }
}