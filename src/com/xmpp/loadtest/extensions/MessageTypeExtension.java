package com.xmpp.loadtest.extensions;


import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.EmbeddedExtensionProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by gagandeep on 1/9/17.
 */

public class MessageTypeExtension implements ExtensionElement {

    public static final String ELEMENT_NAME = "msg_type";
    public static final String ELEMENT_NAMESPACE = "http://www.jeevansathi.com/message_type";
    private final MessageType messageType;
    private final String username;

    public MessageTypeExtension(MessageType messageType) {
        this.messageType = messageType;
        this.username = "";
    }

    public MessageTypeExtension(MessageType messageType, String username) {
        this.messageType = messageType;
        this.username = username;
    }

    @Override
    public String getNamespace() {
        return ELEMENT_NAMESPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public CharSequence toXML() {
        return "<" + ELEMENT_NAME + " xmlns=\"" + ELEMENT_NAMESPACE + "\" type=\"" + messageType.getType() + "\" username=\"" + username + "\">" + messageType.getType() + "</" + ELEMENT_NAME + ">";
    }

    public enum MessageType {
        CHECK("check"), ACCEPT("accept"), EOI("eoi");
        private String type;

        MessageType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static MessageType value(String value) {
            for (MessageType messageType : MessageType.values()) {
                if (messageType.getType().equals(value)) {
                    return messageType;
                }
            }
            return ACCEPT;
        }
    }

    public static class Provider extends EmbeddedExtensionProvider<MessageTypeExtension> {
        private static final String TAG = "MessageTypeExtension$Pr";

        @Override
        protected MessageTypeExtension createReturnExtension(String currentElement, String currentNamespace, Map<String, String> attributeMap, List<? extends ExtensionElement> content) {
            return new MessageTypeExtension(MessageType.value(attributeMap.get("type")),attributeMap.get("username"));
        }

    }
}
