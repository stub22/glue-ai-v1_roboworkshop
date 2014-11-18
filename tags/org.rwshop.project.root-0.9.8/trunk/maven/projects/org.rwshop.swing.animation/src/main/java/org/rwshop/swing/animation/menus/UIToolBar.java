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

import java.awt.event.ActionListener;
import org.mechio.api.animation.editor.actions.AnimationPlayerAction.Play;
import org.mechio.api.animation.editor.actions.AnimationPlayerAction.Stop;
import org.jflux.api.core.Source;
import org.rwshop.swing.animation.actions.FileAction.New;
import org.rwshop.swing.animation.actions.FileAction.Open;
import org.rwshop.swing.animation.actions.FileAction.Save;
import org.mechio.api.animation.editor.history.HistoryStack;
import javax.swing.JButton;
import javax.swing.Icon;
import org.mechio.api.animation.editor.AnimationEditor;
import javax.swing.JToolBar;
import org.jflux.api.common.rk.utils.RKSource;
import org.mechio.api.animation.editor.actions.AnimationPlayerAction.Pause;
import org.mechio.api.animation.editor.actions.HistoryAction;
import org.rwshop.swing.common.utils.SettingsRepository;

import static org.jflux.api.common.rk.localization.Localizer.$;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class UIToolBar {
    /**
     *
     * @param source
     * @param histFact
     * @return
     */
    public static JToolBar FileToolBar(RKSource<AnimationEditor> source, Source<? extends HistoryStack> histFact){
        JToolBar bar = new JToolBar($("file.menu"));
        Icon[] icons = SettingsRepository.getFileIcons();
        bar.add(button(icons[0], $("new"), new New(source, histFact)));
        bar.add(button(icons[1], $("open"), new Open(source, histFact, null)));
        bar.add(button(icons[2], $("save"), new Save(source, false)));
        return bar;
    }

    /**
     *
     * @param source
     * @return
     */
    public static JToolBar HistoryToolBar(RKSource<? extends HistoryStack> source){
        JToolBar bar = new JToolBar($("history.menu"));
        Icon[] icons = SettingsRepository.getHistoryIcons();
        bar.add(button(icons[0], $("undo"), HistoryAction.Undo(source)));
        bar.add(button(icons[1], $("undo"), HistoryAction.Redo(source)));
        return bar;
    }

    /**
     *
     * @param source
     * @return
     */
    public static JToolBar PlayerToolBar(RKSource<AnimationEditor> source){
        JToolBar bar = new JToolBar($("animation.player.menu"));
        Icon[] icons = SettingsRepository.getPlayerIcons();
        bar.add(button(icons[0], $("play"), (ActionListener)new Play(source)));
        bar.add(button(icons[1], $("pause"), (ActionListener)new Pause()));
        bar.add(button(icons[2], $("stop"), (ActionListener)new Stop()));
        return bar;
    }

    private static JButton button(Icon icon, String toolTip, ActionListener action){
        JButton button = new JButton(icon);
        button.setToolTipText(toolTip);
        button.addActionListener(action);
        return button;
    }
}
