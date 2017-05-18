package com.xmpp.loadtest;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.Set;

/**
 * Created by gagandeep on 5/4/17.
 */
public class PrintRosterOperation extends Operation {

    @Override
    public void doRun() {
        System.out.println(String.format("*********************   ROSTER FOR USER (%s)   *********************", getConnection().getUsername()));
        final Set<RosterEntry> entries = getConnection().getRoster().getEntries();
        for (RosterEntry entry : entries) {
            System.out.println(entry.getUser());
        }
        System.out.println(String.format("********************************************************************"));
    }
}
