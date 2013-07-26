package org.jbpm.console.ng.udc.client.detail;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.jbpm.console.ng.udc.client.i8n.Constants;
import org.uberfire.workbench.events.NotificationEvent;

import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

@Dependent
@Templated(value = "DetailUsageDataViewImpl.html")
public class DetailUsageDataViewImpl extends Composite implements DetailUsageDataPresenter.DetailUsageDataEventView {

    private Constants constants = GWT.create(Constants.class);

    @Inject
    @DataField
    public Label usageModuleLabel;

    @Inject
    @DataField
    public TextBox usageModuleText;

    @Inject
    @DataField
    public Label usageUserLabel;

    @Inject
    @DataField
    public TextBox usageUserText;

    @Inject
    @DataField
    public Label usageComponentLabel;

    @Inject
    @DataField
    public TextBox usageComponentText;

    @Inject
    @DataField
    public Label usageActionLabel;

    @Inject
    @DataField
    public TextBox usageActionText;

    @Inject
    @DataField
    public Label usageKeyLabel;

    @Inject
    @DataField
    public TextBox usageKeyText;

    @Inject
    @DataField
    public Label usageLevelLabel;

    @Inject
    @DataField
    public TextBox usageLevelText;

    @Inject
    @DataField
    public Label usageStatusLabel;

    @Inject
    @DataField
    public TextBox usageStatusText;

    @Inject
    @DataField
    public Label usageDescriptionLabel;

    @Inject
    @DataField
    public TextArea textAreaDescription;
    
    @Inject
    @DataField
    public Label vfsLabel;
    
    @Inject
    @DataField
    public Label fileSystemLabel;

    @Inject
    @DataField
    public TextBox fileSystemText;
    
    @Inject
    @DataField
    public Label fileNameLabel;

    @Inject
    @DataField
    public TextBox fileNameText;
    
    @Inject
    @DataField
    public Label itemPathLabel;

    @Inject
    @DataField
    public TextBox itemPathText;

    private DetailUsageDataPresenter presenter;

    @Inject
    private Event<NotificationEvent> notification;

    @Override
    public void init(DetailUsageDataPresenter presenter) {
        this.presenter = presenter;
        this.setTextAllLabels();
        this.disableAllTextBox();
    }

    private void setTextAllLabels() {
        usageModuleLabel.setText(constants.Module());
        usageUserLabel.setText(constants.User());
        usageComponentLabel.setText(constants.Component());
        usageActionLabel.setText(constants.Actions());
        usageKeyLabel.setText(constants.Key());
        usageLevelLabel.setText(constants.Level());
        usageStatusLabel.setText(constants.Status());
        usageDescriptionLabel.setText(constants.Description());
        //vfs
        vfsLabel.setText(constants.Vfs());
        fileSystemLabel.setText(constants.FileSystem());
        fileNameLabel.setText(constants.FileName());
        itemPathLabel.setText(constants.ItemPath());
    }
    
    private void disableAllTextBox(){
        usageModuleText.setEnabled(false);
        usageUserText.setEnabled(false);
        usageComponentText.setEnabled(false);
        usageActionText.setEnabled(false);
        usageKeyText.setEnabled(false);
        usageLevelText.setEnabled(false);
        usageStatusText.setEnabled(false);
        textAreaDescription.setEnabled(false);
        fileSystemText.setEnabled(false);
        fileNameText.setEnabled(false);
        itemPathText.setEnabled(false);
    }

    @Override
    public TextBox getUsageModuleText() {
        return usageModuleText;
    }

    @Override
    public TextBox getUsageUserText() {
        return usageUserText;
    }

    @Override
    public TextBox getUsageComponentText() {
        return usageComponentText;
    }

    @Override
    public TextBox getUsageActionText() {
        return usageActionText;
    }

    @Override
    public TextBox getUsageKeyText() {
        return usageKeyText;
    }

    @Override
    public TextBox getUsageLevelText() {
        return usageLevelText;
    }

    @Override
    public TextBox getUsageStatusText() {
        return usageStatusText;
    }

    @Override
    public TextArea getTextAreaDescription() {
        return textAreaDescription;
    }
    
    @Override
    public void displayNotification(String text) {
        notification.fire(new NotificationEvent(text));
    }

	@Override
	public TextBox getFileSystemText() {
		return fileSystemText;
	}

	@Override
	public TextBox getFileNameText() {
		return fileNameText;
	}

	@Override
	public TextBox getItemPathText() {
		return itemPathText;
	}

}
