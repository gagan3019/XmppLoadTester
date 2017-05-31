package com.xmpp.loadtest;

import com.xmpp.loadtest.extensions.MessageTypeExtension;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gagandeep on 5/18/17.
 */
public class ChatMessageSenderOperation extends Operation implements ChatMessageListener {

        private static final String[] SAMPLE_MESSAGES = {"purus eu magna vulputate luctus cum sociis",
                "mauris laoreet ut rhoncus aliquet pulvinar",
                "quam nec dui luctus rutrum nulla tellus in sagittis",
                "congue vivamus metus arcu adipiscing",
                "pede morbi porttitor lorem id ligula",
                "proin risus praesent lectus vestibulum quam sapien varius ut",
                "tortor sollicitudin mi sit amet lobortis sapien sapien non",
                "ligula sit amet eleifend pede libero quis orci nullam",
                "vivamus vel nulla eget eros elementum",
                "ante vel ipsum praesent blandit lacinia erat vestibulum sed",
                "nisl venenatis lacinia aenean sit",
                "ut nulla sed accumsan felis ut at",
                "a suscipit nulla elit ac",
                "massa donec dapibus duis at",
                "venenatis non sodales sed tincidunt eu felis fusce",
                "rhoncus aliquam lacus morbi quis tortor id nulla ultrices",
                "metus arcu adipiscing molestie hendrerit at vulputate",
                "sapien iaculis congue vivamus metus",
                "turpis nec euismod scelerisque quam turpis adipiscing lorem",
                "potenti cras in purus eu magna vulputate",
                "sapien cum sociis natoque penatibus",
                "luctus ultricies eu nibh quisque id justo sit",
                "nisi vulputate nonummy maecenas tincidunt",
                "lorem integer tincidunt ante vel ipsum praesent blandit",
                "faucibus orci luctus et ultrices posuere",
                "risus auctor sed tristique in tempus sit amet sem fusce",
                "ultrices mattis odio donec vitae nisi",
                "morbi non quam nec dui luctus rutrum nulla tellus in",
                "sit amet eros suspendisse accumsan",
                "pellentesque ultrices mattis odio donec vitae nisi nam ultrices",
                "fusce posuere felis sed lacus morbi sem mauris",
                "consequat morbi a ipsum integer a nibh in",
                "vitae quam suspendisse potenti nullam porttitor lacus at",
                "nec nisi vulputate nonummy maecenas tincidunt lacus",
                "ante ipsum primis in faucibus orci luctus",
                "bibendum morbi non quam nec dui",
                "arcu libero rutrum ac lobortis vel dapibus at",
                "lobortis est phasellus sit amet erat",
                "tempor turpis nec euismod scelerisque quam turpis",
                "aliquam quis turpis eget elit sodales scelerisque",
                "iaculis justo in hac habitasse",
                "nulla ac enim in tempor turpis nec euismod",
                "nunc rhoncus dui vel sem sed sagittis nam congue",
                "at velit vivamus vel nulla eget eros elementum",
                "etiam vel augue vestibulum rutrum rutrum neque aenean auctor",
                "massa id nisl venenatis lacinia aenean",
                "ut dolor morbi vel lectus"};

    private CountDownLatch checkTypeMessageWaitLatch = new CountDownLatch(1);
    private boolean messageReceived;

    @Override
    public void doRun() {

        RosterEntry randomContact = getRandomContactFor("acceptance");
        String randomUserName = XmppUtils.extractUserName(randomContact.getUser());
        Chat chat = getConnection().createOrGetChat(randomUserName, randomContact.getUser());
        try {
            String sampleMessage = SAMPLE_MESSAGES[new Random().nextInt(SAMPLE_MESSAGES.length)];
            System.out.println(String.format("Sending message = [ %s ] to %s ", sampleMessage, randomUserName));
            sendAcceptTypeMessage(chat, sampleMessage);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private String sendAcceptTypeMessage(Chat chat, String message) throws SmackException.NotConnectedException {
        Message acceptTypeMessage = XmppUtils.newXmppMessage();
        acceptTypeMessage.addExtension(new MessageTypeExtension(MessageTypeExtension.MessageType.ACCEPT, getConnection().getUsername()));
        acceptTypeMessage.setBody(message);
        chat.sendMessage(acceptTypeMessage);
        return acceptTypeMessage.getStanzaId();
    }


    @Override
    public void processMessage(Chat chat, Message message) {
        System.out.println("Message Received : " + message.getBody());
    }
}
