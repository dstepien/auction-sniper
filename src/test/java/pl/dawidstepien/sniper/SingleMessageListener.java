package pl.dawidstepien.sniper;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class SingleMessageListener implements MessageListener {

  private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1);

  public void processMessage(Chat chat, Message message) {
    messages.add(message);
  }

  public void receivesAMessage() throws InterruptedException {
    assertThat("Message", messages.poll(5, TimeUnit.SECONDS), is(notNullValue()));
  }
}