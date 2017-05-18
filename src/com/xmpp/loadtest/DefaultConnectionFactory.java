package com.xmpp.loadtest;

/**
 * Created by gagandeep on 5/4/17.
 */
public class DefaultConnectionFactory implements ConnectionFactory {
    @Override
    public RealXmppConnection create(String username) {
        return new RealXmppConnection(username);
    }
}
