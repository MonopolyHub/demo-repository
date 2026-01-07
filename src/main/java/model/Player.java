package model;


import dataStructures.linkedlist.Node;

public class Player {
    private String name;
    private int id;
    private int balance;
    private Node currentPosition;
    private boolean inJail;
    private int position;
    private Status status;
    private int JailTurns=0;

    public Node getCurrentNode() {
    }

    public void setCurrentNode(Node finalNode) {
    }

    private 
    public enum Status {
        ACTIVE,
        INJAIL,
        BANKRUPT
    }
    public Player(String name, int id) {
        this.name = name;
        this.balance = 1500;
        this.inJail = false;
        this.id = id;
        this.position = 1;
        status =Status.ACTIVE;
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



}
