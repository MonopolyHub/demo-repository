package model;

import java.util.Random;

public class Dice {
    Random rand = new Random();
    private Boolean rolling;
    public Dice() {}
    public int roll() {
        return rand.nextInt(6)+1;
    }
    public Boolean rolling() {
        return rolling;
    }
    public void setRolling(Boolean rolling) {
        this.rolling = rolling;
    }


}
