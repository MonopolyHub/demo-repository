package server.actions;

import actions.Action;
import actions.ActionType;
import model.Player;
import tiles.block.TileProperty;
import tiles.data.PropertyData;

/**
 * Buy an unowned property.
 */
public class BuyPropertyAction implements Action {

    private final Player buyer;
    private final TileProperty propertyTile;

    private int prevBuyerBalance;
    private Player prevOwner;
    private int prevOwnerId;

    public BuyPropertyAction(Player buyer, TileProperty propertyTile) {
        this.buyer = buyer;
        this.propertyTile = propertyTile;
    }

    @Override
    public void execute() {
        PropertyData pd = (PropertyData) propertyTile.getData();
        prevBuyerBalance = buyer.getBalance();
        prevOwner = pd.getOwner();
        prevOwnerId = pd.getOwnerId();

        buyer.reduceBalance(pd.getPurchasePrice());
        pd.setOwner(buyer);
        pd.setOwnerId(buyer.getId());
        buyer.assetIncrementProperty(pd);
    }

    @Override
    public void undo() {
        PropertyData pd = (PropertyData) propertyTile.getData();
        buyer.setBalance(prevBuyerBalance);
        pd.setOwner(prevOwner);
        pd.setOwnerId(prevOwnerId);
        // remove from asset tree if it was added
        buyer.assetDecreaseProperty(pd);
    }

    @Override
    public ActionType getType() {
        return ActionType.BUY_PROPERTY;
    }
}
