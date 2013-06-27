/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.console.ng.he.client.history;

import java.util.Comparator;
import java.util.Date;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.jbpm.console.ng.he.client.i8n.Constants;
import org.jbpm.console.ng.he.client.util.ResizableHeader;
import org.jbpm.console.ng.he.client.util.UtilEvent;
import org.jbpm.console.ng.he.model.HumanEventSummary;
import org.uberfire.workbench.events.NotificationEvent;

import com.github.gwtbootstrap.client.ui.DataGrid;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.SimplePager;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

@Dependent
@Templated(value = "ActionHistoryListViewImpl.html")
public class ActionHistoryListViewImpl extends Composite implements ActionHistoryPresenter.ActionHistoryView {
    private Constants constants = GWT.create(Constants.class);

    @Inject
    @DataField
    public NavLink showAllTasksNavLink;

    @Inject
    @DataField
    public NavLink clearEventsNavLink;

    @Inject
    @DataField
    public NavLink exportEventsNavLink;

    @Inject
    @DataField
    public TextBox searchBox;

    @Inject
    @DataField
    public NavLink showGroupTasksNavLink;

    @DataField
    public Heading taskCalendarViewLabel = new Heading(4);

    @Inject
    @DataField
    public FlowPanel eventsViewContainer;

    @Inject
    @DataField
    public IconAnchor refreshIcon;

    @Inject
    private Event<NotificationEvent> notification;

    private ActionHistoryPresenter presenter;

    public DataGrid<HumanEventSummary> myEventListGrid;

    public SimplePager pager;

    private ListHandler<HumanEventSummary> sortHandler;

    private MultiSelectionModel<HumanEventSummary> selectionModel;

    private Date currentDate;

