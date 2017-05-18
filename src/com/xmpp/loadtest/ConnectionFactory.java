package com.xmpp.loadtest;

/**
 * Created by gagandeep on 5/4/17.
 */
public interface ConnectionFactory {
    RealXmppConnection create(String username);
}
