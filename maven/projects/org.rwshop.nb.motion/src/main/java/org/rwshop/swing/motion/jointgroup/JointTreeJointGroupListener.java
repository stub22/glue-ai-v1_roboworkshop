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

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeModel;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;
import org.netbeans.swing.outline.RowModel;
import org.mechio.api.motion.Joint;
import org.mechio.api.motion.jointgroup.JointGroup;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class JointTreeJointGroupListener implements PropertyChangeListener{
    private Outline myJointTree;
    private JointGroup<?,JointGroup,Joint> myJointGroup;
    int myRow;
    private List<JointTreeJointListener> myJointListeners;
    private List<JointTreeJointGroupListener> myGroupListeners;
    private List<String> myPropNames;
    
    public static JointTreeJointGroupListener buildListenerTree(
            JointGroup rootGroup, Outline jointTree, List<String> propNames){
        JointTreeJointGroupListener groupListener = 
                new JointTreeJointGroupListener(rootGroup, jointTree, propNames);
        groupListener.setRow(0);
        return groupListener;
    }
    
    public JointTreeJointGroupListener(JointGroup group, Outline jointTree, List<String> propNames){
        myPropNames = propNames;
        myJointTree = jointTree;
        myJointGroup = group;
        myJointListeners = new ArrayList<JointTreeJointListener>(myJointGroup.getJointCount());
        myGroupListeners = new ArrayList<JointTreeJointGroupListener>(myJointGroup.getGroupCount());
        buildListeners();
    }
    
    private void buildListeners(){
        myJointGroup.addPropertyChangeListener(this);
        for(Joint j : myJointGroup.getJoints()){
            if(j == null){
                continue;
            }
            JointTreeJointListener jtjl = new JointTreeJointListener(j, myJointTree, myPropNames);
            myJointListeners.add(jtjl);
        }
        for(JointGroup g : myJointGroup.getJointGroups()){
            JointTreeJointGroupListener jtjgl = new JointTreeJointGroupListener(g, myJointTree, myPropNames);
            myGroupListeners.add(jtjgl);
        }
    }
    
    public void stopListening(){
        myJointGroup.removePropertyChangeListener(this);
        for(JointTreeJointListener listener : myJointListeners){
            listener.stopListening();
        }
        myJointListeners.clear();
        for(JointTreeJointGroupListener listener : myGroupListeners){
            listener.stopListening();
        }
        myGroupListeners.clear();
    }
    
    public int setRow(int r){
        myRow = r;
        for(JointTreeJointListener jointListener : myJointListeners){
            r++;
            jointListener.setRow(r);
        }
        for(JointTreeJointGroupListener groupListener : myGroupListeners){
            r++;
            r = groupListener.setRow(r);
        }
        return r;
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        String name = pce.getPropertyName();
        Integer i = null;
        if(JointGroup.PROP_ENABLED.equals(name)){
            i = 1;
        }else if(JointGroup.PROP_NAME.equals(name)){
            i = 0;
        }else if(JointGroup.PROP_STRUCTURE_CHANGED.equals(name)){
            stopListening();
            buildListeners();
            try{
                SwingUtilities.invokeLater(new UpdateRunnable());
            }catch(Exception ex){
                
            }
            return;
        }
        if(i == null){
            return;
        }
        Rectangle cell = myJointTree.getCellRect(myRow, i, false);
        myJointTree.repaint(cell);
    }
    
    private class UpdateRunnable implements Runnable{
        @Override
        public void run() {
            setRow(myRow);
            RowModel rm = new JointTreeRowModel(myPropNames);
            TreeModel tm = new JointGroupTreeModel(myJointGroup);
            OutlineModel mdl = DefaultOutlineModel.createOutlineModel(tm, rm);
            myJointTree.setModel(mdl);
            myJointTree.repaint();
        }
        
    }
}
