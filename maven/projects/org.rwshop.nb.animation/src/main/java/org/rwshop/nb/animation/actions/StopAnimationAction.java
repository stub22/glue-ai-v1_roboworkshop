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

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.rwshop.nb.common.cookies.StopCookie;

@ActionID(category = "Animation",
id = "org.rwshop.nb.animation.actions.StopAnimationAction")
@ActionRegistration(iconBase = "org/rwshop/nb/animation/resources/images/Stop16.gif",
displayName = "#CTL_StopAnimationAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/Animation", position = 400)
})
@Messages("CTL_StopAnimationAction=Stop Animation")
public final class StopAnimationAction implements ActionListener {

    private final StopCookie context;

    public StopAnimationAction(StopCookie context) {
        this.context = context;
    }

    public void actionPerformed(ActionEvent ev) {
        context.stop();
    }
}
