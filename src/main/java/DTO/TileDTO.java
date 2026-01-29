package DTO;

public class TileDTO {
    public int index;
    public String type;         // GO / PROPERTY / TAX / CHANCE / JAIL / ...
    public String name;         // display name
    public String colorGroup;   // for properties (may be null)
    public int price;           // purchase price (0 if not applicable)
    public int rent;            // base rent (0 if not applicable)
    public int ownerId;         // -1 if none
    public boolean mortgaged;   // only for properties
    public int houses;         // 0..4 (optional)
    public int hotels;         // 0/1 (optional)

    public TileDTO() {}
}
