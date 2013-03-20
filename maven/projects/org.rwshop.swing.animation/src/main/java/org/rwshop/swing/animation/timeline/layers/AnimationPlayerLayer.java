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
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.DrawableLayer;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class AnimationPlayerLayer implements DrawableLayer{//, IAnimationListener{
    private long myTime = -1;
    private CoordinateScalar myScalar;
    private JPanel myPanel;
    private Map<Integer,Double> myLastPositions;

    /**
     *
     * @param panel
     * @param s
     */
    public AnimationPlayerLayer(JPanel panel, CoordinateScalar s){
        myScalar = s;
        myPanel = panel;
    }

    private Long myLastTime = null;
    /**
     *
     * @param time
     * @param positions
     */
    public void tick(long time, Map<Integer,Double> positions) {
        final int old = (int)myScalar.scaleX(myTime);
        myLastPositions = positions;
        if(myLastTime == null || (time - myLastTime) >= 40){
            myLastTime = myTime;
            myTime = time;
            final int s = (int)myScalar.scaleX(time);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    myPanel.paintImmediately(old-2,0,s-old+10,myPanel.getHeight());
                }
            });
        }
    }

    /**
     *
     */
    public void start(){}
    
    /**
     *
     */
    public void end(){
        myTime = -1;
        myLastTime = null;
        myPanel.repaint();
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        if(myTime < 0){
            return;
        }
        g.setColor(Color.MAGENTA);
        int tX = (int)myScalar.scaleX(myTime);
        int y = myPanel.getHeight();
        g.drawLine(tX, y, tX, 0);
    }

    private static Color[] cols = {new Color(255, 196, 196, 96),
                                   new Color(255, 240, 240, 128),
                                   Color.darkGray,
                                   new Color(96, 0, 0)};
    private void paintPositions(Graphics g, int x){
        for(Entry<Integer,Double> e : myLastPositions.entrySet()){
            double d = e.getValue();
            if(d < 0){
                continue;
            }
            int y = (int)myScalar.scaleY(e.getValue());
            //int in1 = 2, in2 = 4;
            int out1 = 6, out2 = 12;
            //g.setColor(cols[1]);
            //g.fillOval(x-out1, y-out1, out2, out2);
            g.drawOval(x-out1, y-out1, out2, out2);
            //g.setColor(cols[0]);
            //g.fillOval(x-in1, y-in1, in2, in2);
            //g.setColor(cols[2]);
            //g.drawOval(x-in1, y-in1, in2, in2);
        }
    }
}
