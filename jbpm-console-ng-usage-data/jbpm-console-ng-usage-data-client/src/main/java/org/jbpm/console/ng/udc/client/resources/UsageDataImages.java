package org.jbpm.console.ng.udc.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface UsageDataImages extends ClientBundle {    
    
    UsageDataImages INSTANCE = GWT.create(UsageDataImages.class);
    
    //TODO module commons
    @Source("org/jbpm/console/ng/udc/public/images/icons/details-grid-icon.png")
    public ImageResource detailsIcon();

}
