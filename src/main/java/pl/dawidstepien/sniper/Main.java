package pl.dawidstepien.sniper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class Main {

  public static final String STATUS_JOINING = "Joining";

  public static final String STATUS_LOST = "Lost";

  public static final String STATUS_BIDDING = "Bidding";

  public static final String SNIPER_STATUS_NAME = "SNIPER_STATUS_NAME";

  public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Event: JOIN;";

  public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Event: BID; Amount: %d;";

  public static final String STATUS_WINNING = "Winning";

  public static final String STATUS_WON = "Won";

  private MainWindow ui;

  private static final int ARG_HOSTNAME = 0;

  private static final int ARG_USERNAME = 1;

  private static final int ARG_PASSWORD = 2;

  private static final int ARG_ITEM_ID = 3;

  public static final String AUCTION_RESOURCE = "Auction";

  public static final String ITEM_ID_AS_LOGIN = "auction-%s";

  public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

  @SuppressWarnings("unused")
  private Chat notToBeGCd;

  public Main() throws Exception {
    startUserInterface();
  }

  private void startUserInterface() throws Exception {
    SwingUtilities.invokeAndWait(new Runnable() {
      public void run() {
        ui = new MainWindow();
      }
    });
  }

  public static void main(String... args) throws Exception {
    Main main = new Main();
    main.joinAuction(connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]), args[ARG_ITEM_ID]);
  }

  private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
    disconnectWhenUICloses(connection);

    Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);
    this.notToBeGCd = chat;
    Auction auction = new XMPPAuction(chat);

    chat.addMessageListener(
      new AuctionMessageTranslator(
        connection.getUser(),
        new AuctionSniper(auction, new SniperStateDisplayer())
      )
    );
    auction.join();
  }

  public static class XMPPAuction implements Auction {

    private Chat chat;

    public XMPPAuction(Chat chat) {
      this.chat = chat;
    }

    @Override
    public void bid(int amount) {
      sendMessage(String.format(BID_COMMAND_FORMAT, amount));
    }

    @Override
    public void join() {
      sendMessage(JOIN_COMMAND_FORMAT);
    }

    private void sendMessage(final String message) {
      try {
        chat.sendMessage(message);
      } catch (XMPPException e) {
        e.printStackTrace();
      }
    }
  }

  public class SniperStateDisplayer implements SniperListener {

    @Override
    public void sniperLost() {
      showStatus(Main.STATUS_LOST);
    }

    @Override
    public void sniperBidding() {
      showStatus(Main.STATUS_BIDDING);
    }

    @Override
    public void sniperWinning() {
      showStatus(Main.STATUS_WINNING);
    }

    @Override
    public void sniperWon() {
      showStatus(Main.STATUS_WON);
    }

    private void showStatus(final String status) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          ui.showStatus(status);
        }
      });
    }
  }

  private void disconnectWhenUICloses(XMPPConnection connection) {
    ui.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        connection.disconnect();
      }
    });
  }

  private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
    XMPPConnection connection = new XMPPConnection(hostname);
    connection.connect();
    connection.login(username, password, AUCTION_RESOURCE);
    return connection;
  }

  private static String auctionId(String itemId, XMPPConnection connection) {
    return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
  }
}
