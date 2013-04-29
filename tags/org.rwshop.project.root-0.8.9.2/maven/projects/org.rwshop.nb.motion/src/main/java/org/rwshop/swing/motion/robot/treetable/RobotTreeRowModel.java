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

import org.netbeans.swing.outline.RowModel;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.Robot;
import org.robokind.api.motion.servos.Servo;
import org.robokind.api.motion.servos.ServoJoint;
import org.robokind.api.motion.servos.ServoController;
import org.robokind.api.motion.servos.utils.ConnectionStatus;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class RobotTreeRowModel implements RowModel{
    private final static String[] theColumns = new String[]{
            "Type", "Id","Enabled","Status","Connection","Errors"};
    
    public RobotTreeRowModel(){
    }
    
    @Override
    public int getColumnCount() {
        return theColumns.length;
    }

    @Override
    public Object getValueFor(Object node, int column) {
        if(node == null){
            return null;
        }
        if(node instanceof Robot){
            return getValueForRobot((Robot)node, column);
        }else if(node instanceof ServoController){
            return getValueForServoController((ServoController)node, column);
        }else if(node instanceof ServoJoint){
            return getValueForServo(((ServoJoint)node).getServo(), column);
        }else if(node instanceof Joint){
            return getValueForJoint((Joint)node, column);
        }else if(node instanceof Servo){
            return getValueForServo((Servo)node, column);
        }
        return null;
    }
    
    public Object getValueForRobot(Robot robot, int column) {
        switch(column){
            case 0 : return robot.getClass().getSimpleName();
            case 1 : return robot.getRobotId();
            case 2 : return robot.isEnabled();
            case 3 : return robot.isConnected() ? "Connected" : "Disconnected";
            case 4 : return robot.isConnected() ? "Disconnect" : "Connect";
            case 5 : return "---";
        }
        return null;
    }
    
    public Object getValueForServoController(ServoController controller, int column) {
        switch(column){
            case 0 : return controller.getClass().getSimpleName();
            case 1 : return controller.getId();
            case 2 : return controller.getEnabled();
            case 3 : return controller.getConnectionStatus();
            case 4 : return handleServoControllerStatus(controller);
            case 5 : return "---";
        }
        return null;
    }
    
    private Object handleServoControllerStatus(ServoController controller){
        ConnectionStatus status = controller.getConnectionStatus();
        switch(status){
            case CONNECTED : return "Disconnect";
            case DISCONNECTED : return "Connect";
            case CONNECTION_ERROR : return "Clear Errors";
        }
        return null;
    }
    
    public Object getValueForJoint(Joint joint, int column) {
        switch(column){
            case 0 : return joint.getClass().getSimpleName();
            case 1 : return joint.getId();
            case 2 : return joint.getEnabled();
            case 3 : return "";
            case 4 : return "";
            case 5 : return "";
        }
        return null;
    }
    
    public Object getValueForServo(Servo servo, int column) {
        switch(column){
            case 0 : return servo.getClass().getSimpleName();
            case 1 : return servo.getId();
            case 2 : return servo.getEnabled();
            case 3 : return "";
            case 4 : return "";
            case 5 : return "";
        }
        return null;
    }
    
    @Override
    public Class getColumnClass(int column) {
        return column == 2 ? Boolean.class : String.class;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return column == 2;
    }

    @Override
    public void setValueFor(Object node, int column, Object value) {
        if(value == null || !(value instanceof Boolean)
                || node == null || !isCellEditable(node, column)){
            return;
        }
        Boolean val = (Boolean)value;
        if(node instanceof Joint){
            Joint joint = (Joint)node;
            joint.setEnabled(val);
        }if(node instanceof Servo){
            Servo servo = (Servo)node;
            servo.setEnabled(val);
        }else if(node instanceof ServoController){
            ServoController sc = (ServoController)node;
            sc.setEnabled(val);
        }else if(node instanceof Robot){
            Robot robot = (Robot)node;
            robot.setEnabled(val);
        }
    }

    @Override
    public String getColumnName(int column) {
        return theColumns[column];
    }
    
}
