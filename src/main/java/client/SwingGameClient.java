package client;

import DTO.GameStateDTO;
import DTO.PlayerDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Message;
import common.MessageType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class SwingGameClient extends JFrame {

    private static final String HOST = "localhost";
    private static final int PORT = 5051;

    private final Gson gson = new GsonBuilder().create();

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private int myPlayerId = -1;
    private GameStateDTO lastState;

    private final JTextArea logArea = new JTextArea(10, 40);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Player", "Money", "Position"}, 0
    );
    private final JTable table = new JTable(tableModel);

    private final BoardPanel boardPanel = new BoardPanel();

    private final JButton rollBtn = new JButton("Roll Dice");
    private final JButton buyBtn = new JButton("Buy Property");
    private final JButton undoBtn = new JButton("Undo");
    private final JButton redoBtn = new JButton("Redo");
    private final JButton endBtn = new JButton("End Turn");

    private final JButton buildHouseBtn = new JButton("Build House");
    private final JButton buildHotelBtn = new JButton("Build Hotel");

    private final JButton reportTopKBtn = new JButton("Top-K Report");
    private final JButton reportBstBtn = new JButton("Properties <= Price");

    private final JLabel statusLabel = new JLabel("Not connected");

    public static void main(String[] args) {
        // Optional modern Look & Feel (requires flatlaf jar on classpath)
        try {
            Class.forName("com.formdev.flatlaf.FlatLightLaf");
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            SwingGameClient ui = new SwingGameClient();
            ui.setVisible(true);
            ui.connectAndRun();
        });
    }

    public SwingGameClient() {
        super("MonopolyHub - Client");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 520);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new BorderLayout());
        top.add(statusLabel, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // Main area: Board (CENTER) + Players table (EAST) + Log (SOUTH)
        JPanel main = new JPanel(new BorderLayout(10, 10));

        // Board in center
        main.add(boardPanel, BorderLayout.CENTER);

        // Players table on the right
        JPanel right = new JPanel(new BorderLayout(5, 5));
        right.add(new JLabel("Players", SwingConstants.CENTER), BorderLayout.NORTH);
        right.add(new JScrollPane(table), BorderLayout.CENTER);
        right.setPreferredSize(new Dimension(220, 200));
        main.add(right, BorderLayout.EAST);

        // Log at bottom
        logArea.setEditable(false);
        JPanel logPanel = new JPanel(new BorderLayout(5, 5));
        logPanel.add(new JLabel("Log", SwingConstants.CENTER), BorderLayout.NORTH);
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        logPanel.setPreferredSize(new Dimension(200, 160));
        main.add(logPanel, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);
JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.add(rollBtn);
        buttons.add(buyBtn);
        buttons.add(undoBtn);
        buttons.add(redoBtn);
        buttons.add(buildHouseBtn);
        buttons.add(buildHotelBtn);
        buttons.add(reportTopKBtn);
        buttons.add(reportBstBtn);
        buttons.add(endBtn);
        add(buttons, BorderLayout.SOUTH);

        rollBtn.addActionListener(e -> send(new Message(MessageType.ROLL_DICE, System.nanoTime(), null)));
        buyBtn.addActionListener(e -> send(new Message(MessageType.BUY_PROPERTY, System.nanoTime(), null)));
        undoBtn.addActionListener(e -> send(new Message(MessageType.UNDO, System.nanoTime(), null)));
        redoBtn.addActionListener(e -> send(new Message(MessageType.REDO, System.nanoTime(), null)));
        endBtn.addActionListener(e -> send(new Message(MessageType.END_TURN, System.nanoTime(), null)));

        reportTopKBtn.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(this, "Enter K (e.g. 3):", "3");
            int k = 3;
            try { if (s != null) k = Integer.parseInt(s.trim()); } catch (Exception ignored) {}
            send(new Message(MessageType.REPORT_TOPK, System.nanoTime(), k));
        });

        reportBstBtn.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(this, "Max price (e.g. 300):", "300");
            int p = 300;
            try { if (s != null) p = Integer.parseInt(s.trim()); } catch (Exception ignored) {}
            send(new Message(MessageType.REPORT_PROPERTIES_UNDER_PRICE, System.nanoTime(), p));
        });

        setButtonsEnabled(false);
    }

    private void connectAndRun() {
        try {
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            statusLabel.setText("Connected. Waiting for WELCOME...");

            Thread t = new Thread(this::listenLoop);
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            appendLog("Connection failed: " + e.getMessage());
        }
    }

    private void listenLoop() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                Message msg = Message.fromJson(line);
                SwingUtilities.invokeLater(() -> process(msg));
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> appendLog("Disconnected."));
        }
    }

    private void process(Message msg) {
        if (msg.type == MessageType.WELCOME) {
            if (msg.payload instanceof Number) {
                myPlayerId = ((Number) msg.payload).intValue();
            }
            String name = JOptionPane.showInputDialog(this, "Enter your name:", "Player " + myPlayerId);
            if (name == null || name.trim().isEmpty()) {
                name = "Player " + myPlayerId;
            }
            statusLabel.setText("You are Player " + myPlayerId + " (" + name + ")");
            send(new Message(MessageType.HELLO, System.nanoTime(), name));
            return;
        }

        if (msg.type == MessageType.LOG_EVENT) {
            appendLog(String.valueOf(msg.payload));
            return;
        }

        if (msg.type == MessageType.STATE_UPDATE) {
            // payload arrives as a LinkedTreeMap; convert by re-serializing
            GameStateDTO state = gson.fromJson(gson.toJson(msg.payload), GameStateDTO.class);
            this.lastState = state;
            refreshUI(state);
            return;
        }

        if (msg.type == MessageType.ERROR) {
            appendLog("ERROR: " + msg.payload);
        }
    }

    private void refreshUI(GameStateDTO state) {
        // board
        boardPanel.updateState(state, myPlayerId);

        // table
        tableModel.setRowCount(0);
        if (state.players != null) {
            for (PlayerDTO p : state.players) {
                tableModel.addRow(new Object[]{"P" + p.getId(), p.getMoney(), p.getPosition()});
            }
        }

        statusLabel.setText(
                "Turn: P" + state.currentPlayerId +
                        " | Phase: " + state.phase +
                        " | You: P" + myPlayerId +
                        " | Tile: " + state.currentTileIndex
        );

        // buttons based on allowed list and current player
        boolean myTurn = (myPlayerId == state.currentPlayerId);
        setButtonsEnabled(myTurn);
        enableByAllowed(state);

        if (state.finished) {
            setButtonsEnabled(false);
        }
    }

    private void enableByAllowed(GameStateDTO state) {
        rollBtn.setEnabled(false);
        buyBtn.setEnabled(false);
        undoBtn.setEnabled(false);
        redoBtn.setEnabled(false);
        endBtn.setEnabled(false);
        buildHouseBtn.setEnabled(false);
        buildHotelBtn.setEnabled(false);
        buildHouseBtn.setEnabled(false);
        buildHotelBtn.setEnabled(false);

        if (state.allowed == null) return;
        for (MessageType t : state.allowed) {
            if (t == MessageType.ROLL_DICE) rollBtn.setEnabled(true);
            if (t == MessageType.BUY_PROPERTY) buyBtn.setEnabled(true);
            if (t == MessageType.UNDO) undoBtn.setEnabled(true);
            if (t == MessageType.REDO) redoBtn.setEnabled(true);
            if (t == MessageType.END_TURN) endBtn.setEnabled(true);
            if (t == MessageType.BUILD) {
                // enable will be refined by canBuildHouse/canBuildHotel
                buildHouseBtn.setEnabled(state != null && state.canBuildHouse);
                buildHotelBtn.setEnabled(state != null && state.canBuildHotel);
            }
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        rollBtn.setEnabled(enabled);
        buyBtn.setEnabled(enabled);
        undoBtn.setEnabled(enabled);
        redoBtn.setEnabled(enabled);
        endBtn.setEnabled(enabled);
        buildHouseBtn.setEnabled(enabled);
        buildHotelBtn.setEnabled(enabled);
        reportTopKBtn.setEnabled(enabled);
        reportBstBtn.setEnabled(enabled);
    }

    private void send(Message m) {
        if (out == null) return;
        out.println(m.toJson());
    }

    private void appendLog(String text) {
        logArea.append(text + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}
