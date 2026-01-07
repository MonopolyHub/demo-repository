package actions;

import model.Player;
import model.Board;
import dataStructures.linkedlist.Node;

public class MoveAction implements Action {

    private Player player;
    private Board board;
    private int steps;

    private MoveResult result; // برای undo

    public MoveAction(Player player, Board board, int steps) {
        this.player = player;
        this.board = board;
        this.steps = steps;
    }

    @Override
    public void execute() {
        Node startNode = player.getCurrentNode();
        result = board.movePlayer(startNode, steps);
        player.setCurrentNode(result.getFinalNode());
    }

    @Override
    public void undo() {
        if (result != null) {
            player.setCurrentNode(result.getStartNode());
        }
    }

    @Override
    public ActionType getType() {
        return ActionType.MOVE;
    }

    public MoveResult getResult() {
        return result;
    }
}