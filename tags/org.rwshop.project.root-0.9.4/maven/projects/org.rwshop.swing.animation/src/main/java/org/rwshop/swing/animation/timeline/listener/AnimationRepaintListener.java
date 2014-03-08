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

package org.rwshop.swing.animation.timeline.listener;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import org.mechio.api.animation.editor.EditState;
import org.mechio.api.animation.editor.EditorListener;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationRepaintListener extends EditorListener{
    private JComponent myPanel;
    private boolean myPaint;

    /**
     *
     * @param panel
     */
    public AnimationRepaintListener(JComponent panel){
        myPanel = panel;
        myPaint = true;
    }

    /**
     *
     * @param val
     */
    public void setPaint(boolean val){
        myPaint = val;
    }

    /**
     *
     */
    public void repaint(){
        if(!myPaint){
            return;
        }
        RepaintManager.currentManager(myPanel).markCompletelyDirty(myPanel);
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param oldIndex
     * @param newIndex
     */
    @Override
    public void selectionChanged(Object invoker, Object controller, int oldIndex, int newIndex) {
        repaint();
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    @Override
    public void itemAdded(Object invoker, Object controller, int index) {
        repaint();
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param index
     */
    @Override
    public void itemRemoved(Object invoker, Object controller, int index) {
        repaint();
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param oldIndex
     * @param newIndex
     */
    @Override
    public void itemMoved(Object invoker, Object controller, int oldIndex, int newIndex) {
        repaint();
    }

    /**
     *
     * @param invoker
     * @param controller
     * @param state
     * @param value
     */
    @Override
    public void stateChanged(Object invoker, Object controller, EditState state, boolean value) {
        repaint();
    }

    /**
     *
     * @param invoker
     * @param controller
     */
    @Override
    public void structureChanged(Object invoker, Object controller){
       repaint();
    }
}
