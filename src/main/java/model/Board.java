package model;

import dataStructures.linkedlist.CircularLinkedList;

public class Board {
    private final int steps =40;
    private CircularLinkedList tiles;

    public Board() {
        this.tiles = new CircularLinkedList();
        intializeTiles();
    }

    public CircularLinkedList getTiles() {
        return tiles;
    }

    public void intializeTiles() {
    }
}
