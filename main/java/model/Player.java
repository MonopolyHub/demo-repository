package model;


import dataStructures.linkedlist.Node;

public class Player {

    private String name;
    private int balance;
    private Node currentNode;
    private boolean inJail;

    public Player(String name, int balance) {
        this.name = name;
        this.balance = balance;
        this.inJail = false;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void reduceBalance(int amount) {
        this.balance -= amount;
    }
}
