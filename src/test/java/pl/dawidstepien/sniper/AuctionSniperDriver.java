package pl.dawidstepien.sniper;

import static org.hamcrest.CoreMatchers.equalTo;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class AuctionSniperDriver extends JFrameDriver {

  public AuctionSniperDriver(int timeout) {
    super(new GesturePerformer(),
      JFrameDriver.topLevelFrame(
        named(MainWindow.MAIN_WINDOW_NAME),
        showingOnScreen()),
      new AWTEventQueueProber(timeout, 100));
  }

  public void showsSniperStatus(String status) {
    new JLabelDriver(this, named(Main.SNIPER_STATUS_NAME)).hasText(equalTo(status));
  }
}