    @Override
    public void init(ActionHistoryPresenter presenter) {
        this.presenter = presenter;

        refreshIcon.setTitle(constants.Refresh());
        refreshIcon.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                refreshHumanEvents();
                searchBox.setText("");
                displayNotification(constants.Events_Refreshed());
            }
        });

        currentDate = new Date();

        // By Default we will start in Grid View
        initializeGridView();

        // Filters
        clearEventsNavLink.setText(constants.Clear_Events());
        clearEventsNavLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                clearEventsNavLink.setStyleName("active");
                showGroupTasksNavLink.setStyleName("");
                showAllTasksNavLink.setStyleName("");
                exportEventsNavLink.setStyleName("");
                clearHumanEvents();
            }
        });

        showGroupTasksNavLink.setText(constants.Group());
        showGroupTasksNavLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showGroupTasksNavLink.setStyleName("active");
                clearEventsNavLink.setStyleName("");
                showAllTasksNavLink.setStyleName("");
                exportEventsNavLink.setStyleName("");
                // currentEventHumanType = HumanEventType.GROUP;
                refreshHumanEvents();

            }
        });

        showAllTasksNavLink.setText(constants.All());
        showAllTasksNavLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showGroupTasksNavLink.setStyleName("");
                clearEventsNavLink.setStyleName("");
                showAllTasksNavLink.setStyleName("active");
                exportEventsNavLink.setStyleName("");
                // currentEventHumanType = HumanEventType.ALL;
                refreshHumanEvents();

            }
        });

        exportEventsNavLink.setText(constants.Export_Txt());
        exportEventsNavLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showGroupTasksNavLink.setStyleName("");
                clearEventsNavLink.setStyleName("");
                showAllTasksNavLink.setStyleName("");
                exportEventsNavLink.setStyleName("active");
                // currentEventHumanType = HumanEventType.EXPORT;
                exportTxtEvents();
            }
        });

        taskCalendarViewLabel.setText(constants.List_Human_Event());
        taskCalendarViewLabel.setStyleName("");

        searchBox.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13 || event.getNativeKeyCode() == 32) {
                    displayNotification("Filter Event: |" + searchBox.getText() + "|");
                    filterEvents(searchBox.getText());
                }

            }
        });

        refreshHumanEvents();

    }

    public void filterEvents(String text) {
        presenter.filterEvents(text);
    }

    @Override
    public void refreshHumanEvents() {
        presenter.refreshHumanEvent();
    }

    public void clearHumanEvents() {
        presenter.clearHumanEvents();
        displayNotification(constants.Clear_Msj());
    }

    public void exportTxtEvents() {
        presenter.exportToTxt();
    }

    private void initializeGridView() {
        eventsViewContainer.clear();
        myEventListGrid = new DataGrid<HumanEventSummary>();
        myEventListGrid.setStyleName("table table-bordered table-striped table-hover");
        pager = new SimplePager();
        pager.setStyleName("pagination pagination-right pull-right");
        pager.setDisplay(myEventListGrid);
        pager.setPageSize(30);

        eventsViewContainer.add(myEventListGrid);
        eventsViewContainer.add(pager);

        myEventListGrid.setHeight("350px");
        // Set the message to display when the table is empty.
        myEventListGrid.setEmptyTableWidget(new Label(constants.No_Human_Events()));

        // Attach a column sort handler to the ListDataProvider to sort the
        // list.
        sortHandler = new ColumnSortEvent.ListHandler<HumanEventSummary>(presenter.getAllEventsSummaries());

        myEventListGrid.addColumnSortHandler(sortHandler);

        myEventListGrid.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<HumanEventSummary> createCheckboxManager());

        initTableColumns(selectionModel);
        presenter.addDataDisplay(myEventListGrid);

    }

    private void initTableColumns(final SelectionModel<HumanEventSummary> selectionModel) {
        // Timestamp.
        Column<HumanEventSummary, String> timeColumn = new Column<HumanEventSummary, String>(new TextCell()) {
            @Override
            public String getValue(HumanEventSummary object) {
                if (object.getEventTime() != null) {
                    return UtilEvent.getDateTime(object.getEventTime(), UtilEvent.patternDateTime);
                }
                return "";
            }
        };
        timeColumn.setSortable(true);

        myEventListGrid.addColumn(timeColumn, new ResizableHeader(constants.Time(), myEventListGrid, timeColumn));
        sortHandler.setComparator(timeColumn, new Comparator<HumanEventSummary>() {
            @Override
            public int compare(HumanEventSummary o1, HumanEventSummary o2) {
                if (o1.getEventTime() == null || o2.getEventTime() == null) {
                    return 0;
                }
                return o1.getEventTime().compareTo(o2.getEventTime());
            }
        });

        // Component.
        Column<HumanEventSummary, String> componentNameColumn = new Column<HumanEventSummary, String>(new TextCell()) {
            @Override
            public String getValue(HumanEventSummary object) {
                return object.getDescriptionEvent();
            }
        };
        componentNameColumn.setSortable(true);

        myEventListGrid.addColumn(componentNameColumn, new ResizableHeader(constants.Component(), myEventListGrid,
                componentNameColumn));
        sortHandler.setComparator(componentNameColumn, new Comparator<HumanEventSummary>() {
            @Override
            public int compare(HumanEventSummary o1, HumanEventSummary o2) {
                return o1.getDescriptionEvent().compareTo(o2.getDescriptionEvent());
            }
        });

        // Action.
        Column<HumanEventSummary, String> actionNameColumn = new Column<HumanEventSummary, String>(new TextCell()) {
            @Override
            public String getValue(HumanEventSummary object) {
                return object.getAction();
            }
        };
        actionNameColumn.setSortable(true);

        myEventListGrid
                .addColumn(actionNameColumn, new ResizableHeader(constants.Actions(), myEventListGrid, actionNameColumn));
        sortHandler.setComparator(actionNameColumn, new Comparator<HumanEventSummary>() {
            @Override
            public int compare(HumanEventSummary o1, HumanEventSummary o2) {
                return o1.getAction().compareTo(o2.getAction());
            }
        });

        // User.
        Column<HumanEventSummary, String> userNameColumn = new Column<HumanEventSummary, String>(new TextCell()) {
            @Override
            public String getValue(HumanEventSummary object) {
                return object.getUser();
            }
        };
        userNameColumn.setSortable(true);

        myEventListGrid.addColumn(userNameColumn, new ResizableHeader(constants.User(), myEventListGrid, userNameColumn));
        sortHandler.setComparator(userNameColumn, new Comparator<HumanEventSummary>() {
            @Override
            public int compare(HumanEventSummary o1, HumanEventSummary o2) {
                return o1.getUser().compareTo(o2.getUser());
            }
        });

        // idEvent
        Column<HumanEventSummary, Number> taskIdColumn = new Column<HumanEventSummary, Number>(new NumberCell()) {
            @Override
            public Number getValue(HumanEventSummary object) {
                return object.getIdEvent();
            }
        };
        taskIdColumn.setSortable(true);
        myEventListGrid.setColumnWidth(taskIdColumn, "80px");

        myEventListGrid.addColumn(taskIdColumn, new ResizableHeader(constants.Id_Event(), myEventListGrid, taskIdColumn));
        sortHandler.setComparator(taskIdColumn, new Comparator<HumanEventSummary>() {
            @Override
            public int compare(HumanEventSummary o1, HumanEventSummary o2) {
                return Long.valueOf(o1.getIdEvent()).compareTo(Long.valueOf(o2.getIdEvent()));
            }
        });

        // Status.
        Column<HumanEventSummary, String> statusNameColumn = new Column<HumanEventSummary, String>(new TextCell()) {
            @Override
            public String getValue(HumanEventSummary object) {
                return object.getStatus();
            }
        };
        statusNameColumn.setSortable(true);

        myEventListGrid.addColumn(statusNameColumn, new ResizableHeader(constants.Status(), myEventListGrid, statusNameColumn));
        sortHandler.setComparator(statusNameColumn, new Comparator<HumanEventSummary>() {
            @Override
            public int compare(HumanEventSummary o1, HumanEventSummary o2) {
                return o1.getStatus().compareTo(o2.getStatus());
            }
        });

    }

    @Override
    public void displayNotification(String text) {
        notification.fire(new NotificationEvent(text));
    }

    @Override
    public MultiSelectionModel<HumanEventSummary> getSelectionModel() {
        return selectionModel;
    }

    public TextBox getSearchBox() {
        return searchBox;
    }
}
