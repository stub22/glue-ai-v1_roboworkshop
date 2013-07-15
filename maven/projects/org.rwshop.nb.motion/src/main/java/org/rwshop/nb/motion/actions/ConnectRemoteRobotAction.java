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
package org.rwshop.nb.motion.actions;

import org.robokind.demo.robot.replication.RobotReplicator;
import org.robokind.impl.messaging.config.RKMessagingConfigUtils;
import org.robokind.api.motion.protocol.MotionFrameEvent.MotionFrameEventFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.swing.JOptionPane;
import org.osgi.framework.BundleContext;
import org.robokind.api.common.lifecycle.utils.SimpleLifecycle;
import org.robokind.api.common.osgi.OSGiUtils;
import org.robokind.api.common.osgi.lifecycle.OSGiComponent;
import org.robokind.api.common.osgi.lifecycle.OSGiComponentFactory;
import org.robokind.api.motion.Robot;
import org.robokind.api.motion.lifecycle.RemoteRobotClientLifecycle;
import org.robokind.api.motion.lifecycle.RemoteRobotLifecycle;
import org.robokind.api.motion.messaging.RobotRequestFactory;
import org.robokind.api.motion.protocol.MotionFrameEvent;
import org.robokind.api.motion.protocol.RobotRequest;
import org.robokind.api.motion.protocol.RobotResponse;
import org.robokind.api.motion.utils.RobotUtils;
import org.robokind.impl.messaging.lifecycle.BytesMessageBlockingReceiverLifecycle;
import org.robokind.impl.messaging.lifecycle.JMSAvroMessageSenderLifecycle;
import org.robokind.impl.messaging.utils.ConnectionManager;
import org.robokind.impl.messaging.utils.ConnectionUtils;
import org.robokind.impl.motion.messaging.MotionFrameEventRecord;
import org.robokind.impl.motion.messaging.PortableRobotResponse;
import org.robokind.impl.motion.messaging.PortableMotionFrameEvent;
import org.robokind.impl.motion.messaging.PortableRobotRequest;
import org.robokind.impl.motion.messaging.RobotRequestRecord;
import static org.robokind.impl.messaging.utils.ConnectionUtils.TOPIC;

public final class ConnectRemoteRobotAction implements ActionListener {
    private final static Logger theLogger = 
            Logger.getLogger(ConnectRemoteRobotAction.class.getName());
    
    private final static String CONNECTION_ID = "motionConnection";
    private final static String REQUEST_DEST_ID = "robotRequest";
    private final static String RESPONSE_DEST_ID = "robotResponse";
    private final static String MOVE_DEST_ID = "robotMotionFrame";
    private final static Robot.Id ROBOT_ID = new Robot.Id("myRobot");
    private final static String REQUEST_SENDER_ID = "robotRequestSender";
    private final static String RESPONSE_RECEIVER_ID = "robotResponseReceiver";
    private final static String MOVE_SENDER_ID = "robotFrameSender"; 

    @Override
    public void actionPerformed(ActionEvent e) {
        String ip = JOptionPane.showInputDialog("Remote Robot IP ?","127.0.0.1"); 
        if(ip == null){
            theLogger.info("User cancelled ConnectRemoteRobot action.");
            return;
        }else if(ip.isEmpty()){
            ip = "127.0.0.1";
        }
        String tcp = "tcp://" + ip + ":5672";
        theLogger.info(tcp);
        BundleContext context = OSGiUtils.getBundleContext(Robot.class);
        if(context == null){
            theLogger.warning(
                    "Unable to load Robot.  Could not find BundleContext.");
            return;
        }
        String connectionConfigId = RobotReplicator.RECEIVER_CONNECTION_CONFIG_ID;
        
        RKMessagingConfigUtils.registerConnectionConfig(
                connectionConfigId, ip, null, 
                new OSGiComponentFactory(context));
        startRemoteRobotClientServices(context, CONNECTION_ID, 
                REQUEST_DEST_ID, RESPONSE_DEST_ID, 
                MOVE_DEST_ID, REQUEST_SENDER_ID, 
                RESPONSE_RECEIVER_ID, MOVE_SENDER_ID);
        connectMotion(context, tcp, CONNECTION_ID, REQUEST_DEST_ID, 
                RESPONSE_DEST_ID, MOVE_DEST_ID);
        ConnectAction.loadJointGroup(context, ROBOT_ID, "./resources/jointgroup.xml");
        startRobotClientLifecycle(context, "source", "dest", ROBOT_ID, 
                REQUEST_SENDER_ID, RESPONSE_RECEIVER_ID, MOVE_SENDER_ID);
        startRemoteRobot(context, ROBOT_ID);
        RobotUtils.startDefaultBlender(context, ROBOT_ID, 40L);
        new OSGiComponent(context, 
                new SimpleLifecycle(
                        new PortableMotionFrameEvent.Factory(), 
                        MotionFrameEventFactory.class)).start();
    }
    
