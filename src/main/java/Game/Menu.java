package Game;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {
private JPanel mainPanel;
private CardLayout cardLayout;
Image iconFrame = new ImageIcon(getClass().getResource("/images/Icon.jpg")).getImage();
public Menu() {
   setTitle("MonoPoly");
   setUndecorated(false);
   setSize(800,600);
   setLocationRelativeTo(null);
   setDefaultCloseOperation(EXIT_ON_CLOSE);
   setIconImage(iconFrame);

   cardLayout = new CardLayout();
   mainPanel = new JPanel(cardLayout);
   mainPanel.add(menuPanel(),"MenuPanel");


   mainPanel.setOpaque(false);
   BackgroundPanel bgPanel = new BackgroundPanel("/images/menu.png");

   bgPanel.setLayout(new BorderLayout());
   bgPanel.add(mainPanel, BorderLayout.CENTER);
   setContentPane(bgPanel);
   cardLayout.show(mainPanel, "MenuPanel");
   setVisible(true);


}
public JPanel menuPanel(){
JPanel menuPanel = new JPanel(new GridBagLayout());
menuPanel.setOpaque(false);

JPanel centerPanel = new JPanel();
centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
centerPanel.setOpaque(false);

MonopolyButton newGameButton = new MonopolyButton("New Game");
MonopolyButton exitButton = new MonopolyButton("Exit");

newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
newGameButton.addActionListener(e-> {

});
exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
exitButton.addActionListener(e -> {
    System.exit(0);
});

centerPanel.add(Box.createRigidArea(new Dimension(0,150)));
centerPanel.add(newGameButton);
centerPanel.add(Box.createRigidArea(new Dimension(0,30)));
centerPanel.add(exitButton);
menuPanel.add(centerPanel);
return menuPanel;
}
public JPanel newGamePanel(){
//
//    JPanel newGamePanel = new JPanel(new BorderLayout());
//    JLabel enterName = new JLabel("Please enter your name:");
//    newGamePanel.add(enterName,BorderLayout.NORTH);
//    newGamePanel.setOpaque(false);
//    newGamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//
//    GridBagConstraints c = new GridBagConstraints();
//    c.gridx=0;
//    c.gridy=0;
//    JLabel namePlayer1 = new JLabel("Player 1:");
//    c.gridx=1;
//    c.gridy=0;
//    JTextField fieldPlayer1 = new JTextField(8);
//    c.gridx=0;
//    c.gridy=1;
//    JLabel namePlayer2 = new JLabel("Player 2:");
//    c.gridx=1;
//    c.gridy=1;
//    JTextField fieldPlayer2 = new JTextField(8);
//    c.gridx=0;
//    c.gridy=2;
//    JLabel namePlayer3 = new JLabel("Player 3:");
//    c.gridx=1;
//    c.gridy=2;
//    JTextField fieldPlayer3 = new JTextField(8);
//    c.gridx=0;
//    c.gridy=3;
//    JLabel namePlayer4 = new JLabel("Player 4:");
//    c.gridx=1;
//    c.gridy=3;
//    JTextField fieldPlayer4 = new JTextField(8);


return null;
}


}
