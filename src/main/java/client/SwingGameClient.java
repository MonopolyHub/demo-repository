package client;

import javax.swing.*;

public class SwingGameClient extends JFrame {

    private final BoardPanel boardPanel = new BoardPanel();

    public SwingGameClient() {
        super("MonopolyHub - Client");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        add(boardPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingGameClient::new);
    }
}
