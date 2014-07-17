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

import org.mechio.api.animation.editor.EditState;
import org.mechio.api.animation.editor.EditorListener;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.editor.ChannelEditor;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationTableChannelListener extends EditorListener{
    AnimationEditor myController;
    AnimationTableModel myTableModel;

    /**
     *
     * @param controller
     */
    public void setController(AnimationEditor controller){
        myController = controller;
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
     * @param invoker
     * @param controller
     * @param state
     * @param value
     */
    public void stateChanged(Object invoker, Object controller, EditState state, boolean value){
        if(myController == null || myTableModel == null){
            return;
        }
        if(controller == null || !(controller instanceof ChannelEditor)){
            return;
        }
        int i = myController.getChildren().indexOf(controller);
        if(i >= 0){
            int col = -1;
            if(state == state.DISABLED){
                col = 0;
            }else if(state == state.LOCKED){
                col = 3;
            }else if(state == state.VISIBLE){
                col = 4;
            }else{
                return;
            }
            myTableModel.fireTableCellUpdated(i, col);
        }
    }

    /**
     *
     * @param invoker
     * @param controller
     */
    public void structureChanged(Object invoker, Object controller){
        if(myController == null || myTableModel == null){
            return;
        }
        int i = myController.getChildren().indexOf(controller);
        myTableModel.fireTableRowsUpdated(i, i);
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param oldIndex
     * @param newIndex
     */
    public void selectionChanged(Object invoker, Object controller, int oldIndex, int newIndex){}
    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    public void itemAdded(Object invoker, Object controller, int index){}
    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    public void itemRemoved(Object invoker, Object controller, int index){}
    /**
     *
     * @param invoker
     * @param controller
     * @param oldIndex
     * @param newIndex
     */
    public void itemMoved(Object invoker, Object controller, int oldIndex, int newIndex){}
}
