package com.xmpp.loadtest;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gagandeep on 5/4/17.
 */
public class ConnectTask implements Runnable {

    private RealXmppConnection connection;
    private Map<String, RealXmppConnection> connectionMap;

    public ConnectTask(RealXmppConnection connection, Map<String, RealXmppConnection> connectionMap) {
        this.connection = connection;
        this.connectionMap = connectionMap;
    }

    @Override
    public void run() {
        if (connect()) {
            if (authenticate()) {
                connectionMap.put(connection.getUsername(), connection);
            }
        }
    }

    private boolean connect() {
        if (!connection.isConnected()) {
            try {
                connection.connect();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    private boolean authenticate() {
        if (!connection.isAuthenticated()) {
            try {
                connection.authenticate();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
