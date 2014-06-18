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

package org.rwshop.swing.animation.timeline;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import org.mechio.api.animation.editor.AbstractEditor;
import org.mechio.api.animation.editor.EditState;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.editor.ChannelEditor;
import org.mechio.api.animation.editor.EditorListener;
import org.rwshop.swing.animation.ElementSettings;
import org.rwshop.swing.animation.EditorElement;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class TimelineAnimation extends EditorListener implements EditorElement{
    private AnimationEditor myAnimation;
    private List<TimelineChannel> myChannels;
    private ElementSettings mySettings;

    /**
     *
     * @param a
     * @param es
     * @param properties
     */
    public TimelineAnimation(AnimationEditor a, ElementSettings es){
        mySettings = es;
        myAnimation = a;
        setChannels();
    }

    private void setChannels() {
        myChannels = new ArrayList();
        if(myAnimation == null){
            return;
        }
        for (ChannelEditor c : myAnimation.getChildren()) {
            TimelineChannel tlc = new TimelineChannel(c, mySettings);
            c.addConsumer(tlc);
            myChannels.add(tlc);
        }
    }

    @Override
    public boolean contains(int x, int y, double distance) {
        for(TimelineChannel channel : myChannels){
            if(channel.contains(x, y, distance)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {
        if (myChannels.isEmpty()) {
            return;
        }
        TimelineChannel selected = getSelected();
        for(TimelineChannel channel : myChannels){
            if(channel != selected){
                channel.paint(g);
            }
        }
        if(selected != null){
            selected.paint(g);
        }
    }

    /**
     *
     * @return
     */
    public TimelineChannel getSelected(){
        int i = myAnimation.getSelectedIndex();
        if(i < 0 || i >= myChannels.size()){
            return null;
        }
        return myChannels.get(i);
    }

    /**
     *
     * @return
     */
    public List<TimelineChannel> getChannels(){
        return myChannels;
    }

    @Override
    public void selectionChanged(Object invoker, Object controller, int oldIndex, int newIndex) {
        setSelect(oldIndex, false);
        setSelect(newIndex, true);
    }
    
    private void setSelect(int i, boolean val){
        if(i < 0 || i >= myChannels.size()){
            return;
        }
        TimelineChannel child = myChannels.get(i);
        if(child != null && child.getController() != null){
            AbstractEditor ae = child.getController();
            ae.setState(this, EditState.SELECTED, val, ae.getSharedHistory());
        }
    }

    @Override
    public void itemAdded(Object invoker, Object controller, int index) {
        ChannelEditor c = myAnimation.getChildren().get(index);
        TimelineChannel tlc = new TimelineChannel(c, mySettings);
        c.addConsumer(tlc);
        myChannels.add(index, tlc);
    }

    @Override
    public void itemRemoved(Object invoker, Object controller, int index) {
        myChannels.remove(index);
    }

    @Override
    public void itemMoved(Object invoker, Object controller, int oldIndex, int newIndex) {
        TimelineChannel channel = myChannels.remove(oldIndex);
        myChannels.add(newIndex, channel);
    }

    @Override
    public AnimationEditor getController(){
        return myAnimation;
    }

    @Override
    public void stateChanged(Object invoker, Object controller, EditState state, boolean value){}

    @Override
    public void structureChanged(Object invoker, Object controller){}
}
