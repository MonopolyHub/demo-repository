package server;

import common.Message;

public class ClientRequest {
    private final int playerId;
    private final Message message;

    public ClientRequest(int playerId, Message message) {
        this.playerId = playerId;
        this.message = message;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Message getMessage() {
        return message;
    }
}
