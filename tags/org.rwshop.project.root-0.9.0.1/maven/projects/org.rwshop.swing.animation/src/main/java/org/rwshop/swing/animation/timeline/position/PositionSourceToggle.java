/*
 * Copyright 2013 Hanson Robokind LLC.
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
package org.rwshop.swing.animation.timeline.position;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

/**
 *
 * @author Jason G. Pallack <jgpallack@gmail.com>
 */
public class PositionSourceToggle implements ActionListener {
    private PositionSource myPositionSource;
    private boolean myAdd;
    private JPanel myPanel;

    public PositionSourceToggle(
            PositionSource source, boolean add) {
        myPositionSource = source;
        myAdd = add;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(myAdd) {
            myPositionSource.toggleAddEnabled();
        } else {
            myPositionSource.toggleEditEnabled();
        }
        
        RepaintManager.currentManager(myPanel).markCompletelyDirty(myPanel);
    }
    
    public void setPanel(JPanel panel) {
        myPanel = panel;
    }
}