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
package org.rwshop.swing.motion.robot.treetable;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.Robot;
import org.robokind.api.motion.servos.Servo;
import org.robokind.api.motion.servos.ServoController;
import org.robokind.api.motion.servos.ServoRobot;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class RobotTreeModel implements TreeModel{
    private Robot myRobot;
    
    public RobotTreeModel(Robot robot){
        myRobot = robot;
    }
    
    @Override
    public Object getRoot() {
        return myRobot;
    }

    @Override
    public Object getChild(Object parent, int i) {
        if(parent == null){
            return null;
        }else if(parent instanceof Joint){
            return null;
        }else if(parent instanceof Servo){
            return null;
        }else if(parent instanceof ServoController){
            return ((ServoController)parent).getServos().get(i);
        }else if(parent instanceof ServoRobot){
            return getServoRobotChild((ServoRobot)parent, i);
        }else if(parent instanceof Robot){
            return ((Robot)parent).getJointList().get(i);
        }
        return null;
    }
    
    public Object getServoRobotChild(ServoRobot robot, int i){
        int controllerLen = robot.getControllerList().size();
        if(i < controllerLen){
            return robot.getControllerList().get(i);
        }
        i -= controllerLen;
        return robot.getJointList().get(i);
    }

    @Override
    public int getChildCount(Object parent) {
        if(parent == null){
            return 0;
        }else if(parent instanceof Joint){
            return 0;
        }else if(parent instanceof Servo){
            return 0;
        }else if(parent instanceof ServoController){
            return ((ServoController)parent).getServos().size();
        }else if(parent instanceof ServoRobot){
            ServoRobot robot = (ServoRobot)parent;
            int count = robot.getControllerList().size();
            count += robot.getJointList().size();
            return count;
        }else if(parent instanceof Robot){
            return ((Robot)parent).getJointList().size();
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object o) {
        if(o == null || o instanceof Joint || o instanceof Servo){
           return true; 
        }
        return getChildCount(o) == 0;
    }

    @Override
    public void valueForPathChanged(TreePath tp, Object o) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if(parent == null){
            return -1;
        }else if(parent instanceof Joint){
            return -1;
        }else if(parent instanceof ServoController){
            if(!(child instanceof Servo)){
                return -1;
            }
            return ((ServoController)parent).getServos().indexOf(child);
        }else if(parent instanceof ServoRobot){
            ServoRobot robot = (ServoRobot)parent;
            if(child instanceof ServoController){
                return robot.getControllerList().indexOf(child);
            }else if(child instanceof Joint){
                int controllerLen = robot.getControllerList().size();
                return robot.getJointList().indexOf(child) + controllerLen;
            }
            return -1;
        }else if(parent instanceof Robot){
            if(!(child instanceof Joint)){
                return -1;
            }
            return ((Robot)parent).getJointList().indexOf(child);
        }
        return -1;
    }

    
    @Override
    public void addTreeModelListener(TreeModelListener tl) {}

    @Override
    public void removeTreeModelListener(TreeModelListener tl) {}
    
}
