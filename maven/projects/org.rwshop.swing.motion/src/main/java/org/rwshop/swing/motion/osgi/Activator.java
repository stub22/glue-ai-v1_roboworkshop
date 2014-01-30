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

package org.rwshop.swing.motion.osgi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.jflux.api.common.rk.position.NormalizedDouble;
import org.mechio.api.motion.Joint;
import org.mechio.api.motion.Robot;
import org.mechio.impl.motion.sync.PortableSynchronizedJointConfig;
import org.mechio.impl.motion.sync.PortableSynchronizedRobotConfig;
import org.mechio.impl.motion.sync.SynchronizedRobotConfigWriter;
import org.rwshop.swing.motion.connection.IPFrame;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class Activator implements BundleActivator {
    
    @Override
    public void start(BundleContext context) throws Exception {
        /*List<PortableSynchronizedJointConfig> joints = new ArrayList();
        joints.add(new PortableSynchronizedJointConfig(
                new Joint.Id(0), "Joint 0", new NormalizedDouble(0.5)));
        joints.add(new PortableSynchronizedJointConfig(
                new Joint.Id(1), "Joint 1", new NormalizedDouble(0.75)));
        PortableSynchronizedRobotConfig conf = new PortableSynchronizedRobotConfig(new Robot.Id("myRobotId"), joints);
        new SynchronizedRobotConfigWriter().writeConfiguration(conf, new File("/home/matt/Desktop/SyncRobot.json"));*/
        /*final BundleContext fc = context;
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                SettingsRepository.setUIManager();
                RobotControllerFrame frame = new RobotControllerFrame();
                frame.setContext(fc);
                frame.setVisible(true);
            }
        });
        */
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                IPFrame.instantiate();
            }
        });
    }
    
    public void stop(BundleContext context) throws Exception {
    }
}
