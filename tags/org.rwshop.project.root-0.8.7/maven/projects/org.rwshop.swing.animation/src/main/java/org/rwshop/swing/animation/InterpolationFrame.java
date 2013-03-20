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

/*
 * InterpolationFrame.java
 *
 * Created on Sep 2, 2011, 1:24:53 PM
 */
package org.rwshop.swing.animation;

import java.util.Collections;
import org.robokind.api.animation.Animation;
import org.robokind.api.animation.Channel;
import org.robokind.api.animation.MotionPath;
import org.robokind.api.interpolation.bezier.BezierInterpolatorFactory;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.scaling.DefaultCoordinateScalar;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class InterpolationFrame extends javax.swing.JFrame {

    /** Creates new form InterpolationFrame */
    public InterpolationFrame() {
        initComponents();
        Animation anim = new Animation();
        Channel chan = new Channel(0, "channel");
        MotionPath mp = new MotionPath(new BezierInterpolatorFactory());
        mp.addPoint(0, 0.5);
        mp.addPoint(25, 0.0);
        mp.addPoint(75, 1.0);
        mp.addPoint(100, 0.5);
        chan.addPath(mp);
        anim.addChannel(chan);
        CoordinateScalar scalar = new DefaultCoordinateScalar(getWidth(), getHeight(), true);
        animationRenderer1.setScalar(scalar);
        animationRenderer1.setAnimation(anim, Collections.EMPTY_MAP);
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        animationRenderer1 = new org.rwshop.swing.animation.AnimationRenderer();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout animationRenderer1Layout = new javax.swing.GroupLayout(animationRenderer1);
        animationRenderer1.setLayout(animationRenderer1Layout);
        animationRenderer1Layout.setHorizontalGroup(
            animationRenderer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 241, Short.MAX_VALUE)
        );
        animationRenderer1Layout.setVerticalGroup(
            animationRenderer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 112, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(animationRenderer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(animationRenderer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new InterpolationFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.rwshop.swing.animation.AnimationRenderer animationRenderer1;
    // End of variables declaration//GEN-END:variables
}
