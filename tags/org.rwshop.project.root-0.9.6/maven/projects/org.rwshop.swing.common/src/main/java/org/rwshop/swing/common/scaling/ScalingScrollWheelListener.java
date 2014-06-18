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

package org.rwshop.swing.common.scaling;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JScrollPane;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 * @author Aamir Syed
 */
public class ScalingScrollWheelListener implements MouseWheelListener {
    private JScrollPane myScrollPane;
    private double myScrollRate;
    private ScalingManager myScalingManager;
    private double myPreviousScrollPosition;

    /**
     *
     * @param panel
     * @param pane
     */
    public void init(JScrollPane pane, ScalingManager scalingManager){
        if(pane == null || scalingManager == null){
            throw new NullPointerException();
        }
        myScrollPane = pane;
        myScrollRate = 0.95;
        myScalingManager = scalingManager;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.isShiftDown()){
            return;
        }
        CoordinateScalar sc = myScalingManager.getScalar();
        double scroll = e.getWheelRotation();
        scroll = Math.pow(myScrollRate, scroll);
        scroll *= sc.getScaleX();
        if(scroll >= CoordinateScalar.MAX_SCALE_AMOUNT || 
                scroll <= CoordinateScalar.MIN_SCALE_AMOUNT){
            return;
        }
        int cursorX = e.getX();
        double val = sc.unscaleX(cursorX);
        double oldVal = myScrollPane.getHorizontalScrollBar().getValue();
        if(((int)myPreviousScrollPosition) == oldVal){
            oldVal = myPreviousScrollPosition;
        }
        double cursorWindowX = cursorX - oldVal;
        
        sc.setScaleX(scroll);
        double newKnobX = sc.scaleX(val) - cursorWindowX;
        myScalingManager.setFocusPosition((int)newKnobX, null);
        myPreviousScrollPosition = newKnobX;
    }
}
