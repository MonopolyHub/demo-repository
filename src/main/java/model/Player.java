package model;


import dataStructures.linkedlist.Node;

public class Player {

    private String name;
    private int id;
    private int balance;
    private Node currentNode;
    private boolean inJail;
    private int position;


    public Player(String name, int id) {
        this.name = name;
        this.balance = 1500;
        this.inJail = false;
        this.id = id;
        this.position = 1;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBalance(int balance) {
        this.balance = balance;
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
       this.position = position%40;
    }

    public void move(int steps)
    {
        this.position +=( this.position + steps ) % 40;

    }


}
