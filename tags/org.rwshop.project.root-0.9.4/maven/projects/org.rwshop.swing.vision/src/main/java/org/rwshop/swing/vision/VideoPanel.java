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
 * VideoPanel.java
 *
 * Created on Jul 8, 2011, 8:42:02 PM
 */
package org.rwshop.swing.vision;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.SwingUtilities;
import org.jflux.api.core.Listener;
import org.mechio.api.vision.ImageEvent;
import org.mechio.api.vision.ImageRegion;
import org.mechio.api.vision.ImageRegionList;
import org.mechio.impl.vision.PortableImageUtils;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class VideoPanel extends javax.swing.JPanel{
    private final static double IMAGE_WIDTH = 320.0;
    private final static double IMAGE_HEIGHT = 240.0;
    
    private Image myImage;
    private ImageRegionList<ImageRegion> myImageRegions;
    private Runnable myRepaint;
    private ImageCache myImageCache;
    private ImageRegionListCache myImageRegionListCache;
    
    /** Creates new form VideoPanel */
    public VideoPanel() {
        initComponents();
        myRepaint = new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        };
        myImageCache = new ImageCache();
        myImageRegionListCache = new ImageRegionListCache();
    }
    
    public Listener<ImageEvent> getImageEventListener(){
        return myImageCache;
    }
    
    public Listener<ImageRegionList> getImageRegionListListener(){
        return myImageRegionListCache;
    }
    
    @Override public void paint(Graphics g){
        if(myImage != null){
            g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
        }
        if(myImageRegions != null && myImageRegions.getRegions() != null){
            g.setColor(Color.red);
            double width = getWidth();
            double height = getHeight();
            double wRatio = width/IMAGE_WIDTH;
            double hRatio = height/IMAGE_HEIGHT;
            for(ImageRegion r: myImageRegions.getRegions()){
                int x = (int)(r.getX() * wRatio);
                int y = (int)(r.getY() * hRatio);
                int w = (int)(r.getWidth() * wRatio);
                int h = (int)(r.getHeight() * hRatio);
                g.drawRect(x, y, w, h);
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
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    class ImageCache implements Listener<ImageEvent>{        
        @Override
        public void handleEvent(ImageEvent event) {
            if(event == null){
                return;
            }
            Image img = PortableImageUtils.unpackImage(event);
            if(img == null){
                return;
            }
            myImage = img;
            SwingUtilities.invokeLater(myRepaint);
        }
        
    }
    
    class ImageRegionListCache implements Listener<ImageRegionList>{        
        @Override
        public void handleEvent(ImageRegionList event) {
            if(event == null){
                return;
            }
            myImageRegions = event;
        }
        
    }
}
