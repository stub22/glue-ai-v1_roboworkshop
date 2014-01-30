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
package org.rwshop.swing.motion.robot.treetable;

import java.awt.Color;
import javax.swing.Icon;
import org.netbeans.swing.outline.RenderDataProvider;
import org.mechio.api.motion.Joint;
import org.mechio.api.motion.Robot;
import org.mechio.api.motion.servos.Servo;
import org.mechio.api.motion.servos.ServoController;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class RobotTreeRenderer implements RenderDataProvider{

    @Override
    public String getDisplayName(Object o) {
        if(o == null){
            return "";
        }else if(o instanceof Joint){
            return ((Joint)o).getName();
        }else if(o instanceof Servo){
            return ((Servo)o).getName();
        }else if(o instanceof ServoController){
            return ((ServoController)o).getId().toString();
        }else if(o instanceof Robot){
            return ((Robot)o).getRobotId().toString();
        }
        return "";
    }

    @Override
    public boolean isHtmlDisplayName(Object o) {
        return false;
    }

    @Override
    public Color getBackground(Object o) {
        return null;
    }

    @Override
    public Color getForeground(Object o) {
        return Color.BLACK;
    }

    @Override
    public String getTooltipText(Object o) {
        if(o == null){
            return null;
        }
        return o.toString();
    }

    @Override
    public Icon getIcon(Object o) {
        return null;
    }
}
