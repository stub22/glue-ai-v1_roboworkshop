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

package org.rwshop.swing.common;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class FocusRequestor implements MouseListener, MouseWheelListener, KeyListener {
    private JPanel myPanel;
    
    /**
     *
     * @param panel
     * @return
     */
    public static FocusRequestor setFocusable(JPanel panel){
        panel.setFocusable(true);
        FocusRequestor f = new FocusRequestor();
        f.init(panel);
        return f;
    }

    private void init(JPanel panel){
        myPanel = panel;
        myPanel.addMouseListener(this);
        myPanel.addMouseWheelListener(this);
        myPanel.addKeyListener(this);
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        myPanel.requestFocusInWindow();
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseWheelMoved(MouseWheelEvent e) {
        myPanel.requestFocusInWindow();
    }

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        myPanel.requestFocusInWindow();
    }

    public void keyReleased(KeyEvent e) {}
}
