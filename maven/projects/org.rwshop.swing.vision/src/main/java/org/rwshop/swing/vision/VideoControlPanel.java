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
 * VideoControlPanel.java
 *
 * Created on Nov 18, 2011, 3:11:44 AM
 */
package org.rwshop.swing.vision;

import java.net.URISyntaxException;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import org.apache.qpid.client.AMQConnectionFactory;
import org.apache.qpid.client.AMQQueue;
import org.apache.qpid.client.AMQTopic;
import org.jflux.api.core.util.EmptyAdapter;
import org.jflux.impl.messaging.rk.JMSAvroMessageAsyncReceiver;
import org.jflux.impl.messaging.rk.JMSAvroMessageSender;
import org.robokind.api.messaging.services.ServiceCommand;
import org.robokind.api.messaging.services.ServiceCommandFactory;
import org.robokind.api.messaging.services.ServiceError;
import org.robokind.api.vision.ImageEvent;
import org.robokind.api.vision.ImageRegionList;
import org.robokind.api.vision.config.CameraServiceConfig;
import org.robokind.api.vision.config.FaceDetectServiceConfig;
import org.robokind.api.vision.messaging.RemoteImageRegionServiceClient;
import org.robokind.api.vision.messaging.RemoteImageServiceClient;
import org.robokind.impl.messaging.JMSAvroServiceFacade;
import org.robokind.impl.messaging.ServiceCommandRecord;
import org.robokind.impl.messaging.ServiceErrorRecord;
import org.robokind.impl.messaging.services.PortableServiceCommand;
import org.robokind.impl.vision.CameraConfig;
import org.robokind.impl.vision.FaceDetectConfig;
import org.robokind.impl.vision.ImageRecord;
import org.robokind.impl.vision.ImageRegionListRecord;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class VideoControlPanel extends javax.swing.JPanel {
    private VideoPanel myVideoPanel;
    /** Creates new form VideoControlPanel */
    public VideoControlPanel() {
        initComponents();
    }
    
    public void setVideoPanel(VideoPanel panel){
        myVideoPanel = panel;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlConnect = new javax.swing.JPanel();
        txtBrokerAddress = new javax.swing.JTextField();
        btnConnect = new javax.swing.JButton();
        pnlControl = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        myVideoControl = new org.rwshop.swing.common.PlayControlPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        myFaceDetectControl = new org.rwshop.swing.common.PlayControlPanel();
        jLabel3 = new javax.swing.JLabel();

        txtBrokerAddress.setText("127.0.0.1:5672");

        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlConnectLayout = new javax.swing.GroupLayout(pnlConnect);
        pnlConnect.setLayout(pnlConnectLayout);
        pnlConnectLayout.setHorizontalGroup(
            pnlConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConnectLayout.createSequentialGroup()
                .addComponent(btnConnect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBrokerAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlConnectLayout.setVerticalGroup(
            pnlConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnConnect)
                .addComponent(txtBrokerAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Video");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(myVideoControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(myVideoControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Face Detection");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(myFaceDetectControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(myFaceDetectControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3))
        );

        javax.swing.GroupLayout pnlControlLayout = new javax.swing.GroupLayout(pnlControl);
        pnlControl.setLayout(pnlControlLayout);
        pnlControlLayout.setHorizontalGroup(
            pnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlControlLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlControlLayout.setVerticalGroup(
            pnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlConnect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlConnect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(pnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        String addr = txtBrokerAddress.getText();
        String con = "amqp://admin:admin@clientid/test?brokerlist='tcp://" 
                + addr + "'";
        try{
            RemoteImageServiceClient videoService = imageClient(con);
            RemoteImageRegionServiceClient faceService = imageRegionClient(con);
            myVideoControl.setPlayable(videoService);
            myFaceDetectControl.setPlayable(faceService);
            videoService.addImageListener(myVideoPanel.getImageEventListener());
            faceService.addImageRegionsListener(
                    myVideoPanel.getImageRegionListListener());
        }catch(Throwable t){
            t.printStackTrace();
        }
    }//GEN-LAST:event_btnConnectActionPerformed
    
    private RemoteImageServiceClient imageClient(String con) throws 
            URISyntaxException, JMSException, Exception{
        ConnectionFactory cf = new AMQConnectionFactory(con);
        Connection connection = cf.createConnection();
        Session session = 
                connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        connection.start();
        
        Destination cmdDest = new AMQQueue(
                "camera0Command; {create: always, node: {type: queue}}");
        Destination errDest = new AMQTopic(
                "camera0Error; {create: always, node: {type: topic}}");
        Destination imgDest = new AMQTopic(
                "camera0Event; {create: always, node: {type: topic}}");
        
        JMSAvroMessageSender<ServiceCommand, ServiceCommandRecord> cmdSender = 
                new JMSAvroMessageSender<ServiceCommand, ServiceCommandRecord>(
                        session, cmdDest);
        JMSAvroMessageSender<CameraServiceConfig, CameraConfig> configSender = 
                new JMSAvroMessageSender<CameraServiceConfig, CameraConfig>(
                        session, cmdDest);
        JMSAvroMessageAsyncReceiver<ServiceError, ServiceErrorRecord> errorReceiver = 
                new JMSAvroMessageAsyncReceiver<ServiceError, ServiceErrorRecord>(
                        session, errDest, 
                        ServiceErrorRecord.class, ServiceErrorRecord.SCHEMA$);
        ServiceCommandFactory cmdFactory = new PortableServiceCommand.Factory();
        JMSAvroMessageAsyncReceiver<ImageEvent, ImageRecord> imageReceiver = 
                new JMSAvroMessageAsyncReceiver<ImageEvent, ImageRecord>(
                        session, imgDest, 
                        ImageRecord.class, ImageRecord.SCHEMA$);
        
        RemoteImageServiceClient<CameraServiceConfig> service = 
                new RemoteImageServiceClient<CameraServiceConfig>(
                        CameraServiceConfig.class, "imageService", "remoteId", 
                        cmdSender, configSender, errorReceiver, 
                        cmdFactory, imageReceiver);
        
        cmdSender.setAdapter(new EmptyAdapter());
        cmdSender.setDefaultContentType(JMSAvroServiceFacade.COMMAND_MIME_TYPE);
        cmdSender.start();
        
//        configSender.setAdapter(new PortableCameraServiceConfig.MessageRecordAdapter());
//        configSender.setDefaultContentType(JMSAvroServiceFacade.CONFIG_MIME_TYPE);
//        configSender.start();
        
        errorReceiver.setAdapter(new EmptyAdapter());
        errorReceiver.start();
        
        imageReceiver.setAdapter(new EmptyAdapter());
        imageReceiver.start();
        
        return service;
    }
    
    private RemoteImageRegionServiceClient imageRegionClient(String con) throws 
            URISyntaxException, JMSException, Exception{
        ConnectionFactory cf = new AMQConnectionFactory(con);
        Connection connection = cf.createConnection();
        Session session = 
                connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        connection.start();
        
        Destination cmdDest = new AMQQueue(
                "visionproc0Command; {create: always, node: {type: queue}}");
        Destination errDest = new AMQTopic(
                "visionproc0Error; {create: always, node: {type: topic}}");
        Destination imgRgnDest = new AMQTopic(
                "visionproc0Event; {create: always, node: {type: topic}}");
        
        JMSAvroMessageSender<ServiceCommand, ServiceCommandRecord> cmdSender = 
                new JMSAvroMessageSender<ServiceCommand, ServiceCommandRecord>(
                        session, cmdDest);
        JMSAvroMessageSender<FaceDetectServiceConfig, FaceDetectConfig> configSender = 
                new JMSAvroMessageSender<FaceDetectServiceConfig, FaceDetectConfig>(
                        session, cmdDest);
        JMSAvroMessageAsyncReceiver<ServiceError, ServiceErrorRecord> errorReceiver = 
                new JMSAvroMessageAsyncReceiver<ServiceError, ServiceErrorRecord>(
                        session, errDest, 
                        ServiceErrorRecord.class, ServiceErrorRecord.SCHEMA$);
        ServiceCommandFactory cmdFactory = new PortableServiceCommand.Factory();
        JMSAvroMessageAsyncReceiver<ImageRegionList, ImageRegionListRecord> imageRgnReceiver = 
                new JMSAvroMessageAsyncReceiver<ImageRegionList, ImageRegionListRecord>(
                        session, imgRgnDest, 
                        ImageRegionListRecord.class, 
                        ImageRegionListRecord.SCHEMA$);
        
        RemoteImageRegionServiceClient<FaceDetectServiceConfig> service = 
                new RemoteImageRegionServiceClient<FaceDetectServiceConfig>(
                        FaceDetectServiceConfig.class, "imageService", "remoteId", 
                        cmdSender, configSender, errorReceiver, 
                        cmdFactory, imageRgnReceiver);
        
        cmdSender.setAdapter(new EmptyAdapter());
        cmdSender.setDefaultContentType(JMSAvroServiceFacade.COMMAND_MIME_TYPE);
        cmdSender.start();
        
//        configSender.setAdapter(new PortableFaceDetectServiceConfig.MessageRecordAdapter());
//        configSender.setDefaultContentType(JMSAvroServiceFacade.CONFIG_MIME_TYPE);
//        configSender.start();
        
        errorReceiver.setAdapter(new EmptyAdapter());
        errorReceiver.start();
        
        imageRgnReceiver.setAdapter(new EmptyAdapter());
        imageRgnReceiver.start();        
        
        return service;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private org.rwshop.swing.common.PlayControlPanel myFaceDetectControl;
    private org.rwshop.swing.common.PlayControlPanel myVideoControl;
    private javax.swing.JPanel pnlConnect;
    private javax.swing.JPanel pnlControl;
    private javax.swing.JTextField txtBrokerAddress;
    // End of variables declaration//GEN-END:variables
}
