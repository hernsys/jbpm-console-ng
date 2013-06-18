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

package org.jbpm.console.ng.ht.client.history;

import java.util.LinkedList;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberView;

@Dependent
@WorkbenchScreen(identifier = "Actions Histories")
public class ActionHistoryPresenter {
    
    @Inject
    private ActionHistory actionHistory;
    

    public void saveHistory(@Observes @History PointHistory pointHistory) {
        updateHistory(pointHistory);
    }
    
    private void updateHistory(PointHistory pointHistory){
        if(actionHistory.getPoints()==null){
            actionHistory.setPoints(new LinkedList<PointHistory>());
        }
        actionHistory.getPoints().add(pointHistory);
    }

    public interface ActionHistoryView extends UberView<ActionHistoryPresenter> {
    
     void displayNotification( String text );
    
     //TaskListMultiDayBox getTaskListMultiDayBox();
    
     //MultiSelectionModel<TaskSummary> getSelectionModel();
    
     //TextBox getSearchBox();
    
     //void refreshTasks();
    }

}