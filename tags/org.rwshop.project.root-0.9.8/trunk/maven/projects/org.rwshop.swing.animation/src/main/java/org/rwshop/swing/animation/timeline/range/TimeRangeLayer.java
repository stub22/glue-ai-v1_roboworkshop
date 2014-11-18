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

package org.rwshop.swing.animation.timeline.range;

import java.awt.Color;
import java.awt.Graphics;
import org.mechio.api.animation.editor.features.AnimationTimeRange;
import org.rwshop.swing.common.DrawableLayer;
import org.rwshop.swing.common.scaling.CoordinateScalar;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class TimeRangeLayer implements DrawableLayer{
    private AnimationTimeRange myTimeRange;
    private CoordinateScalar myScalar;
    private Color myStartColor;
    private Color myStopColor;
    
    public TimeRangeLayer(){
        myStartColor = Color.GREEN;
        myStopColor = Color.RED;
    }
    
    public void setAnimationTimeRange(AnimationTimeRange startStop){
        myTimeRange = startStop;
    }
    
    public void setScalar(CoordinateScalar s){
        myScalar = s;
    }
    
    public void setStartColor(Color col){
        if(col == null){
            return;
        }
        myStartColor = col;
    }
    
    public void setStopColor(Color col){
        if(col == null){
            return;
        }
        myStopColor = col;
    }

    @Override
    public void paint(Graphics g) {
        if(myTimeRange == null || myScalar == null){
            return;
        }
        Long start = myTimeRange.getStartTime();
        Long stop = myTimeRange.getStopTime();
        
        drawLine(g, start, myStartColor);
        drawLine(g, stop, myStopColor);
    }
    
    private void drawLine(Graphics g, Long time, Color col){
        if(time == null){
            return;
        }
        int x = (int)myScalar.scaleX(time);
        int y0 = (int)myScalar.scaleY(0.0);
        int y1 = (int)myScalar.scaleY(1.0);
        g.setColor(col);
        g.drawLine(x, y0, x, y1);
    }
}
