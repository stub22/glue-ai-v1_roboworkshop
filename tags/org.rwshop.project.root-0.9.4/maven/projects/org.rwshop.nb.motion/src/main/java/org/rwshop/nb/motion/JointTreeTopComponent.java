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
package org.rwshop.nb.motion;

import java.util.logging.Logger;
import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.osgi.framework.BundleContext;
import org.mechio.api.motion.jointgroup.JointGroup;

/**
 * 
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
@ConvertAsProperties(dtd = "-//org.rwshop.motion//JointTree//EN",
autostore = false)
public final class JointTreeTopComponent extends TopComponent {

    private static JointTreeTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "JointTreeTopComponent";

    public JointTreeTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(JointTreeTopComponent.class, "CTL_JointTreeTopComponent"));
        setToolTipText(NbBundle.getMessage(JointTreeTopComponent.class, "HINT_JointTreeTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jointTreePanel1 = new org.rwshop.swing.motion.jointgroup.JointTreePanel();
        jToggleButton1 = new javax.swing.JToggleButton();

        jToggleButton1.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton1, org.openide.util.NbBundle.getMessage(JointTreeTopComponent.class, "JointTreeTopComponent.jToggleButton1.text")); // NOI18N
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jointTreePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton1)
                .addContainerGap(107, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jointTreePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1))
        );
    }// </editor-fold>//GEN-END:initComponents

private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
    if(jToggleButton1.isSelected()){
        jointTreePanel1.startUpdates(100);
    }else{
        jointTreePanel1.stopUpdates();
    }

}//GEN-LAST:event_jToggleButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton jToggleButton1;
    private org.rwshop.swing.motion.jointgroup.JointTreePanel jointTreePanel1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized JointTreeTopComponent getDefault() {
        if (instance == null) {
            instance = new JointTreeTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the JointTreeTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized JointTreeTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(JointTreeTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof JointTreeTopComponent) {
            return (JointTreeTopComponent) win;
        }
        Logger.getLogger(JointTreeTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        if (jointTreePanel1.isInitialized()) {
            return;
        }
        BundleContext context = OSGiUtils.getBundleContext(JointGroup.class);
        if (context != null) {
            jointTreePanel1.initialize(context);
        }
        jointTreePanel1.startUpdates(100);
    }

    @Override
    public void componentClosed() {
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
}
