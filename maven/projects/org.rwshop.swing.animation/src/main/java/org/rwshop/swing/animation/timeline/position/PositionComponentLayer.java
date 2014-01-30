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

package org.rwshop.swing.animation.timeline.position;

import java.awt.Color;
import java.awt.Graphics;
import org.rwshop.swing.common.DrawableLayer;
import org.rwshop.swing.common.scaling.CoordinateScalar;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class PositionComponentLayer implements DrawableLayer{
    private PositionSource myPositionSource;
    private CoordinateScalar myScalar;
    private Color myEnabledColor;
    private Color myDisabledColor;
    
    public PositionComponentLayer(){
        myEnabledColor = Color.GREEN;
        myDisabledColor = Color.RED;
    }
    
    public void setPositionSource(PositionSource posSource){
        myPositionSource = posSource;
    }
    
    public void setScalar(CoordinateScalar s){
        myScalar = s;
    }
    
    public void setEnabledColor(Color col){
        if(col == null){
            return;
        }
        myEnabledColor = col;
    }
    
    public void setDisabledColor(Color col){
        if(col == null){
            return;
        }
        myDisabledColor = col;
    }

    @Override
    public void paint(Graphics g) {
        if(myPositionSource == null || myScalar == null){
            return;
        }
        boolean add = myPositionSource.getAddEnabled();
        drawTitle(g, "Add Frames", add, 0);
        boolean edit = myPositionSource.getEditEnabled();
        drawTitle(g, "Move Joints", edit, 1);
        
    }
    
    private void drawTitle(Graphics g, String title, boolean enabled, int order){
        Color col = enabled ? myEnabledColor : myDisabledColor;
        int y = (order+1)*18;
        g.setColor(col);
        g.drawString(title, 20, y);
    }
}
