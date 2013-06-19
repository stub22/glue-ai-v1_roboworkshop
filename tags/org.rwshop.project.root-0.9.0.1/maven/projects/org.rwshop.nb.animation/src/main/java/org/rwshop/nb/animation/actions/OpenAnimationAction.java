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

package org.rwshop.nb.animation.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.rwshop.nb.animation.AnimationTimelineEditor;
import org.rwshop.nb.animation.history.UndoRedoFactory;
import org.rwshop.swing.animation.actions.FileAction;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.common.utils.RKSource.SourceImpl;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.scaling.DefaultCoordinateScalar;
import org.rwshop.swing.common.scaling.ScalingManager;

/**
 * 
 * @author Matthew Stevenson <www.robokind.org>
 */
public final class OpenAnimationAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        SourceImpl<AnimationEditor> cS = new SourceImpl<AnimationEditor>();
        new FileAction.Open(cS, new UndoRedoFactory(), null).actionPerformed(null);
        if(cS.getValue() == null){
            return;
        }
        AnimationTimelineEditor editor = new AnimationTimelineEditor();
        editor.open();
        editor.requestActive();
        CoordinateScalar scaler = new DefaultCoordinateScalar(0.02, 400, true);
        ScalingManager sm = new ScalingManager(scaler);
        editor.init(sm);
        editor.setController(cS.getValue());
    }
}
