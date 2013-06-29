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
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.servos.ServoController;
import org.robokind.api.motion.servos.config.ServoControllerConfig;

/**
 * 
 * @author Matthew Stevenson <www.robokind.org>
 */
public class JointControllerNode extends AbstractNode implements PropertyChangeListener{

    public JointControllerNode(ServoController controller) {
        super(new JointControllerChildren(controller), Lookups.singleton(controller));
        if(controller == null){
            throw new NullPointerException("Cannot create JointNode with null Joint.");
        }
        ServoControllerConfig config = controller.getConfig();
        setDisplayName(config.getServoControllerId().toString());
        controller.addPropertyChangeListener(WeakListeners.propertyChange(this, controller));
    }

    @Override
    public String getHtmlDisplayName() {
        ServoController controller = getLookup().lookup(ServoController.class);
        if (controller != null) {
            ServoControllerConfig conf = controller.getConfig();
            return "<font color='!textText'>" + conf.getServoControllerId() + 
                    " </font><font color='!controlShadow'><i>(" +
                    controller.getClass().getSimpleName() + ")</i></font>";
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
        ServoController obj = getLookup().lookup(ServoController.class);
        if(obj == null){
            return new Property[0];
        }
        Property enabledProp = new PropertySupport.Reflection(obj, Boolean.class, Joint.PROP_ENABLED);
        enabledProp.setName("Enabled");
        return new Property[]{enabledProp};
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
        set.setName("JointController Properties");
        set.setDisplayName("JointController Properties");
        sheet.put(set);
        Node node = getChildren().findChild(ServoControllerConfig.class.getName());
        if(node != null){
            ControllerConfigNode confNode = (ControllerConfigNode)node;
            Sheet.Set[] sets = confNode.getPropertySheetSets();
            for(Sheet.Set confSet : sets){
                sheet.put(confSet);
            }
        }
        return sheet;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /*if (ServoControllerConfig.PROP_VERSION.equals(evt.getPropertyName())) {
            this.fireDisplayNameChange(null, getDisplayName());
        }*/
    }

    public static class JointControllerChildren extends Children.Keys{
        private ServoController myController;

        public JointControllerChildren(ServoController controller){
            myController = controller;
        }

        @Override
        protected void addNotify(){
            super.addNotify();
            List keys = new LinkedList();
            Object conf = myController.getConfig();
            if(conf != null){
                keys.add(myController.getConfig());
            }
            /*List joints = myController.getJoints();
            if(joints != null && !joints.isEmpty()){
                for(Object j : joints){
                    if(j == null){
                        continue;
                    }
                    keys.add(j);
                }
            }*/
            setKeys(keys);
        }

        @Override
        protected Node[] createNodes(Object key) {
            if(key instanceof Joint){
                return new Node[]{new JointNode((Joint)key)};
            }else if(key instanceof ServoControllerConfig){
                return new Node[]{new ControllerConfigNode((ServoControllerConfig)key)};
            }
            return null;
        }
    }
}
