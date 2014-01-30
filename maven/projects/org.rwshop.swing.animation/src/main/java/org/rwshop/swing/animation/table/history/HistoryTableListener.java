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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.mechio.api.animation.editor.history.HistoryStack;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class HistoryTableListener implements ListSelectionListener, TableModelListener {
    private HistoryStack myHistory;
    private JTable myTable;

    /**
     *
     * @param table
     */
    public void setTable(JTable table){
        myTable = table;
    }

    /**
     *
     * @param hist
     */
    public void setHistory(HistoryStack hist){
        myHistory = hist;
    }

    public void valueChanged(ListSelectionEvent e) {
        if(myHistory == null || myTable == null){
            return;
        }
        int i = myTable.getSelectedRow();
        myHistory.gotoTime(i);
    }

    public void tableChanged(TableModelEvent e) {
        if(e.getType() == TableModelEvent.INSERT){
            int i = myTable.getRowCount()-1;
            myTable.getSelectionModel().setSelectionInterval(i,i);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    myTable.scrollRectToVisible(myTable.getCellRect(myTable.getRowCount()-1, myTable.getColumnCount(), true));
                }
            });
        }
    }
}
