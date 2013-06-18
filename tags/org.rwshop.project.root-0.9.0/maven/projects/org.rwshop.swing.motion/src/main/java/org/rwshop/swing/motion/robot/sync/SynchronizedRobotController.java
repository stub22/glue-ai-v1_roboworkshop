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
package org.rwshop.swing.motion.robot.sync;

import org.robokind.api.motion.sync.SynchronizedRobot;
import org.robokind.api.motion.utils.RobotManager;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class SynchronizedRobotController {
    private SynchronizedRobot myRobot;
    private RobotManager myManager;
    
    public SynchronizedRobotController(SynchronizedRobot robot, RobotManager manager){
        if(robot == null || manager == null){
            throw new NullPointerException();
        }
        myRobot = robot;
        myManager = manager;
    }
    
    
}
