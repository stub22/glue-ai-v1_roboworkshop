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
import javax.swing.Action;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.robokind.api.motion.Joint;

/**
 * 
 * @author Matthew Stevenson <www.robokind.org>
 */
public class JointNode extends AbstractNode implements PropertyChangeListener{
    
    public JointNode(Joint joint) {
        super(Children.LEAF, Lookups.singleton(joint));
        if(joint == null){
            throw new NullPointerException("Cannot create JointNode with null Joint.");
        }
        setDisplayName(joint.getName());
        joint.addPropertyChangeListener(WeakListeners.propertyChange(this, joint));
    }

    @Override
    public String getHtmlDisplayName() {
        Joint joint = getLookup().lookup(Joint.class);
        if (joint != null) {
            return "<font color='!textText'>" + joint.getName() + 
                    " </font><font color='!controlShadow'><i>(" 
                    + joint.getClass().getSimpleName() + ")</i></font>";
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
        Joint obj = getLookup().lookup(Joint.class);
        Property goalPosProp = new PropertySupport.Reflection(obj, Double.class, Joint.PROP_GOAL_POSITION);
        goalPosProp.setName("Goal Position");
        Property enabledProp = new PropertySupport.Reflection(obj, Boolean.class, Joint.PROP_ENABLED);
        enabledProp.setName("Enabled");
        return new Property[]{goalPosProp, enabledProp};
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
        set.setName("Joint Properties");
        set.setDisplayName("Joint Properties");
        sheet.put(set);
        /*if(node != null){
            JointConfigNode confNode = (JointConfigNode)node;
            try{
                Property[] props = confNode.getProperties();
                Sheet.Set pSet = Sheet.createPropertiesSet();
                pSet.put(props);
                pSet.setName("Configuration Properties");
                pSet.setDisplayName("Configuration Properties");
                sheet.put(pSet);
            }catch(NoSuchMethodException ex){
                ex.printStackTrace();
            }
        }*/
        return sheet;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /*if (JointConfig.PROP_NAME.equals(evt.getPropertyName())) {
            this.fireDisplayNameChange(null, getDisplayName());
        }*/
    }

    /*public static class JointChildren extends Children.Keys<JointConfig> {
        private Joint myJoint;

        public JointChildren(Joint joint){
            myJoint = joint;
        }

        @Override
        protected void addNotify(){
            super.addNotify();
            setKeys(new JointConfig[]{myJoint.getConfig()});
        }

        @Override
        protected Node[] createNodes(JointConfig key) {
            return new JointConfigNode[]{new JointConfigNode(key)};
        }
    }*/
}
