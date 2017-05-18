package com.xmpp.loadtest;

import com.xmpp.loadtest.extensions.MessageTypeExtension;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by gagandeep on 5/18/17.
 */
public class CheckStanzaSenderOperation extends Operation implements ChatMessageListener {

    private CountDownLatch checkTypeMessageWaitLatch = new CountDownLatch(1);
    private boolean messageReceived;

    @Override
    public void doRun() {
        Message checkTypeMessage = XmppUtils.newXmppMessage();
        checkTypeMessage.addExtension(new MessageTypeExtension(MessageTypeExtension.MessageType.CHECK));
        checkTypeMessage.setBody(" ");

        RosterEntry randomContact = getRandomContactFor("acceptance");
        Chat chat = getConnection().createOrGetChat(XmppUtils.extractUserName(randomContact.getUser()), randomContact.getUser());
        try {
            chat.addMessageListener(this);
            chat.sendMessage(checkTypeMessage);
            checkTypeMessageWaitLatch.await(5, TimeUnit.SECONDS);
            if (!messageReceived) {
                System.err.println("Check message did not get reply");
            }
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void processMessage(Chat chat, Message message) {
        List<ExtensionElement> extensions = message.getExtensions();
        for (ExtensionElement extension : extensions) {
            if (extension instanceof MessageTypeExtension) {
                MessageTypeExtension messageTypeExtension = (MessageTypeExtension) extension;
                MessageTypeExtension.MessageType messageType = messageTypeExtension.getMessageType();
                if (messageType == MessageTypeExtension.MessageType.CHECK) {
                    System.out.println(String.format("Yeah , check message reply came [ %s ]", message.getBody()));
                    messageReceived = true;
                    checkTypeMessageWaitLatch.countDown();
                }
            }
        }
    }
}
