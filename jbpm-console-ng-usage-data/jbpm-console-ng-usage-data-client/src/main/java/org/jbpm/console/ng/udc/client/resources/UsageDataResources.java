package org.jbpm.console.ng.udc.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface UsageDataResources extends ClientBundle {    
    
    UsageDataResources INSTANCE = GWT.create(UsageDataResources.class);

    UsageDataImages images();

}
