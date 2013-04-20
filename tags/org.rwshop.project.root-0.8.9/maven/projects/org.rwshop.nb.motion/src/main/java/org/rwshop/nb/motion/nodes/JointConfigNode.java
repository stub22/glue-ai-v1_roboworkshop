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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.robokind.api.motion.servos.config.ServoConfig;

/**
 * 
 * @author Matthew Stevenson <www.robokind.org>
 */
public class JointConfigNode extends AbstractNode implements PropertyChangeListener{
    private final static Logger theLogger =  Logger.getLogger(JointConfigNode.class.getName());

    public JointConfigNode(ServoConfig config) {
        super(Children.LEAF, Lookups.singleton(config));
        if(config == null){
            throw new NullPointerException("Cannot create JointConfigNode with null JointConfig.");
        }
        setName(ServoConfig.class.getName());
        setDisplayName(config.getName() + " (config)");
        config.addPropertyChangeListener(WeakListeners.propertyChange(this, config));
    }

    @Override
    public String getHtmlDisplayName() {
        ServoConfig obj = getLookup().lookup(ServoConfig.class);
        if (obj != null) {
            return "<font color='!textText'>" + obj.getName() + 
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
        ServoConfig obj = getLookup().lookup(ServoConfig.class);
        Property logIdProp = new PropertySupport.Reflection(obj, Integer.class, ServoConfig.PROP_ID);
        logIdProp.setName("Servo Id");
        Property nameProp = new PropertySupport.Reflection(obj, String.class, ServoConfig.PROP_NAME);
        nameProp.setName("Name");
        Property minPosProp = new PropertySupport.Reflection(obj, Integer.class, ServoConfig.PROP_MIN_POSITION);
        minPosProp.setName("Minimum Position");
        Property maxPosProp = new PropertySupport.Reflection(obj, Integer.class, ServoConfig.PROP_MAX_POSITION);
        maxPosProp.setName("Maximum Position");
        Property defPosProp = new PropertySupport.Reflection(obj, Integer.class, ServoConfig.PROP_DEF_POSITION);
        defPosProp.setName("Default Position");
        return new Property[]{logIdProp, nameProp, minPosProp, maxPosProp, defPosProp};
    }

    protected Sheet.Set[] getPropertySheetSets(){
        Sheet.Set set = Sheet.createPropertiesSet();
        try{
            set.put(getProperties());
        }catch(NoSuchMethodException ex){
            theLogger.log(Level.SEVERE, "Error creating PropertySheets.", ex);
        }
        return new Sheet.Set[]{set};
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
        if (ServoConfig.PROP_NAME.equals(evt.getPropertyName())) {
            this.fireDisplayNameChange(null, getDisplayName());
        }
    }
}
