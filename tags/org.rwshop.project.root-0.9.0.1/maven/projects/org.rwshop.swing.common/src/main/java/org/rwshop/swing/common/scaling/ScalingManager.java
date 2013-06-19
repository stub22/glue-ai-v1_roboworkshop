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

import org.rwshop.swing.common.scaling.ScalableComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class ScalingManager {
    private List<ScalableComponent> myComponents;
    private CoordinateScalar myScalar;
    private ScalarListener myScalarListener;
    
    public ScalingManager(CoordinateScalar scalar){
        if(scalar == null){
            throw new NullPointerException();
        }
        myScalar = scalar;
        myScalarListener = new ScalarListener();
        myScalar.addPropertyChangeListener(myScalarListener);
        myComponents = new ArrayList<ScalableComponent>();
    }
    
    public CoordinateScalar getScalar(){
        return myScalar;
    }
    
    public void rescaleComponents(){
        int maxWidth = 0;
        int maxHeight = 0;
        for(ScalableComponent component : myComponents){
            maxWidth = Math.max(maxWidth,component.getMinWidth());
            maxHeight = Math.max(maxHeight,component.getMinHeight());
        }
        for(ScalableComponent component : myComponents){
            component.rescale(maxWidth, maxHeight);
        }
    }
    
    public void setFocusPosition(Integer x, Integer y){
        for(ScalableComponent component : myComponents){
            component.setFocusPosition(x, y);
        }
    }
    
    public void addComponent(ScalableComponent component){
        if(component == null){
            throw new NullPointerException();
        }
        if(myComponents.contains(component)){
            return;
        }
        myComponents.add(component);
        component.setScalar(myScalar);
        rescaleComponents();
    }
    
    public boolean removeComponent(ScalableComponent component, CoordinateScalar scalar){
        if(component == null || scalar == null){
            throw new NullPointerException();
        }else if(myScalar.equals(scalar)){
            throw new IllegalArgumentException(
                    "Cannot reuse LinkScalingManager's CoordinateScalar.  "
                    + "A different one must be provided.");
        }
            
        if(!myComponents.remove(component)){
            return false;
        }
        component.setScalar(scalar);
        rescaleComponents();
        return true;
    }
    
    private class ScalarListener implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(!CoordinateScalar.PROP_SCALE_X.equals(evt.getPropertyName())){
                return;
            }
            rescaleComponents();
        }
    }
}
