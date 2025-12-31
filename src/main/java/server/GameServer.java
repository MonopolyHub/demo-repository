package server;

import com.google.gson.Gson;
import common.Message;
import common.MessageType;
import dataStructures.hashtable.HashTable;
import dataStructures.queue.BlockingQueue;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class GameServer {
    private static final int PORT = 5051;
    private final Gson gson = new Gson();
    private final AtomicInteger nextPlayerId = new AtomicInteger(0);
    private final HashTable<Integer,ClientConnection> clients = new HashTable<>();
    private final BlockingQueue<ClientRequest> queue = new BlockingQueue<>();
    private final GameState gameState = new GameState();
    public static void main(String[] args) throws Exception {
        new GameServer().start();
    }
    private void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("server started on port: " + PORT);
        new Thread(this::processLoop).start();
        while (true) {
            Socket socket = serverSocket.accept();
            int id = nextPlayerId.getAndIncrement();
            ClientConnection connection = new ClientConnection(id,socket);
            clients.put(id, connection);
            gameState.addPlayer(id,"Player"+nextPlayerId.get());
            send(connection,new Message(MessageType.WELCOME,null,id));
            new Thread(()->readLoop(connection)).start();
        }
    }
    private void processLoop() {
        while (true) {
            try {
                ClientRequest req = queue.dequeue();
                handle(req);
            }
            catch (Exception ignored) {}
        }
    }
    private void readLoop(ClientConnection connection) {
        try {
            String line;
            while ((line = connection.getIn().readLine()) != null) {
                Message msg = gson.fromJson(line, Message.class);
                queue.enqueue(new ClientRequest(connection.getPlayerId(), msg));
            }
        } catch (Exception e) {
            clients.remove(connection.getPlayerId());
        }
    }
    private void handle (ClientRequest req)
    {
        Message message = req.getMessage();
        if (message.type == MessageType.ROLL_DICE)
        {
            if (req.getPlayerId() != gameState.getCurrentPlayer())
            {send(clients.get(req.getPlayerId()),new Message(MessageType.ERROR, message.messageId,"Not your turn"));
            return;}
        }
        int dice = gameState.rollDice();
        gameState.movePlayer(req.getPlayerId(), dice);
        broadCast(new Message(MessageType.LOG_EVENT,null,"player " + req.getPlayerId()+"rolled "+dice));
        broadCast(new Message(MessageType.STATE_UPDATE,null,gameState.snapShot()));
        if (message.type == MessageType.END_TURN)
        {
            gameState.endTurn();
            broadCast(new Message(MessageType.STATE_UPDATE,null,gameState.snapShot()));
        }
    }
    public void send(ClientConnection c , Message message)
    {
    c.getOut().println(gson.toJson(message));
    }
    public void broadCast(Message message)
    {
        for (ClientConnection c : clients.values())
        {
            send(c,message);
        }
    }





}
