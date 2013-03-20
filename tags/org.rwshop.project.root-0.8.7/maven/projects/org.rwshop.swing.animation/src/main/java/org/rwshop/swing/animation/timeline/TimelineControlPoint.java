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
import org.robokind.api.animation.editor.EditState;
import org.robokind.api.animation.editor.EditorListener;
import org.robokind.api.animation.editor.ControlPointEditor;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.animation.config.PointProperties.PointConfigRenderer;
import org.rwshop.swing.animation.ElementSettings;
import org.rwshop.swing.animation.EditorElement;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class TimelineControlPoint extends EditorListener implements EditorElement {
    private ControlPointEditor myController;
    private ElementSettings mySettings;

    /**
     *
     * @param p
     * @param es
     * @param properties
     */
    public TimelineControlPoint(ControlPointEditor p, ElementSettings es){
        mySettings = es;
        myController = p;
    }
    /**
     *
     * @param p
     */
    public void setPoint(ControlPointEditor p){
        myController = p;
    }
    
    /**
     *
     * @return
     */
    public Integer getX(){
        CoordinateScalar scalar = mySettings == null ? null : mySettings.getScalar();
        if(scalar == null){
            return -1;
        }
        return (int)scalar.scaleX(myController.getSelected());
    }

    /**
     *
     * @return
     */
    public Integer getY(){
        CoordinateScalar scalar = mySettings == null ? null : mySettings.getScalar();
        if(scalar == null){
            return -1;
        }
        return (int)scalar.scaleY(myController.getSelected());
    }

    @Override
    public void paint(Graphics g){
        CoordinateScalar scalar = mySettings == null ? null : mySettings.getScalar();
        if(scalar == null){
            return;
        }
        PointConfigRenderer renderer = mySettings == null ? null : mySettings.getPointRenderer();
        if(renderer == null){
            return;
        }
        renderer.paint(g, myController.getChildren(), myController.getStateFlags(), scalar, null);
    }

    @Override
    public boolean contains(int x, int y, double distance) {
        return Point2D.Double.distance(getX(), getY(), x, y) <= distance;
    }
    @Override
    public ControlPointEditor getController(){
        return myController;
    }
    
    @Override
    public void selectionChanged(Object invoker, Object controller, int oldIndex, int newIndex) {}
    @Override
    public void itemAdded(Object invoker, Object controller, int index) {}
    @Override
    public void itemRemoved(Object invoker, Object controller, int index) {}
    @Override
    public void itemMoved(Object invoker, Object controller, int oldIndex, int newIndex) {}
    @Override
    public void stateChanged(Object invoker, Object controller, EditState state, boolean value) {}
    @Override
    public void structureChanged(Object invoker, Object controller){}
}
