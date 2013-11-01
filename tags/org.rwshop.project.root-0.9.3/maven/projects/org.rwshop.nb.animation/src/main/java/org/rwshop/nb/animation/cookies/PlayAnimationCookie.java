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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.osgi.framework.BundleContext;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.animation.player.AnimationPlayer;
import org.robokind.api.animation.utils.AnimationUtils;
import org.robokind.api.common.utils.RKSource.SourceImpl;
import org.rwshop.nb.common.cookies.PlayCookie;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class PlayAnimationCookie 
        extends SourceImpl<AnimationEditor> implements PlayCookie{
    private final static Logger theLogger = Logger.getLogger(PlayAnimationCookie.class.getName());
    
    public PlayAnimationCookie(AnimationEditor controller){
        super(controller);
    }
    
    @Override
    public void play(){
        BundleContext context = 
                OSGiUtils.getBundleContext(AnimationPlayer.class);
        if(context == null){
            theLogger.log(Level.SEVERE, 
                    "Unable to find BundleContext for AnimationPlayer");
            return;
        }
        AnimationUtils.playAnimation(
                context, null, getValue().getEnabledAnimation());
    }
}
