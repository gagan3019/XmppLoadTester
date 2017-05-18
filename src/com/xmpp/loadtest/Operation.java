package com.xmpp.loadtest;

/**
 * Created by gagandeep on 5/4/17.
 */
public abstract class Operation implements Runnable {
    
    private RealXmppConnection connection;

    public void setConnection(RealXmppConnection connection) {
        this.connection = connection;
    }

    public RealXmppConnection getConnection() {
        return connection;
    }

    @Override
    public final void run() {
        if (connection == null) {
            throw new IllegalStateException("Connection cannot be null");
        }

        doRun();
    }

    public abstract void doRun();
}
