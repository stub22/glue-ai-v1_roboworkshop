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
 * AnimationTimelineEditor.java
 *
 * Created on Mar 15, 2011, 5:02:23 PM
 */

package org.rwshop.nb.animation;

import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import org.rwshop.nb.animation.history.UndoRedoHistoryStack;
import org.mechio.api.animation.editor.AnimationEditor;
import org.rwshop.swing.animation.timeline.scroll.AnimationScrollPanel;
import org.openide.awt.UndoRedo;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.mechio.api.animation.editor.AbstractEditor;
import org.rwshop.swing.common.scaling.ScalingManager;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationTimelineEditor extends TopComponent implements ComponentListener, PropertyChangeListener {
    private InstanceContent content = new InstanceContent();
    private AnimationScrollPanel myPanel;
    private AnimationEditor myController;
    private CoordinateScalar myScalar;
    private boolean myIsInitialized;
    /** Creates new form AnimationTimelineEditor */
    public AnimationTimelineEditor() {
        initComponents();
        myIsInitialized = false;
        myPanel = new AnimationScrollPanel();
        setComponent(myPanel);
        addComponentListener(this);
        associateLookup (new AbstractLookup(content));
    }
    
    public void init(ScalingManager scalingManager){
        myPanel.init(scalingManager);
    }
    
    public void setController(AnimationEditor controller){
        if(myController != null){
            myController.removePropertyChangeListener(this);
        }
        myController = controller;
        myPanel.setController(myController);
        AnimationNode an = new AnimationNode(myController);
        content.set(Collections.singleton (an), null);
        String name = myController == null ? "Untitled Animation" : myController.getName();
        setAnimName(name);
        if(myController != null){
            myController.addPropertyChangeListener(this);
        }
        an.registerCookies(content, getLookup());
        //content.add(an.getCookie(SaveCookie.class));
        //setActivatedNodes(new Node[]{an});
    }
    
    

    private void setComponent(Container component){
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(component, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(component, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
    
    private void setAnimName(String name){
        setName(name);
        setDisplayName(name);
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

    @Override
    public int getPersistenceType(){
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public UndoRedo getUndoRedo() {
        if(myController == null){
            return super.getUndoRedo();
        }
        return (UndoRedoHistoryStack)myController.getSharedHistory();
    }

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentResized(ComponentEvent e) {
        if(!myIsInitialized){
            myPanel.resetSize();
            myIsInitialized = true;
        }
    }
    @Override
    public void componentHidden(ComponentEvent e) {}
    @Override
    public void componentMoved(ComponentEvent e) {}

    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(AbstractEditor.PROP_NAME)){
            setAnimName(evt.getNewValue().toString());
        }
    }
}
