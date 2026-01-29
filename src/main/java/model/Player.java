package model;


import dataStructures.linkedlist.Node;
import dataStructures.tree.Asset;
import tiles.ColorGroup;
import tiles.data.PropertyData;
import tiles.structures.Structure;

public class Player {
    private String name;
    private int id;
    private int balance;
    private Node currentPosition;
    private boolean inJail;
    private int position;
    private Status status;
    private int JailTurns = 0;
    private Asset<Player, ColorGroup> assetTree;
    private int profitRent;

    public Player(String name, int id) {
        this.name = name;
        this.balance = 1500;
        this.inJail = false;
        this.id = id;
        this.position = 1;
        status = Status.ACTIVE;
        assetTree = new Asset<Player, ColorGroup>(this);
        this.profitRent = 0;
    }

    public void assetIncrementProperty(PropertyData propertyData) {
        ColorGroup colorGroup = propertyData.getColorGroup();
        Asset<ColorGroup, PropertyData> assetColorGroup = findAssetColorGroup(assetTree,colorGroup);

        if (assetColorGroup == null) {
            assetColorGroup = new Asset<>(colorGroup);
            assetTree.addChild(assetColorGroup);
        }

        Asset<PropertyData, Structure> assetPropertyData = new Asset<>(propertyData);
        assetColorGroup.addChild(assetPropertyData);
    }

    public void assetDecreaseProperty(PropertyData propertyData) {
        ColorGroup colorGroup = propertyData.getColorGroup();
        Asset<ColorGroup, PropertyData> assetColorGroup = findAssetColorGroup(assetTree, colorGroup);

        if (assetColorGroup != null) {
            Asset<PropertyData, Structure> assetPropertyData = findAssetPropertyData(assetColorGroup, propertyData);
            if (assetPropertyData != null) {
                assetColorGroup.removeChild(assetPropertyData);
            }
        }
    }

    private Asset<ColorGroup, PropertyData> findAssetColorGroup(Asset<Player, ColorGroup> assetTree, ColorGroup colorGroup) {
        return assetTree.search(colorGroup) ? (Asset<ColorGroup, PropertyData>) assetTree.getChild(assetTree.searchIndex(colorGroup)) : null;
    }

    private Asset<PropertyData, Structure> findAssetPropertyData(Asset<ColorGroup, PropertyData> assetColorGroup, PropertyData propertyData) {
        return assetColorGroup.search(propertyData) ? (Asset<PropertyData, Structure>) assetColorGroup.getChild(assetColorGroup.searchIndex(propertyData)) : null;
    }
    private Asset<Structure, Object> findAssetStructure(Asset<PropertyData, Structure> propertyDataAsset, PropertyData propertyData) {
        return propertyDataAsset.search(propertyData.getStructure()) ? (Asset<Structure, Object>) propertyDataAsset.getChild(propertyDataAsset.searchIndex(propertyData.getStructure())) : null;
    }

    public void assetIncreaseStructure(PropertyData propertyData) {
        ColorGroup colorGroup = propertyData.getColorGroup();
        Asset<ColorGroup, PropertyData> assetColorGroup =  findAssetColorGroup(assetTree, colorGroup);
        if (assetColorGroup != null) {
            Asset<PropertyData, Structure> assetPropertyData = findAssetPropertyData(assetColorGroup, propertyData);
            Asset<Structure,Object> assetStructureData = new Asset<>(propertyData.getStructure());
            assetPropertyData.addChild(assetStructureData);
        }

    }

    public void assetDecreaseStructure(PropertyData propertyData) {
        ColorGroup colorGroup = propertyData.getColorGroup();
        Asset<ColorGroup, PropertyData> assetColorGroup =  findAssetColorGroup(assetTree, colorGroup);
        if (assetColorGroup != null) {
            Asset<PropertyData, Structure> assetPropertyData = findAssetPropertyData(assetColorGroup, propertyData);
            Asset<Structure,Object> assetStructureData = findAssetStructure(assetPropertyData, propertyData);
            assetPropertyData.removeChild(assetStructureData);
        }

    }


    public Node getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Node currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void reduceBalance(int amount) {
        this.balance -= amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position % 40;
    }

    public void move(int steps) {
        this.position += (this.position + steps) % 40;

    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getJailTurns() {
        return JailTurns;
    }

    public void setJailTurns(int jailTurns) {
        JailTurns = jailTurns;
    }

    public Node getCurrentNode() {
        return currentPosition;
    }

    public void setCurrentNode(Node finalNode) {
        this.currentPosition = finalNode;
    }

    public Asset<Player, ColorGroup> getAssetTree() {
        return assetTree;
    }

    public void setAssetTree(Asset<Player, ColorGroup> assetTree) {
        this.assetTree = assetTree;
    }

    public int getProfitRent() {
        return profitRent;
    }

    public void setProfitRent(int profitRent) {
        this.profitRent = profitRent;
    }

    public void addProfitRent(int amount) {
        this.profitRent += amount;
    }

    public enum Status {
        ACTIVE,
        INJAIL,
        BANKRUPT
    }


}
