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

package org.rwshop.swing.animation.timeline;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import org.osgi.framework.BundleContext;
import org.robokind.api.animation.utils.AnimationUtils;
import org.robokind.api.animation.utils.ChannelsParameterSource;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.animation.editor.ChannelEditor;
import org.robokind.api.animation.utils.ChannelsParameter;
import org.robokind.api.animation.utils.PositionAdder;
import org.robokind.api.common.osgi.OSGiUtils;
import org.rwshop.swing.animation.timeline.listener.AnimationRepaintListener;
import org.rwshop.swing.animation.timeline.listener.TimelineMouseListener;
import org.rwshop.swing.animation.timeline.listener.TimelineContextMenuManager;
import org.rwshop.swing.animation.timeline.listener.TimelineKeyListener;
import org.rwshop.swing.animation.config.PropertyReader;
import org.rwshop.swing.animation.config.PropertySet;
import org.rwshop.swing.common.ResizableGrid;
import org.rwshop.swing.common.FocusRequestor;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.DrawableLayer;
import org.rwshop.swing.common.utils.SettingsRepository;
import org.rwshop.swing.animation.ElementSettings;
import org.rwshop.swing.animation.config.PathProperties.PathConfigRenderer;
import org.rwshop.swing.animation.config.PointProperties.PointConfigRenderer;
import org.rwshop.swing.animation.timeline.position.PositionSourceComponent;
import org.rwshop.swing.animation.timeline.range.TimeRangeComponent;
import org.rwshop.swing.common.scaling.ScalableComponent;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class AnimationTimelinePanel extends ResizableGrid implements ScalableComponent{
    private AnimationEditor myController;
    private TimelineAnimation myAnimationTimeline;
    private TimelineMouseListener myMotionPathMouseListener;
    private TimelineKeyListener myKeyListener;
    private AnimationRepaintListener myRepainter;
    private List<DrawableLayer> myLayers;
    private ElementSettings myElementSettings;
    private TimelineContextMenuManager myContextMenu;
    private TimeRangeComponent myTimeRangeComponent;
    private PositionSourceComponent myPositionSourceComponent;

    /**
     *
     */
    public AnimationTimelinePanel() {
        initComponents();
    }

    /**
     *
     * @param scalar
     */
    @Override
    public void setScalar(CoordinateScalar scalar){
        myScalar = scalar;

        PropertySet pathProps = PropertyReader.getPathProperties(null, "default.channel");
        PropertySet pointProps = PropertyReader.getPointProperties("controlpoint");
        PathConfigRenderer pathRenderer = new PathConfigRenderer(pathProps);
        PointConfigRenderer pointRenderer = new PointConfigRenderer(pointProps);
        
        myElementSettings = new ElementSettings(myScalar, pathRenderer, pointRenderer);
        myRepainter = new AnimationRepaintListener(this);
        myContextMenu = new TimelineContextMenuManager(this, myScalar);
        myMotionPathMouseListener = new TimelineMouseListener(myScalar, myContextMenu);
        myKeyListener = new TimelineKeyListener(this, myScalar);
        myTimeRangeComponent = new TimeRangeComponent();
        //myInfoLayer = new AnimationInfoLayer();

        FocusRequestor.setFocusable(this);
        addMouseListener(myMotionPathMouseListener);
        addMouseMotionListener(myMotionPathMouseListener);
        addMouseWheelListener(myMotionPathMouseListener);
        addKeyListener(myMotionPathMouseListener);
        addKeyListener(myKeyListener);
        
        
        myLayers = new ArrayList<DrawableLayer>();
        addTimelineComponent(myTimeRangeComponent);
        
        try{
            BundleContext context = OSGiUtils.getBundleContext(PositionAdder.class);
            myPositionSourceComponent = new PositionSourceComponent(context);
        }catch(Exception ex){
            myPositionSourceComponent = null;
        }
        if(myPositionSourceComponent != null){
            addTimelineComponent(myPositionSourceComponent);
            myMotionPathMouseListener.setPositionSource(myPositionSourceComponent.getPositionSource());
        }
    }
    
    private void addTimelineComponent(TimelineComponent comp){
        myContextMenu.addMenuProvider(comp.getMenuProvider());
        comp.setScalar(myScalar);
        comp.setPanel(this);
        myLayers.add(comp.getDrawLayer());
    }

    /**
     *
     * @param controller
     */
    public void setController(AnimationEditor controller){
        if(myAnimationTimeline != null){
            myAnimationTimeline.cleanConsumer();
        }
        myRepainter.cleanConsumer();

        myController = controller;
        myAnimationTimeline = new TimelineAnimation(myController, myElementSettings);
        //myInfoLayer.setController(myController);
        myMotionPathMouseListener.setAnimation(myController, myAnimationTimeline);
        myKeyListener.setController(myController);
        myContextMenu.setTimeline(myAnimationTimeline);
        myTimeRangeComponent.setEditor(controller);
        if(myPositionSourceComponent != null){
            myPositionSourceComponent.setEditor(controller);
        }
        
        if(myController == null){
            return;
        }
        myController.addConsumer(myAnimationTimeline);
        myController.recursiveAdd(myRepainter);
        resetSize();
        repaint();
    }

    /**
     *
     * @return
     */
    public CoordinateScalar getScalar(){
        return myScalar;
    }

    /**
     *
     */
    public void resetSize(){
        setMinimumSize(new Dimension(getMinWidth(), getPreferredSize().height));
    }

    /**
     *
     * @return
     */
    @Override
    public int getMinWidth(){
        if(myController == null){
            return 1;
        }
        long time = myController.getEnd();
        return (int)myScalar.scaleX(time) + SettingsRepository.theAnimationWidthOffset;
    }

    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(myAnimationTimeline == null || myController == null){
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
        if(channel != null){
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
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public void rescale(int minWidth, int minHeight) {
        int minAnim = getMinWidth();
        int minPnl = getMinimumSize().width;
        int size = Math.max(minAnim, minPnl);
        size = Math.max(size, minWidth);
        setPreferredSize(new Dimension(size, getPreferredSize().height));
        revalidate();
        repaint();
    }

    @Override
    public void setFocusPosition(Integer x, Integer y) {}

}
