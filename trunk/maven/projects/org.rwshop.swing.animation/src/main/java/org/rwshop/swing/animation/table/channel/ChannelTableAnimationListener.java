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

import org.mechio.api.animation.editor.EditState;
import org.mechio.api.animation.editor.EditorListener;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.editor.ChannelEditor;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class ChannelTableAnimationListener extends EditorListener{
    ChannelTable myTable;

    /**
     *
     * @param ctm
     */
    public void init(ChannelTable ctm){
        myTable = ctm;
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param oldIndex
     * @param newIndex
     */
    public void selectionChanged(Object invoker, Object controller, int oldIndex, int newIndex){
        AnimationEditor ac = (AnimationEditor)controller;
        ChannelEditor cc;
        if(ac.getChildren().isEmpty() || ac.getSelectedIndex() == -1){
            cc = null;
        }else{
            cc = ac.getSelected();
        }
        myTable.setController(cc);
    }

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
    /**
     *
     * @param invoker
     * @param controller
     */
    public void structureChanged(Object invoker, Object controller){}
}