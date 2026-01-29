package model;


import dataStructures.linkedlist.CircularLinkedList;
import dataStructures.linkedlist.Node;
import tiles.ColorGroup;
import tiles.ColorGroupProperty;
import tiles.TileType;
import tiles.block.*;
import tiles.data.GoData;
import tiles.data.PropertyData;


public class Board {
    private final int STEPS = 40;
    int counter = 0;
    int countColorGroup = 0;
    private ColorGroup[] colorGroup = ColorGroup.values();
    private CircularLinkedList tiles;
    private ColorGroupProperty[] colorGroupProperties;


    public Board() {
        this.tiles = new CircularLinkedList();
        // must initialize before using inside intializeTiles()
        this.colorGroupProperties = new ColorGroupProperty[8];
        intializeTiles();
    }

    public CircularLinkedList getTiles() {
        return tiles;
    }

    public void intializeTiles() {
        for (int i = 0; i < STEPS; i++) {
            if (i == 0) tiles.addTile(new TileGo(0, 200));
            else if (i % 10 == 0) tiles.addTile(new TileJail(i));
            else if (i % 10 == 4) tiles.addTile(new TileTax(i));
            else if (i % 10 == 5) tiles.addTile(new TileChance(i));
            else if (countColorGroup < colorGroup.length) {
                int limit = (countColorGroup % 2 == 0) ? 3 : 4;
                if (colorGroupProperties[countColorGroup] == null) {
                    colorGroupProperties[countColorGroup] = new ColorGroupProperty(colorGroup[countColorGroup], limit);
                }
                if (counter < limit) {
                    TileProperty tileProperty  =new TileProperty(i, colorGroup[countColorGroup]);
                    tiles.addTile(tileProperty);
                    PropertyData propertyData = (PropertyData) tileProperty.getData();
                    colorGroupProperties[countColorGroup].addProperty(propertyData);
                    counter++;
                    if (counter >= limit) {
                        counter = 0;
                        countColorGroup++;
                    }
                }
            }
        }
    }

    //    public MoveResult movePlayer(Node startNode, int steps) {
//
//    }
    public void movePlayer(Player player, int move) {
        Node currentPosition = null;
        for (int i = 0; i < move; i++) {
            currentPosition = player.getCurrentNode();
            if (currentPosition.getTile().getTileType() == TileType.GO) {
                GoData goData = (GoData) currentPosition.getTile().getData();
                goData.addReward(player);
            }
            currentPosition = currentPosition.getNext();

        }
        player.setCurrentNode(currentPosition);
    }

    public int getSTEPS() {
        return STEPS;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCountColorGroup() {
        return countColorGroup;
    }

    public void setCountColorGroup(int countColorGroup) {
        this.countColorGroup = countColorGroup;
    }

    public ColorGroup[] getColorGroup() {
        return colorGroup;
    }

    public void setColorGroup(ColorGroup[] colorGroup) {
        this.colorGroup = colorGroup;
    }

    public void setTiles(CircularLinkedList tiles) {
        this.tiles = tiles;
    }

    public ColorGroupProperty[] getColorGroupProperties() {
        return colorGroupProperties;
    }

    public ColorGroupProperty getColorGroupProperty(int colorGroup) {
        return colorGroupProperties[colorGroup];
    }

    public void setColorGroupProperties(ColorGroupProperty[] colorGroupProperties) {
        this.colorGroupProperties = colorGroupProperties;
    }
}
