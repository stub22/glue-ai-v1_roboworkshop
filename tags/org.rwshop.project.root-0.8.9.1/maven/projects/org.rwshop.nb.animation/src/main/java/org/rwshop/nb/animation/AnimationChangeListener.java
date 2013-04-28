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

package org.rwshop.nb.animation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.robokind.api.animation.editor.EditState;
import org.robokind.api.animation.editor.EditorListener;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class AnimationChangeListener extends EditorListener implements PropertyChangeListener {
    public final static String PROP_ANIMATION_CHANGE = "AnimationChange";
    private PropertyChangeSupport mySupport;
    
    public AnimationChangeListener(){
        mySupport = new PropertyChangeSupport(this);
    }
    
    @Override
    public void selectionChanged(Object invoker, Object controller, int oldIndex, int newIndex) {
    }

    @Override
    public void itemAdded(Object invoker, Object controller, int index) {
        fireAnimationChange(controller);
    }

    @Override
    public void itemRemoved(Object invoker, Object controller, int index) {
        fireAnimationChange(controller);
    }

    @Override
    public void itemMoved(Object invoker, Object controller, int oldIndex, int newIndex) {
        fireAnimationChange(controller);
    }

    @Override
    public void stateChanged(Object invoker, Object controller, EditState state, boolean value) {
        fireAnimationChange(controller);
    }

    @Override
    public void structureChanged(Object invoker, Object controller) {
        fireAnimationChange(controller);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        fireAnimationChange(evt.getNewValue());
    }
    
    private void fireAnimationChange(Object controller){
        mySupport.firePropertyChange(PROP_ANIMATION_CHANGE, null, controller);
    }
    
    public void addListener(PropertyChangeListener listener){
        mySupport.addPropertyChangeListener(listener);
    }
    
    public void removeListener(PropertyChangeListener listener){
        mySupport.removePropertyChangeListener(listener);
    }
    
}
