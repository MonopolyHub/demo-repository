package server.actions;

import actions.Action;
import actions.ActionType;
import model.Player;
import tiles.block.TileProperty;
import tiles.data.PropertyData;

/**
 * Build one house on a property (0..4).
 * Uses existing PropertyData houseCount + player balance.
 */
public class BuildHouseAction implements Action {

    private final Player player;
    private final TileProperty tile;
    private final int cost;

    private int prevBalance;
    private int prevHouseCount;

    public BuildHouseAction(Player player, TileProperty tile, int cost) {
        this.player = player;
        this.tile = tile;
        this.cost = cost;
    }

    @Override
    public void execute() {
        PropertyData pd = (PropertyData) tile.getData();
        prevBalance = player.getBalance();
        prevHouseCount = pd.getHouseCount();

        player.reduceBalance(cost);
        pd.setHouseCount(prevHouseCount + 1);
        pd.setHasHotel(false);
    }

    @Override
    public void undo() {
        PropertyData pd = (PropertyData) tile.getData();
        player.setBalance(prevBalance);
        pd.setHouseCount(prevHouseCount);
        // hotel unchanged (should be false when building houses)
        pd.setHasHotel(false);
    }

    @Override
    public ActionType getType() {
        return ActionType.BUILD;
    }
}
