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

package org.rwshop.swing.animation.menus;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import org.robokind.api.animation.editor.actions.EditorAction;
import org.robokind.api.animation.editor.AbstractEditor;
import org.robokind.api.animation.editor.EditState;
import static org.robokind.api.common.localization.Localizer.*;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class UIMenuItem extends JMenuItem{
    /**
     *
     * @param action
     */
    public UIMenuItem(String label, ActionListener action){
        setText(label);
        addActionListener(action);
    }
    
    /**
     *
     * @param editor
     * @return
     */
    public static List<JComponent> buildStateMenuItems(AbstractEditor editor){
        String[] labels = {
            stateLabel(editor, EditState.SELECTED, $("select"), $("deselect")),
            stateLabel(editor, EditState.VISIBLE, $("show"), $("hide")),
            stateLabel(editor, EditState.DISABLED, $("disable"), $("enabled")),
            stateLabel(editor, EditState.LOCKED, $("lock"), $("unlock"))
        };
        return UIMenuItem.buildMenuItems(labels,
            EditorAction.Select(editor),
            EditorAction.Visible(editor),
            EditorAction.Disabled(editor),
            EditorAction.Locked(editor)
        );
    }
    
    private static String stateLabel(AbstractEditor editor, EditState state, String set, String unset){
        if(editor.hasFlag(state)){
            return unset;
        }
        return set;
    }
    /**
     *
     * @param actions
     * @return
     */
    public static List<JComponent> buildMenuItems(String[] labels, ActionListener...actions){
        List<JComponent> items = new ArrayList();
        int len = Math.min(actions.length, labels.length);
        for(int i=0; i<len; i++){
            items.add(new UIMenuItem(labels[i], actions[i]));
        }
        return items;
    }
}
