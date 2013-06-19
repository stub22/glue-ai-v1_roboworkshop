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

package org.rwshop.swing.motion.jointgroup;

import java.util.List;
import org.netbeans.swing.outline.RowModel;
import org.robokind.api.common.position.NormalizedDouble;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.jointgroup.JointGroup;
import org.robokind.api.motion.JointProperty;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class JointTreeRowModel implements RowModel{
    public final static int theColCount = 2;
    private List<String> myProperties;
    
    public JointTreeRowModel(List<String> props){
        myProperties = props;
    }
    
    @Override
    public int getColumnCount() {
        if(myProperties == null){
            return theColCount;
        }
        return theColCount + myProperties.size();
    }

    @Override
    public Object getValueFor(Object node, int column) {
        if(node instanceof JointGroup){
            JointGroup group = (JointGroup)node;
            switch(column){
                case 0 : return group.getEnabled();
                default: return null;
            }
        }
        if(!(node instanceof Joint)){
            return null;
        }
        Joint joint =(Joint)node;
        if(joint == null){
            return null;
        }
        switch(column){
            case 0 : return joint.getEnabled();
            case 1 : return getGoalPos(joint);
        }
        return getPropertyValue(joint, column-theColCount);
    }
    
    private final static int thePropStringLen = 7;
    private Object getPropertyValue(Joint j, int i){
        if(myProperties == null || i < 0 || i >= myProperties.size()){
            return null;
        }
        String name = myProperties.get(i);
        JointProperty prop = j.getProperty(name);
        if(prop == null){
            return null;
        }
        Object cached = prop.getValue();
        if(cached == null){
            return null;
        }
        String ret = cached.toString();
        if(ret.length() > thePropStringLen){
            return ret.substring(0, thePropStringLen-1);
        }
        return ret;
    }
    
    private String getGoalPos(Joint j){
        NormalizedDouble pos = j.getGoalPosition();
        if(pos == null){
            pos = new NormalizedDouble(0.0);
        }
        String ret = pos.toString();
        if(ret.length() > thePropStringLen){
            return ret.substring(0, thePropStringLen-1);
        }
        return ret;
    }

    @Override
    public Class getColumnClass(int column) {
        switch(column){
            case 0 : return Boolean.class;
            case 1 : return String.class;
        }
        int i = column - theColCount;
        if(i < 0 || i >= myProperties.size()){
            return null;
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return column == 0;
    }

    @Override
    public void setValueFor(Object node, int column, Object value) {
        if(column != 0 || node == null || value == null || !(value instanceof Boolean)){
            return;
        }
        Boolean val = (Boolean)value;
        if(node instanceof Joint){
            Joint joint = (Joint)node;
            joint.setEnabled(val);
        }else if(node instanceof JointGroup){
            JointGroup jg = (JointGroup)node;
            jg.setEnabled(val);
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0 : return "Enabled";
            case 1 : return "Goal";
        }
        int i = column - theColCount;
        if(i < 0 || i >= myProperties.size()){
            return null;
        }
        return myProperties.get(i);
    }
    
}
