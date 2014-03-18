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

package org.rwshop.swing.animation.timeline.layers;

import java.awt.Graphics;
import org.mechio.api.animation.editor.AnimationEditor;
import org.rwshop.swing.common.DrawableLayer;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationInfoLayer implements DrawableLayer {
    private AnimationEditor myController;

    /**
     *
     * @param controller
     */
    public void setController(AnimationEditor controller){
        myController = controller;
    }

    static int sbc = 0;
    /**
     *
     * @param g
     */
    public void paint(Graphics g){
        if(myController == null){
            return;
        }
        /*sbc=0;
        StringBuilder sb = new StringBuilder();
        sb.append("Selected - ");
        ap(sb, "Servo", myAnim.getSelectedIndex());
        ChannelController channel = myAnim.getSelected();
        ap(sb, "Path", channel == null ? -1 : channel.getSelectedIndex());
        MotionPathController motionPath = channel == null ? null : channel.getSelected();
        ap(sb, "Point", motionPath == null ? -1 : motionPath.getSelectedIndex());
        g.setColor(Color.BLACK);
        g.drawString(sb.toString(), 20, 16);*/
    }

    private void ap(StringBuilder sb, String str, int i){
        if(sbc > 0){
            sb.append(", ");
        }
        sb.append(str);
        sb.append(": ");
        sb.append(i);
        sbc++;
    }
}
