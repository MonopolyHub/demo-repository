package tiles.data;

import model.Player;

public class TAXData extends TileData {

    private int taxAmount=30;

    public TAXData() {
    }
    public void taxPlayer(Player player) {
        player.reduceBalance(taxAmount);
    }

    public int getTaxAmount() {
        return taxAmount;
    }

}