package tiles.data;

import model.Player;
import tiles.ColorGroup;
import tiles.structures.Structure;

public class PropertyData extends TileData {
   private Structure structure;
   private Player owner;
   private int ownerId;
   private String name;
   private ColorGroup colorGroup;//گروه بندی بر اساس رنگ
   private int purchasePrice;//قیمت خرید ملک
   private int baseRent;//مبلغ اجاره پایه
   private int houseCount;//تعداد خانه ساخته شده روی ملک
   private boolean hasHotel;
   private boolean isMortgaged;//ایا ملک وام گرفته
    private int mortgagePrice;

    public PropertyData(ColorGroup colorGroup) {
        structure = null;
        owner = null;
        ownerId = -1;
        this.colorGroup = colorGroup;
        purchasePrice = 200;
        baseRent = 30;
        houseCount = 0;
        hasHotel = false;
        isMortgaged = false;
        mortgagePrice = 100;
    }
    public Structure getStructure() {
        return structure;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColorGroup getColorGroup() {
        return colorGroup;
    }

    public void setColorGroup(ColorGroup colorGroup) {
        this.colorGroup = colorGroup;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getBaseRent() {
        return baseRent;
    }

    public void setBaseRent(int baseRent) {
        this.baseRent = baseRent;
    }

    public int getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(int houseCount) {
        this.houseCount = houseCount;
    }

    public boolean isHasHotel() {
        return hasHotel;
    }

    public void setHasHotel(boolean hasHotel) {
        this.hasHotel = hasHotel;
    }

    public boolean isMortgaged() {
        return isMortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        isMortgaged = mortgaged;
    }

    public int getMortgagePrice() {
        return mortgagePrice;
    }

    public void setMortgagePrice(int mortgagePrice) {
        this.mortgagePrice = mortgagePrice;
    }
}