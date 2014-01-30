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

package org.rwshop.swing.animation.menus;

import java.awt.Component;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class PopupManager {
    private Component myComponent;

    /**
     *
     * @param component
     */
    public PopupManager(Component component){
        myComponent = component;
    }
    /**
     * Creates  a new JPopupMenu from the given JComponents.  The JPopup is
     * shown at the given x, y, of the PopupManager's component.
     *
     * @param items the list of menu items.  This should be limited to JComponents
     * displayable in a menu.
     * @param x the x-coordinate to display the popup
     * @param y the y-coordinate to display the popup
     */
    public void showMenu(List<JComponent> items, int x, int y){
        buildMenu(items).show(myComponent, x, y);
    }

    private JPopupMenu buildMenu(List<JComponent> items){
        JPopupMenu popup = new JPopupMenu();
        for(JComponent item : items){
            popup.add(item);
        }
        return popup;
    }
}
