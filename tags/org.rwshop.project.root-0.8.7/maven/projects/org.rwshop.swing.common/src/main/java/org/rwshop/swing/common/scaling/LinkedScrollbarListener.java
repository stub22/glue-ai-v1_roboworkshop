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
package org.rwshop.swing.common.scaling;

import java.awt.Adjustable;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class LinkedScrollbarListener implements AdjustmentListener{
    private int myOrientation;
    private ScalingManager myScalingManager;

    public LinkedScrollbarListener(int orientation, ScalingManager scalingManager){
        myOrientation = orientation;
        myScalingManager = scalingManager;
    }
    
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        int val = e.getValue();
        if(Adjustable.HORIZONTAL == myOrientation){
            myScalingManager.setFocusPosition(val, null);
        }else if(Adjustable.VERTICAL == myOrientation){
            myScalingManager.setFocusPosition(null, val);
        }
    }
    
}
