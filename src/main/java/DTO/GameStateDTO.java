package DTO;

import common.MessageType;

/**
 * Snapshot DTO that server broadcasts to all clients.
 *
 * For simplicity we always send the full snapshot.
 */
public class GameStateDTO {
    public int turnCounter;
    public int currentPlayerId;
    public String phase; // TURN_START / ROLL / RESOLVE / DECISION / TURN_END / FINISHED
    public boolean finished;

    public PlayerDTO[] players;
    public TileDTO[] tiles; // board tiles (for GUI)
    public int currentTileIndex; // current player's tile index (simple view)
    public boolean canBuyProperty; // true when current player landed on unowned property and has money

    public boolean canBuildHouse;
    public boolean canBuildHotel;

    /**
     * Allowed commands for the current player, used by GUI to enable/disable buttons.
     */
    public MessageType[] allowed;

    public GameStateDTO() {
    }
}
