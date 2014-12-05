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
 * AnimationJobPanel.java
 *
 * Created on Apr 27, 2011, 6:52:43 PM
 */

package org.rwshop.swing.animation.joblist;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import org.jflux.api.common.rk.config.VersionProperty;
import org.jflux.api.common.rk.playable.PlayState;
import org.jflux.api.common.rk.playable.PlayableListener;
import org.jflux.api.common.rk.utils.TimeUtils;
import org.mechio.api.animation.player.AnimationJob;
import org.mechio.api.animation.player.AnimationJobListener;
import org.mechio.api.animation.editor.ChannelEditor;
import org.mechio.api.animation.Animation;
import org.mechio.api.animation.Channel;
import org.rwshop.swing.animation.timeline.layers.AnimationProgressLayer;
import org.rwshop.swing.common.utils.SettingsRepository;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.animation.config.PathProperties;
import org.rwshop.swing.common.scaling.DefaultCoordinateScalar;

import static org.jflux.api.common.rk.playable.PlayState.COMPLETED;
import static org.jflux.api.common.rk.playable.PlayState.PAUSED;
import static org.jflux.api.common.rk.playable.PlayState.PENDING;
import static org.jflux.api.common.rk.playable.PlayState.RUNNING;
import static org.jflux.api.common.rk.playable.PlayState.STOPPED;
import org.jflux.api.core.Listener;
import org.mechio.api.animation.player.AnimationPlayer;
import org.mechio.api.animation.protocol.AnimationSignal;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationJobPanel extends javax.swing.JPanel
        implements PlayableListener, AnimationJobListener,
        Listener<AnimationSignal> {
    /**
     *
     */
    public final static String ANIMATION_JOB_LABEL = "Animation Job";
    private AnimationJob myAnimationJob;
    private List<ActionListener> myCloseListeners;
    private AnimationProgressLayer myAnimationLayer;
    private CoordinateScalar myScalar;
    private long myStartTime;
    private long myAnimationLength;
    private PlayState myCachedPlayState;
    private AnimationPlayer myPlayer;
    
    /** Creates new form AnimationJobPanel */
    public AnimationJobPanel() {
        initComponents();
        myStartTime = 0;
        if(myHeader == null){
            return;
        }
        myHeader.addCloseActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
        }});
    }

    /**
     *
     * @return
     */
    public AnimationJob getAnimationJob(){
        return myAnimationJob;
    }

    /**
     *
     * @param job
     */
    public void setAnimationJob(AnimationJob job){
        if(myAnimationJob != null){
            myAnimationJob.removePlayableListener(this);
            myAnimationJob.removeAnimationListener(this);
        }
        myAnimationJob = job;
        myPlayControl.setPlayable(job);
        if(myAnimationJob == null){
            return;
        }
        myAnimationJob.addPlayableListener(this);
        myAnimationJob.addAnimationListener(this);
        VersionProperty version = myAnimationJob.getAnimation().getVersion();
        myHeader.setTitle(version.getName(), version.getNumber());
        myHeader.setLabel(null);//ANIMATION_JOB_LABEL);
        setHeaderState(myAnimationJob.getPlayState());
        Animation anim = myAnimationJob.getAnimation();
        Map<Integer,PathProperties> props = new HashMap();
        for(Channel chan : anim.getChannels()){
            int id = chan.getId();
            Color col = ChannelEditor.getChannelColor(id);
            PathProperties chanProps = new PathProperties(null, col, null);
            props.put(chan.getId(), chanProps);
        }
        myScalar = new DefaultCoordinateScalar();
        myAnimationRenderer.setScalar(myScalar);
        myAnimationRenderer.setAnimation(anim, props);
        myAnimationLength = myAnimationJob.getAnimationLength();
        String len = TimeUtils.timeString(myAnimationJob.getAnimationLength());
        myTotalTimeLabel.setText(len);

        myAnimationLayer = new AnimationProgressLayer(myScalar);
        myAnimationLayer.setPanel(myAnimationRenderer);
        myAnimationRenderer.addLayer(myAnimationLayer);
        myAnimationJob.addAnimationListener(myAnimationLayer);
        updateTime(job.getElapsedPlayTime(TimeUtils.now()));
        boolean loop = myAnimationJob.getLoop();
        myLoopCheckBox.setSelected(loop);
        
        myPlayer = myAnimationJob.getSource();
        myPlayer.addAnimationSignalListener(this);
    }

    private void updateTime(long time) {
        long elapsed = time;
        if(elapsed > myAnimationLength){
            elapsed = myAnimationLength;
        }
        String elStr = TimeUtils.timeString(elapsed);
        myElapsedTimeLabel.setText(elStr);
    }

    /**
     *
     * @param listener
     */
    public void addCloseListener(ActionListener listener){
        if(myCloseListeners == null){
            myCloseListeners = new ArrayList();
        }
        myCloseListeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeCloseListener(ActionListener listener){
        if(myCloseListeners == null){
            return;
        }
        myCloseListeners.remove(listener);
    }

    /**
     *
     */
    protected void close(){
        if(myCloseListeners == null){
            return;
        }
        ActionEvent event = new ActionEvent(this, 0, null);
        for(ActionListener listener : myCloseListeners){
            listener.actionPerformed(event);
        }
        
        if(myPlayer != null) {
            myPlayer.removeAnimationSignalListener(this);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myHeader = new org.rwshop.swing.common.ComponentHeaderPanel();
        myPlayControl = new org.rwshop.swing.common.PlayControlPanel();
        myElapsedTimeLabel = new javax.swing.JLabel();
        myTotalTimeLabel = new javax.swing.JLabel();
        myAnimationBorder = new javax.swing.JPanel();
        myAnimationRenderer = new org.rwshop.swing.animation.AnimationRenderer();
        myLoopCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        myElapsedTimeLabel.setText("Elapsed Time");

        myTotalTimeLabel.setText("Total Time");

        myAnimationBorder.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout myAnimationRendererLayout = new javax.swing.GroupLayout(myAnimationRenderer);
        myAnimationRenderer.setLayout(myAnimationRendererLayout);
        myAnimationRendererLayout.setHorizontalGroup(
            myAnimationRendererLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 177, Short.MAX_VALUE)
        );
        myAnimationRendererLayout.setVerticalGroup(
            myAnimationRendererLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout myAnimationBorderLayout = new javax.swing.GroupLayout(myAnimationBorder);
        myAnimationBorder.setLayout(myAnimationBorderLayout);
        myAnimationBorderLayout.setHorizontalGroup(
            myAnimationBorderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(myAnimationRenderer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        myAnimationBorderLayout.setVerticalGroup(
            myAnimationBorderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(myAnimationRenderer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        myLoopCheckBox.setAlignmentY(0.0F);
        myLoopCheckBox.setBorder(null);
        myLoopCheckBox.setContentAreaFilled(false);
        myLoopCheckBox.setIconTextGap(0);
        myLoopCheckBox.setMaximumSize(new java.awt.Dimension(20, 16));
        myLoopCheckBox.setMinimumSize(new java.awt.Dimension(20, 16));
        myLoopCheckBox.setPreferredSize(new java.awt.Dimension(20, 16));
        myLoopCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myLoopCheckBoxActionPerformed(evt);
            }
        });

        jLabel1.setText("Loop");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(myHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(myPlayControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(myLoopCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(myElapsedTimeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(myTotalTimeLabel))
                    .addComponent(myAnimationBorder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(myHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(myPlayControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(myAnimationBorder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(myElapsedTimeLabel)
                    .addComponent(myTotalTimeLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addComponent(myLoopCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel1))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void myLoopCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myLoopCheckBoxActionPerformed
        boolean val = myLoopCheckBox.isSelected();
        myAnimationJob.setLoop(val);
    }//GEN-LAST:event_myLoopCheckBoxActionPerformed




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel myAnimationBorder;
    private org.rwshop.swing.animation.AnimationRenderer myAnimationRenderer;
    private javax.swing.JLabel myElapsedTimeLabel;
    private org.rwshop.swing.common.ComponentHeaderPanel myHeader;
    private javax.swing.JCheckBox myLoopCheckBox;
    private org.rwshop.swing.common.PlayControlPanel myPlayControl;
    private javax.swing.JLabel myTotalTimeLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void playStateChanged(PlayState prev, PlayState state, long time) {
        setHeaderState(state);
        Color col = AnimationProgressLayer.PLAYING_COLOR;
        switch(state){
            case PAUSED:
                col = AnimationProgressLayer.PAUSED_COLOR;
                break;
            case STOPPED:
                col = AnimationProgressLayer.STOPPED_COLOR;
                break;
            case COMPLETED:
                col = AnimationProgressLayer.COMPLETED_COLOR;
                break;
        }
        myAnimationLayer.setColor(col);
        myAnimationRenderer.repaint();
        myCachedPlayState = state;
    }

    private void setHeaderState(PlayState state){
        myHeader.setStatus(state.name());
        Icon[] icons = SettingsRepository.getPlayStatusIcons();
        Icon icon = null;
        switch(state){
            case RUNNING: icon = icons[0]; break;
            case PAUSED: icon = icons[1]; break;
            case STOPPED: icon = icons[2]; break;
            case PENDING: icon = icons[2]; break;
            case COMPLETED: icon = icons[3]; break;
        }
        myHeader.setIcon(icon);
    }

    /**
     *
     * @param time
     */
    @Override
    public void animationAdvanced(long time) {
        updateTime(time);
    }

    @Override
    public void animationStart(long start, Long end) {
        myStartTime = start;
    }

    @Override
    public void handleEvent(AnimationSignal input) {
        PlayState state;
        
        if(input.getEventType().equals(AnimationSignal.EVENT_CANCEL)) {
            state = STOPPED;
        } else if(input.getEventType().equals(AnimationSignal.EVENT_COMPLETE)) {
            state = COMPLETED;
        } else if(input.getEventType().equals(AnimationSignal.EVENT_PAUSE)) {
            state = PAUSED;
        } else if(input.getEventType().equals(AnimationSignal.EVENT_RESUME) ||
                input.getEventType().equals(AnimationSignal.EVENT_START)) {
            state = RUNNING;
        } else {
            throw new IllegalArgumentException("Invalid signal type.");
        }
        
        playStateChanged(myCachedPlayState, state, input.getAnimationLength());
        myPlayControl.playStateChanged(
                myCachedPlayState, state, input.getAnimationLength());
        
        boolean loop =
                input.getAnimationProperties().contains(
                        AnimationSignal.PROP_LOOP);
        
        if(state == STOPPED && !myLoopCheckBox.isSelected()) {
            myLoopCheckBox.setSelected(false);
        } else {
            myLoopCheckBox.setSelected(loop);
        }
    }
}
