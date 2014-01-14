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

package org.jbpm.console.ng.gc.client.list.base;

import java.util.Comparator;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.jbpm.console.ng.gc.client.util.DataGridUtils;
import org.jbpm.console.ng.gc.client.util.ResizableHeader;
import org.jbpm.console.ng.ht.model.TaskSummary;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DataGrid;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.HTMLPanel;

@Dependent
@Templated(value = "ListViewImpl.html")
public class ListViewImpl extends BaseViewImpl<TaskSummary, ListPresenter> implements ListPresenter.ListView {

    /* left buttons */
    @Inject
    @DataField
    public NavLink btnLeftNavLink;

    /* right buttons */
    @Inject
    @DataField
    public Button btnRightNavLink;

    @Override
    public void init(ListPresenter presenter) {
        this.presenter = presenter;
        this.initializeGridView();
        this.initializeLeftButtons();
        this.initializeRightButtons();

    }

    @Override
    public void initializeGridView() {
        viewContainer.clear();

        myListGrid = new DataGrid<TaskSummary>();
        myListGrid.setStyleName(GRID_STYLE);

        pager.setDisplay(myListGrid);
        pager.setPageSize(DataGridUtils.pageSize);

        viewContainer.add(myListGrid);
        myListGrid.setEmptyTableWidget(new HTMLPanel(constants.No_Tasks_Found()));

        sortHandler = new ColumnSortEvent.ListHandler<TaskSummary>(presenter.getDataProvider().getList());

        myListGrid.getColumnSortList().setLimit(1);

        this.setSelectionModel();
        this.setGridEvents();
        this.initGridColumns();

        myListGrid.addColumnSortHandler(sortHandler);

        presenter.addDataDisplay(myListGrid);

        this.refreshItems();
    }

    @Override
    public void setSelectionModel() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initializeLeftButtons() {
        btnLeftNavLink.setText(constants.Grid());
        btnLeftNavLink.setStyleName("btn btn-small active");
    }

    @Override
    public void initializeRightButtons() {
        btnRightNavLink.setText(constants.Active());
        btnRightNavLink.setStyleName("btn btn-small active");
    }

    @Override
    public void addHandlerPager() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGridEvents() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initGridColumns() {
        this.idColumn();
        this.nameColumn();

    }

    private void idColumn() {
        Column<TaskSummary, Number> taskIdColumn = new Column<TaskSummary, Number>(new NumberCell()) {
            @Override
            public Number getValue(TaskSummary object) {
                return object.getId();
            }
        };
        taskIdColumn.setSortable(true);
        myListGrid.setColumnWidth(taskIdColumn, "50px");
        myListGrid.addColumn(taskIdColumn, new ResizableHeader(constants.Id(), myListGrid, taskIdColumn));
        sortHandler.setComparator(taskIdColumn, new Comparator<TaskSummary>() {
            @Override
            public int compare(TaskSummary o1, TaskSummary o2) {
                return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
            }
        });
    }

    private void nameColumn() {
        Column<TaskSummary, String> taskNameColumn = new Column<TaskSummary, String>(new TextCell()) {
            @Override
            public String getValue(TaskSummary object) {
                return object.getName();
            }
        };
        taskNameColumn.setSortable(true);
        myListGrid.addColumn(taskNameColumn, new ResizableHeader("Description", myListGrid, taskNameColumn));
        sortHandler.setComparator(taskNameColumn, new Comparator<TaskSummary>() {
            @Override
            public int compare(TaskSummary o1, TaskSummary o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    @Override
    public void refreshItems() {
        presenter.refreshItems();
    }

}
