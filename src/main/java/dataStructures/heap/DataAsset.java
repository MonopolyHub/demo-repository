package dataStructures.heap;

import model.Player;

public class DataAsset {
    Player owner;
    int data;

    public DataAsset(Player owner, int data) {
        this.owner = owner;
        this.data = data;
    }
    public int getValue() {
        return data;
    }
    public void setValue(int value) {
        this.data = value;
    }

    @Override
    public String toString() {
        return owner.getName() + " → " + data;
    }
}
