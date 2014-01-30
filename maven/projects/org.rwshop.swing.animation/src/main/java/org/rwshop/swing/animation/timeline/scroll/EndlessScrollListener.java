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

package org.rwshop.swing.animation.timeline.scroll;

import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class EndlessScrollListener implements AdjustmentListener, ComponentListener {
    private boolean myStartReset = true;
    private Integer myStartValue = null;
    private JPanel myPanel;
    private int myAdjustRate;

    /**
     *
     * @param scrollRate
     */
    public EndlessScrollListener(int scrollRate){
        myAdjustRate = 15;
    }

    /**
     *
     * @param panel
     * @param pane
     */
    public void init(JPanel panel, JScrollPane pane){
        myPanel = panel;
        pane.addComponentListener(this);
        pane.getHorizontalScrollBar().addAdjustmentListener(this);
        resizePanel(0, myPanel.getPreferredSize().width);
    }

    /**
     *
     * @param rate
     */
    public void setAdjustRate(int rate){
        myAdjustRate = rate;
    }

    /**
     *
     * @return
     */
    public int getAdjustRate(){
        return myAdjustRate;
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if(myPanel == null){
            return;
        }
        int max = e.getAdjustable().getMaximum() - e.getAdjustable().getVisibleAmount();
        int val = e.getAdjustable().getValue();
        int extent = e.getAdjustable().getVisibleAmount();
        if(!e.getValueIsAdjusting()){
            handleScrollEnd(e, max, val, extent);
        }else{
            handleDuringScroll(e, max, val, extent);
        }
    }

    private void handleScrollEnd(AdjustmentEvent e, int max, int val, int extent){
        if(myStartValue != null && val < myStartValue){
            //int diff = myStartValue - val;
            int n = myPanel.getPreferredSize().width;
            resizePanel(n, extent);
        }else if(max - val < myAdjustRate){
            int n = myPanel.getPreferredSize().width+myAdjustRate;
            resizePanel(n, extent);
        }
        myStartValue = val;
        myStartReset = true;
    }

    private void handleDuringScroll(AdjustmentEvent e, int max, int val, int extent){
        if(max - val  < myAdjustRate){
            resizePanel(myPanel.getPreferredSize().width + myAdjustRate, extent);
        }
        if(myStartValue == null || myStartReset){
            myStartValue = val;
            myStartReset = false;
        }
    }

    private void resizePanel(int size, int extent){
        size = Math.max(size, extent + myAdjustRate);
        size = Math.max(size, myPanel.getMinimumSize().width);
        myPanel.setPreferredSize(new Dimension(size, myPanel.getPreferredSize().height));
        myPanel.revalidate();
    }

    public void componentResized(ComponentEvent e) {
        if(myPanel == null){
            return;
        }
        resizePanel(0, e.getComponent().getPreferredSize().width);
    }

    public void componentMoved(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}

    public void componentHidden(ComponentEvent e) {}
}
