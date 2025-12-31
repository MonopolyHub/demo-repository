package client;

import com.google.gson.Gson;
import common.Message;
import common.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class GameClient {
    public static void main() throws IOException {
        Socket socket = new Socket("192.168.71.48", 5051);
        Gson gson = new Gson();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null)
                    System.out.println("SERVER: " + line);
            } catch (Exception ignored) {
            }
        }).start();
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String cmd = console.readLine();
            MessageType type = cmd.equals("roll") ? MessageType.ROLL_DICE : MessageType.END_TURN;

            out.println(gson.toJson(new Message(type, UUID.randomUUID().toString(), null)));
        }
    }
}
}
