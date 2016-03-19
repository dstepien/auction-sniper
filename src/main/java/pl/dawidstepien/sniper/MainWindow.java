package pl.dawidstepien.sniper;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainWindow extends JFrame {

  public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";

  private final JLabel sniperStatus = createLabel(Main.STATUS_JOINING);

  private JLabel createLabel(String label) {
    JLabel result = new JLabel(label);
    result.setName(Main.SNIPER_STATUS_NAME);
    result.setBorder(new LineBorder(Color.BLACK));
    return result;
  }

  public MainWindow() {
    super("Auction Sniper");
    setName(MAIN_WINDOW_NAME);
    add(sniperStatus);
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  public void showStatus(String status) {
    sniperStatus.setText(status);
  }
}
