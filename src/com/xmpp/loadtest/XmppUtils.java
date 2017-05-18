package com.xmpp.loadtest;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.id.StanzaIdUtil;

/**
 * Created by gagandeep on 12/13/16.
 */

public final class XmppUtils {

    private XmppUtils() {
    }

    public static String newStanzaId() {
        return StanzaIdUtil.newStanzaId() + "-" + System.currentTimeMillis();
    }

    public static Message newXmppMessage() {
        Message chatStanza = new Message();
        chatStanza.setStanzaId(newStanzaId());
        return chatStanza;
    }

    public static Message newXmppMessage(String messageId) {
        if (messageId == null || messageId.isEmpty()) {
            messageId = newStanzaId();
        }
        Message chatStanza = new Message();
        chatStanza.setStanzaId(messageId);
        return chatStanza;
    }

    public static String extractUserName(String jid) {
        if (jid == null || jid.isEmpty()) {
            return "";
        }

        if (jid.contains("@")) {
            return jid.split("@")[0];
        }
        return "";
    }

    public static String extractBareJid(String jid) {
        if (jid == null || jid.isEmpty()) {
            return "";
        }

        if (jid.contains("/")) {
            return jid.split("/")[0];
        }
        return jid;
    }

    public static String extractResource(String jid) {
        if (jid == null || jid.isEmpty()) {
            return "";
        }

        //TODO - arrayout of bound exception check
        if (jid.contains("/") && (jid.indexOf("/")!=(jid.length()-1))) {
            return jid.split("/")[1];
        }
        return "";
    }

    public static String extractDomain(String jid) {
        if (jid == null || jid.isEmpty()) {
            return "";
        }

        if (jid.contains("@")) {
            jid = jid.split("@")[1];
        }

        if (jid.contains("/")) {
            return jid.split("/")[1];
        }
        return "";
    }
}
