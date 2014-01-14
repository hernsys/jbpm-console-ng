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
import javax.inject.Inject;

import org.jbpm.console.ng.ht.model.TaskSummary;
import org.jbpm.console.ng.ht.model.events.TaskSearchEvent;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberView;

import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;

@Dependent
@WorkbenchScreen(identifier = "Base List")
public class ListPresenter extends BasePresenter<TaskSummary> {

    @Inject
    private ListViewImpl view;

    public interface ListView extends UberView<ListPresenter> {
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

    /**
     * this method must invoke the services to get info to show in the grid
     */
    @Override
    protected void refreshItems() {
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
        TaskSummary child13 = new TaskSummary();
        TaskSummary child14 = new TaskSummary();
        TaskSummary child15 = new TaskSummary();
        TaskSummary child16 = new TaskSummary();
        TaskSummary child17 = new TaskSummary();
        TaskSummary child18 = new TaskSummary();
        TaskSummary child19 = new TaskSummary();
        TaskSummary child20 = new TaskSummary();
        TaskSummary child21 = new TaskSummary();
        TaskSummary child22 = new TaskSummary();
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
        child13.setId(13);
        child13.setName("13 item");
        child14.setId(14);
        child14.setName("14 item");
        child15.setId(15);
        child15.setName("15 item");
        child16.setId(16);
        child16.setName("16 item");
        child17.setId(17);
        child17.setName("17 item");
        child18.setId(18);
        child18.setName("18 item");
        child19.setId(19);
        child19.setName("19 item");
        child20.setId(20);
        child20.setName("20 item");
        child21.setId(21);
        child21.setName("21 item");
        child22.setId(22);
        child22.setName("22 item");
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
        allItemsSummaries.add(child13);
        allItemsSummaries.add(child14);
        allItemsSummaries.add(child15);
        allItemsSummaries.add(child16);
        allItemsSummaries.add(child17);
        allItemsSummaries.add(child18);
        allItemsSummaries.add(child19);
        allItemsSummaries.add(child20);
        allItemsSummaries.add(child21);
        allItemsSummaries.add(child22);

        filterItems(view.getCurrentFilter());
    }

    @Override
    public void filterItems(String text) {
        GWT.log("TEXT SEARCH " + text);
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
                GWT.log("ts.getName() " + ts.getName());
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

}
