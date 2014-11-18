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

import javax.swing.JPanel;
import org.osgi.framework.BundleContext;
import org.mechio.api.animation.editor.AnimationEditor;
import org.rwshop.swing.animation.menus.MenuProvider;
import org.rwshop.swing.animation.menus.UIMenuItem;
import org.rwshop.swing.animation.timeline.TimelineComponent;
import org.rwshop.swing.common.DrawableLayer;
import org.rwshop.swing.common.scaling.CoordinateScalar;

import static org.jflux.api.common.rk.localization.Localizer.$;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class PositionSourceComponent implements TimelineComponent {
    private PositionSource myPositionSource;
    private PositionComponentLayer myLayer;
    private PositionSourceMenuProvider myMenuProvider;
    private JPanel myPanel;
    private CoordinateScalar myScalar;
    private PositionSourceToggle myAddToggle;
    private PositionSourceToggle myEditToggle;
    
    public PositionSourceComponent(BundleContext context){
        myPositionSource = new PositionSource(context);
        myLayer = new PositionComponentLayer();
        myMenuProvider = new PositionSourceMenuProvider();
        myLayer.setPositionSource(myPositionSource);
        
        myAddToggle = new PositionSourceToggle(myPositionSource, true);
        myEditToggle = new PositionSourceToggle(myPositionSource, false);
        
        myMenuProvider.addMenuItem(
                new UIMenuItem($("add.frames"), myAddToggle));
        myMenuProvider.addMenuItem(
                new UIMenuItem($("move.joints"), myEditToggle));
    }
        
    @Override
    public void setPanel(JPanel panel){
        myPanel = panel;
        myAddToggle.setPanel(panel);
        myEditToggle.setPanel(panel);
    }
    
    public PositionSource getPositionSource(){
        return myPositionSource;
    }
    
    @Override
    public void setEditor(AnimationEditor editor){
        myPositionSource.setAnimationEditor(editor);
    }
    
    @Override
    public void setScalar(CoordinateScalar scalar){
        myScalar = scalar;
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
    
}
