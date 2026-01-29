package server.actions;

import actions.Action;
import actions.ActionType;
import model.Player;
import tiles.block.TileProperty;
import tiles.data.PropertyData;

/**
 * Build a hotel on a property (requires 4 houses).
 * We convert 4 houses -> 1 hotel.
 */
public class BuildHotelAction implements Action {

    private final Player player;
    private final TileProperty tile;
    private final int cost;

    private int prevBalance;
    private int prevHouseCount;
    private boolean prevHasHotel;

    public BuildHotelAction(Player player, TileProperty tile, int cost) {
        this.player = player;
        this.tile = tile;
        this.cost = cost;
    }

    @Override
    public void execute() {
        PropertyData pd = (PropertyData) tile.getData();
        prevBalance = player.getBalance();
        prevHouseCount = pd.getHouseCount();
        prevHasHotel = pd.isHasHotel();

        player.reduceBalance(cost);
        pd.setHouseCount(0);
        pd.setHasHotel(true);
    }

    @Override
    public void undo() {
        PropertyData pd = (PropertyData) tile.getData();
        player.setBalance(prevBalance);
        pd.setHouseCount(prevHouseCount);
        pd.setHasHotel(prevHasHotel);
    }

    @Override
    public ActionType getType() {
        return ActionType.BUILD;
    }
}
