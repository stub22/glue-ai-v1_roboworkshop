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

/*
 * AQTTSPanel.java
 *
 * Created on Jul 28, 2011, 12:58:51 AM
 */
package org.rwshop.swing.speech;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;
import org.jflux.api.core.Adapter;
import org.jflux.api.core.Listener;
import org.jflux.impl.messaging.rk.config.RKMessagingConfigUtils;
import org.jflux.impl.services.rk.lifecycle.DependencyDescriptor;
import org.jflux.impl.services.rk.lifecycle.config.GenericLifecycle;
import org.jflux.impl.services.rk.lifecycle.config.GenericLifecycle.DependencyChange;
import org.jflux.impl.services.rk.lifecycle.utils.ManagedServiceFactory;
import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponent;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponentFactory;
import org.osgi.framework.BundleContext;
import org.robokind.api.common.utils.RKConstants;
import org.robokind.api.speech.messaging.RemoteSpeechServiceClient;
import org.robokind.api.speech.SpeechService;
import org.robokind.impl.speech.RemoteSpeechUtils;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class AQTTSPanel extends javax.swing.JPanel {
    private final static Logger theLogger = 
            Logger.getLogger(AQTTSPanel.class.getName());
    public final static String CONNECTION_CONFIG_ID = "speechServiceConnectionConfig";
    private final static String SPEECH_SERVICE_ID = RKConstants.DEFAULT_SPEECH_ID;
    private final static String SPEECH_DEFAULT_PREFIX = "speech";

    /** Creates new form AQTTSPanel */
    public AQTTSPanel() {
        initComponents();
    }

    public void setAQTTSFacade(RemoteSpeechServiceClient speechService){
        mySpeechControl.setPlayable(speechService);
        myTTSPanel.setTTSEngine(speechService);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myTTSPanel = new org.rwshop.swing.speech.TTSPanel();
        jPanel1 = new javax.swing.JPanel();
        mySpeechControl = new org.rwshop.swing.common.PlayControlPanel();
        btnConnect = new javax.swing.JButton();
        txtBrokerAddress = new javax.swing.JTextField();

        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        txtBrokerAddress.setText("127.0.0.1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnConnect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBrokerAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mySpeechControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnConnect, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                        .addComponent(txtBrokerAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(mySpeechControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
            .addComponent(myTTSPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myTTSPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        String broker = txtBrokerAddress.getText();
        connect(broker);
    }//GEN-LAST:event_btnConnectActionPerformed

    private void connect(String broker){
        BundleContext context = OSGiUtils.getBundleContext(SpeechService.class);
        if(context == null){
            theLogger.warning("Unable to connect speech Service.  "
                    + "Could not find BundleContext.");
            return;
        }
        bindSpeechPanel(context);
        ManagedServiceFactory fact = new OSGiComponentFactory(context);
        RKMessagingConfigUtils.registerConnectionConfig(
                CONNECTION_CONFIG_ID, broker, null, fact);
        RemoteSpeechUtils.connect(
                fact, SPEECH_SERVICE_ID, 
                SPEECH_DEFAULT_PREFIX, CONNECTION_CONFIG_ID);
    }
    
    private final static String theSpeechService = "speechService";
    private void bindSpeechPanel(BundleContext context){
        final Listener listener = new Listener<DependencyChange<?,SpeechService>>(){
            @Override
            public void handleEvent(DependencyChange<?, SpeechService> event) {
                SpeechService speech = event.getDependency();
                if(speech == null){
                    setAQTTSFacade(null);
                }else if(speech instanceof RemoteSpeechServiceClient){
                    setAQTTSFacade((RemoteSpeechServiceClient)speech);
                }
            }
        };
        new OSGiComponent(context, new GenericLifecycle<Object>(
                new String[]{Object.class.getName()}, 
                Arrays.asList(new DependencyDescriptor(theSpeechService, 
                        SpeechService.class, null)), null, 
                new Adapter<Map<String, Object>, Object>() {
                    @Override
                    public Object adapt(Map<String, Object> a) {
                        SpeechService speech = (SpeechService) a.get(theSpeechService);
                        if(!(speech instanceof RemoteSpeechServiceClient)){
                            return null;
                        }
                        setAQTTSFacade((RemoteSpeechServiceClient)speech);
                        return new Object();
                    }
                }, 
                new Adapter<String, Listener<DependencyChange>>() {
                    @Override
                    public Listener<DependencyChange> adapt(String a) {
                        if(theSpeechService.equals(a)){
                            return listener;
                        }
                        return null;
                    }
                },
                new Listener<Object>() {
                    @Override public void handleEvent(Object input) {
                        setAQTTSFacade(null);
                    }
                })).start();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JPanel jPanel1;
    private org.rwshop.swing.common.PlayControlPanel mySpeechControl;
    private org.rwshop.swing.speech.TTSPanel myTTSPanel;
    private javax.swing.JTextField txtBrokerAddress;
    // End of variables declaration//GEN-END:variables
}
