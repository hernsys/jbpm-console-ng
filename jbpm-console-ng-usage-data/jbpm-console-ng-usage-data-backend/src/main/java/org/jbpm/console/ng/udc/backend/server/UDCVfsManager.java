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

import java.util.Date;
import java.util.Queue;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jbpm.console.ng.udc.model.EventTypes;
import org.jbpm.console.ng.udc.model.UsageEventSummary;
import org.kie.commons.io.IOService;
import org.kie.commons.java.nio.file.Path;
import org.uberfire.backend.server.UserServicesImpl;
import org.uberfire.security.Identity;

import com.thoughtworks.xstream.XStream;

public abstract class UDCVfsManager extends UDCSessionManager {

	@Inject
	private UserServicesImpl userServices;

	@Inject
	@Named("ioStrategy")
	protected IOService ioService;

	@Inject
	@SessionScoped
	protected Identity identity;

	private static final String patternName = "yyyyMMdd_HHmmss-SSS";

	private static final String UDC = "udc";

	protected static final String RECENT_EDITED_ID = EventTypes.RECENT_EDITED_ID
			.getFileName();

	protected static final String RECENT_VIEWED_ID = EventTypes.RECENT_VIEWED_ID
			.getFileName();

	public static final String INCOMING_ID = EventTypes.INCOMING_ID
			.getFileName();

	protected static final String INBOX = "inbox";

	protected static final int MAX_RECENT_EDITED = 200;

	protected Path getPathBySession() {
		return userServices.buildPath(UDC, getUniqueNameByFile());
	}

	protected Path getPath(String inbox, String boxName) {
		return userServices.buildPath(inbox, boxName);
	}

	protected Path getPath(String user, String inbox, String boxName) {
		return userServices.buildPath(user, inbox, boxName);
	}

	protected XStream getXStream() {
		XStream xs = new XStream();
		xs.alias("inbox-entries", Queue.class);
		xs.alias("entry", UsageEventSummary.class);
		return xs;
	}

	protected String getUniqueNameByFile() {
		String userName = identity.getName();
		if (this.getSession().getAttribute(userName) == null) {
			this.getSession().setAttribute(userName,
					this.getUniqueKeyByUser(userName));
		}
		return String.valueOf(this.getSession().getAttribute(userName));
	}

	private String getUniqueKeyByUser(String userName) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				patternName);
		return new StringBuilder(userName).append("-")
				.append(sdf.format(new Date())).toString();
	}

	protected void removeEventsByType(EventTypes eventType, String userName) {
		Path path;
		switch (eventType) {
		case ALL:
			for(EventTypes type : EventTypes.values()){
				path = getPathByType(type, userName);
				if(type != EventTypes.ALL && ioService.exists(path)){
					ioService.delete(path);
				}
			}
		default:
			path = getPathByType(eventType, userName);
			if(ioService.exists(path)){
				ioService.delete(getPathByType(eventType, userName));
			}
			break;
		}
	}
	
	private Path getPathByType(EventTypes eventType, String userName) {
		Path path;
		switch (eventType) {
		case USAGE_DATA:
			path = getPathBySession();
			break;
		default:
			path = getPath(userName, INBOX, eventType.getFileName());
			break;
		}
		return path;
	}
	
	

}