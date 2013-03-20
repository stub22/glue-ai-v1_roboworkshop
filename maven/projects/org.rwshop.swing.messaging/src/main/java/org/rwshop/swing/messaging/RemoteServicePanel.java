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
 * RemoteServicePanel.java
 *
 * Created on Jul 20, 2011, 10:49:53 PM
 */
package org.rwshop.swing.messaging;

import java.io.File;
import javax.swing.JFileChooser;
import org.apache.avro.Schema;
import org.robokind.api.messaging.services.RemoteServiceClient;
import org.robokind.bind.apache_avro.AvroUtils;
import org.apache.avro.generic.IndexedRecord;
import org.jflux.api.core.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class RemoteServicePanel<Conf extends IndexedRecord> extends javax.swing.JPanel {
    private final static Logger theLogger = 
            LoggerFactory.getLogger(RemoteServicePanel.class);
    
    private RemoteServiceClient<Conf> myService;
    private Source<Conf> myConfigFactory;
    private Class<Conf> myConfigClass;
    /** Creates new form RemoteServicePanel */
    public RemoteServicePanel() {
        initComponents();
    }
    
    public void setService(RemoteServiceClient<Conf> service){
        myService = service;
        init();
    }
    
    public void setConfigParams(Class<Conf> clazz, Source<Conf> fact){
        myConfigClass = clazz;
        myConfigFactory = fact;
    }
    
    public void setPath(String configPath){
        txtConfigPath.setText(configPath);
    }
    
    private void init(){
        if(myService == null){
            return;
        }
        myPlayControl.setPlayable(myService);
        myService.addPlayableListener(myStatusPanel);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myConfigChooser = new javax.swing.JFileChooser();
        txtConfigPath = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnBrowse = new javax.swing.JButton();
        btnLoad = new javax.swing.JButton();
        myPlayControl = new org.rwshop.swing.common.PlayControlPanel();
        myStatusPanel = new org.rwshop.swing.common.PlayableStatusPanel();

        jLabel1.setText("Config:");

        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        btnLoad.setText("Load");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
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
                        .addComponent(txtConfigPath, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLoad))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(myPlayControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(myStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtConfigPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btnLoad)
                    .addComponent(btnBrowse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(myPlayControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(myStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        if(myService == null){
            return;
        }
        int result = myConfigChooser.showOpenDialog(this);
        if(result != JFileChooser.APPROVE_OPTION){
            return;
        }
        File file = myConfigChooser.getSelectedFile();
        txtConfigPath.setText(file.getPath());
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        if(myService == null){
            theLogger.warn("Unable to load config for null service.");
            return;
        }else if(myConfigClass == null || myConfigFactory == null){
            theLogger.warn("Unable to load config, Config Params are null.");
            return;
        }
        try{
            String file = txtConfigPath.getText();
            Class cls = myConfigClass;
            Conf config = myConfigFactory.getValue();
            Schema schema = config.getSchema();            
            AvroUtils.readFromFile(cls, config, schema, new File(file), true);
            myService.initialize(config);
            myStatusPanel.setStatusText("initialized");
        }catch(Exception ex){
            theLogger.error("There was an error initializing the service.", ex);
        }
    }//GEN-LAST:event_btnLoadActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnLoad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JFileChooser myConfigChooser;
    private org.rwshop.swing.common.PlayControlPanel myPlayControl;
    private org.rwshop.swing.common.PlayableStatusPanel myStatusPanel;
    private javax.swing.JTextField txtConfigPath;
    // End of variables declaration//GEN-END:variables
}
