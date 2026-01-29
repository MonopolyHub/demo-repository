package tiles.data;

import model.Player;

public class GoData extends TileData {

    private int reward;
    private int reward2=300;

    public GoData(int reward) {
        this.reward = reward;
    }

    public int getReward() {
        return reward;
    }

    public void addReward(Player player) {
        player.addBalance(reward);
    }

    public void ResidenceReward(Player player) {
        player.addBalance(reward2);
    }


}