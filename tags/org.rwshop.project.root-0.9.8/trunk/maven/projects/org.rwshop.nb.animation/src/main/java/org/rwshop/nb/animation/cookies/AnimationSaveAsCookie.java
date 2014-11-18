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

package org.rwshop.nb.animation.cookies;

import org.jflux.api.common.rk.utils.RKSource.SourceImpl;
import org.rwshop.swing.animation.actions.FileAction.Save;
import org.mechio.api.animation.editor.AnimationEditor;
import org.rwshop.nb.common.cookies.SaveAsCookie;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationSaveAsCookie extends SourceImpl<AnimationEditor> implements SaveAsCookie{
    public AnimationSaveAsCookie(AnimationEditor controller){
        super(controller);
    }
    
    @Override
    public void saveAs(){
        new Save(this, true).actionPerformed(null);
    }
}
