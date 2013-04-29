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

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.robokind.api.animation.compiled.CompiledPath;
import org.robokind.api.animation.editor.AbstractEditor;
import org.robokind.api.animation.editor.EditState;
import org.robokind.api.animation.editor.MotionPathEditor;
import org.robokind.api.animation.editor.EditorListener;
import org.robokind.api.animation.editor.ControlPointEditor;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.animation.ElementSettings;
import org.rwshop.swing.animation.config.PathProperties.PathConfigRenderer;
import org.rwshop.swing.animation.EditorElement;
import org.rwshop.swing.animation.config.PathProperties;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class TimelineMotionPath extends EditorListener implements EditorElement {
    private MotionPathEditor myController;
    private List<TimelineControlPoint> myControlPoints;
    private ElementSettings mySettings;
    private PathProperties myPathProperties;

    /**
     *
     * @param mp
     * @param es
     * @param properties
     */
    public TimelineMotionPath(MotionPathEditor mp, ElementSettings es, PathProperties pathProps){
        mySettings = es;
        myController = mp;
        myPathProperties = pathProps;
        setControlPoints();
    }

    private void setControlPoints() {
        if(myControlPoints != null){
            for(TimelineControlPoint tcp : myControlPoints){
                tcp.getController().clearConsumers();
            }
        }
        myControlPoints = new ArrayList<TimelineControlPoint>();
        for (ControlPointEditor p : myController.getChildren()) {
            TimelineControlPoint tcp = new TimelineControlPoint(p, mySettings);
            p.addConsumer(tcp);
            myControlPoints.add(tcp);
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
            if(Point2D.Double.distance(x, y, i, getY(i,cp)) <= distance){
                return true;
            }
        }
        return false;
    }
    
    private int getY(int x, CompiledPath cp){
        CoordinateScalar scalar = mySettings == null ? null : mySettings.getScalar();
        if(scalar == null){
            return -1;
        }
        double lT = scalar.unscaleX(x);
        double lP = cp.estimatePosition((long)lT);
        return (int)scalar.scaleY(lP);
    }

    private void drawControlPoints(Graphics g) {
        int i = 0;
        for (TimelineControlPoint cp : myControlPoints) {
            /*if(myController.getSelectedIndex() == i){
                cp.getController().setState(this, EditState.SELECTED, true, null); //selected
            }*/
            cp.paint(g);
            i++;
        }
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g){
        if (myControlPoints.isEmpty()) {
            return;
        }
        CoordinateScalar scalar = mySettings == null ? null : mySettings.getScalar();
        if(scalar == null){
            return;
        }
        PathConfigRenderer renderer = mySettings == null ? null : mySettings.getPathRenderer();
        if(renderer == null){
            return;
        }
        if(myController.hasFlag(EditState.VISIBLE)){
            boolean drawControls = myController.hasFlag(EditState.SELECTED) && !myController.isLocked();
            if(drawControls){
                drawControlLines(g);
            }
            renderer.paint(g, myController.getInterpolatedPoints(), myController.getInheritedFlags(), scalar, myPathProperties);
            if(drawControls){
                drawControlPoints(g);
            }
        }else if(myController.hasFlag(EditState.SELECTED)){
            renderer.paint(g, myController.getInterpolatedPoints(), scalar, "outline", null);
        }
        if(myController.getTempPath() != null){
            renderer.paint(g, myController.getTempPath().getInterpolatedPoints(), scalar, "outline", null);
        }
    }

    /**
     *
     * @param g
     */
    public void drawControlLines(Graphics g){
        CoordinateScalar scalar = mySettings == null ? null : mySettings.getScalar();
        if(scalar == null){
            return;
        }
        PathConfigRenderer renderer = mySettings == null ? null : mySettings.getPathRenderer();
        if(renderer == null){
            return;
        }
        renderer.paint(g, myController.getControlPoints(), scalar, "controllines", null);
    }

    /**
     *
     * @return
     */
    public List<TimelineControlPoint> getControlPoints(){
        return myControlPoints;
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
        if(i < 0 || i >= myControlPoints.size()){
            return;
        }
        TimelineControlPoint child = myControlPoints.get(i);
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
        ControlPointEditor p = myController.getChild(index);
        TimelineControlPoint cp = new TimelineControlPoint(p, mySettings);
        p.addConsumer(cp);
        myControlPoints.add(index,cp);
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    @Override
    public void itemRemoved(Object invoker, Object controller, int index) {
        myControlPoints.remove(index);
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
        TimelineControlPoint p = myControlPoints.remove(oldIndex);
        p.setPoint(myController.getChild(newIndex));
        myControlPoints.add(newIndex, p);
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param state
     * @param value
     */
    @Override
    public void stateChanged(Object invoker, Object controller, EditState state, boolean value){
         if(state == EditState.SELECTED){
        }
    }

    /**
     *
     * @return
     */
    @Override
    public MotionPathEditor getController(){
        return myController;
    }

    /**
     *
     * @param invoker
     * @param controller
     */
    @Override
    public void structureChanged(Object invoker, Object controller){
        //setControlPoints();
    }
    
    public static int findControlPoint(MotionPathEditor editor, int x, int y, CoordinateScalar s, double distance){
        if(editor == null || s == null){
            return -1;
        }
        List<Point2D> points = editor.getControlPoints();
        for(int i=0; i<points.size(); i++){
            Point2D p = points.get(i);
            if(Point2D.Double.distance(s.scaleX(p), s.scaleY(p), x, y) <= distance){
                return i;
            }
        }
        return -1;
    }
}