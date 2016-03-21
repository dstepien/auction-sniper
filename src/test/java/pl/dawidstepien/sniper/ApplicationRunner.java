package pl.dawidstepien.sniper;

public class ApplicationRunner {

  public static final String SNIPER_ID = "sniper";

  public static final String SNIPER_PASSWORD = "sniper";

  public static final int TIMEOUT_IN_MILLISECONDS = 1000;

  public static final String SNIPER_XMPP_ID = SNIPER_ID + "@localhost/Auction";

  private AuctionSniperDriver driver;

  public void startBiddingIn(final FakeAuctionServer auction) {
    Thread thread = new Thread("Test Application") {
      @Override
      public void run() {
        try {
          Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    thread.setDaemon(true);
    thread.start();

    driver = new AuctionSniperDriver(TIMEOUT_IN_MILLISECONDS);
    driver.showsSniperStatus(Main.STATUS_JOINING);
  }

  public void hasShownSniperIsBidding() {
    driver.showsSniperStatus(Main.STATUS_BIDDING);
  }

  public void showsSniperHasLostAuction() {
    driver.showsSniperStatus(Main.STATUS_LOST);
  }

  public void stop() {
    if(driver != null) {
      driver.dispose();
    }
  }

  public void hasShownSniperIsWinning() {
    driver.showsSniperStatus(Main.STATUS_WINNING);
  }

  public void showsSniperHasWonAuction() {
    driver.showsSniperStatus(Main.STATUS_WON);
  }
}
