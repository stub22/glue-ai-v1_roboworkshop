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

package org.rwshop.nb.animation.history;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.mechio.api.animation.editor.history.HistoryAction;
import org.mechio.api.animation.editor.history.HistoryListener;
import org.mechio.api.animation.editor.history.HistoryStack;
import org.openide.awt.UndoRedo;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class UndoRedoHistoryStack extends HistoryStack implements UndoRedo, HistoryListener{
    private List<ChangeListener> myChangeListeners;

    public UndoRedoHistoryStack(){
        super();
        addListener(this);
    }

    @Override
    public boolean canUndo() {
        return getCurrentUndoCount() > 0;
    }

    @Override
    public boolean canRedo() {
        return getCurrentRedoCount() > 0;
    }

    @Override
    public void undo() throws CannotUndoException {
        move(-1);
    }

    @Override
    public void redo() throws CannotRedoException {
        move(1);
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        if(myChangeListeners == null){
            myChangeListeners = new ArrayList<ChangeListener>();
        }
        if(!myChangeListeners.contains(l)){
            myChangeListeners.add(l);
        }
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        if(myChangeListeners != null){
            myChangeListeners.remove(l);
        }
    }

    @Override
    public String getUndoPresentationName() {
        return getEvent(getSelectedIndex()).getName();
    }

    @Override
    public String getRedoPresentationName() {
        return getEvent(getSelectedIndex()).getName();
    }


    @Override
    public void eventAdded(HistoryStack stack, HistoryAction event, int oldSize) {
        notifyChangeListeners();
    }

    @Override
    public void timeSelected(HistoryStack stack, int time) {
        notifyChangeListeners();
    }

    private void notifyChangeListeners(){
        if(myChangeListeners == null){
            return;
        }
        ChangeEvent e = new ChangeEvent(this);
        for(int i=0; i<myChangeListeners.size(); i++){
            ChangeListener cl = myChangeListeners.get(i);
            cl.stateChanged(e);
        }
    }
}
