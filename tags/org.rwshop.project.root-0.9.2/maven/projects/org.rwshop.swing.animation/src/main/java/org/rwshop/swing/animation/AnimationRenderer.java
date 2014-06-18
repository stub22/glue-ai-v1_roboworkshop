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

package org.rwshop.swing.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.robokind.api.animation.Animation;
import org.robokind.api.animation.Channel;
import org.rwshop.swing.common.utils.SettingsRepository;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.DrawableLayer;
import org.rwshop.swing.animation.config.PropertySet;
import org.rwshop.swing.animation.config.UIStateProperties;
import org.rwshop.swing.animation.config.PathProperties;

/**
 * Renders a static animation.
 * @author Matthew Stevenson <www.robokind.org>
 */
public class AnimationRenderer extends javax.swing.JPanel implements ComponentListener {
    private CoordinateScalar myScalar;
    private List<DrawableLayer> myLayers;
    private double myLength;

    /**
     *
     */
    public AnimationRenderer() {
        initComponents();
        myLayers = new ArrayList<DrawableLayer>();
        addComponentListener(this);
    }

    /**
     *
     * @param scalar
     */
    public void setScalar(CoordinateScalar scalar){
        myScalar = scalar;
    }
    
    public CoordinateScalar getScaler(){
        return myScalar;
    }

    /**
     *
     * @param layer
     */
    public void addLayer(DrawableLayer layer){
        myLayers.add(layer);
    }

    /**
     *
     * @param animation
     * @param properties
     */
    public void setAnimation(Animation animation, Map<Integer,PathProperties> properties){
        if(myScalar == null){
            return;
        }
        myLength = 0;
        for(Channel channel : animation.getChannels()){
            int id = channel.getId();
            PathProperties prop = properties.get(id);
            List<Point2D> points = channel.getInterpolatedPoints(-1, -1);
            if(points.isEmpty()){
                continue;
            }
            Point2D end = points.get(points.size()-1);
            double time = end.getX();
            if(time > myLength){
                myLength = time;
            }
            if(prop == null){
                prop = new PathProperties(null, Color.red, null);
            }
            UIStateProperties<PathProperties> uisp = new UIStateProperties(prop, null, null);
            PropertySet ps = new PropertySet(uisp, PathProperties.Helper);
            myLayers.add(new PathRenderer(points, ps, myScalar));
        }
        setSize();
        repaint();
    }

    @Override public void paintComponent(Graphics g) {
        setSize();
        super.paintComponent(g);
        if(SettingsRepository.getAntiAliasingRenderer()){
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        }
        if(myLayers != null){
            for(DrawableLayer layer : myLayers){
                layer.paint(g);
            }
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 84, Short.MAX_VALUE)
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
    public void componentShown(ComponentEvent e) {
        setSize();
    }

    @Override public void componentMoved(ComponentEvent e) {}
    @Override public void componentHidden(ComponentEvent e) {}

    private void setSize(){
        if(myScalar == null){
            return;
        }
        if(myLength <= 0){
            return;
        }
        double w = getWidth();//+5;
        double scale = w/(double)myLength;
        myScalar.setScaleX(scale);
        myScalar.setScaleY(getHeight());
    }    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}