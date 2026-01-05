package dataStructures.linkedlist;


import tiles.Tile;

public class Node {
    private Tile tile;
    private Node next;

    public Node (Tile tile) {
         this.tile = tile;
         this.next = null;
    }
    public Tile getTile() {
        return tile;

    }
    public void setTile(Tile tile) {
        this.tile =  tile;
    }
    public Node getNext() {
        return next;
    }
    public void setNext(Node next) {
        this.next = next;
    }
}
