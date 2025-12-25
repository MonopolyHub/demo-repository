package dataStructures.linkedlist;

import actions.MoveResult;
import model.Tile;

public class CircularLinkedList {

    private Node head;   // خانه GO
    private Node tail;
    private int size;

    public CircularLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }


    // اضافه کردن خانه به صفحه
    public void addTile(Tile tile) {
        Node newNode = new Node(tile);

        if (head == null) {
            // اولین خانه (GO)
            head = newNode;
            tail = newNode;
            newNode.setNext(newNode); // حلقوی
        } else {
            tail.setNext(newNode);
            newNode.setNext(head);
            tail = newNode;
        }

        size++;
    }


    // حرکت روی صفحه
    public MoveResult move(Node startNode, int steps) {
        if (startNode == null || steps < 0) {
            throw new IllegalArgumentException("Invalid move parameters");
        }

        Node current = startNode;
        boolean passedGO = false;

        for (int i = 0; i < steps; i++) {
            current = current.getNext();

            // تشخیص عبور از GO
            if (current == head) {
                passedGO = true;
            }
        }

        return new MoveResult(current, passedGO);
    }

    // Getter ها
    public Node getHead() {
        return head;
    }

    public int getSize() {
        return size;
    }
}