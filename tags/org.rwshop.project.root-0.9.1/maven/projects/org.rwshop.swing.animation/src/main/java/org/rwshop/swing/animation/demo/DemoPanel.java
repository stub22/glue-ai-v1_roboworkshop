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

/*
 * DemoPanel.java
 *
 * Created on Jul 6, 2012, 10:56:13 AM
 */
package org.rwshop.swing.animation.demo;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.swing.JOptionPane;
import org.osgi.framework.BundleContext;
import org.robokind.api.animation.Animation;
import org.robokind.api.animation.lifecycle.AnimationPlayerClientLifecycle;
import org.robokind.api.animation.messaging.RemoteAnimationPlayerClient;
import org.robokind.api.animation.protocol.AnimationEvent;
import org.robokind.api.animation.protocol.AnimationEvent.AnimationEventFactory;
import org.robokind.api.animation.protocol.AnimationSignal;
import org.robokind.api.common.lifecycle.ManagedService;
import org.robokind.api.common.lifecycle.utils.SimpleLifecycle;
import org.robokind.api.common.osgi.OSGiUtils;
import org.robokind.api.common.osgi.lifecycle.OSGiComponent;
import org.robokind.avrogen.animation.AnimationRecord;
import org.robokind.avrogen.animation.AnimationSignallingRecord;
import org.robokind.impl.animation.messaging.PortableAnimationEvent;
import org.robokind.impl.animation.messaging.PortableAnimationSignal;
import org.robokind.impl.messaging.lifecycle.JMSAvroAsyncReceiverLifecycle;
import org.robokind.impl.messaging.lifecycle.JMSAvroMessageSenderLifecycle;
import org.robokind.impl.messaging.utils.ConnectionManager;
import org.robokind.impl.messaging.utils.ConnectionUtils;

/**
 *
 * @author Matthew Stevenson
 */
public class DemoPanel extends javax.swing.JPanel {
    private AnimationPlayerClientLifecycle myLifecycle;
    private ManagedService myPlayerService;
    private ManagedService mySenderService;
    private ManagedService myConnectionService;
    private ManagedService mySessionService;
    private boolean myStartFlag;

    /** Creates new form DemoPanel */
    public DemoPanel() {
        initComponents();
        myStartFlag = false;
        btnDisconnect.setEnabled(false);
        btnClear.setEnabled(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSet = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtIP = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        btnDisconnect = new javax.swing.JButton();

        btnSet.setText("Set Remote Player");
        btnSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetActionPerformed(evt);
            }
        });

        jLabel1.setText("IP Addr:");

        txtIP.setText("127.0.0.1");

        btnClear.setText("Clear Anims");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnDisconnect.setText("Disconnect");
        btnDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIP, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnClear)
                        .addGap(145, 145, 145)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDisconnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear)
                    .addComponent(btnDisconnect))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetActionPerformed
        startOnce();
        btnSet.setEnabled(!myStartFlag);
        btnDisconnect.setEnabled(myStartFlag);
        btnClear.setEnabled(myStartFlag);
    }//GEN-LAST:event_btnSetActionPerformed

    private synchronized void startOnce(){
        if(myStartFlag){
            return;
        }
        BundleContext context = OSGiUtils.getBundleContext(Animation.class);
        String ip = txtIP.getText();
        Connection con = ConnectionManager.createConnection(
                "admin", "admin", "client1", "test", 
                "tcp://" + ip + ":5672");
        try{
            con.start();
        }catch(JMSException ex){
            JOptionPane.showMessageDialog(this, "Unable to connect to " + ip, "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        myConnectionService = new OSGiComponent(context, new SimpleLifecycle(con, Connection.class));
        myConnectionService.start();
        ConnectionUtils.ensureSession(context, 
                "remoteAnimConnection", con, null);
        ConnectionUtils.ensureDestinations(context, 
                "remoteAnimationRequest", "animationRequest", ConnectionUtils.TOPIC, null);
        JMSAvroMessageSenderLifecycle senderLife = 
                new JMSAvroMessageSenderLifecycle(
                        new PortableAnimationEvent.MessageRecordAdapter(), 
                        AnimationEvent.class, AnimationRecord.class, 
                        "remoteAnimSender", "remoteAnimConnection", 
                        "remoteAnimationRequest");
        registerEventFactory(context);
        mySenderService = new OSGiComponent(context, senderLife);
        mySenderService.start();
        ConnectionUtils.ensureSession(context, 
                "remoteSignalConnection", con, null);
        ConnectionUtils.ensureDestinations(context, 
                "remoteAnimationSignal", "animationSignal", ConnectionUtils.TOPIC, null);
        JMSAvroAsyncReceiverLifecycle receiverLife =
                new JMSAvroAsyncReceiverLifecycle(
                new PortableAnimationSignal.RecordMessageAdapter(),
                AnimationSignal.class, AnimationSignallingRecord.class,
                AnimationSignallingRecord.SCHEMA$, "remoteSignalReceiver",
                "remoteSignalConnection", "remoteAnimationSignal");
        ManagedService myReceiverService = new OSGiComponent(context, receiverLife);
        myReceiverService.start();
        myLifecycle = 
                new AnimationPlayerClientLifecycle(
                "remotePlayer", "remotePlayer", "remoteAnimSender",
                "remoteSignalReceiver", context);
        myPlayerService = new OSGiComponent(context, myLifecycle);
        myPlayerService.start();
        myStartFlag = true;
    }
    
    private void registerEventFactory(BundleContext context){
        if(OSGiUtils.serviceExists(context, AnimationEvent.AnimationEventFactory.class, null)){
            return;
        }
        new OSGiComponent(context, 
                new SimpleLifecycle(
                        new PortableAnimationEvent.Factory(), 
                        AnimationEventFactory.class)
                ).start();
    }
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        if(myLifecycle == null){
            return;
        }
        RemoteAnimationPlayerClient client = myLifecycle.getService();
        if(client == null){
            return;
        }
        client.clearAnimations();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectActionPerformed
        stop();
    }//GEN-LAST:event_btnDisconnectActionPerformed

    private synchronized void stop(){
        if(myPlayerService != null){
            myPlayerService.dispose();
            myPlayerService = null;
        }
        if(mySenderService != null){
            mySenderService.dispose();
            mySenderService = null;
        }
        if(myConnectionService != null){
            myConnectionService.dispose();
            myConnectionService = null;
        }
        if(mySessionService != null){
            mySessionService.dispose();
            mySessionService = null;
        }
        myStartFlag = false;
        btnSet.setEnabled(!myStartFlag);
        btnDisconnect.setEnabled(myStartFlag);
        btnClear.setEnabled(myStartFlag);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JButton btnSet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtIP;
    // End of variables declaration//GEN-END:variables
}
