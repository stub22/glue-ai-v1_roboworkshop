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

import java.awt.Color;
import javax.swing.Icon;
import org.netbeans.swing.outline.RenderDataProvider;
import org.mechio.api.motion.Joint;
import org.mechio.api.motion.jointgroup.JointGroup;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class JointTreeRenderer implements RenderDataProvider{

    @Override
    public String getDisplayName(Object o) {
        if(o == null){
            return "";
        }else if(o instanceof Joint){
            Joint j = (Joint)o;
            Joint.Id id = j.getId();
            String name = j.getName();
            return String.format("(%s) - %s", id.toString(), name);
        }else if(o instanceof Integer){
            return String.format("(%d) - Joint Not Found", o);
        }else if(o instanceof JointGroup){
            return ((JointGroup)o).getName();
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
