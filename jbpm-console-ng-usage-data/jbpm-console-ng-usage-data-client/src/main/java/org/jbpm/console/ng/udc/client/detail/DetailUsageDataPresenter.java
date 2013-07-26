package org.jbpm.console.ng.udc.client.detail;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jbpm.console.ng.udc.client.i8n.Constants;
import org.jbpm.console.ng.udc.client.util.UtilUsageData;
import org.jbpm.console.ng.udc.model.UsageEventSummary;
import org.uberfire.client.annotations.OnReveal;
import org.uberfire.client.annotations.OnStart;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchPopup;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.security.Identity;
import org.uberfire.workbench.events.BeforeClosePlaceEvent;

import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.TextBox;

@Dependent
@WorkbenchPopup(identifier = "Detail Usage Data")
public class DetailUsageDataPresenter {

    private Constants constants = GWT.create(Constants.class);

    @WorkbenchPartTitle
    public String getTitle() {
        return constants.Title_Detail();
    }

    @WorkbenchPartView
    public UberView<DetailUsageDataPresenter> getView() {
        return view;
    }

    @Inject
    DetailUsageDataEventView view;

    @Inject
    Identity identity;

    @Inject
    private Event<BeforeClosePlaceEvent> closePlaceEvent;

    private PlaceRequest place;
    
    @OnStart
    public void onStart( final PlaceRequest place ) {
        this.place = place;
    }
    
    @OnReveal
    public void onReveal() {
        refreshDetailUsageData(UtilUsageData.getUsageDataParam(place));
    }

    public interface DetailUsageDataEventView extends UberView<DetailUsageDataPresenter> {
        void displayNotification(String text);

        TextBox getUsageModuleText();

        TextBox getUsageUserText();

        TextBox getUsageComponentText();

        TextBox getUsageActionText();

        TextBox getUsageKeyText();

        TextBox getUsageLevelText();

        TextBox getUsageStatusText();

        TextArea getTextAreaDescription();
        
        TextBox getFileSystemText();
        
        TextBox getFileNameText();
        
        TextBox getItemPathText();
        
    }

    private void refreshDetailUsageData(UsageEventSummary usageData) {
        view.getUsageModuleText().setText(usageData.getModule());
        view.getUsageUserText().setText(usageData.getUser());
        view.getUsageComponentText().setText(usageData.getComponent());
        view.getUsageActionText().setText(usageData.getAction());
        view.getUsageKeyText().setText(usageData.getKey());
        view.getUsageLevelText().setText(usageData.getLevel());
        view.getUsageStatusText().setText(usageData.getStatus());
        view.getTextAreaDescription().setText(usageData.getDescription());
        view.getFileSystemText().setText(usageData.getFileSystem());
        view.getFileNameText().setText(usageData.getFileName());
        view.getItemPathText().setText(usageData.getItemPath());
    }

    public void close() {
        closePlaceEvent.fire(new BeforeClosePlaceEvent(this.place));
    }

}
