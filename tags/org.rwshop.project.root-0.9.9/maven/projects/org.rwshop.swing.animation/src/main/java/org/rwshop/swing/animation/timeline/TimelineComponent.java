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

package org.rwshop.swing.animation.timeline;

import javax.swing.JPanel;
import org.mechio.api.animation.editor.AnimationEditor;
import org.rwshop.swing.animation.menus.MenuProvider;
import org.rwshop.swing.common.DrawableLayer;
import org.rwshop.swing.common.scaling.CoordinateScalar;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public interface TimelineComponent {
    public void setPanel(JPanel panel);
    public void setEditor(AnimationEditor editor);
    public void setScalar(CoordinateScalar scalar);
    public MenuProvider getMenuProvider();
    public DrawableLayer getDrawLayer();
}
