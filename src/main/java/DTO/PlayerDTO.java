package DTO;

public class PlayerDTO {
    private int id;
    private int money;
    private int position;
    public PlayerDTO(int id, int money, int position) {
        this.id = id;
        this.money = money;
        this.position = position;
    }

    public int getMoney() {
        return money;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }
}
