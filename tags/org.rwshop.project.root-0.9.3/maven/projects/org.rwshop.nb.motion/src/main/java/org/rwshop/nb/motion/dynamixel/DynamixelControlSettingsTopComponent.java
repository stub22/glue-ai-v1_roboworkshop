/*
 * Copyright 2012 Hanson Robokind LLC.
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
package org.rwshop.nb.motion.dynamixel;

import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.jflux.impl.services.rk.osgi.SingleServiceListener;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.osgi.framework.BundleContext;
import org.robokind.api.motion.servos.ServoController;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.rwshop.nb.motion.dynamixel//DynamixelControlSettings//EN",
autostore = false)
@TopComponent.Description(preferredID = "DynamixelControlSettingsTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "rightSlidingSide", openAtStartup = false)
@ActionID(category = "Window", id = "org.rwshop.nb.motion.dynamixel.DynamixelControlSettingsTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_DynamixelControlSettingsAction",
preferredID = "DynamixelControlSettingsTopComponent")
public final class DynamixelControlSettingsTopComponent extends TopComponent {
    private SingleServiceListener<ServoController> myServiceListener;
    public DynamixelControlSettingsTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(DynamixelControlSettingsTopComponent.class, "CTL_DynamixelControlSettingsTopComponent"));
        setToolTipText(NbBundle.getMessage(DynamixelControlSettingsTopComponent.class, "HINT_DynamixelControlSettingsTopComponent"));

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dynamixelControlLoopPanel1 = new org.rwshop.swing.motion.dynamixel.DynamixelControlLoopPanel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dynamixelControlLoopPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dynamixelControlLoopPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.rwshop.swing.motion.dynamixel.DynamixelControlLoopPanel dynamixelControlLoopPanel1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        if(myServiceListener != null){
            return;
        }
        BundleContext context = OSGiUtils.getBundleContext(ServoController.class);
        if(context == null){
            return;
        }
        dynamixelControlLoopPanel1.initialize(context);
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
