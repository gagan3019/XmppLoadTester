package com.xmpp.loadtest;

import com.xmpp.loadtest.extensions.MessageTypeExtension;
import org.jivesoftware.smack.provider.ProviderManager;

import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        List<String> profiles   = Data.USERS;
        profiles = profiles.subList(0, 1000);
        Scheduler scheduler = new Scheduler(new DefaultConnectionFactory());
        scheduler.connect(profiles);

        ProviderManager.addExtensionProvider(MessageTypeExtension.ELEMENT_NAME, MessageTypeExtension.ELEMENT_NAMESPACE, new MessageTypeExtension.Provider());


        Thread.sleep(5000);
        Random random = new Random();
        int numOfProfiles = profiles.size();
        while (true) {
            scheduler.submitOperation(profiles.get(random.nextInt(profiles.size())), new ChatMessageSenderOperation());
            Thread.sleep(200);
        }

//        scheduler.disconnect(profiles.toArray(new String[]{}));
    }
}
