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

package org.rwshop.nb.motion.nodes;

import java.awt.Image;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Sheet;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.robokind.api.motion.servos.config.ServoRobotConfig;

/**
 * 
 * @author Matthew Stevenson <www.robokind.org>
 */
public class RobotConfigNode extends AbstractNode{

    public RobotConfigNode(ServoRobotConfig config) {
        super(Children.LEAF, Lookups.singleton(config));
        if(config == null){
            throw new NullPointerException("Cannot create RobotConfigNode with null ServoRobotConfig.");
        }
        setName(ServoRobotConfig.class.getName());
        setDisplayName(config.getRobotId().toString());
    }

    @Override
    public String getHtmlDisplayName() {
        ServoRobotConfig obj = getLookup().lookup(ServoRobotConfig.class);
        if (obj != null) {
            return "<font color='!textText'>" + obj.getRobotId().toString() +
                    " </font><font color='!controlShadow'><i>(" +
                    obj.getClass().getSimpleName() + ")</i></font>";
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
        return new Property[]{};
    }

    protected Sheet.Set[] getPropertySheetSets(){
        ServoRobotConfig obj = getLookup().lookup(ServoRobotConfig.class);
        /*try{
            Sheet.Set versionSet = VersionPropertySheet.getVersionPropertySheetSet("Version", "Servo Robot Config", obj.getVersion());
            return new Sheet.Set[]{versionSet};
        }catch(NoSuchMethodException ex){}*/
        return new Sheet.Set[0];
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

    public static class RobotConfigChildren extends Children.Keys {
        private ServoRobotConfig myConfig;

        public RobotConfigChildren(ServoRobotConfig config){
            myConfig = config;
        }

        @Override
        protected void addNotify(){
            super.addNotify();/*
            List keys = new LinkedList();
            keys.addAll(myConfig.getServoControllerConfigs());
            keys.addAll(myConfig.getJointConfigs().values());
            setKeys(keys);*/
        }

        @Override
        protected Node[] createNodes(Object key) {/*
            if(key instanceof ServoControllerConfig){
                return new Node[]{new ControllerConfigNode((ServoControllerConfig)key)};
            }else if(key instanceof JointConfig){
                return new Node[]{new JointConfigNode((JointConfig)key)};
            }*/
            return null;
        }
    }
}
