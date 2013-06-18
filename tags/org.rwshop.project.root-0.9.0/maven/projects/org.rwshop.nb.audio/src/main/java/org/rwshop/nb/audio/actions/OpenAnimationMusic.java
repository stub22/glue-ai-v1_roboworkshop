/*
 * Copyright 2013 Hanson Robokind LLC.
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
package org.rwshop.nb.audio.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.openide.windows.Mode;
import org.openide.windows.WindowManager;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.audio.WavPlayer;
import org.robokind.api.common.playable.Playable;
import org.robokind.api.common.services.addon.ServiceAddOn;
import org.robokind.api.common.utils.RKSource;
import org.rwshop.nb.audio.AnimationMusicTopComponent;
import org.rwshop.nb.animation.AnimationTimelineEditor;
import org.rwshop.nb.animation.history.UndoRedoFactory;
import org.rwshop.swing.animation.actions.FileAction;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.scaling.DefaultCoordinateScalar;
import org.rwshop.swing.common.scaling.ScalingManager;

/**
 *
 * @author matt
 */
public class OpenAnimationMusic implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        RKSource.SourceImpl<AnimationEditor> cS = new RKSource.SourceImpl<AnimationEditor>();
        new FileAction.Open(cS, new UndoRedoFactory(), null).actionPerformed(null);
        if(cS.getValue() == null){
            return;
        }
        List<ServiceAddOn<Playable>> addons = cS.getValue().getAnimation().getAddOns();
        WavPlayer player = (WavPlayer)addons.get(0).getAddOn();
        CoordinateScalar scalar = new DefaultCoordinateScalar(0.02, 400, true);
        ScalingManager sm = new ScalingManager(scalar);
        AnimationTimelineEditor editor = new AnimationTimelineEditor();
        editor.init(sm);
        editor.setController(cS.getValue());
        editor.open();
        editor.requestActive();
        WindowManager wm = WindowManager.getDefault();
        Mode bottom = wm.findMode("output");
        AnimationMusicTopComponent music = new AnimationMusicTopComponent();
        bottom.dockInto(music);
        music.init(player, sm);
        music.open();
    }
}
