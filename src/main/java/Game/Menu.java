package Game;

//import client.SwingGameClient;
import client.SwingGameClient;
import server.GameServer;

import javax.swing.*;
import java.awt.*;

/**
 * Main Menu (Legacy UI package) - now complete.
 * Features:
 * - Start Server
 * - Connect page (host/port/clients) + launch clients
 * - Player Setup (names + token colors)
 * - Start Local Game (server + clients)
 * - About + Exit
 *
 * Uses LayoutManagers only (no absolute layout).
 */
public class Menu extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    // Optional presets for client launches (set via Player Setup dialog)
    private String[] presetNames = null;
    private String[] presetColors = null;

    public Menu() {
        super("MonopolyHub - Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        BackgroundPanel bg = new BackgroundPanel("/images/menu.png"); // optional image
        bg.setLayout(new BorderLayout(10, 10));
        bg.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cards.setOpaque(false);
        cards.add(createHomePanel(), "HOME");
        cards.add(createConnectPanel(), "CONNECT");
        cards.add(createAboutPanel(), "ABOUT");

        bg.add(cards, BorderLayout.CENTER);
        setContentPane(bg);

        cardLayout.show(cards, "HOME");
        setVisible(true);
    }

    private JPanel createHomePanel() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("MONOPOLY HUB", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 36f));

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        MonopolyButton startServerBtn = new MonopolyButton("Start Server");
        MonopolyButton startLocalBtn = new MonopolyButton("Start Local Game");
        MonopolyButton connectBtn = new MonopolyButton("Connect...");
        MonopolyButton startClientBtn = new MonopolyButton("Start Client(s)");
        MonopolyButton playerSetupBtn = new MonopolyButton("Player Setup");
        MonopolyButton settingsBtn = new MonopolyButton("Settings");
        MonopolyButton aboutBtn = new MonopolyButton("About");
        MonopolyButton exitBtn = new MonopolyButton("Exit");

        startServerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startLocalBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        connectBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startClientBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerSetupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hint = new JLabel("Tip: Start Server once, then launch Clients. Use Connect page for Host/Port.", SwingConstants.CENTER);
        hint.setForeground(new Color(255, 255, 255, 200));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        startServerBtn.addActionListener(e -> startServer());
        startLocalBtn.addActionListener(e -> startLocalGame());
        connectBtn.addActionListener(e -> cardLayout.show(cards, "CONNECT"));
        startClientBtn.addActionListener(e -> startClient());
        playerSetupBtn.addActionListener(e -> openPlayerSetup());
        settingsBtn.addActionListener(e -> openSettings());
        aboutBtn.addActionListener(e -> cardLayout.show(cards, "ABOUT"));
        exitBtn.addActionListener(e -> System.exit(0));

        box.add(startServerBtn);
        box.add(Box.createVerticalStrut(10));
        box.add(startLocalBtn);
        box.add(Box.createVerticalStrut(10));
        box.add(connectBtn);
        box.add(Box.createVerticalStrut(10));
        box.add(startClientBtn);
        box.add(Box.createVerticalStrut(10));
        box.add(playerSetupBtn);
        box.add(Box.createVerticalStrut(10));
        box.add(settingsBtn);
        box.add(Box.createVerticalStrut(10));
        box.add(aboutBtn);
        box.add(Box.createVerticalStrut(18));
        box.add(hint);
        box.add(Box.createVerticalStrut(18));
        box.add(exitBtn);

        c.gridy = 0;
        root.add(title, c);
        c.gridy = 1;
        root.add(box, c);

        return root;
    }

    private JPanel createConnectPanel() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setOpaque(false);

        JLabel title = new JLabel("Connect", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;

        JTextField hostField = new JTextField(GameSettings.host, 18);
        JTextField portField = new JTextField(String.valueOf(GameSettings.port), 8);
        JSpinner clientsSpinner = new JSpinner(new SpinnerNumberModel(GameSettings.clientCount, 1, 4, 1));

        c.gridx = 0; c.gridy = 0;
        form.add(labelWhite("Host:"), c);
        c.gridx = 1;
        form.add(hostField, c);

        c.gridx = 0; c.gridy = 1;
        form.add(labelWhite("Port:"), c);
        c.gridx = 1;
        form.add(portField, c);

        c.gridx = 0; c.gridy = 2;
        form.add(labelWhite("Clients:"), c);
        c.gridx = 1;
        form.add(clientsSpinner, c);

        JTextArea help = new JTextArea();
        help.setEditable(false);
        help.setOpaque(false);
        help.setLineWrap(true);
        help.setWrapStyleWord(true);
        help.setForeground(new Color(255, 255, 255, 200));
        help.setText(
                "1) Make sure Server is running on the given port.\n" +
                "2) Click 'Launch Clients' to open client windows.\n" +
                "3) (Optional) Use Player Setup to set names & token colors."
        );

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(form, BorderLayout.NORTH);
        center.add(help, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);

        MonopolyButton backBtn = new MonopolyButton("Back");
        MonopolyButton saveBtn = new MonopolyButton("Save");
        MonopolyButton launchBtn = new MonopolyButton("Launch Clients");

        backBtn.addActionListener(e -> cardLayout.show(cards, "HOME"));
        saveBtn.addActionListener(e -> {
            if (applyConnectSettings(hostField.getText(), portField.getText(), (Integer) clientsSpinner.getValue())) {
                JOptionPane.showMessageDialog(this, "Saved!", "Connect", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        launchBtn.addActionListener(e -> {
            if (applyConnectSettings(hostField.getText(), portField.getText(), (Integer) clientsSpinner.getValue())) {
                startClient();
            }
        });

        bottom.add(backBtn);
        bottom.add(saveBtn);
        bottom.add(launchBtn);

        root.add(title, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private JPanel createAboutPanel() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setOpaque(false);

        JLabel title = new JLabel("About", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));

        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setOpaque(false);
        info.setForeground(new Color(255, 255, 255, 220));
        info.setFont(info.getFont().deriveFont(14f));
        info.setText(
                "MonopolyHub (Java Swing)\n" +
                "- Server: server.GameServer\n" +
                "- Client: client.SwingGameClient\n\n" +
                "Data Structures implemented manually (per PDF):\n" +
                "Stack, Queue, HashTable, Graph(AdjList), BST, Heap(Top-K).\n\n" +
                "UI uses FlatLaf for modern look."
        );

        MonopolyButton backBtn = new MonopolyButton("Back");
        backBtn.addActionListener(e -> cardLayout.show(cards, "HOME"));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(backBtn);

        root.add(title, BorderLayout.NORTH);
        root.add(info, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private void openSettings() {
        SettingsDialog dlg = new SettingsDialog(this);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            // If number of clients changed, reset presets to avoid mismatch
            if (presetNames != null && presetNames.length != GameSettings.clientCount) presetNames = null;
            if (presetColors != null && presetColors.length != GameSettings.clientCount) presetColors = null;

            JOptionPane.showMessageDialog(
                    this,
                    "Saved! Host=" + GameSettings.host + "  Port=" + GameSettings.port + "  Clients=" + GameSettings.clientCount,
                    "Settings",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void openPlayerSetup() {
        PlayerSetupDialog dlg = new PlayerSetupDialog(this, GameSettings.clientCount);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            presetNames = dlg.getNames();
            presetColors = dlg.getColors();
            JOptionPane.showMessageDialog(this, "Player setup saved.", "Players", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void startLocalGame() {
        startServer();
        // small delay so server starts listening
        try { Thread.sleep(250); } catch (Exception ignored) { }
        startClient();
    }

    private void startServer() {
        // Start server in a background thread so UI doesn't freeze
        new Thread(() -> {
            try {
                GameServer.main(new String[]{String.valueOf(GameSettings.port)});
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        this,
                        "Server failed to start: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                ));
            }
        }, "MonopolyHub-ServerThread").start();

        JOptionPane.showMessageDialog(
                this,
                "Server start requested. Check console for server logs.",
                "Server",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void startClient() {
        for (int i = 0; i < GameSettings.clientCount; i++) {
            String name = (presetNames != null && i < presetNames.length) ? presetNames[i] : ("Player " + (i + 1));
            String color = (presetColors != null && i < presetColors.length) ? presetColors[i] : "";
            SwingGameClient.main(new String[]{GameSettings.host, String.valueOf(GameSettings.port), name, color});
        }
    }

    private JLabel labelWhite(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        return l;
    }

    private boolean applyConnectSettings(String hostText, String portText, int clients) {
        String host = (hostText == null) ? "" : hostText.trim();
        if (host.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Host cannot be empty.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int port;
        try {
            port = Integer.parseInt((portText == null) ? "" : portText.trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Port must be a number.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (port < 1024 || port > 65535) {
            JOptionPane.showMessageDialog(this, "Port must be between 1024 and 65535.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        GameSettings.host = host;
        GameSettings.port = port;
        GameSettings.clientCount = clients;

        // reset presets if size changed
        if (presetNames != null && presetNames.length != clients) presetNames = null;
        if (presetColors != null && presetColors.length != clients) presetColors = null;

        return true;
    }
}
