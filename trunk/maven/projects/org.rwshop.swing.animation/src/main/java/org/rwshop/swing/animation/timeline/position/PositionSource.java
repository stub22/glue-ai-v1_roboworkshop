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

package org.rwshop.swing.animation.timeline.position;

import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleContext;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.animation.utils.AnimationEditListener;
import org.robokind.api.animation.utils.PositionAdder;
import org.robokind.api.common.osgi.ClassTracker;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class PositionSource {
    private ClassTracker<PositionAdder> myPositionAdderTracker;
    private ClassTracker<AnimationEditListener> myEditListenerTracker;
    private AnimationEditor myEditor;
    private boolean myAddFlag;
    private boolean myEditFlag;
    
    public PositionSource(BundleContext context){
        if(context == null){
            throw new NullPointerException();
        }
        myPositionAdderTracker = new ClassTracker<PositionAdder>(context,PositionAdder.class.getName(),null,null);
        myEditListenerTracker = new ClassTracker<AnimationEditListener>(context,AnimationEditListener.class.getName(),null,null);
        myAddFlag = false;
        myEditFlag = false;
    }
    
    public boolean getAddEnabled(){
        return myAddFlag;
    }
    
    public void setAddEnabled(boolean enabled){
        myAddFlag = enabled;
    }
    
    public void toggleAddEnabled(){
        setAddEnabled(!myAddFlag);
    }
    
    public boolean getEditEnabled(){
        return myEditFlag;
        
    }
    
    public void setEditEnabled(boolean enabled){
        List<AnimationEditListener> listeners = myEditListenerTracker.getServices();
        if(listeners == null){
            return;
        }
        for(AnimationEditListener listener : listeners){
            listener.setEditEnabled(enabled);
        }        
        myEditFlag = enabled;
    }
    
    public void toggleEditEnabled(){
        setEditEnabled(!myEditFlag);
    }
    
    public void setAnimationEditor(AnimationEditor editor){
        myEditor = editor;
    }
    
    public void addPositions(long time){
        if(myEditor == null || !myAddFlag){
            return;
        }
        PositionAdder pa = myPositionAdderTracker.getTopService();
        if(pa == null){
            return;
        }
        pa.addPositions(myEditor, time);
    }
    
    public void handlePositionEdit(long time, Map<Integer,Double> positions){
        if(myEditor == null || !myEditFlag){
            return;
        }
        List<AnimationEditListener> listeners = myEditListenerTracker.getServices();
        if(listeners == null){
            return;
        }
        for(AnimationEditListener listener : listeners){
            listener.handlePositions(time, positions);
        }
    }
}
