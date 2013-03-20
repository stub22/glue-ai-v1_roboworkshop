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
import org.openide.util.Lookup;
import org.robokind.api.motion.Robot;
import org.rwshop.nb.motion.RobotEditorTopComponent;

/**
 * 
 * @author Matthew Stevenson <www.robokind.org>
 */
public final class RobotEditor implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Robot robot = Lookup.getDefault().lookup(Robot.class);
        if(robot == null){
            return;
        }
        RobotEditorTopComponent editor = new RobotEditorTopComponent();
        editor.setRobot(robot);
        editor.open();
    }
}
