package model;

public class GameState {

    private boolean gameOver;
    private int currentPlayerIndex;

    public GameState() {
        this.gameOver = false;
        this.currentPlayerIndex = 0;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void nextPlayer(int totalPlayers) {
        currentPlayerIndex = (currentPlayerIndex + 1) % totalPlayers;
    }
}