/*
 * Copyright 2013 JBoss by Red Hat.
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

import java.util.LinkedList;
import java.util.Queue;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.Service;
import org.jbpm.console.ng.udc.model.EventTypes;
import org.jbpm.console.ng.udc.model.UsageEventSummary;
import org.jbpm.console.ng.udc.service.UDCSessionService;

/**
 * This service stored in a Queue session
 */
@Service
@ApplicationScoped
public class UDCSessionServiceImpl extends UDCSessionManager implements UDCSessionService {

    private static final String KEY_EVENTS = "usageDataEvent";
    
    @Override
    @SuppressWarnings("unchecked")
    public Queue<UsageEventSummary> readUsageDataCollector() {
         return (super.getSession().getAttribute(KEY_EVENTS) != null) ?
          (Queue<UsageEventSummary>) super.getSession().getAttribute(KEY_EVENTS) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void saveUsageDataEvent(UsageEventSummary usageData) {
        Queue<UsageEventSummary> points = (super.getSession().getAttribute(KEY_EVENTS) == null) ? new LinkedList<UsageEventSummary>()
                : (Queue<UsageEventSummary>) super.getSession().getAttribute(KEY_EVENTS);
        points.add(usageData);
        super.getSession().setAttribute(KEY_EVENTS, points);
    }

    @Override
    public void clearEventsByFilter(EventTypes eventType, String userName){
    	//remove Only type UDC
        super.getSession().removeAttribute(KEY_EVENTS);
    }

    @Override
    @SuppressWarnings("unchecked")
    public UsageEventSummary getUsageDataByKey(String key) {
        UsageEventSummary usageData = null;
        Queue<UsageEventSummary> points = (Queue<UsageEventSummary>) super.getSession().getAttribute(KEY_EVENTS);
        for(UsageEventSummary ud : points){
            if(ud.getKey().equals(key)){
                usageData = ud;
                break;
            }
        }
        return usageData;
    }

	@Override
	public Queue<UsageEventSummary> readUsagesByFilter(EventTypes eventType, String userName) {
		// TODO Auto-generated method stub
		return null;
	}
}
