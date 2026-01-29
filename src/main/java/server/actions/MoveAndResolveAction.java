package server.actions;

import actions.Action;
import actions.ActionType;
import dataStructures.linkedlist.Node;
import model.Player;
import server.GameState;
import actions.MoveResult;

/**
 * One turn's move (dice) + resolve tile.
 *
 * We store enough information to undo:
 * - start node, start balance, start jail/status
 * - end node, end balance, end jail/status
 */
public class MoveAndResolveAction implements Action {

    private final GameState state;
    private final Player player;
    private final int steps;

    private Node startNode;
    private int startBalance;
    private boolean startInJail;
    private int startJailTurns;

    private Node endNode;
    private int endBalance;
    private boolean endInJail;
    private int endJailTurns;

    public MoveAndResolveAction(GameState state, Player player, int steps) {
        this.state = state;
        this.player = player;
        this.steps = steps;
    }

    @Override
    public void execute() {
        startNode = player.getCurrentNode();
        startBalance = player.getBalance();
        startInJail = player.isInJail();
        startJailTurns = player.getJailTurns();

        MoveResult mr = state.getBoard().getTiles().move(startNode, steps);
        player.setCurrentNode(mr.getFinalNode());
        player.setPosition(player.getCurrentNode().getTile().getIndex());

        // resolve tile effects
        state.resolveAfterMove(player, mr.hasPassedGO());

        endNode = player.getCurrentNode();
        endBalance = player.getBalance();
        endInJail = player.isInJail();
        endJailTurns = player.getJailTurns();
    }

    @Override
    public void undo() {
        player.setCurrentNode(startNode);
        player.setPosition(startNode.getTile().getIndex());
        player.setBalance(startBalance);
        player.setInJail(startInJail);
        player.setJailTurns(startJailTurns);
    }

    @Override
    public ActionType getType() {
        return ActionType.MOVE;
    }
}
