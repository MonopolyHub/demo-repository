package actions;

import dataStructures.linkedlist.Node;

public class MoveResult {

    private Node startNode;
    private Node finalNode;
    private boolean passedGO;

    public MoveResult(Node startNode, Node finalNode, boolean passedGO) {
        this.startNode = startNode;
        this.finalNode = finalNode;
        this.passedGO = passedGO;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getFinalNode() {
        return finalNode;
    }

    public boolean hasPassedGO() {
        return passedGO;
    }
}