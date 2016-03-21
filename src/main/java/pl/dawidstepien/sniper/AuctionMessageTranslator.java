package pl.dawidstepien.sniper;

import static pl.dawidstepien.sniper.AuctionEventListener.*;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class AuctionMessageTranslator implements MessageListener {

  private final String sniperId;

  private AuctionEventListener listener;

  public AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
    this.sniperId = sniperId;
    this.listener = listener;
  }

  @Override
  public void processMessage(Chat chat, Message message) {
    AuctionEvent event = AuctionEvent.from(message.getBody());
    String eventType = event.type();

    if("CLOSE".equals(eventType)) {
      listener.auctionClosed();
    } else if("PRICE".equals(eventType)) {
      listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
    }
  }

  private static class AuctionEvent {

    private final Map<String, String> fields = new HashMap<>();

    public String type() {
      return get("Event");
    }

    public int currentPrice() {
      return getInt("CurrentPrice");
    }

    public int increment() {
      return getInt("Increment");
    }

    private String get(String fieldName) {
      return fields.get(fieldName);
    }

    private int getInt(String fieldName) {
      return Integer.parseInt(get(fieldName));
    }

    private void addField(String field) {
      String[] pair = field.split(":");
      fields.put(pair[0].trim(), pair[1].trim());
    }

    static AuctionEvent from(String messageBody) {
      AuctionEvent event = new AuctionEvent();
      for(String field : fieldsIn(messageBody)) {
        event.addField(field);
      }
      return event;
    }

    private static String[] fieldsIn(String messageBody) {
      return messageBody.split(";");
    }

    public PriceSource isFrom(String sniperId) {
      return sniperId.equals(bidder()) ? PriceSource.FROM_SNIPER : PriceSource.FROM_OTHER_BIDDER;
    }

    private String bidder() {
      return get("Bidder");
    }
  }
}
