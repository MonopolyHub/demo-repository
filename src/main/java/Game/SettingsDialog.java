package Game;

import javax.swing.*;
import java.awt.*;

/**
 * Settings dialog for host/port/client count.
 * Uses LayoutManagers and basic validation.
 */
public class SettingsDialog extends JDialog {

    private final JTextField hostField = new JTextField(GameSettings.host, 16);
    private final JTextField portField = new JTextField(String.valueOf(GameSettings.port), 8);
    private final JSpinner clientCountSpinner = new JSpinner(new SpinnerNumberModel(GameSettings.clientCount, 1, 4, 1));

    private boolean saved = false;

    public SettingsDialog(JFrame owner) {
        super(owner, "Settings", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 240);
        setLocationRelativeTo(owner);
        setContentPane(buildUI());
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel title = new JLabel("Game Settings");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Host:"), c);
        c.gridx = 1;
        hostField.setToolTipText("Example: 127.0.0.1");
        form.add(hostField, c);

        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("Port:"), c);
        c.gridx = 1;
        portField.setToolTipText("Example: 5555");
        form.add(portField, c);

        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("Clients to launch:"), c);
        c.gridx = 1;
        form.add(clientCountSpinner, c);

        JTextArea hint = new JTextArea();
        hint.setEditable(false);
        hint.setOpaque(false);
        hint.setLineWrap(true);
        hint.setWrapStyleWord(true);
        hint.setText("Tip: Start the server once, then you can launch multiple clients from the menu.");
        hint.setForeground(new Color(0, 0, 0, 140));

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.add(form, BorderLayout.NORTH);
        center.add(hint, BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(cancelBtn);
        bottom.add(saveBtn);

        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private void onSave() {
        String host = hostField.getText().trim();
        if (host.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Host cannot be empty.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Port must be a number.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (port < 1024 || port > 65535) {
            JOptionPane.showMessageDialog(this, "Port must be between 1024 and 65535.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int count = (Integer) clientCountSpinner.getValue();

        GameSettings.host = host;
        GameSettings.port = port;
        GameSettings.clientCount = count;

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}
