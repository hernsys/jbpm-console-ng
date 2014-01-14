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

import javax.enterprise.event.Observes;

import org.jbpm.console.ng.ht.model.events.TaskSearchEvent;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

public abstract class BasePresenter<T> {

    protected ListDataProvider<T> dataProvider = new ListDataProvider<T>();
    protected List<T> allItemsSummaries;

    protected abstract void refreshItems();

    protected abstract void filterItems(String text);

    protected abstract void onSearchEvent(@Observes final TaskSearchEvent searchEvent);

    protected void addDataDisplay(HasData<T> display) {
        dataProvider.addDataDisplay(display);
    }

    protected ListDataProvider<T> getDataProvider() {
        return dataProvider;
    }

}
