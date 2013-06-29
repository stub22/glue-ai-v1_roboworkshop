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

package org.rwshop.swing.animation.timeline.range;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import org.robokind.api.animation.editor.features.AnimationTimeRange;
import org.rwshop.swing.animation.menus.UIMenuItem;
import org.rwshop.swing.animation.menus.MenuProvider;
import org.rwshop.swing.common.scaling.CoordinateScalar;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class TimeRangeMenuProvider implements MenuProvider{
    private AnimationTimeRange myTimeRange;
    private CoordinateScalar myScalar;
    
    public TimeRangeMenuProvider(){
    }
    
    public void setAnimationTimeRange(AnimationTimeRange timeRange){
        myTimeRange = timeRange;
    }
    
    public void setScalar(CoordinateScalar scalar){
        myScalar = scalar;
    }
    
    @Override
    public JMenu getContextSubMenu(MouseEvent e) {
        if(myTimeRange == null || myScalar == null || e == null){
            return null;
        }
        int x = e.getX();
        long time = (long)myScalar.unscaleX(x);
        JMenu menu = new JMenu("Start/Stop Times");
        menu.add(getStart(time));
        menu.add(getStop(time));
        UIMenuItem item = getPlay(time);
        if(item != null){
            menu.add(item);
        }
        return menu;
    }
    
    private UIMenuItem getStart(long time){
        ActionListener startAction = myTimeRange.getSetStartAction(time);
        return new UIMenuItem("Set Start Time", startAction);
    }
    
    private UIMenuItem getStop(long time){
        ActionListener startAction = myTimeRange.getSetStopAction(time);
        return new UIMenuItem("Set Stop Time", startAction);
    }
    
    private UIMenuItem getPlay(long time){
        ActionListener startAction = myTimeRange.getPlayAction(time);
        if(startAction == null){
            return null;
        }
        return new UIMenuItem("Play Range", startAction);
    }
    
}
