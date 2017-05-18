package com.xmpp.loadtest;

import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;

import java.util.List;
import java.util.Random;

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

    protected RosterEntry getRandomContactFor(String group) {
        RosterGroup rosterGroup = getConnection().getRoster().getGroup(group);
        List<RosterEntry> entries = rosterGroup.getEntries();
        if (entries != null && entries.size() > 0) {
//            int randomIndex = new Random().nextInt(entries.size());
//            randomIndex = randomIndex < 0 && randomIndex >= entries.size() ? 0 : randomIndex;
            return entries.get(new Random().nextInt(entries.size()));
        }
        return null;
    }
}
