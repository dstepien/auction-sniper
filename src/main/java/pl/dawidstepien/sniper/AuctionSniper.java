package pl.dawidstepien.sniper;

public class AuctionSniper implements AuctionEventListener {

  private Auction auction;

  private SniperListener sniperListener;

  private boolean isWinning = false;

  public AuctionSniper(Auction auction, SniperListener sniperListener) {
    this.auction = auction;
    this.sniperListener = sniperListener;
  }

  @Override
  public void auctionClosed() {
    if(isWinning) {
      sniperListener.sniperWon();
    } else {
      sniperListener.sniperLost();
    }
  }

  @Override
  public void currentPrice(int price, int increment, PriceSource priceSource) {
    isWinning = priceSource.equals(PriceSource.FROM_SNIPER);

    if(isWinning) {
      sniperListener.sniperWinning();
    } else {
      auction.bid(price + increment);
      sniperListener.sniperBidding();
    }
  }
}
