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
import org.apache.avro.Schema.Field;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
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
    private EditorListPanel myEditorList;
    
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
        
        if(myEditorList != null) {
            myEditorList.setSchema(schema);
        }
    }
    
    public void setEditorList(EditorListPanel editorList) {
        myEditorList = editorList;
    }
    
    public void activate() {
        jButton1.setEnabled(true);
        jButton3.setEnabled(true);
        
        if(myEditorList.getRecords().size() > 0) {
            jButton2.setEnabled(true);
        }
    }
    
    public void deactivate() {
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
    }
    
    private void loadMessages() {
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
            
            myEditorList.clear();
            
            for(IndexedRecord record: records) {
                myEditorList.addRecord(record);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Records loaded.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void playMessages() {
        List<IndexedRecord> records = myEditorList.getRecords();
        
        JMSBytesMessageSender msgSender = new JMSBytesMessageSender();
        msgSender.setSession(mySession);
        msgSender.setDestination(myDestination);
        msgSender.openProducer();
        JMSAvroRecordSender<IndexedRecord> sender =
                new JMSAvroRecordSender<IndexedRecord>(msgSender);

        Comparator<IndexedRecord> cmp = new TimestampComparator();
        boolean useHeader = false;
        int timestampIndex = -1;
        int headerIndex = -1;
        long timeline = 0;

        for(int i = 0; i < mySchema.getFields().size(); i++) {
            String fieldName = mySchema.getFields().get(i).name();
            if(fieldName.equals("header")) {
                useHeader = true;
                headerIndex = i;
                
                Field field = mySchema.getFields().get(i);
                Schema schema = field.schema();
                
                for(int j = 0; j < schema.getFields().size(); j++) {
                    String headerFieldName = schema.getFields().get(i).name();
                    
                    if(headerFieldName.equals("timestamp")) {
                        timestampIndex = j;
                        break;
                    }
                }
                
                break;
            } else if(fieldName.equals("timestampMillisecUTC")) {
                timestampIndex = i;
                break;
            }
        }

        if(timestampIndex < 0) {
            int delay = -1;

            while(delay < 0) {
                try {
                    String delayInput =
                            JOptionPane.showInputDialog(
                            this,
                            "Enter delay between records (in milliseconds):",
                            "Delay", JOptionPane.QUESTION_MESSAGE);
                    delay = Integer.parseInt(delayInput);
                } catch(NumberFormatException ex) {
                }
            }
            
            for(IndexedRecord record: records) {
                sender.sendRecord(record);
//                System.out.println("Sleeping " + delay + " msec.");
                TimeUtils.sleep(delay);
            }
            
            msgSender.closeProducer();
            
            JOptionPane.showMessageDialog(
                    this, "Playback complete.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        Collections.sort(records, cmp);
        Long firstTimestamp =
                getTimestamp(
                records.get(0), useHeader, timestampIndex, headerIndex);

        for(IndexedRecord record: records) {
            Long totalDelay =
                    getTimestamp(record, useHeader, timestampIndex, headerIndex)
                    - firstTimestamp;
            Long delay = totalDelay - timeline;

            if(delay > 0) {
//                System.out.println("Sleeping " + delay + " msec.");
                TimeUtils.sleep(delay);
            }

            timeline += delay;
            sender.sendRecord(record);
        }

        msgSender.closeProducer();

        JOptionPane.showMessageDialog(
                this, "Playback complete.", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Long getTimestamp(
            IndexedRecord record, boolean useHeader, int timestampIndex,
            int headerIndex) {
        if(useHeader) {
            IndexedRecord header = (IndexedRecord)record.get(headerIndex);
            return (Long)header.get(timestampIndex);
        } else {
            return (Long)record.get(timestampIndex);
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
        jButton3 = new javax.swing.JButton();

        jButton1.setText("Load");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Play");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Add");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                .addComponent(jButton2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadMessages();
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        playMessages();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
//        IndexedRecord lastRecord = myEditorList.getLastRecord();
//        IndexedRecord copy =
//                (IndexedRecord)GenericData.get().deepCopy(mySchema, lastRecord);
//        myEditorList.addRecord(copy);
        myEditorList.newRecord();
        jButton2.setEnabled(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    // End of variables declaration//GEN-END:variables
}
