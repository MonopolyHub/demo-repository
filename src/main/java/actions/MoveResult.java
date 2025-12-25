package actions;

import dataStructures.linkedlist.Node;

public class MoveResult {

    private Node finalNode;
    private boolean passedGO;

    public MoveResult(Node finalNode, boolean passedGO) {
        this.finalNode = finalNode;
        this.passedGO = passedGO;
    }

    public Node getFinalNode() {
        return finalNode;
    }

    public boolean hasPassedGO() {
        return passedGO;
    }
}