package server;

import model.common.Message;

public class ClientRequest {
    private int playerId;
    private Message message;
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
