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

package org.rwshop.swing.animation.timeline.layers;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.robokind.api.animation.player.AnimationJobListener;
import org.robokind.api.common.utils.Utils;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.DrawableLayer;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class AnimationProgressLayer implements AnimationJobListener, DrawableLayer {
    /**
     *
     */
    public final static Color PLAYING_COLOR = new Color(0.2f, 0.4f, 0.9f, 0.4f);
    /**
     *
     */
    public final static Color PAUSED_COLOR = new Color(0.8f, 0.65f, 0.2f, 0.4f);
    /**
     *
     */
    public final static Color STOPPED_COLOR = new Color(0.9f, 0.2f, 0.1f, 0.4f);
    /**
     *
     */
    public final static Color COMPLETED_COLOR = new Color(0.2f, 0.9f, 0.35f, 0.4f);

    private JPanel myPanel;
    private long myStartTime;
    private Long myEndTime;
    private long myCurrentTime;
    private Color myColor;
    private CoordinateScalar myScalar;

    /**
     *
     * @param scalar
     */
    public AnimationProgressLayer(CoordinateScalar scalar){
        myCurrentTime = 0;
        myStartTime = 0;
        myEndTime = null;
        myColor = PLAYING_COLOR;
        myScalar = scalar;
    }

    /**
     *
     * @param panel
     */
    public void setPanel(JPanel panel){
        myPanel = panel;
    }

    /**
     *
     * @param time
     */
    @Override
    public void animationAdvanced(long time) {
        myCurrentTime = time;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                myPanel.repaint();
            }
        });
    }

    @Override
    public void animationStart(long start, Long end) {
        myStartTime = start;
        myEndTime = end;
    }

    /**
     *
     * @param col
     */
    public void setColor(Color col){
        myColor = col;
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        int x1 = (int)myScalar.scaleX(myStartTime);
        int y1 = (int)myScalar.scaleY(1.0);
        int w = (int)myScalar.scaleX(myCurrentTime-myStartTime);
        if(w < 0){
            return;
        }
        int h = (int)myScalar.scaleY(0.0);
        g.setColor(myColor);
        g.fillRect(x1,y1, w, h);
        g.setColor(STOPPED_COLOR);
        if(myEndTime != null){
            int end = (int)myScalar.scaleX(myEndTime.intValue());
            g.drawLine(end, y1, end, h);
        }
    }
}
