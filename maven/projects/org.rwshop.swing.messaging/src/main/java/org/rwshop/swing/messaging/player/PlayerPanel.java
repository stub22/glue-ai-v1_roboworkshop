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
package org.rwshop.swing.messaging.player;

import org.rwshop.swing.messaging.monitor.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.jms.Destination;
import javax.jms.Session;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.IndexedRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.robokind.api.common.utils.TimeUtils;
import org.robokind.impl.messaging.JMSAvroRecordSender;
import org.robokind.impl.messaging.JMSBytesMessageSender;

/**
 *
 * @author Jason G. Pallack <jgpallack@gmail.com>
 */
public class PlayerPanel extends javax.swing.JPanel {
    private Session mySession;
    private Destination myDestination;
    private Schema mySchema;
    
    /**
     * Creates new form SaveLoadPanel
     */
    public PlayerPanel() {
        initComponents();
    }
    
    public void setSession(Session session) {
        mySession = session;
    }
    
    public void setDestination(Destination destination) {
        myDestination = destination;
    }
    
    public void setSchema(Schema schema) {
        mySchema = schema;
    }
    
    public void activate() {
        jButton1.setEnabled(true);
        jButton2.setEnabled(true);
    }
    
    public void deactivate() {
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
    }
    
    private void loadMessages(boolean replay) {
        JFileChooser fc = new JFileChooser();
        int retVal = fc.showOpenDialog(this);
        List<IndexedRecord> records = new ArrayList<IndexedRecord>();
        
        if(retVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            
            DatumReader<IndexedRecord> reader =
                    new SpecificDatumReader<IndexedRecord>(mySchema);
            
            try {
                DataFileReader<IndexedRecord> fileReader =
                        new DataFileReader<IndexedRecord>(file, reader);
                
                while(fileReader.hasNext()) {
                    records.add(fileReader.next());
                }
                
                fileReader.close();
            } catch(IOException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ex.getMessage(),
                        "Failure", JOptionPane.ERROR_MESSAGE);
            }
            
            JMSBytesMessageSender msgSender = new JMSBytesMessageSender();
            msgSender.setSession(mySession);
            msgSender.setDestination(myDestination);
            msgSender.openProducer();
            JMSAvroRecordSender<IndexedRecord> sender =
                    new JMSAvroRecordSender<IndexedRecord>(msgSender);

            if(!replay) {
                for(IndexedRecord record: records) {
                    sender.sendRecord(record);
                }
            } else {
                Comparator<IndexedRecord> cmp = new TimestampComparator();
                int timestampIndex = -1;
                long timeline = 0;
                
                for(int i = 0; i < mySchema.getFields().size(); i++) {
                    if(mySchema.getFields().get(i).name().equals(
                            "timestampMillisecUTC")) {
                        timestampIndex = i;
                        break;
                    }
                }
                
                if(timestampIndex < 0) {
                    msgSender.closeProducer();
                    
                    JOptionPane.showMessageDialog(
                            this,
                            "Error: no timestamp information present.",
                            "Failure", JOptionPane.ERROR_MESSAGE);
                    
                    return;
                }
                
                Collections.sort(records, cmp);
                Long firstTimestamp = (Long)records.get(0).get(timestampIndex);
                
                for(IndexedRecord record: records) {
                    Long totalDelay =
                            (Long)record.get(timestampIndex) - firstTimestamp;
                    Long delay = totalDelay - timeline;
                    
                    if(delay > 0) {
                        TimeUtils.sleep(delay);
                    }
                    
                    timeline += delay;
                    sender.sendRecord(record);
                }
            }

            msgSender.closeProducer();

            JOptionPane.showMessageDialog(
                    this,
                    "Records loaded.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jButton1.setText("Load");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Replay");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 261, Short.MAX_VALUE)
                .addComponent(jButton2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadMessages(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        loadMessages(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    // End of variables declaration//GEN-END:variables
}
