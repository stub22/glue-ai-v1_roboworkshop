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

package org.rwshop.nb.animation.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jflux.api.common.rk.utils.RKSource.SourceImpl;
import org.rwshop.nb.animation.AnimationTimelineEditor;
import org.rwshop.nb.animation.history.UndoRedoFactory;
import org.rwshop.swing.animation.actions.FileAction;
import org.mechio.api.animation.editor.AnimationEditor;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.scaling.DefaultCoordinateScalar;
import org.rwshop.swing.common.scaling.ScalingManager;

/**
 * 
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public final class NewAnimationAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        SourceImpl<AnimationEditor> cS = new SourceImpl<AnimationEditor>();
        new FileAction.New(cS, new UndoRedoFactory()).actionPerformed(e);
        CoordinateScalar scalar = new DefaultCoordinateScalar(0.2, 400, true);
        ScalingManager scalingManager = new ScalingManager(scalar);
        AnimationTimelineEditor editor = new AnimationTimelineEditor();
        editor.init(scalingManager);
        editor.open();
        editor.requestActive();
        editor.setController(cS.getValue());
    }
}
