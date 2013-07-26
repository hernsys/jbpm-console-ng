package org.jbpm.console.ng.udc.service;

import org.jboss.errai.bus.server.annotations.Remote;
import org.jbpm.console.ng.udc.model.EventTypes;
import org.jbpm.console.ng.udc.model.InboxPageRequest;
import org.jbpm.console.ng.udc.model.InboxPageRow;
import org.uberfire.paging.PageResponse;

@Remote
public interface UDCVfsService extends UDCStorageService {
    
	void addToIncoming(String itemPath, String note, String userFrom, String userName);
	
	PageResponse<InboxPageRow> loadInbox( InboxPageRequest request );
	
	void clearEventsByFilter(EventTypes eventType, String userName);

}
