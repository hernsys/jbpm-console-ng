package org.jbpm.console.ng.udc.backend.server;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.jbpm.console.ng.udc.model.EventTypes;
import org.jbpm.console.ng.udc.model.InboxPageRequest;
import org.jbpm.console.ng.udc.model.InboxPageRow;
import org.jbpm.console.ng.udc.model.UsageEventSummary;
import org.jbpm.console.ng.udc.service.UDCVfsService;
import org.kie.commons.java.nio.file.Path;
import org.kie.commons.validation.PortablePreconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.paging.PageResponse;
import org.uberfire.workbench.events.ResourceOpenedEvent;
import org.uberfire.workbench.events.ResourceUpdatedEvent;

import com.google.common.collect.Lists;

@Service
@ApplicationScoped
public class UDCVfsServiceImpl extends UDCVfsManager implements UDCVfsService {

	private static final Logger log = LoggerFactory.getLogger(UDCVfsServiceImpl.class);

	@Inject
	protected MailboxService mailboxService;

	@Override
	public Queue<UsageEventSummary> readUsageDataCollector() {
		return getEntriesByPath(super.getPathBySession());
	}

	@Override
	public void saveUsageDataEvent(UsageEventSummary usageEvent) {
		Path path = getPathBySession();
		usageEvent.setItemPath(String.valueOf(path.toString()));
		usageEvent.setFileName(String.valueOf(path.getFileName()));
		usageEvent.setFileSystem(String.valueOf(path.getFileSystem()));
		Queue<UsageEventSummary> usages = this.readUsageDataCollector();
		usages.add(usageEvent);
		writeEntries(usages, path);
	}

	public void clearEventsByFilter(EventTypes eventType, String userName){
		super.removeEventsByType(eventType, userName);
	}

	@Override
	public Queue<UsageEventSummary> readUsagesByFilter(EventTypes eventType, String userName) {
		Queue<UsageEventSummary> usages = null;
		switch (eventType) {
		case USAGE_DATA:
			usages = readUsageDataCollector();
			break;
		case ALL:
			usages = readAllEventsByTypes(userName);
			break;	
		default:
			//default (RECENT_EDITED_ID, RECENT_VIEWED_ID, INCOMING_ID)
			usages = readEntries(userName, eventType.getFileName());
			break;
		}
		return usages;
	}

	@Override
	public PageResponse<InboxPageRow> loadInbox(InboxPageRequest request) {
		PortablePreconditions.checkNotNull("request", request);
		if (request.getPageSize() != null && request.getPageSize() < 0) {
			throw new IllegalArgumentException("pageSize cannot be less than zero.");
		}

		Queue<UsageEventSummary> entries = readEntries(identity.getName(), request.getInboxName());
		Iterator<UsageEventSummary> iterator = entries.iterator();
		List<InboxPageRow> rowList = new InboxPageRowBuilder()
				.withPageRequest(request).withIdentity(identity)
				.withContent(iterator).build();

		return new PageResponseBuilder<InboxPageRow>()
				.withStartRowIndex(request.getStartRowIndex())
				.withTotalRowSize(entries.size()).withTotalRowSizeExact()
				.withPageRowList(rowList).withLastPage(!iterator.hasNext())
				.build();
	}

	@Override
	public void addToIncoming(String itemPath, String note, String userFrom,
			String userName) {
		addToInbox(INCOMING_ID, itemPath, note, userFrom, userName);
	}

	public void recordOpeningEvent(@Observes final ResourceOpenedEvent event) {
		PortablePreconditions.checkNotNull("event", event);
		final org.uberfire.backend.vfs.Path resourcePath = event.getPath();
		recordOpeningEvent(resourcePath.toURI(), resourcePath.getFileName()
				.toString());
	}

	public void recordUserEditEvent(@Observes final ResourceUpdatedEvent event) {
		PortablePreconditions.checkNotNull("event", event);
		final org.uberfire.backend.vfs.Path resourcePath = event.getPath();
		recordUserEditEvent(resourcePath.toURI(), resourcePath.getFileName()
				.toString());
	}

