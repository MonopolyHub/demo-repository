package tiles.structures;

import model.Player;
import javax.swing.*;
import java.awt.*;

public class Structure {
    private String name;
    private int price;
    private int rent;

    private Player owner;

    public Structure(String name, int price, int rent,  Player owner) {
        this.name = name;
        this.price = price;
        this.rent = rent;

        this.owner = owner;
    }


//    public static Image loadImage() {
//        String url = icon;
//        if (url == null) {
//            System.err.println("TileGo icon not found!");
//            return null;
//        }
//        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
//    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

//    public int getRahn() {
//        return rahn;
//    }

//    public void setRahn(int rahn) {
//        this.rahn = rahn;
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
