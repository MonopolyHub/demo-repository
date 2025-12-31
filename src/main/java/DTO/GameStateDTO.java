package DTO;

public class GameStateDTO {
    private int currentPlayerId;
    public PlayerDTO[] players;
    public GameStateDTO(int currentPlayerId, PlayerDTO[] players) {
        this.currentPlayerId = currentPlayerId;
        this.players = players;
    }
}

