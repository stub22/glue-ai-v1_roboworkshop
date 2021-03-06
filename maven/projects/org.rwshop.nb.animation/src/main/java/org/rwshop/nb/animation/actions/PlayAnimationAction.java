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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.rwshop.nb.common.cookies.PlayCookie;

/**
 * 
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public final class PlayAnimationAction implements ActionListener {

    private final PlayCookie context;

    public PlayAnimationAction(PlayCookie context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.play();
    }
}
