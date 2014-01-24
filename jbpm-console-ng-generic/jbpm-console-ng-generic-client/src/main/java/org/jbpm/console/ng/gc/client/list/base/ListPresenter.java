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

import java.util.List;

import javax.enterprise.context.Dependent;

import org.jbpm.console.ng.ht.model.TaskSummary;
import org.jbpm.console.ng.ht.model.events.TaskSearchEvent;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberView;
import org.uberfire.workbench.model.menu.Menus;

import com.google.common.collect.Lists;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;

@Dependent
@WorkbenchScreen(identifier = "Base List")
public class ListPresenter extends BasePresenter<TaskSummary, ListViewImpl> {

    public interface ListView extends UberView<ListPresenter> {
    }

    @WorkbenchMenu
    public Menus getMenus() {
        return menus;
    }

    @WorkbenchPartView
    public UberView<ListPresenter> getView() {
        return view;
    }

    @DefaultStringValue("Title")
    @WorkbenchPartTitle
    public String getTitle() {
        return null;
    }

    @Override
    protected void refreshItems() {
        // TODO it must call to getAll in backend Service
        allItemsSummaries = Lists.newArrayList();
        TaskSummary child = new TaskSummary();
        TaskSummary child2 = new TaskSummary();
        TaskSummary child3 = new TaskSummary();
        TaskSummary child4 = new TaskSummary();
        TaskSummary child5 = new TaskSummary();
        TaskSummary child6 = new TaskSummary();
        TaskSummary child7 = new TaskSummary();
        TaskSummary child8 = new TaskSummary();
        TaskSummary child9 = new TaskSummary();
        TaskSummary child10 = new TaskSummary();
        TaskSummary child11 = new TaskSummary();
        TaskSummary child12 = new TaskSummary();
        child.setId(1);
        child.setName("1 item");
        child2.setId(2);
        child2.setName("2 item");
        child3.setId(3);
        child3.setName("3 item");
        child4.setId(4);
        child4.setName("4 item");
        child5.setId(5);
        child5.setName("5 item");
        child6.setId(6);
        child6.setName("6 item");
        child7.setId(7);
        child7.setName("7 item");
        child8.setId(8);
        child8.setName("8 item");
        child9.setId(9);
        child9.setName("9 item");
        child10.setId(10);
        child10.setName("10 item");
        child11.setId(11);
        child11.setName("11 item");
        child12.setId(12);
        child12.setName("12 item");
        allItemsSummaries.add(child);
        allItemsSummaries.add(child2);
        allItemsSummaries.add(child3);
        allItemsSummaries.add(child4);
        allItemsSummaries.add(child5);
        allItemsSummaries.add(child6);
        allItemsSummaries.add(child7);
        allItemsSummaries.add(child8);
        allItemsSummaries.add(child9);
        allItemsSummaries.add(child10);
        allItemsSummaries.add(child11);
        allItemsSummaries.add(child12);

        filterItems(view.getCurrentFilter());
    }

    @Override
    public void filterItems(String text) {
        ColumnSortList.ColumnSortInfo sortInfo = view.getListGrid().getColumnSortList().size() > 0 ? view.getListGrid()
                .getColumnSortList().get(0) : null;
        if (allItemsSummaries != null) {
            this.filterGrid(sortInfo, text);
        }
    }

    private void filterGrid(ColumnSortList.ColumnSortInfo sortInfo, String text) {
        List<TaskSummary> filteredTasksSimple = Lists.newArrayList();
        if (!text.equals("")) {
            for (TaskSummary ts : allItemsSummaries) {
                if (ts.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredTasksSimple.add(ts);
                }
            }
        } else {
            filteredTasksSimple = allItemsSummaries;
        }
        dataProvider.getList().clear();
        dataProvider.getList().addAll(filteredTasksSimple);
        if (sortInfo != null && sortInfo.isAscending()) {
            view.getListGrid().getColumnSortList().clear();
            ColumnSortInfo columnSortInfo = new ColumnSortInfo(sortInfo.getColumn(), sortInfo.isAscending());
            view.getListGrid().getColumnSortList().push(columnSortInfo);
            ColumnSortEvent.fire(view.getListGrid(), view.getListGrid().getColumnSortList());
        }
    }

    @Override
    public void onSearchEvent(TaskSearchEvent searchEvent) {
        view.setCurrentFilter(searchEvent.getFilter());
        refreshItems();
    }

    @Override
    protected void deleteItem(Long id) {
        view.displayNotification("The item with ID:" + id + " was deleted.");
        // TODO it must call to backend service to delete it
        List<TaskSummary> mockList = Lists.newArrayList();
        for (TaskSummary t : allItemsSummaries) {
            if (t.getId() != id) {
                mockList.add(t);
            }
        }
        allItemsSummaries.clear();
        allItemsSummaries.addAll(mockList);
        // TODO this method must be called
        // this.refreshItems();
        filterItems(view.getCurrentFilter());

    }

    @Override
    protected void createItem() {
        // TODO Auto-generated method stub
        view.displayNotification("TODO to create a new Item");
    }

    @Override
    protected void updateItem(Long id) {
        // TODO Auto-generated method stub
        view.displayNotification("TODO update item with ID:" + id);
    }

    @Override
    protected void readItem(Long id) {
        // TODO Auto-generated method stub
        view.displayNotification("TODO View item with ID:" + id);
    }

}
