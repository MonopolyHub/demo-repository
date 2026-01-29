package Game;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for choosing player names + token colors for N clients.
 * Educational, simple, LayoutManager-based.
 */
public class PlayerSetupDialog extends JDialog {

    private final JTextField[] nameFields;
    private final JComboBox<String>[] colorBoxes;
    private boolean saved = false;

    private static final String[] COLORS = {
            "Blue", "Red", "Green", "Yellow", "Purple", "Black", "Cyan", "Pink"
    };

    @SuppressWarnings("unchecked")
    public PlayerSetupDialog(JFrame owner, int clientCount) {
        super(owner, "Player Setup", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 320);
        setLocationRelativeTo(owner);

        nameFields = new JTextField[clientCount];
        colorBoxes = new JComboBox[clientCount];

        setContentPane(buildUI(clientCount));
    }

    private JPanel buildUI(int n) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel title = new JLabel("Players");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        root.add(title, BorderLayout.NORTH);

        JPanel table = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        table.add(new JLabel("#"), c);
        c.gridx = 1;
        table.add(new JLabel("Name"), c);
        c.gridx = 2;
        table.add(new JLabel("Token Color"), c);

        for (int i = 0; i < n; i++) {
            c.gridy = i + 1;

            c.gridx = 0;
            table.add(new JLabel(String.valueOf(i + 1)), c);

            c.gridx = 1;
            nameFields[i] = new JTextField("Player " + (i + 1), 16);
            table.add(nameFields[i], c);

            c.gridx = 2;
            colorBoxes[i] = new JComboBox<>(COLORS);
            colorBoxes[i].setSelectedIndex(i % COLORS.length);
            table.add(colorBoxes[i], c);
        }

        root.add(table, BorderLayout.CENTER);

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
        for (JTextField f : nameFields) {
            if (f.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Names cannot be empty.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    public String[] getNames() {
        String[] out = new String[nameFields.length];
        for (int i = 0; i < nameFields.length; i++) out[i] = nameFields[i].getText().trim();
        return out;
    }

    public String[] getColors() {
        String[] out = new String[colorBoxes.length];
        for (int i = 0; i < colorBoxes.length; i++) out[i] = String.valueOf(colorBoxes[i].getSelectedItem());
        return out;
    }
}
