package server;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    private int playerId;
    private PrintWriter out;
    private BufferedReader in;

    public ClientConnection(int playerId, Socket socket) throws IOException {
        this.playerId = playerId;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    public int getPlayerId() {
        return playerId;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }
}
