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

package org.rwshop.swing.animation.table.animation;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import org.mechio.api.animation.editor.actions.ChannelActions;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.editor.ChannelEditor;
import org.rwshop.swing.animation.actions.UIAction;
import org.rwshop.swing.animation.menus.UIMenuItem;

import static org.jflux.api.common.rk.localization.Localizer.$;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationTableContextMenu implements MouseListener{
    private JTable myTable;
    private AnimationEditor myController;

    /**
     *
     * @param table
     */
    public AnimationTableContextMenu(JTable table){
        myTable = table;
        myTable.addMouseListener(this);

        //This allows the menu to come up in empty table areas
        myTable.getParent().addMouseListener(this);
    }

    /**
     *
     * @param controller
     */
    public void setController(AnimationEditor controller){
        myController = controller;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        tryShowPopup(e);
    }
    @Override
    public void mousePressed(MouseEvent e) {
        tryShowPopup(e);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        tryShowPopup(e);
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     *
     * @param e
     */
    public void tryShowPopup(MouseEvent e){
        if(myController == null){
            return;
        }
        if(e.getButton() != MouseEvent.BUTTON3 || !e.isPopupTrigger()){
            return;
        }
        JPopupMenu menu = new JPopupMenu();
        int i = myTable.rowAtPoint(e.getPoint());
        if(i >= 0){
            ChannelEditor controller = myController.getChild(i);
            menu.add(new UIMenuItem($("remove"), ChannelActions.Remove(controller)));
            for(JComponent c : UIMenuItem.buildStateMenuItems(controller)){
                menu.add(c);
            }
            menu.addSeparator();
        }
        menu.add(UIAction.AddChannelMenu(myController));
        menu.show(myTable, e.getX(), e.getY());
    }
}
