package model;

public class Card {

    private String description;
    private int value;

    public Card(String description, int value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public int getValue() {
        return value;
    }

}