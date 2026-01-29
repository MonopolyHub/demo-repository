package tiles.data;

import model.Player;

public class JailData extends TileData {
    private Player playerJail;
    private Boolean isJail;
    private int jailTurns;

    public JailData() {
        this.jailTurns = 0;
        this.isJail = false;

    }
    public void jail(Player player) {
        if (!isJail) {
            this.playerJail = player;
            this.isJail = true;
            this.jailTurns++;
        }


    }
    public void  unJail(Player player) {
        isJail = false;
        playerJail = null;
    }


}