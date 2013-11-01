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

package org.rwshop.nb.animation.cookies;

import java.io.IOException;
import org.openide.cookies.SaveCookie;
import org.rwshop.swing.animation.actions.FileAction.Save;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.common.utils.RKSource.SourceImpl;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class AnimationSaveCookie extends SourceImpl<AnimationEditor> implements SaveCookie{
    public AnimationSaveCookie(AnimationEditor controller){
        super(controller);
    }
    
    @Override
    public void save() throws IOException {
        new Save(this, false).actionPerformed(null);
    }
}
