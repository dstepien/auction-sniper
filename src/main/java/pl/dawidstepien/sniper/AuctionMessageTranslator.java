package pl.dawidstepien.sniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.HashMap;
import java.util.Map;

public class AuctionMessageTranslator implements MessageListener {

  private AuctionEventListener listener;

  public AuctionMessageTranslator(AuctionEventListener listener) {
    this.listener = listener;
  }

  @Override
  public void processMessage(Chat chat, Message message) {
    AuctionEvent event = AuctionEvent.from(message.getBody());
    String eventType = event.type();

    if("CLOSE".equals(eventType)) {
      listener.auctionClosed();
    } else if("PRICE".equals(eventType)) {
      listener.currentPrice(event.currentPrice(), event.increment());
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
  }

  private HashMap<String, String> unpackEventFrom(Message message) {
    HashMap<String, String> event = new HashMap<>();
    for(String element : message.getBody().split(";")) {
      String[] pair = element.split(":");
      event.put(pair[0].trim(), pair[1].trim());
    }
    return event;
  }
}
