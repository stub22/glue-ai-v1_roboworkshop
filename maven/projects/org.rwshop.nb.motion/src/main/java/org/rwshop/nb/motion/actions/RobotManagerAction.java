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
package org.rwshop.nb.motion.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.osgi.framework.BundleContext;
import org.robokind.api.motion.Robot;
import org.robokind.api.motion.utils.RobotManager;
import org.robokind.api.motion.utils.RobotUtils;
import org.rwshop.nb.motion.RobotManagerTopComponent;

public final class RobotManagerAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        RobotManagerTopComponent manageComp = RobotManagerTopComponent.findInstance();
        if(manageComp.isOpened()){
            return;
        }
        BundleContext context = OSGiUtils.getBundleContext(Robot.class);
        if(context == null){
            throw new NullPointerException();
        }
        RobotManager manager = RobotUtils.getRobotManager(context);
        manageComp.initialize(manager);
        manageComp.open();
    }
}
