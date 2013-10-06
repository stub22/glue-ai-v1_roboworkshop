/*
 * Copyright 2011 Hanson Robokind LLC.
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

package org.rwshop.swing.animation.table.motionpath;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import org.robokind.api.animation.editor.EditState;
import org.robokind.api.animation.editor.EditorListener;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class MotionPathTablePathListener extends EditorListener{
    private JTable myTable;
    private MotionPathTableModel myTableModel;

    /**
     *
     * @param m
     */
    public MotionPathTablePathListener(MotionPathTableModel m){
        myTableModel = m;
    }

    /**
     *
     * @param table
     */
    public void setTable(JTable table){
        myTable = table;
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param oldIndex
     * @param newIndex
     */
    public void selectionChanged(Object invoker, Object controller, int oldIndex, int newIndex){
        if(myTable == null){
            return;
        }
        myTable.getSelectionModel().setSelectionInterval(newIndex,newIndex);
        final int i = newIndex;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                myTable.scrollRectToVisible(myTable.getCellRect(i, myTable.getColumnCount(), true));
            }
        });
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    public void itemAdded(Object invoker, Object controller, int index) {
        myTableModel.fireTableRowsInserted(index, index);
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    public void itemRemoved(Object invoker, Object controller, int index) {
        myTableModel.fireTableRowsDeleted(index, index);
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param oldIndex
     * @param newIndex
     */
    public void itemMoved(Object invoker, Object controller, int oldIndex, int newIndex) {
        int min = oldIndex > newIndex ? newIndex : oldIndex;
        int max = oldIndex < newIndex ? newIndex : oldIndex;
        myTableModel.fireTableRowsUpdated(min, max);
    }

    /**
     *
     * @param invoker
     * @param controller
     */
    public void structureChanged(Object invoker, Object controller){
        myTableModel.fireTableDataChanged();
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param state
     * @param value
     */
    public void stateChanged(Object invoker, Object controller, EditState state, boolean value){}
}
