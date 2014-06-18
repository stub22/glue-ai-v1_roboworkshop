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
import java.util.List;
import javax.swing.SwingUtilities;
import org.netbeans.swing.outline.Outline;
import org.mechio.api.motion.Joint;
import org.mechio.api.motion.servos.config.ServoConfig;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class JointTreeJointListener implements PropertyChangeListener{
    private Outline myJointTree;
    private Joint myJoint;
    private Integer myRow;
    private List<String> myPropertyNames;
    
    public JointTreeJointListener(Joint joint, Outline jointTree, List<String> props){
        if(joint == null){
            throw new IllegalArgumentException("Joint must not be null.");
        }else if(jointTree == null){
            throw new IllegalArgumentException("Outline must not be null.");
        }
        myPropertyNames = props;
        myJointTree = jointTree;
        myJoint = joint;
        myJoint.addPropertyChangeListener(this);
        //myJoint.getConfig().addPropertyChangeListener(this);
    }
    
    public void setRow(Integer row){
        myRow = row;
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if(myRow == null){
            return;
        }
        String name = pce.getPropertyName();
        Integer i = null;
        if(Joint.PROP_GOAL_POSITION.equals(name)){
            i = 2;
        }else if(Joint.PROP_ENABLED.equals(name)){
            i = 1; 
        }else if(ServoConfig.PROP_NAME.equals(name)){
            i = 0;
        }
        if(i == null){
            i = myPropertyNames.indexOf(name);
            if(i == -1){
                return;
            }
            i += JointTreeRowModel.theColCount + 1;
        }
        if(i == 2){
            final int n = i;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Rectangle cell = myJointTree.getCellRect(myRow, n, false);
                    Rectangle cell2 = myJointTree.getCellRect(myRow, n+1, false);
                    myJointTree.repaint(cell);
                    myJointTree.repaint(cell2);
                }
            });
        }else{
            final int n = i;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Rectangle cell = myJointTree.getCellRect(myRow, n, false);
                    myJointTree.repaint(cell);
                }
            });
        }
    }
    
    public void stopListening(){
        myJoint.removePropertyChangeListener(this);
    }
}