    private static void startRobotClientLifecycle(BundleContext context,
            String sourceId, String destId, Robot.Id robotId, 
            String reqSenderId, String respReceiverId, String frameSenderId){
        RemoteRobotClientLifecycle lifecycle = 
                new RemoteRobotClientLifecycle(sourceId, destId, robotId, 
                        reqSenderId, respReceiverId, frameSenderId);
        OSGiComponent comp = new OSGiComponent(context, lifecycle);
        comp.start();
    }
    
    private static void startRemoteRobot(
            BundleContext context, Robot.Id robotId){
        RemoteRobotLifecycle lifecycle = 
                new RemoteRobotLifecycle(robotId);
        OSGiComponent comp = new OSGiComponent(context, lifecycle);
        comp.start();
    }
    
    private void connectMotion(BundleContext context, String connectionStr,
            String connectionId, String requestDestId, 
            String responseDestId, String moveDestId){
        theLogger.info("Registering Motion Connection and Destinations");
        Connection con = ConnectionManager.createConnection(
                "admin", "admin", "client1", "test", 
                connectionStr);
        if(con == null){
            return;
        }
        try{
            con.start();
        }catch(JMSException ex){
            theLogger.log(Level.SEVERE, 
                    "Unable to start connection: " + connectionId, ex);
            return;
        }
        ConnectionUtils.ensureSession(context, 
                connectionId, con, null);
        ConnectionUtils.ensureDestinations(context, 
                requestDestId, "robotmyRobothostrobotRequest", TOPIC, null, 
                responseDestId, "robotmyRobothostrobotResponse", TOPIC, null,
                moveDestId, "robotmyRobothostmotionFrame", TOPIC, null);
        theLogger.info("Motion Connection and Destinations Registered");
    }
    
    private static void startRemoteRobotClientServices(
            BundleContext context, String connectionId, 
            String requestDestId, String responseDestId, 
            String moveDestId, String requestSenderId, 
            String responseReceiverId, String moveSenderId){
        BytesMessageBlockingReceiverLifecycle<RobotResponse> respRecLifecycle = 
                new BytesMessageBlockingReceiverLifecycle<RobotResponse>(
                        new PortableRobotResponse.RecordMessageAdapter(), 
                        RobotResponse.class, responseReceiverId, 
                        connectionId, responseDestId);
        OSGiComponent respSender = new OSGiComponent(context, respRecLifecycle);
        respSender.start();
        JMSAvroMessageSenderLifecycle reqSenderLifecycle = 
                new JMSAvroMessageSenderLifecycle(
                        new PortableRobotRequest.MessageRecordAdapter(), 
                        RobotRequest.class, RobotRequestRecord.class, 
                        requestSenderId, connectionId, requestDestId);
        OSGiComponent reqRec = new OSGiComponent(context, reqSenderLifecycle);
        reqRec.start();
        JMSAvroMessageSenderLifecycle moveSenderLifecycle = 
                new JMSAvroMessageSenderLifecycle(
                        new PortableMotionFrameEvent.MessageRecordAdapter(), 
                        MotionFrameEvent.class, MotionFrameEventRecord.class, 
                        moveSenderId, connectionId, moveDestId);
        OSGiComponent moveRec = new OSGiComponent(context, moveSenderLifecycle);
        moveRec.start();
        RobotRequestFactory reqFact = new PortableRobotRequest.Factory();
        OSGiComponent reqFactComp = new OSGiComponent(context, 
                new SimpleLifecycle(reqFact, RobotRequestFactory.class));
        reqFactComp.start();
    }
}
