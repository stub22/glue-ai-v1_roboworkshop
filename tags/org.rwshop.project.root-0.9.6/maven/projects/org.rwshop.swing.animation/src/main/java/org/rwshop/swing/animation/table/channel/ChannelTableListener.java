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

package org.rwshop.swing.animation.table.channel;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.mechio.api.animation.editor.ChannelEditor;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class ChannelTableListener implements ListSelectionListener {
    ChannelEditor myController;
    JTable myTable;

    ChannelTableListener(JTable table) {
        myTable = table;
    }

    /**
     *
     * @param controller
     */
    public void setController(ChannelEditor controller){
        myController = controller;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(myController == null || myTable == null){
            return;
        }
        int row = myTable.getSelectedRow();
        int col = myTable.getSelectedColumn();
        int sel = myController.getSelectedIndex();

        if(col == 1 && row != sel){
            //Only select the channel if the Name column was clicked
            myController.select(this, row, myController.getSharedHistory());
        }else if(col != 1){
            /* If a column other than Name is click, make sure to reset the
             * Selection back to the selected channel so the table reflects
             * the actual selection. */
            myTable.getSelectionModel().setSelectionInterval(sel, sel);
        }
    }
}
