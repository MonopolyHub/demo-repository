package model;

import actions.MoveResult;
import dataStructures.linkedlist.CircularLinkedList;
import dataStructures.linkedlist.Node;

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

    public MoveResult movePlayer(Node startNode, int steps) {
    }
}