	/**
	 * Helper method to note the event. addToRecentEdited and deliver messages
	 * to users inboxes (ie., the edited item is the itme that the current
	 * logged in user has edited in the past, or commented on)
	 */
	// 1 - add to inbox RECENT_EDITED_ID and write in RECENT_EDITED_ID
	// 2 - add to inbox INCOMING_ID and write in INCOMING_ID
	// 3 - call mailboxService
	private synchronized void recordUserEditEvent(String itemPath,
			String itemName) {
		addToRecentEdited(itemPath, itemName);
		addToIncoming(itemPath, itemName, identity.getName(),
				MailboxService.MAIL_MAN);
		mailboxService.processOutgoing();
		mailboxService.wakeUp();
	}

	// 1 - add to inbox RECENT_VIEWED_ID and write in RECENT_VIEWED_ID
	// 2 - write in INCOMING_ID
	private synchronized void recordOpeningEvent(String itemPath,
			String itemName) {
		addToRecentOpened(itemPath, itemName);
		Queue<UsageEventSummary> unreadIncoming = removeAnyExisting(itemPath,
				readEntries(identity.getName(), INCOMING_ID));
		writeEntries(unreadIncoming, getPath(INBOX, INCOMING_ID));
	}
	
	/**
	 * This should be called when the user edits or comments on an asset. Simply
	 * adds to the list...
	 */
	public void addToRecentEdited(String itemPath, String note) {
		addToInbox(RECENT_EDITED_ID, itemPath, note, identity.getName(), identity.getName());
	}

	public void addToRecentOpened(String itemPath, String note) {
		addToInbox(RECENT_VIEWED_ID, itemPath, note, identity.getName(), identity.getName());
	}

	private void addToInbox(String boxName, String itemPath, String note,
			String userFrom, String userName) {
		assert boxName.equals(RECENT_EDITED_ID) || boxName.equals(RECENT_VIEWED_ID) || boxName.equals(INCOMING_ID);
		Queue<UsageEventSummary> entries = removeAnyExisting(itemPath, readEntries(userName, boxName));
		if (entries.size() >= MAX_RECENT_EDITED) {
			entries.remove(0);
		}
		entries.add(new UsageEventSummary(itemPath, note, userFrom));
		writeEntries(entries, getPath(INBOX, boxName));
	}

	private Queue<UsageEventSummary> removeAnyExisting(String itemPath,
			Queue<UsageEventSummary> inboxEntries) {
		Iterator<UsageEventSummary> it = inboxEntries.iterator();
		while (it.hasNext()) {
			UsageEventSummary e = it.next();
			if (e.getItemPath().equals(itemPath)) {
				it.remove();
				return inboxEntries;
			}
		}
		return inboxEntries;
	}

	private Queue<UsageEventSummary> readEntries(String userName, String boxName) {
		return getEntriesByPath(getPath(userName, INBOX, boxName));
	}

	private void writeEntries(Queue<UsageEventSummary> usagesData, Path path) {
		String entry = getXStream().toXML(usagesData);
		ioService.write(path, entry);
	}

	@SuppressWarnings("unchecked")
	private Queue<UsageEventSummary> getEntriesByPath(Path path) {
		Queue<UsageEventSummary> entries = Lists.newLinkedList();
		if (ioService.exists(path)) {
			final String xml = ioService.readAllString(path);
			if (xml != null && !xml.equals("")) {
				entries = (Queue<UsageEventSummary>) getXStream().fromXML(xml);
			}
		}
		return entries;
	}
	
	private Queue<UsageEventSummary> readAllEventsByTypes(String userName){
		Queue<UsageEventSummary> events = Lists.newLinkedList();
		for(EventTypes type : EventTypes.values()){
			if(type!=EventTypes.ALL){
				events.addAll(readUsagesByFilter(type, userName));
			}
		}
		return events;
	}

}