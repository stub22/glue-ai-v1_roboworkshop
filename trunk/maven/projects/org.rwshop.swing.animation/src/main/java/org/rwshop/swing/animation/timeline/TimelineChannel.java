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

package org.rwshop.swing.animation.timeline;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.robokind.api.animation.compiled.CompiledPath;
import org.robokind.api.animation.editor.AbstractEditor;
import org.robokind.api.animation.editor.EditState;
import org.robokind.api.animation.editor.ChannelEditor;
import org.robokind.api.animation.editor.MotionPathEditor;
import org.robokind.api.animation.editor.EditorListener;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.animation.ElementSettings;
import org.rwshop.swing.animation.config.PathProperties.PathConfigRenderer;
import org.rwshop.swing.animation.EditorElement;
import org.rwshop.swing.animation.config.StrokeProperties;
import org.rwshop.swing.animation.config.PathProperties;
import org.robokind.api.common.utils.Utils;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class TimelineChannel extends EditorListener implements EditorElement {
    private ChannelEditor myController;
    private List<TimelineMotionPath> myMotionPaths;
    private ElementSettings mySettings;
    private PathProperties myPathProperties;

    /**
     *
     * @param channel
     * @param es
     * @param properties
     */
    public TimelineChannel(ChannelEditor channel, ElementSettings es){
        mySettings = es;
        myController = channel;
        setPathProperties();
        setMotionPaths();
    }

    private void setMotionPaths() {
        myMotionPaths = new ArrayList();
        for (MotionPathEditor c : myController.getChildren()) {
            TimelineMotionPath tlmp = new TimelineMotionPath(c, mySettings, myPathProperties);
            c.addConsumer(tlmp);
            myMotionPaths.add(tlmp);
        }
    }
    
    private void setPathProperties(){
        ChannelEditor editor = getController();
        if(editor == null){
            return;
        }
        Color col = editor.getPrimaryColor();
        if(col == null){
            return;
        }
        if(myPathProperties == null){
            StrokeProperties stroke = new StrokeProperties(1.0f, null, null);
            myPathProperties = new PathProperties(stroke, col, 1.0f);
        }else{
            myPathProperties.setColor(col);
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param distance
     * @return
     */
    @Override
    public boolean contains(int x, int y, double distance) {
        CoordinateScalar scalar = mySettings == null ? null : mySettings.getScalar();
        if(scalar == null){
            return false;
        }
        long stepLength = (long)scalar.unscaleX(distance);
        long start = (long)scalar.unscaleX(x-distance);
        long end = (long)scalar.unscaleX(x+distance);
        CompiledPath cp = myController.getCompiledPath(start, end, stepLength);
        if(cp == null){
            return false;
        }
        for(int i=x-(int)distance; i<=x+distance; i++){
            double cpY = getY(i, cp);
            if(cpY < 0){
                continue;
            }
            if(Point2D.Double.distance(i, cpY, x, y) <= distance){
                return true;
            }
        }
        return false;
    }
    
    public Double getY(long time) {
        long stepLength = 100;
        long start = myController.getStart();
        long end = myController.getEnd();
        start = Utils.bound(time-stepLength, start, end);
        end = Utils.bound(time+stepLength, start, end);
        time = Utils.bound(time, start, end-1);
        CompiledPath cp = myController.getCompiledPath(start, end, stepLength);
        if(cp == null){
            return null;
        }
        return cp.estimatePosition(time);
    }

    private int getY(int x, CompiledPath cp){
        CoordinateScalar scalar = mySettings == null ? null : mySettings.getScalar();
        if(scalar == null){
            return -1;
        }
        double lT = scalar.unscaleX(x);
        double lP = cp.estimatePosition((long)lT);
        if(lP == -1){
            return -1;
        }
        return (int)scalar.scaleY(lP);
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g){
        CoordinateScalar scalar = mySettings == null ? null : mySettings.getScalar();
        if(scalar == null){
            return;
        }
        PathConfigRenderer renderer = mySettings == null ? null : mySettings.getPathRenderer();
        if(renderer == null){
            return;
        }
        if (myController.getChildren().isEmpty()) {
            return;
        }
        if(myController.hasFlag(EditState.VISIBLE)){
            if(myController.hasFlag(EditState.SELECTED)){
                drawMotionPaths(g);
            }else{
                renderer.paint(g, myController.getInterpolatedPoints(), myController.getInheritedFlags(), scalar, myPathProperties);
            }
        }else if(myController.hasFlag(EditState.SELECTED)){
            renderer.paint(g, myController.getInterpolatedPoints(), scalar, "outline", null);
        }
    }

    /**
     *
     * @param g
     */
    public void drawMotionPaths(Graphics g){
        TimelineMotionPath selected = getSelected();
        for(TimelineMotionPath tmp : myMotionPaths){
            if(tmp != selected){
                tmp.paint(g);
            }
        }
        if(selected != null){
            selected.paint(g);
        }
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param oldIndex
     * @param newIndex
     */
    @Override
    public void selectionChanged(Object invoker, Object controller, int oldIndex, int newIndex) {
        setSelect(oldIndex, false);
        setSelect(newIndex, true);
    }
    
    private void setSelect(int i, boolean val){
        if(i < 0 || i >= myMotionPaths.size()){
            return;
        }
        TimelineMotionPath child = myMotionPaths.get(i);
        if(child != null && child.getController() != null){
            AbstractEditor ae = child.getController();
            ae.setState(this, EditState.SELECTED, val, ae.getSharedHistory());
        }
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    @Override
    public void itemAdded(Object invoker, Object controller, int index) {
        MotionPathEditor c = myController.getChildren().get(index);
        TimelineMotionPath tlmp = new TimelineMotionPath(c, mySettings, myPathProperties);
        c.addConsumer(tlmp);
        myMotionPaths.add(index, tlmp);
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    @Override
    public void itemRemoved(Object invoker, Object controller, int index) {
        myMotionPaths.remove(index);
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param oldIndex
     * @param newIndex
     */
    @Override
    public void itemMoved(Object invoker, Object controller, int oldIndex, int newIndex) {
        TimelineMotionPath m = myMotionPaths.remove(oldIndex);
        myMotionPaths.add(newIndex, m);
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param state
     * @param value
     */
    @Override
    public void stateChanged(Object invoker, Object controller, EditState state, boolean value){}

    /**
     *
     * @return
     */
    public TimelineMotionPath getSelected(){
        int i = myController.getSelectedIndex();
        if(i < 0 || i >= myMotionPaths.size()){
            return null;
        }
        return myMotionPaths.get(i);
    }

    /**
     *
     * @return
     */
    @Override
    public ChannelEditor getController(){
        return myController;
    }

    /**
     *
     * @return
     */
    public List<TimelineMotionPath> getMotionPaths(){
        return myMotionPaths;
    }

    /**
     *
     * @param invoker
     * @param controller
     */
    @Override
    public void structureChanged(Object invoker, Object controller){
        setPathProperties();
    }
}
