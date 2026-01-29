package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Message;
import common.MessageType;
import tiles.block.TileProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5051;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson;
    private boolean isRunning;

    // شناسه بازیکنی که سرور به ما اختصاص می‌دهد
    private int myPlayerId;

    public static void main(String[] args) {
        GameClient client = new GameClient();
        client.start();
    }

    public GameClient() {
        this.gson = new GsonBuilder().create();
    }

    public void start() {
        try {
            // 1. اتصال به سرور
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            isRunning = true;

            System.out.println("✅ Connected to game server on port " + PORT);

            // 2. ساخت یک Thread برای گوش دادن به پیام‌های سرور
            Thread listenThread = new Thread(this::listenToServer);
            listenThread.setDaemon(true); // با بسته شدن برنامه اصلی، این هم بسته شود
            listenThread.start();

            // 3. حلقه اصلی برای خواندن دستورات کاربر از کنسول و ارسال به سرور
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter commands (roll, build [index], trade, exit):");

            while (isRunning) {
                String input = scanner.nextLine();
                handleUserInput(input);
            }

        } catch (IOException e) {
            System.err.println("❌ Connection error: " + e.getMessage());
        } finally {
            close();
        }
    }

    // --- مدیریت ورودی‌های کاربر و تبدیل به پیام سرور ---
    private void handleUserInput(String input) {
        String[] parts = input.split(" ");
        String command = parts[0].toLowerCase();

        switch (command) {
            case "roll":
                sendGameAction(MessageType.ROLL_DICE, null); // نال یعنی پارامتر اضافی ندارد
                break;

            case "build":
                if (parts.length < 2) {
                    System.out.println("Usage: build <property_index>");
                    return;
                }
                // ساخت یک دامی پراپرتی برای ارسال (بسته به ساختار کلاس TileProperty شما)
                // اینجا فرض می‌کنیم کلاینت فقط ایندکس را می‌فرستد و سرور می‌فهمد
                // اما چون سرور آبجکت TileProperty می‌خواهد، باید ساختاری شبیه آن بسازیم
                sendBuildRequest(Integer.parseInt(parts[1]));
                break;

            case "exit":
            case "quit":
                isRunning = false;
                break;

            default:
                System.out.println("Unknown command.");
        }
    }

    // --- متد کمکی برای ارسال اکشن‌ها ---
    private void sendGameAction(MessageType type, GameAction actionPayload) {
        if (actionPayload == null) {
            actionPayload = new GameAction();
        }

        // تنظیم خودمان به عنوان انجام دهنده کار
        // نکته: سرور کلس ClientConnection دارد، اما ما اینجا فقط ID یا یک Stub می‌فرستیم
        // چون Gson سمت سرور تلاش می‌کند JSON را به کلاس خودش مپ کند.
        // ما فرض می‌کنیم یک کلاس PlayerRef داریم که فقط ID دارد
        actionPayload.actor = new PlayerReference(myPlayerId);
        actionPayload.actionType = mapTypeToActionType(type);

        // نکته مهم: طبق کد سرور، Payload مسیج اصلی باید یک رشته JSON باشد
        String actionJson = gson.toJson(actionPayload);

        Message message = new Message(type, System.nanoTime(), actionJson);
        sendMessage(message);
    }

    private void sendBuildRequest(int propertyIndex) {
        GameAction action = new GameAction();
        // اینجا باید ساختاری که سرور از TileProperty انتظار دارد را بسازید
        // action.property = ...
        sendGameAction(MessageType.BUILD, action);
    }

    private void sendMessage(Message message) {
        String json = gson.toJson(message);
        out.println(json); // ارسال به سرور (با یک خط جدید)
    }

    // --- حلقه گوش دادن به سرور ---
    private void listenToServer() {
        try {
            String line;
            while (isRunning && (line = in.readLine()) != null) {
                // تبدیل خط دریافتی به آبجکت Message
                try {
                    Message msg = gson.fromJson(line, Message.class);
                    processServerMessage(msg);
                } catch (Exception e) {
                    System.out.println("⚠️ Received unparseable message: " + line);
                }
            }
        } catch (IOException e) {
            if (isRunning) System.out.println("❌ Disconnected from server.");
        }
    }

    // --- پردازش پیام‌های دریافتی از سرور ---
    private void processServerMessage(Message msg) {
        switch (msg.type) {
            case WELCOME:
                // سرور در Payload آی‌دی ما را فرستاده است (به صورت int یا double)
                // Gson اعداد را معمولا Double می‌گیرد، پس کست می‌کنیم
                if (msg.payload instanceof Number) {
                    this.myPlayerId = ((Number) msg.payload).intValue();
                    System.out.println("🎉 Welcome! You are Player ID: " + myPlayerId);
                }
                break;

            case SERVER_FULL:
                System.out.println("⛔ Server is full. Closing connection...");
                isRunning = false;
                break;

            case LOG_EVENT:
                System.out.println("🔔 GAME LOG: " + msg.payload);
                break;

            case STATE_UPDATE:
                // اینجا وضعیت کل بازی (مکان بازیکن‌ها و ...) می‌آید
                // چون payload در سرور به عنوان Object ارسال شده، اینجا یک LinkedTreeMap است
                // برای استفاده واقعی باید آن را دوباره به GameStateDTO تبدیل کنید
                System.out.println("🔄 Game State Updated.");
                // GameStateDTO state = gson.fromJson(gson.toJson(msg.payload), GameStateDTO.class);
                break;

            default:
                System.out.println("Unknown message type: " + msg.type);
        }
    }

    private void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            // ignored
        }
    }

    // --- کلاس‌های کمکی داخلی برای تطابق با JSON سرور ---

    // نگاشت نوع پیام به نوع اکشن (برای پر کردن GameAction)
    private ActionType mapTypeToActionType(MessageType type) {
        switch (type) {
            case ROLL_DICE: return null; // رول دایس شاید اکشن تایپ نخواهد
            case BUILD: return ActionType.BUILD;
            case MORTGAGE: return ActionType.MORTGAGE;
            // سایر موارد...
            default: return null;
        }
    }

    // کپی ساختار GameAction که در سرور دارید (برای سریالایز شدن صحیح)
    static class GameAction {
        public ActionType actionType;
        public PlayerReference actor; // در کلاینت به جای ClientConnection از رفرنس استفاده می‌کنیم
        public PlayerReference target;
        public TileProperty property;
        // public Structure structure;
    }

    // کپی Enum سرور
    enum ActionType {
        TRADE, MORTGAGE, UNMORTGAGE, JAIL_PAY_FINE, JAIL_TRY_DOUBLE, BUILD
    }

    // کلاسی برای شبیه‌سازی فیلد actor.getPlayerId() سمت سرور
    // وقتی این کلاس سریالایز شود، اگر سرور انتظار آبجکت کاملی داشته باشد ممکن است به مشکل بخورد
    // اما معمولا برای DTO فقط ID کافی است.
    static class PlayerReference {
        private int id;
        public PlayerReference(int id) { this.id = id; }
        // این متد برای Gson لازم نیست اما اگر سرور مستقیماً getPlayerId صدا می‌زند شاید لازم شود
    }
}
