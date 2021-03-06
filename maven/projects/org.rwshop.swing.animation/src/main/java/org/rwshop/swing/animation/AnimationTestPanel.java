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

/*
 * AnimationTestPanel.java
 *
 * Created on Nov 9, 2011, 6:31:43 PM
 */
package org.rwshop.swing.animation;

import java.beans.Beans;
import org.mechio.api.animation.Animation;
import org.mechio.api.animation.Channel;
import org.mechio.api.animation.MotionPath;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.editor.history.HistoryStack;
import org.mechio.api.interpolation.cspline.CSplineInterpolatorFactory;
import org.rwshop.swing.common.scaling.ScalingManager;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationTestPanel extends javax.swing.JPanel {

    /** Creates new form AnimationTestPanel */
    public AnimationTestPanel() {
        initComponents();
    }
    
    private Animation myAnimation;
    
    public void init(ScalingManager scaleManager){
        scaleManager.getScalar().scaleY(animationScrollPanel1.getHeight()-30);
        animationScrollPanel1.init(scaleManager);
        HistoryStack hist = new HistoryStack();
        myAnimation = new Animation();
        Channel chan = new Channel(0, "Test Channel");
        MotionPath path = new MotionPath(new CSplineInterpolatorFactory());
        path.addPoint(0, 0.5);
        path.addPoint(1000, 0.05);
        path.addPoint(3000, 0.95);
        path.addPoint(4000, 0.5);
        chan.addPath(path);
        myAnimation.addChannel(chan);
        AnimationEditor editor = new AnimationEditor(myAnimation, null, hist);
        animationScrollPanel1.setController(editor);
    }
    
    public Animation getAnimation(){
        return myAnimation;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        animationScrollPanel1 = new org.rwshop.swing.animation.timeline.scroll.AnimationScrollPanel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(animationScrollPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(animationScrollPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.rwshop.swing.animation.timeline.scroll.AnimationScrollPanel animationScrollPanel1;
    // End of variables declaration//GEN-END:variables
}
