package model;

import dataStructures.linkedlist.CircularLinkedList;

public class Board {

    private CircularLinkedList tiles;

    public Board() {
        this.tiles = new CircularLinkedList();
    }

    public CircularLinkedList getTiles() {
        return tiles;
    }
}
