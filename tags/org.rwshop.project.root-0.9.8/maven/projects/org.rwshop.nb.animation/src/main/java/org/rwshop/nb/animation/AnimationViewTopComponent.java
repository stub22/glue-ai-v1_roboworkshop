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

package org.rwshop.nb.animation;

import java.awt.Container;
import java.util.Collection;
import java.util.logging.Logger;
import org.rwshop.nb.animation.history.UndoRedoHistoryStack;
import org.mechio.api.animation.editor.AnimationEditor;
import org.rwshop.swing.animation.table.animation.AnimationTable;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Top component which displays something.
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
@ConvertAsProperties(dtd = "-//org.rwshop.nb.animation//AnimationView//EN",
autostore = false)
public final class AnimationViewTopComponent extends TopComponent implements LookupListener {
    private Lookup.Result result = null;
    private static AnimationViewTopComponent instance;
    private AnimationTable myTable;
    private AnimationNode myNode;
    private AnimationEditor myController;
    private InstanceContent myContent;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "AnimationViewTopComponent";

    public AnimationViewTopComponent() {
        initComponents();
        myContent = new InstanceContent();
        associateLookup(new AbstractLookup(myContent));
        myTable = new AnimationTable();
        myTable.hideHeader();
        setComponent(myTable);
        setName(NbBundle.getMessage(AnimationViewTopComponent.class, "CTL_AnimationViewTopComponent"));
        setToolTipText(NbBundle.getMessage(AnimationViewTopComponent.class, "HINT_AnimationViewTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 54, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized AnimationViewTopComponent getDefault() {
        if (instance == null) {
            instance = new AnimationViewTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the AnimationViewTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized AnimationViewTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(AnimationViewTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof AnimationViewTopComponent) {
            return (AnimationViewTopComponent) win;
        }
        Logger.getLogger(AnimationViewTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener (this);
        result = null;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
    
    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(AnimationNode.class);
        result.allItems();
        result.addLookupListener (this);
    }

    @Override
    public void resultChanged(LookupEvent lookupEvent) {
        Lookup.Result r = (Lookup.Result) lookupEvent.getSource();
        Collection c = r.allInstances();
        if (!c.isEmpty()) {
            myNode = (AnimationNode)c.iterator().next();
            myController = myNode.getAnimationController();
            myTable.setController(myController);
            myNode.registerCookies(myContent, getLookup());
        } else {
            //myTable.setController(null);
        }
    }

    @Override
    public UndoRedo getUndoRedo() {
        if(myController == null){
            return super.getUndoRedo();
        }
        return (UndoRedoHistoryStack)myController.getSharedHistory();
    }
}
