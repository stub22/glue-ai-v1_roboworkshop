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

import org.rwshop.swing.common.scaling.CoordinateScalar;
import java.awt.geom.Point2D;
import org.robokind.api.common.property.PropertyChangeNotifier;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class DefaultCoordinateScalar 
        extends PropertyChangeNotifier implements CoordinateScalar{
    private double myScaleX, myScaleY;
    private double myXOffset, myYOffset;
    private boolean myInvertY;

    /**
     *
     */
    public DefaultCoordinateScalar(){
        myScaleX = 1.0;
        myScaleY = 1.0;
        myInvertY = true;
        myYOffset = 0;
        myXOffset = 0;
    }

    /**
     *
     * @param x
     * @param y
     * @param i
     */
    public DefaultCoordinateScalar(double x, double y, boolean i){
        myScaleX = x;
        myScaleY = y;
        myInvertY = i;
        myYOffset = 0;
        myXOffset = 0;
    }
    
    @Override
    public double getScaleX(){
        return myScaleX;
    }
    
    @Override
    public void setScaleX(double s){
        if(s <= 0){
            return;
        }
        double oldVal = myScaleX;
        myScaleX = Math.min(s, MAX_SCALE_AMOUNT);
        firePropertyChange(PROP_SCALE_X, oldVal, myScaleX);
    }
    
    @Override
    public void setXOffset(double val){
        double oldVal = myXOffset;
        myXOffset = val;
        firePropertyChange(PROP_OFFSET_X, oldVal, myXOffset);
    }
    
    @Override
    public double getScaleY(){
        return myScaleY;
    }
    
    @Override
    public void setScaleY(double s){
        if(s <= 0){
            return;
        }
        double oldVal = myScaleY;
        myScaleY = s;
        firePropertyChange(PROP_SCALE_Y, oldVal, myScaleY);
    }
    
    @Override
    public void setYOffset(double val){
        double oldVal = myYOffset;
        myYOffset = val;
        firePropertyChange(PROP_OFFSET_Y, oldVal, myYOffset);
    }
    
    @Override
    public double scaleX(double x){
        return (int)(x*myScaleX + myXOffset);
    }
    
    @Override
    public double scaleX(Point2D p){
        return scaleX(p.getX());
    }
    
    @Override
    public double scaleY(double y){
        if(myInvertY){
            y = 1.0-y;
        }
        return (int)(y*myScaleY + myYOffset);
    }
    
    @Override
    public double scaleY(Point2D p){
        return scaleY(p.getY());
    }
    
    @Override
    public double unscaleX(double x){
        return (x - myXOffset)/myScaleX;
    }
    
    @Override
    public double unscaleX(Point2D p){
        return unscaleX(p.getX());
    }

    @Override
    public double unscaleY(double y){
        double dY = (y - myYOffset)/myScaleY;
        if(myInvertY){
            dY = 1.0-dY;
        }
        return dY;
    }
    
    @Override
    public double unscaleY(Point2D p){
        return unscaleY(p.getY());
    }
}
