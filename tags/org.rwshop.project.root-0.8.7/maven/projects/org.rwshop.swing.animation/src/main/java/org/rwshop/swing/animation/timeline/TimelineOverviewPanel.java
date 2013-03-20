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
 * TimelineOverviewPanel.java
 *
 * Created on Mar 10, 2011, 8:05:07 PM
 */

package org.rwshop.swing.animation.timeline;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.robokind.api.animation.utils.AnimationUtils;
import org.robokind.api.animation.utils.ChannelsParameterSource;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.animation.editor.ChannelEditor;
import org.robokind.api.animation.utils.ChannelsParameter;
import org.rwshop.swing.animation.timeline.layers.AnimationPlayerLayer;
import org.rwshop.swing.animation.timeline.listener.AnimationRepaintListener;
import org.rwshop.swing.common.utils.SettingsRepository;
import org.rwshop.swing.animation.config.PropertyReader;
import org.rwshop.swing.animation.config.PropertySet;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.animation.ElementSettings;
import org.rwshop.swing.common.DrawableLayer;
import org.rwshop.swing.animation.config.PathProperties.PathConfigRenderer;
import org.rwshop.swing.animation.config.PointProperties.PointConfigRenderer;
import org.rwshop.swing.common.scaling.DefaultCoordinateScalar;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class TimelineOverviewPanel extends javax.swing.JPanel implements ComponentListener{//, IAnimationListener {
    private CoordinateScalar myScalar;
    private AnimationEditor myController;
    private TimelineAnimation myAnimationTimeline;
    private AnimationRepaintListener myRepainter;
    private List<DrawableLayer> myLayers;
    private AnimationPlayerLayer myPlayerListener;
    private ElementSettings myElementSettings;
    private boolean myInitializedFlag;

    /**
     *
     */
    public TimelineOverviewPanel() {
        initComponents();
        myInitializedFlag = false;
        ensureInitialized();
    }
    
    private boolean ensureInitialized(){
        if(myInitializedFlag){
            return true;
        }
        if(!SettingsRepository.isInitialized()){
            return false;
        }
        myInitializedFlag = true;
        try{
            myScalar = new DefaultCoordinateScalar();
            myScalar.setXOffset(5);
            myScalar.setYOffset(5);
            PropertySet pathProps = PropertyReader.getPathProperties(null, "simple.channel");
            PropertySet pointProps = PropertyReader.getPointProperties("simplepoint");
            PathConfigRenderer pathRenderer = new PathConfigRenderer(pathProps);
            PointConfigRenderer pointRenderer = new PointConfigRenderer(pointProps);

            myElementSettings = new ElementSettings(myScalar, pathRenderer, pointRenderer);
            myElementSettings.setScaler(myScalar);
            myRepainter = new AnimationRepaintListener(this);
            //myPlayerListener = new AnimationPlayerLayer(this, myScalar);

            myLayers = new ArrayList<DrawableLayer>();
            //myLayers.add(myPlayerListener);
            addComponentListener(this);
        }catch(Throwable t){
            myInitializedFlag = false;
        }
        return myInitializedFlag;
    }

    /**
     *
     * @param layer
     */
    public void addLayer(DrawableLayer layer){
        if(!ensureInitialized()){
            return;
        }
        myLayers.add(layer);
    }

    /**
     *
     * @param controller
     */
    public void setController(AnimationEditor controller){
        if(!ensureInitialized()){
            return;
        }
        if(myAnimationTimeline != null){
            myAnimationTimeline.cleanConsumer();
        }
        myRepainter.cleanConsumer();
        myController = controller;
        myAnimationTimeline = new TimelineAnimation(myController, myElementSettings);

        if(myController == null){
            return;
        }
        myController.addConsumer(myAnimationTimeline);
        myController.recursiveAdd(myRepainter);
        setSize();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });
    }

    @Override public void paintComponent(Graphics g) {
        setSize();
        super.paintComponent(g);
        if(!ensureInitialized() || 
                myAnimationTimeline == null || myController == null){
            return;
        }
        if(SettingsRepository.getAntiAliasing()){
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        }
        myAnimationTimeline.paint(g);
        for(DrawableLayer layer : myLayers){
            layer.paint(g);
        }
        ChannelEditor channel = myController.getSelected();
        if(channel == null){
            return;
        }
        ChannelsParameterSource paramSource = 
                AnimationUtils.getChannelsParameterSource();
        if(paramSource == null){
            return;
        }
        int id = channel.getId();
        Double def = null;
        
        for(ChannelsParameter chanParam: paramSource.getChannelParameters()) {
            if(chanParam.getChannelID() == id) {
                def = chanParam.getDefaultPosition().getValue();
                break;
            }
        }
        
        if(def == null){
            return;
        }
        int y = (int)myScalar.scaleY(def);
        g.setColor(channel.getPrimaryColor());
        g.drawLine(0, y, getWidth(), y);

    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 54, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void componentResized(ComponentEvent e) {
        setSize();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {
        setSize();
    }

    @Override
    public void componentHidden(ComponentEvent e) {}

    private void setSize(){
        if(!ensureInitialized() || myController == null){
            return;
        }
        double w = getWidth()-10;
        long time = myController.getEnd();
        double scale = w/(double)time;
        myScalar.setScaleX(scale);
        myScalar.setScaleY(getHeight()-10);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
