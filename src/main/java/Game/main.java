package Game;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

public class main {
   public static void main(String[] args) {
       SplashScreen spalsh = new SplashScreen("/images/Start.png");
       spalsh.showSplash(2500);
      try{
          UIManager.setLookAndFeel(new FlatDarculaLaf());
      } catch (Exception e) {
         e.printStackTrace();
      }
      SwingUtilities.invokeLater(()-> new Menu());
   }
}
