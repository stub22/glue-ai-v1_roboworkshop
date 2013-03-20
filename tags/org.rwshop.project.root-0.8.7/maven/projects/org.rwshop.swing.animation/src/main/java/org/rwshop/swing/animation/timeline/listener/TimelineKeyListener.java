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

package org.rwshop.swing.animation.timeline.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map.Entry;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.robokind.api.animation.utils.AnimationUtils;
import org.robokind.api.animation.utils.ChannelsParameterSource;
import org.rwshop.swing.animation.actions.FileAction.Save;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.animation.editor.history.HistoryStack;
import org.robokind.api.animation.Channel;
import org.robokind.api.animation.MotionPath;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.robokind.api.common.utils.RKSource.SourceImpl;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class TimelineKeyListener implements KeyListener {
    private JPanel myPanel;
    private AnimationEditor myController;
    private int myKeyMode;
    private CoordinateScalar myScalar;
    private HistoryStack mySharedHistory;

    /**
     *
     * @param p
     * @param s
     */
    public TimelineKeyListener(JPanel p, CoordinateScalar s){
        myScalar = s;
        myPanel = p;
        myKeyMode = 0;
    }

    /**
     *
     * @param controller
     */
    public void setController(AnimationEditor controller){
        myController = controller;
        if(myController == null){
            mySharedHistory = null;
        }else{
            mySharedHistory = myController.getSharedHistory();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(myController == null || mySharedHistory == null){
            return;
        }
        if(e.isControlDown() && ((e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z) || e.getKeyCode() == KeyEvent.VK_Y)){
            mySharedHistory.forward(1);
            e.consume();
            return;
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
            mySharedHistory.back(1);
            e.consume();
            return;
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
            
        }else{
            kp0(e);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                myPanel.repaint();
            }
        });
    }

    private void kp0(KeyEvent e){
        if(myController == null || mySharedHistory == null){
            return;
        }
        if(!e.isControlDown()){
            return;
        }
        switch(e.getKeyCode()){
            case KeyEvent.VK_S:
                try{
                    new Save(new SourceImpl(myController), true).actionPerformed(null);
                }catch(Throwable t){
                    t.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
