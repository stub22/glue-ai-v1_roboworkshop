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

package org.rwshop.nb.motion.nodes;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Sheet;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.mechio.api.motion.servos.config.ServoControllerConfig;
import org.mechio.api.motion.servos.config.ServoConfig;
import org.rwshop.nb.common.VersionPropertySheet;

/**
 * 
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class ControllerConfigNode extends AbstractNode implements PropertyChangeListener{

    public ControllerConfigNode(ServoControllerConfig config) {
        super(new ControllerConfigChildren(config), Lookups.singleton(config));
        if(config == null){
            throw new NullPointerException("Cannot create ControllerConfigNode with null ControllerConfig.");
        }
        setName(ServoControllerConfig.class.getName());
        setDisplayName(config.getServoControllerId().toString());
        config.addPropertyChangeListener(WeakListeners.propertyChange(this, config));
    }

    @Override
    public String getHtmlDisplayName() {
        ServoControllerConfig conf = getLookup().lookup(ServoControllerConfig.class);
        if (conf != null) {
            return "<font color='!textText'>" + 
                    conf.getServoControllerId().toString() + 
                    " </font><font color='!controlShadow'><i>(" +
                    conf.getClass().getSimpleName() + ")</i></font>";
        } else {
            return null;
        }
    }

    @Override
    public Image getIcon(int type) {
        return Utilities.loadImage("org/myorg/myeditor/icon.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{};
    }

    protected Property[] getProperties() throws NoSuchMethodException{
        ServoConfig obj = getLookup().lookup(ServoConfig.class);
        return new Property[]{};
    }

    protected Sheet.Set[] getPropertySheetSets(){
        ServoControllerConfig conf = getLookup().lookup(ServoControllerConfig.class);
        if(conf == null){
            return new Sheet.Set[0];
        }
        List<Sheet.Set> sets = new ArrayList(2);
        try{
            Sheet.Set versionSet = VersionPropertySheet.getVersionPropertySheetSet("Version", "Joint Config", conf);
            sets.add(versionSet);
        }catch(NoSuchMethodException ex){}
        try{
            Sheet.Set controllerTypSet = VersionPropertySheet.getVersionPropertySheetSet("ControllerTypeVersion", "Controller Type", conf);
            sets.add(controllerTypSet);
        }catch(NoSuchMethodException ex){}
        return sets.toArray(new Sheet.Set[0]);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set[] sets = getPropertySheetSets();
        for(Sheet.Set set : sets){
            sheet.put(set);
        }
        return sheet;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    public static class ControllerConfigChildren extends Children.Keys<ServoConfig>{
        private ServoControllerConfig myConfig;

        public ControllerConfigChildren(ServoControllerConfig controller){
            myConfig = controller;
        }

        @Override
        protected void addNotify(){
            super.addNotify();
            setKeys(myConfig.getServoConfigs().values());
        }

        @Override
        protected Node[] createNodes(ServoConfig key) {
            return new JointConfigNode[]{new JointConfigNode(key)};
        }
    }
}
