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

package org.rwshop.swing.animation.table.animation;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import org.mechio.api.animation.editor.EditState;
import org.mechio.api.animation.editor.EditorListener;
import org.mechio.api.animation.editor.AnimationEditor;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationTableAnimationListener extends EditorListener{
    JTable myTable;
    AnimationTableModel myTableModel;
    AnimationTableChannelListener myChannelListener;

    /**
     *
     * @param table
     */
    public void setTable(JTable table){
        myTable = table;
    }

    /**
     *
     * @param model
     */
    public void setTableModel(AnimationTableModel model){
        myTableModel = model;
    }

    /**
     *
     * @param listener
     */
    public void setChannelListener(AnimationTableChannelListener listener){
        myChannelListener = listener;
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    public void itemAdded(Object invoker, Object controller, int index){
        if(myTableModel == null || myChannelListener == null){
            return;
        }
        ((AnimationEditor)controller).getChild(index).addConsumer(myChannelListener);
        myTableModel.fireTableRowsInserted(index, index);
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    public void itemRemoved(Object invoker, Object controller, int index){
        if(myTableModel == null || myChannelListener == null){
            return;
        }
        myTableModel.fireTableRowsDeleted(index, index);
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
     */
    public void structureChanged(Object invoker, Object controller){
        //myTableModel.fireTableStructureChanged();
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param state
     * @param value
     */
    public void stateChanged(Object invoker, Object controller, EditState state, boolean value){}
    /**
     *
     * @param invoker
     * @param controller
     * @param oldIndex
     * @param newIndex
     */
    public void itemMoved(Object invoker, Object controller, int oldIndex, int newIndex){}

}