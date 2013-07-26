/*
 * Copyright 2013 JBoss Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.jbpm.console.ng.udc.client.util;

import org.jbpm.console.ng.udc.client.event.EventsUsageData;
import org.jbpm.console.ng.udc.client.event.LevelsUsageEvent;
import org.jbpm.console.ng.udc.client.event.StatusUsageEvent;
import org.jbpm.console.ng.udc.model.UsageEventSummary;

public class UsageEventSummaryBuilder {
	
    private final UsageEventSummary usageEventSummary = new UsageEventSummary();

    public UsageEventSummaryBuilder key( final String key ) {
    	usageEventSummary.setKey(key);
        return this;
    }

    public UsageEventSummaryBuilder description( final String description ) {
    	usageEventSummary.setDescription(description);
        return this;
    }

    public UsageEventSummaryBuilder user( final String user ) {
    	usageEventSummary.setUser(user);
        return this;
    }

    public UsageEventSummaryBuilder eventsUsageData( final EventsUsageData event ) {
    	usageEventSummary.setComponent(event.getComponent());
    	usageEventSummary.setAction(event.getAction());
    	usageEventSummary.setModule(event.getModule());
        return this;
    }

    public UsageEventSummaryBuilder status( final StatusUsageEvent status ) {
    	usageEventSummary.setStatus(status.toString()); 
        return this;
    }

    public UsageEventSummaryBuilder level(LevelsUsageEvent level) {
    	usageEventSummary.setLevel(level.toString()); 
        return this;
    }

    public UsageEventSummary build() {
        return usageEventSummary;
    }

}
