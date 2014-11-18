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

import org.mechio.api.animation.editor.history.HistoryStack;
import org.jflux.api.core.Source;
import org.rwshop.swing.animation.actions.FileAction.Open;
import org.rwshop.swing.animation.actions.FileAction.Save;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import org.jflux.api.common.rk.utils.RKSource;
import org.mechio.api.animation.editor.AnimationEditor;

import static org.jflux.api.common.rk.localization.Localizer.$;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class MenuBarManager {
    private JMenuBar myMenuBar;
    private RKSource<AnimationEditor> mySource;
    private Source<HistoryStack> myHistoryFactory;

    /**
     *
     * @param menu
     * @param source
     * @param histFact
     */
    public MenuBarManager(JMenuBar menu, RKSource<AnimationEditor> source, Source<HistoryStack> histFact){
        myMenuBar = menu;
        mySource = source;
        myHistoryFactory = histFact;
    }

    /**
     *
     */
    public void setupMenu(){
        JMenu fileMenu = new JMenu($("file"));
        fileMenu.add(new UIMenuItem($("save"), new Save(mySource, false)));
        fileMenu.add(new UIMenuItem($("save.as"), new Save(mySource, true)));
        fileMenu.add(new UIMenuItem($("open"), new Open(mySource, myHistoryFactory, null)));
        myMenuBar.add(fileMenu);
    }
}
