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

package org.rwshop.swing.animation.timeline.position;

import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import org.rwshop.swing.animation.menus.MenuProvider;
import org.rwshop.swing.animation.menus.UIMenuItem;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class PositionSourceMenuProvider implements MenuProvider{
    private JMenu myMenu;
    
    public PositionSourceMenuProvider() {
        myMenu = new JMenu("Editor Options");
    }

    @Override
    public JMenu getContextSubMenu(MouseEvent e) {
        return myMenu;
    }
    
    public void addMenuItem(UIMenuItem item) {
        myMenu.add(item);
    }
}
