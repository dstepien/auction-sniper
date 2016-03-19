package pl.dawidstepien.sniper;

public interface AuctionEventListener {

  void auctionClosed();
  void currentPrice(int price, int increment);
}
