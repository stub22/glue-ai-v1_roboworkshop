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

package org.rwshop.swing.motion.jointgroup;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.mechio.api.motion.Joint;
import org.mechio.api.motion.jointgroup.JointGroup;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class JointGroupTreeModel implements TreeModel{
    private JointGroup myJointGroup;
    
    public JointGroupTreeModel(JointGroup group){
        myJointGroup = group;
    }
    
    @Override
    public Object getRoot() {
        return myJointGroup;
    }

    @Override
    public Object getChild(Object parent, int i) {
        if(!(parent instanceof JointGroup)){
            return null;
        }
        JointGroup group = (JointGroup)parent;
        int jointCount = group.getJointCount();
        if(i < jointCount){
            Joint j = group.getJoint(i);
            if(j == null){
                return group.getJointId(i);
            }
            return group.getJoint(i);
        }
        i -= jointCount;
        if(i > group.getGroupCount()){
            return null;
        }
        return group.getJointGroups().get(i);
    }

    @Override
    public int getChildCount(Object parent) {
        if(!(parent instanceof JointGroup)){
            return 0;
        }
        JointGroup group = (JointGroup)parent;
        return group.getJointCount() + group.getGroupCount();
    }

    @Override
    public boolean isLeaf(Object o) {
        if(!(o instanceof JointGroup)){
           return true; 
        }
        JointGroup group = (JointGroup)o;
        if(group.getJointCount() == 0 && group.getGroupCount() == 0){
            return true;
        }
        return false;
    }

    @Override
    public void valueForPathChanged(TreePath tp, Object o) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if(!(parent instanceof JointGroup)){
            return -1;
        }
        JointGroup group = (JointGroup)parent;
        if(child instanceof Integer){
            return group.getJointIds().indexOf((Integer)child);
        }else if(child instanceof JointGroup){
            int index = group.getJointGroups().indexOf((JointGroup)child);
            if(index == -1){
                return -1;
            }
            int joints = group.getJointCount();
            return joints + index;
        }
        return -1;
    }

    
    @Override
    public void addTreeModelListener(TreeModelListener tl) {}

    @Override
    public void removeTreeModelListener(TreeModelListener tl) {}
    
}
