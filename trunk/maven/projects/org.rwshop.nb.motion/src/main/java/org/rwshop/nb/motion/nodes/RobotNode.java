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
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Sheet;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.mechio.api.motion.Robot;
import org.mechio.api.motion.Joint;
import org.mechio.api.motion.servos.ServoController;
import org.mechio.api.motion.servos.config.ServoRobotConfig;

/**
 * 
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class RobotNode extends AbstractNode implements PropertyChangeListener{

    public RobotNode(Robot dev) {
        super(new RobotNodeChildren(dev), Lookups.singleton(dev));
        if(dev == null){
            throw new NullPointerException("Cannot create RobotNode with null Robot.");
        }
        setDisplayName(dev.getRobotId().toString());
        //dev.addPropertyChangeListener(WeakListeners.propertyChange(this, dev));
    }

    @Override
    public String getHtmlDisplayName() {
        Robot dev = getLookup().lookup(Robot.class);
        if (dev != null) {
            return "<font color='!textText'>" + getName() +
                    " </font><font color='!controlShadow'><i>(" +
                    dev.getClass().getSimpleName() + ")</i></font>";
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

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        try {
            set.put(getProperties());
        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }
        sheet.put(set);
        Node node = getChildren().findChild(ServoRobotConfig.class.getName());
        if(node != null){
            RobotConfigNode confNode = (RobotConfigNode)node;
            Sheet.Set[] sets = confNode.getPropertySheetSets();
            for(Sheet.Set confSet : sets){
                sheet.put(confSet);
            }
        }
        return sheet;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}

    public static class RobotNodeChildren extends Children.Keys {
        private Robot myRobot;

        public RobotNodeChildren(Robot robot){
            myRobot = robot;
        }

        @Override
        protected void addNotify(){
            super.addNotify();
            List keys = new LinkedList();
            //keys.add(myRobot.getConfig());
            //keys.addAll(myRobot.getControllers());
            keys.addAll(myRobot.getJointList());
            setKeys(keys);
        }

        @Override
        protected Node[] createNodes(Object key) {
            if(key instanceof ServoRobotConfig){
                return new Node[]{new RobotConfigNode((ServoRobotConfig)key)};
            }else if(key instanceof ServoController){
                return new Node[]{new JointControllerNode((ServoController)key)};
            }else if(key instanceof Joint){
                return new Node[]{new JointNode((Joint)key)};
            }
            return null;
        }
    }
}
