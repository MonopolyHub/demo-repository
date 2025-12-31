package server;

import DTO.GameStateDTO;
import DTO.PlayerDTO;
import dataStructures.hashtable.HashTable;
import model.Player;

import java.util.Random;

public class GameState {
    private final HashTable<Integer, Player> players = new HashTable<>();
    private final Random random = new Random();
    private int currentPlayer = 0;

    public void addPlayer(int id, String name) {
        players.put(id, new Player(name, id));

    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int rollDice() {
        return random.nextInt(6) + random.nextInt(6) + 2;
        //باید واسش کلاس بسازم
    }

    public void movePlayer(int playerId, int steps) {
        Player player = players.get(playerId);
        player.move(steps);
    }

    public void endTurn() {
        currentPlayer = (currentPlayer + 1) % 4;
    }

    public GameStateDTO snapShot() {
        PlayerDTO[] arr = new PlayerDTO[players.length()];

        for (int i = 0; i < arr.length; i++) {
            Player player = players.get(i);
            if (player == null) {
                throw new IllegalStateException("Player " + i + " not initialized");
            }
            else {
                arr[i] = new PlayerDTO(player.getId(), player.getBalance(), player.getPosition());
            }
        }
        return new GameStateDTO(currentPlayer, arr);
    }


}
