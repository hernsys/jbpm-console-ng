/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.console.ng.udc.backend.server;

import static org.kie.commons.io.FileSystemType.Bootstrap.BOOTSTRAP_INSTANCE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jbpm.console.ng.udc.model.EventTypes;
import org.jbpm.console.ng.udc.model.UsageEventSummary;
import org.jbpm.console.ng.udc.service.UDCVfsService;
import org.kie.commons.io.IOService;
import org.kie.commons.java.nio.file.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This service the "delivery" of messages to users inboxes for events.
 * Ideally only one instance of this running at a time (at least on a node) to avoid doubling up.
 */
@ApplicationScoped
public class MailboxService {

    private static final Logger log = LoggerFactory.getLogger( MailboxService.class );

    private ExecutorService executor = null;
    public static final String MAIL_MAN = "mailman";

    @Inject
    private UDCVfsService udcVfsService;

    @Inject
    @Named("ioStrategy")
    private IOService ioService;
    
    

    private org.kie.commons.java.nio.file.Path bootstrapRoot = null;

    @PostConstruct
    public void setup() {
        final Iterator<FileSystem> fsIterator = ioService.getFileSystems( BOOTSTRAP_INSTANCE ).iterator();
        if ( fsIterator.hasNext() ) {
            final FileSystem bootstrap = fsIterator.next();
            final Iterator<org.kie.commons.java.nio.file.Path> rootIterator = bootstrap.getRootDirectories().iterator();
            if ( rootIterator.hasNext() ) {
                this.bootstrapRoot = rootIterator.next();
            }
        }

        executor = Executors.newSingleThreadExecutor();
        log.info( "mailbox service is up" );
        wakeUp();
    }

    @PreDestroy
    public void destroy() {
        stopExecutor();
    }

    public void stopExecutor() {
        log.info( "Shutting down mailbox service" );
        executor.shutdown();

        try {
            if ( !executor.awaitTermination( 10, TimeUnit.SECONDS ) ) {
                executor.shutdownNow();
                if ( !executor.awaitTermination( 10, TimeUnit.SECONDS ) ) {
                    System.err.println( "executor did not terminate" );
                }
            }
        } catch ( InterruptedException e ) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info( "Mailbox service is shutdown." );
    }

    public void wakeUp() {
        log.debug( "Waking up" );
        executor.execute( new Runnable() {
            public void run() {
                processOutgoing();
            }
        } );
    }

    /**
     * Call this to note that there has been a change - will then publish to any interested parties.
     * @param item
     */
    /**
     * Process any waiting messages
     */
    void processOutgoing() {
        executor.execute( new Runnable() {
            public void run() {
            	final Queue<UsageEventSummary> es = udcVfsService.readUsagesByFilter(EventTypes.INCOMING_ID, MAIL_MAN);
                log.debug( "Outgoing messages size " + es.size() );
                //wipe out inbox for mailman here...

                String[] userList = listUsers();
                System.out.println( "userServices:" + userList.length );
                for ( String toUser : userList ) {
                    System.out.println( "userServices:" + toUser );
                    log.debug( "Processing any inbound messages for " + toUser );
                    if ( toUser.equals( MAIL_MAN ) ) {
                        return;
                    }

                    Set<String> recentEdited = makeSetOf( udcVfsService.readUsagesByFilter(EventTypes.RECENT_EDITED_ID, toUser ) );
                    for ( UsageEventSummary e : es ) {
                        //the user who edited the item wont receive a message in inbox.
                        if ( !e.getFrom().equals( toUser ) && recentEdited.contains( e.getItemPath() ) ) {
                        	udcVfsService.addToIncoming( e.getItemPath(), e.getFileName(), e.getFrom(), toUser );
                        }
                    }
                }
            }
        } );

    }

    private Set<String> makeSetOf( Queue<UsageEventSummary> inboxEntries ) {
        Set<String> entries = new HashSet<String>();
        for ( UsageEventSummary e : inboxEntries ) {
            entries.add( e.getItemPath() );
        }
        return entries;
    }

    public String[] listUsers() {
        //TODO: a temporary hack to retrieve user list. Please refactor later.
        List<String> userList = new ArrayList<String>();
        org.kie.commons.java.nio.file.Path userRoot = bootstrapRoot.resolve( "/.metadata/.users/" );
        final Iterator<org.kie.commons.java.nio.file.Path> userIterator = userRoot.iterator();
        if ( userIterator.hasNext() ) {
            org.kie.commons.java.nio.file.Path userDir = userIterator.next();
            userList.add( userDir.getFileName().toString() );
        }

        String[] result = new String[ userList.size() ];
        return userList.toArray( result );
    }

}
