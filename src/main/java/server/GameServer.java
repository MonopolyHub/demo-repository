package server;

import DTO.GameStateDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


public class GameServer {

    public static final int PORT = 5051;
    private static final int MAX_PLAYERS = 4;

    private final Semaphore capacity = new Semaphore(MAX_PLAYERS, true);
    private final AtomicInteger nextPlayerId = new AtomicInteger(1);

    private final Map<Integer, ClientConnection> clients = new HashMap<>();
    private final GameState gameState = new GameState();
    private final Gson gson = new GsonBuilder().create();

    public static void main(String[] args) throws Exception {
        new GameServer().start();
    }

    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port: " + PORT);

        new Thread(() -> {
            while (true) {
                gameState.process();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();

        while (true) {
            Socket socket = serverSocket.accept();

            if (!capacity.tryAcquire()) {
                sendServerFull(socket);
                socket.close();
                continue;
            }

            int id = nextPlayerId.getAndIncrement();
            ClientConnection connection = new ClientConnection(id, socket);

            synchronized (clients) {
                clients.put(id, connection);
            }

            send(connection, new Message(MessageType.WELCOME, generateId(), id));

            new Thread(() -> readLoop(connection)).start();
        }
    }

    private void readLoop(ClientConnection connection) {
        try {
            while (true) {
                Message message = connection.read();
                if (message == null) {
                    break;
                }
                handle(connection, message);
            }
        } catch (Exception ignored) {
        } finally {
            disconnect(connection);
        }
    }

    private void handle(ClientConnection connection, Message message) {
        int pid = connection.getPlayerId();

        switch (message.type) {
            case HELLO -> {
                String name = (message.payload == null) ? ("Player " + pid) : String.valueOf(message.payload);
                gameState.addPlayer(pid, name);
                broadCast(log(name + " joined"));
                broadCast(new Message(MessageType.STATE_UPDATE, generateId(), gameState.snapshot()));
            }
            case ROLL_DICE -> {
                String text = gameState.cmdRollDice(pid);
                broadCast(log(text));
                broadCast(new Message(MessageType.STATE_UPDATE, generateId(), gameState.snapshot()));
            }
            case BUY_PROPERTY -> {
                String text = gameState.cmdBuyProperty(pid);
                broadCast(log(text));
                broadCast(new Message(MessageType.STATE_UPDATE, generateId(), gameState.snapshot()));
            }
            case END_TURN -> {
                String text = gameState.cmdEndTurn(pid);
                broadCast(log(text));
                broadCast(new Message(MessageType.STATE_UPDATE, generateId(), gameState.snapshot()));
            }
            case UNDO -> {
                String text = gameState.cmdUndo(pid);
                broadCast(log(text));
                broadCast(new Message(MessageType.STATE_UPDATE, generateId(), gameState.snapshot()));
            }
            case REDO -> {
                String text = gameState.cmdRedo(pid);
                broadCast(log(text));
                broadCast(new Message(MessageType.STATE_UPDATE, generateId(), gameState.snapshot()));
            }
            case BUILD -> {
                String kind = null;
                try {
                    if (message.payload instanceof String s) {
                        kind = s;
                    } else if (message.payload instanceof java.util.Map<?, ?> mp) {
                        Object k = mp.get("kind");
                        if (k != null) kind = String.valueOf(k);
                    }
                } catch (Exception ignored) {
                }
                String text = gameState.cmdBuild(pid, kind);
                broadCast(log(text));
                broadCast(new Message(MessageType.STATE_UPDATE, generateId(), gameState.snapshot()));
            }
            default -> send(connection, new Message(MessageType.ERROR, generateId(), "Unknown/unsupported command"));
        }
    }

    private void disconnect(ClientConnection connection) {
        try {
            connection.close();
        } catch (Exception ignored) {
        }

        synchronized (clients) {
            clients.remove(connection.getPlayerId());
        }
        capacity.release();

        broadCast(log("Player " + connection.getPlayerId() + " disconnected"));
    }


    private void send(ClientConnection client, Message message) {
        synchronized (client) {
            client.send(message);
        }
    }

    private void broadCast(Message message) {
        synchronized (clients) {
            for (ClientConnection c : clients.values()) {
                c.send(message);
            }
        }
    }

    private void sendServerFull(Socket socket) {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            Message message = new Message(MessageType.SERVER_FULL, generateId(), "Server is full");
            String jsonMessage = gson.toJson(message);
            out.println(jsonMessage);
        } catch (IOException e) {
            System.err.println("Failed to send 'Server Full' message: " + e.getMessage());
        }
    }

    private Message log(String text) {
        return new Message(MessageType.LOG_EVENT, generateId(), text);
    }

    private long generateId() {
        return System.nanoTime();
    }
}
