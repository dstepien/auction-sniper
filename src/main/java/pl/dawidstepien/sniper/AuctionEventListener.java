package pl.dawidstepien.sniper;

import java.util.EventListener;

public interface AuctionEventListener extends EventListener {

  enum PriceSource {
    FROM_SNIPER, FROM_OTHER_BIDDER
  }

  void auctionClosed();

  void currentPrice(int price, int increment, PriceSource priceSource);
}
