package org.jbpm.console.ng.udc.service;

import org.jboss.errai.bus.server.annotations.Remote;
import org.jbpm.console.ng.udc.model.UsageEventSummary;

@Remote
public interface UDCSessionService extends UDCStorageService {

    UsageEventSummary getUsageDataByKey(String key);
    
}
