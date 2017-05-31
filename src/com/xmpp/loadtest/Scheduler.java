package com.xmpp.loadtest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gagandeep on 5/4/17.
 */
public final class Scheduler {

    private final Map<String, RealXmppConnection> CONNECTIONS = new ConcurrentHashMap<>();
    private ExecutorService connectionService = Executors.newFixedThreadPool(50);
    private ExecutorService operationsService = Executors.newFixedThreadPool(50);

    private ConnectionFactory connectionFactory;

    public Scheduler(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void connect(List<String> usernames) {
        for (String username : usernames) {
            if (!CONNECTIONS.containsKey(username)) {
                connectionService.execute(new ConnectTask(connectionFactory.create(username), CONNECTIONS));
            }
        }
    }

    public void submitOperation(String username, Operation operation) {
        if (CONNECTIONS.containsKey(username)) {
            operation.setConnection(CONNECTIONS.get(username));
            operationsService.submit(operation);
        }
    }

    public void disconnect(String... usernames) {
        if (usernames != null) {
            for (String user : usernames) {
                disconnect(user);
            }
        }
    }

    private void disconnect(String username) {
        if (CONNECTIONS.containsKey(username)) {
            RealXmppConnection connection = CONNECTIONS.get(username);
            connection.disconnect();
        }
    }
}
