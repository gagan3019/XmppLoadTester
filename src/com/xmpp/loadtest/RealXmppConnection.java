package com.xmpp.loadtest;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.id.StanzaIdUtil;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gagandeep on 5/4/17.
 */
public class RealXmppConnection implements ConnectionListener {

    private String username;
    private AbstractXMPPConnection connection;
    private CountDownLatch connectCountDownLatch = new CountDownLatch(1);
    private CountDownLatch loginCountDownLatch = new CountDownLatch(1);

    private Roster roster;
    private ChatManager chatManager;
    private Map<String, Chat> activeChats = new ConcurrentHashMap<>();
    
    public RealXmppConnection(String username) {
        this.username = username;

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration
                .builder()
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setServiceName("chat.jeevansathi.com")
                .setHost("")
                .setPort(5222)
                .setDebuggerEnabled(false)
                .setResource("xmppLoadTest-" + StanzaIdUtil.newStanzaId())
                .setCompressionEnabled(false).build();

        connection = new XMPPTCPConnection(config);
        connection.setPacketReplyTimeout(60 * 1000);

        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
        ReconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();
        reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
        reconnectionManager.setFixedDelay(5);

        connection.addConnectionListener(this);
    }

    public void connect() throws IOException, XMPPException, SmackException, InterruptedException {
        connection.connect();
        connectCountDownLatch.await();
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    public void authenticate() throws IOException, XMPPException, SmackException, InterruptedException {
        connection.login(username, md5(username));
        loginCountDownLatch.await();
    }

    public boolean isAuthenticated() {
        return connection.isAuthenticated();
    }
    
    public void disconnect() {
        connection.disconnect();
    }

    public static String md5(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return value;
    }

    public String getUsername() {
        return username;
    }

    public Roster getRoster() {
        return roster;
    }

    Chat createOrGetChat(String profileId,String jid) {
        if (chatManager == null) {
            return null;
        }

        Chat chat = activeChats.get(profileId);
        if (chat == null) {
            chat = chatManager.createChat(jid);
            activeChats.put(profileId, chat);
        }

        return chat;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    @Override
    public void connected(XMPPConnection xmppConnection) {
        connectCountDownLatch.countDown();
    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {
        loginCountDownLatch.countDown();
        if (roster == null) {
            roster = Roster.getInstanceFor(connection);
        }

        if (chatManager == null) {
            chatManager = ChatManager.getInstanceFor(xmppConnection);
        }
    }

    @Override
    public void connectionClosed() {
        
    }

    @Override
    public void connectionClosedOnError(Exception e) {

    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int i) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }
}
