/*
 *  Copyright 2011 by The Cogchar Project (www.cogchar.org).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 * WavSpectrogramPanel.java
 *
 * Created on Apr 26, 2011, 12:47:34 AM
 */

package org.rwshop.swing.audio.visualization;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.Beans;
import javax.swing.JComponent;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import org.robokind.api.audio.AudioProgressListener;
import org.robokind.api.audio.WavPlayer;
import org.robokind.api.audio.processing.SampleProcessor.ProcessorListener;
import org.robokind.api.audio.processing.WavProcessor;
import org.robokind.impl.audio.visualization.SpectrogramImage;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.scaling.ScalableComponent;

/**
 *
 * @author Matthew Stevenson <matt@hansonrobokind.com>
 */
public class WavSpectrogramPanel extends javax.swing.JPanel implements ScalableComponent{
    private WavProcessor myWavProc;
    private WavPlayer myWavPlayer;
    private SpectrogramImage myImages;
    private CoordinateScalar myScalar;
    private boolean myInitFlag;
    private double myCurrentPositionMillisec;
    private Image myCachedSubimage;
    private WavListener myWavListener;
    private int myImageDrawWidth;
    
    
    /** Creates new form WavSpectrogramPanel */
    public WavSpectrogramPanel() {
        initComponents();
        myInitFlag = false;
    }
    
    public void init(WavProcessor proc, WavPlayer player){
        if(myScalar == null){
            throw new IllegalStateException(
                    "CoordinateScalar must be set before initialization.");
        }
        if(myWavPlayer != null && myWavListener != null){
            myWavPlayer.removeAudioProgressListener(myWavListener);
        }
        myWavProc = proc;
        myCurrentPositionMillisec = 0;
        myWavListener = new WavListener();
        player.addAudioProgressListener(myWavListener);
        myWavPlayer = player;
        spectAsync();
    }
    
    private void spectAsync(){
        final JComponent comp = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                myImages = new SpectrogramImage(myWavProc, myWavProc.getSamplesBufferSize(), 1, 2.5);
                ProcessorPM meanPL = new ProcessorPM(comp, 
                        "Analyzing Audio", "Mean", 
                        0, (int)myWavProc.getFrameCount());
                ProcessorPM spectPL = new ProcessorPM(comp, 
                        "Analyzing Audio", "Frequencies", 
                        0, (int)myWavProc.getFrameCount());
                myImages.getMeanCalculator().addProcessorListener(meanPL);
                myImages.addProcessorListener(spectPL);
                myImages.createSpectrograms();
                myInitFlag = true;
                cacheImage();
                rescale(0,0);
            }
        }).start();    
    }
    
    @Override
    public void paint(Graphics g){
        if(!myInitFlag){
            return;
        }
        long framesTotal = myWavPlayer.getLengthFrames();
        if(myCachedSubimage == null){
            return;
        }
        if(myCachedSubimage != null){
            int h = getHeight();
            int w = getWidth();
            if(myImageDrawWidth < w){
                g.setColor(Color.BLACK);
                g.fillRect(myImageDrawWidth, 0, w-myImageDrawWidth, h);
            }
            g.drawImage(myCachedSubimage,0,0,myImageDrawWidth,h,null);
        }
        paintProgress(g, framesTotal);
    }
    
    private void cacheImage(){
        if(!myInitFlag){
            return;
        }
        Image fullImg = myImages.getImage(0);
        long totalLen = myWavPlayer.getLengthFrames();
        long delay = myWavPlayer.getStartDelayFrames();
        long start = myWavPlayer.getStartPositionFrame();
        long stop = myWavPlayer.getEndPositionFrame();
        long wavFullLen = totalLen - delay;
        if(wavFullLen <= 0){
            wavFullLen = 1;
        }
        int imgWidth = fullImg.getWidth(null);
        double ratio = (double)imgWidth/(double)wavFullLen;
        int imgStart = (int)(start*ratio);
        int imgStop = (int)(stop*ratio);
        int imgDelay = (int)(delay*ratio);
        
        int newLen = imgWidth + imgDelay;
        int height = fullImg.getHeight(null);
        BufferedImage image = new BufferedImage(newLen, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, imgDelay, height);
        g.drawImage(fullImg, imgDelay, 0, null);
        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.4f));
        g.fillRect(imgDelay, 0, imgStart, height);
        
        int stopPixel = imgDelay + imgStop;
        int stopWidth = imgWidth - imgStop;
        g.fillRect(stopPixel, 0, stopWidth, height);
        g.dispose();
        myCachedSubimage = image;
    }
    
    private void paintProgress(Graphics g, long framesTotal){
        if(myCurrentPositionMillisec <= 0){
            return;
        }
        int w = (int)myScalar.scaleX(myCurrentPositionMillisec);
        g.setColor(new Color(1.0f, 0.0f, 0.0f, 0.3f));
        g.fillRect(0, 0, w, getHeight());
    }
    
    @Override
    public void rescale(int minWidth, int minHeight){
        if(!myInitFlag || myScalar == null){
            return;
        }
        if(myCachedSubimage == null){
            cacheImage();
        }
        myImageDrawWidth = getMinWidth();
        Dimension d = getPreferredSize();
        d.width = Math.max(myImageDrawWidth, minWidth);
        setPreferredSize(d);
        revalidate();
        repaint();
    }

    @Override
    public void setScalar(CoordinateScalar scalar) {
        myScalar = scalar;
    }

    @Override
    public int getMinWidth() {
        if(!myInitFlag){
            return 0;
        }
        double time = myWavPlayer.getLengthMicrosec()/1000.0;
        int drawWidth = (int)myScalar.scaleX(time);
        drawWidth = drawWidth < 1 ? 1 : drawWidth;
        return drawWidth;
    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public void setFocusPosition(Integer x, Integer y) {
    }
    
    class ProcessorPM implements ProcessorListener{
        private ProgressMonitor myMonitor;
        
        public ProcessorPM(Component comp, 
                String title, String note, int start, int count) {
            myMonitor = new ProgressMonitor(comp, title, note, start, count);
            myMonitor.setMillisToDecideToPopup(0);
            myMonitor.setMillisToPopup(0);
        }

        @Override
        public void framesProcessed(int framesDone, int totalFrames) {
            myMonitor.setProgress(framesDone);
            if(framesDone == totalFrames){
                myMonitor.close();
            }
        }
        
    }
    private class WavListener implements AudioProgressListener{

        @Override
        public void update(long frame, double usec) {
            myCurrentPositionMillisec = usec/1000.0;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    repaint();
                }
            });
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

        jScrollPane1 = new javax.swing.JScrollPane();
        wavPlayControlPanel1 = new org.rwshop.swing.audio.wav.WavPlayControlPanel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(wavPlayControlPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wavPlayControlPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private org.rwshop.swing.audio.wav.WavPlayControlPanel wavPlayControlPanel1;
    // End of variables declaration//GEN-END:variables

}
