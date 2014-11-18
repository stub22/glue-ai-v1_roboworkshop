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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.editor.features.AnimationTimeRange;
import org.rwshop.swing.animation.menus.MenuProvider;
import org.rwshop.swing.animation.timeline.TimelineComponent;
import org.rwshop.swing.common.DrawableLayer;
import org.rwshop.swing.common.scaling.CoordinateScalar;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class TimeRangeComponent implements 
        TimelineComponent, PropertyChangeListener{
    private AnimationTimeRange myTimeRange;
    private TimeRangeLayer myLayer;
    private TimeRangeMenuProvider myMenuProvider;
    private JPanel myPanel;
    private CoordinateScalar myScalar;
    
    public TimeRangeComponent(){
        myTimeRange = new AnimationTimeRange();
        myLayer = new TimeRangeLayer();
        myMenuProvider = new TimeRangeMenuProvider();
        myLayer.setAnimationTimeRange(myTimeRange);
        myMenuProvider.setAnimationTimeRange(myTimeRange);
        myTimeRange.addPropertyChangeListener(this);
    }
    
    @Override
    public void setPanel(JPanel panel){
        myPanel = panel;
    }
    
    @Override
    public void setEditor(AnimationEditor editor){
        myTimeRange.setEditor(editor);
    }
    
    @Override
    public void setScalar(CoordinateScalar scalar){
        myScalar = scalar;
        myMenuProvider.setScalar(scalar);
        myLayer.setScalar(scalar);
    }

    @Override
    public MenuProvider getMenuProvider() {
        return myMenuProvider;
    }

    @Override
    public DrawableLayer getDrawLayer() {
        return myLayer;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(myPanel == null){
            return;
        }
        String name = evt.getPropertyName();
        if(name.equals(AnimationTimeRange.PROP_START_TIME) ||
                name.equals(AnimationTimeRange.PROP_STOP_TIME)){
            SwingUtilities.invokeLater(new Runnable() {
                 @Override
                public void run() {
                     myPanel.repaint();
                }
            });
        }
    }
}
