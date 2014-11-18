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
 * ResizableGrid.java
 *
 * Created on Feb 3, 2011, 11:14:25 PM
 */

package org.rwshop.swing.common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.rwshop.swing.common.scaling.CoordinateScalar;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class ResizableGrid extends javax.swing.JPanel implements ComponentListener{
    /**
     *
     */
    protected CoordinateScalar myScalar;
    private int myKeyHeight = 30;
    private int myKeyPlacement;
    /** Creates new form ResizableGrid */
    public ResizableGrid() {
        initComponents();
        addComponentListener(this);
    }

    /**
     *
     * @param s
     * @param keyHeight
     */
    public void init(CoordinateScalar s, int keyHeight){
        myKeyHeight = keyHeight;
        myScalar = s;
    }

    @Override
    protected void paintComponent(Graphics g){
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        drawGridLines(g);
        drawTimelineKey(g);
    }
    
    private void drawTimelineKey(Graphics g){
        if(myScalar == null){
            return;
        }
        int w = getWidth();
        int h = getHeight();
        g.setColor(Color.black);
        double tStep = calcStep();
        double max = myScalar.unscaleX(w);
        for(double t=0, i=0; t<max; t+=tStep, i++){
            int l = 5;
            int x = (int)myScalar.scaleX(t);
            if(i%5 == 0 && i != 0){
                g.setColor(Color.lightGray);
                g.drawLine(x, 0, x, h-myKeyHeight);
                g.setColor(Color.black);
            }
            if(i%20 == 0){
                l = 15;
                NumberFormat nf = new DecimalFormat("0.000");
                g.drawString(nf.format(t/1000.0), x, h-17);
            }else if(i%10 == 0){
                l = 11;
            }else if(i%5 == 0){
                l = 8;
            }
            if(i != 0){
                g.drawLine(x, h, x, h-l);
            }
        }
    }

    private double calcStep(){
        double t = myScalar.unscaleX(5);
        double log = Math.log10(t);
        double n1 = log;// + 1.301029996; //log(t*20)
        double n2 = log - 0.301029996;//log10(t*10)
        double n5 = log - 0.698970004;//log10(t*2)
        n1 = Math.round(n1);
        n2 = Math.round(n2);
        n5 = Math.round(n5);
        double t1 = Math.pow(10, n1);
        double t2 = 2.0*Math.pow(10, n2);
        double t5 = 5.0*Math.pow(10, n5);
        n1 = Math.abs(t1 - t);
        n2 = Math.abs(t2 - t);
        n5 = Math.abs(t5 - t);
        if(n1 < n2 && n1 < n5){
            return t1;
        }
        if(n2 < n1 && n2 < n5){
            return t2;
        }
        return t5;
    }

    private void drawGridLines(Graphics g){
        if(myScalar == null){
            return;
        }
        for(int i=0; i<16; i++){
            float y = (float)i/16.0F;
            int h = (int)myScalar.scaleY(y);
            if(y == 0.5F){
                g.setColor(Color.black);
                g.drawLine(0, h-1, getWidth(), h-1);
            }else if(y == 0.75F || y == 0.25F){
                g.setColor(Color.darkGray);
            }else{
                g.setColor(Color.lightGray);
            }
            g.drawLine(0, h, getWidth(), h);
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
            .addGap(0, 12, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void componentHidden(ComponentEvent e){}
    @Override
    public void componentMoved(ComponentEvent e){}
    @Override
    public void componentResized(ComponentEvent e){
        adjustScalar();
    }
    @Override
    public void componentShown(ComponentEvent e){
        adjustScalar();
    }

    private void adjustScalar() {
        if (myScalar == null) {
            return;
        }
        myScalar.setScaleY(getHeight() - myKeyHeight);
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
