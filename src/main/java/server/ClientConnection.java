package server;

import common.Message;
import java.io.*;
import java.net.Socket;

public class ClientConnection implements Closeable {
    private final int playerId;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Socket socket;

    // "final" removed, added "volatile" for thread visibility
    private volatile boolean closed = false;

    public ClientConnection(int playerId, Socket socket) throws IOException {
        this.playerId = playerId;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    public int getPlayerId() {
        return playerId;
    }

    public void send(Message message) {
        if (!closed) {
            out.println(message.toJson());
        }
    }

    public Message read() throws IOException {
        if (closed) throw new IOException("Connection closed");

        String line = in.readLine();
        if (line == null) {
            close();
            return null; // نشان‌دهنده قطع ارتباط
        }
        // تبدیل جیسون به آبجکت
        return Message.fromJson(line);
    }

    @Override
    public void close() {
        if (closed) return;
        closed = true;
        try {
            socket.close(); // بستن سوکت، استریم‌ها را هم می‌بندد
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }
}
