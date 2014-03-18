/*
 * Copyright 2014 the RoboWorkshop Project
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

package org.rwshop.swing.animation.table.history;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import org.mechio.api.animation.editor.history.HistoryAction;
import org.mechio.api.animation.editor.history.HistoryListener;
import org.mechio.api.animation.editor.history.HistoryStack;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class HistoryTableHistoryStackListener implements HistoryListener{
    private HistoryTableModel myModel;
    private JTable myTable;    

    /**
     *
     * @param table
     */
    public HistoryTableHistoryStackListener(JTable table){
        myTable = table;
    }

    /**
     *
     * @param model
     */
    public void setModel(HistoryTableModel model){
        myModel = model;
    }

    /**
     *
     * @param stack
     * @param event
     * @param oldLength
     */
    public void eventAdded(HistoryStack stack, HistoryAction event, int oldLength) {
        if(myModel == null){
            return;
        }
        if(oldLength >= stack.size()){
            myModel.fireTableRowsDeleted(stack.size()-1, oldLength-1);
        }
        myModel.fireTableRowsInserted(stack.size()-1, stack.getHistory().size()-1);
        final int i = myTable.getRowCount()-1;
        myTable.getSelectionModel().setSelectionInterval(i,i);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                myTable.scrollRectToVisible(myTable.getCellRect(i, myTable.getColumnCount(), true));
            }
        });
    }

    /**
     *
     * @param stack
     * @param time
     */
    public void timeSelected(HistoryStack stack, int time) {
        myTable.getSelectionModel().setSelectionInterval(time,time);
        final int i = time;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                myTable.scrollRectToVisible(myTable.getCellRect(i, myTable.getColumnCount(), true));
            }
        });
    }
}
