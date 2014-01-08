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
import org.robokind.api.motion.protocol.MotionFrameEvent.MotionFrameEventFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.swing.JOptionPane;
import org.jflux.impl.messaging.rk.config.RKMessagingConfigUtils;
import org.jflux.impl.messaging.rk.lifecycle.BytesMessageBlockingReceiverLifecycle;
import org.jflux.impl.messaging.rk.lifecycle.JMSAvroMessageSenderLifecycle;
import org.jflux.impl.messaging.rk.utils.ConnectionManager;
import org.jflux.impl.messaging.rk.utils.ConnectionUtils;
import org.jflux.impl.services.rk.lifecycle.ManagedService;
import org.jflux.impl.services.rk.lifecycle.utils.SimpleLifecycle;
import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponent;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponentFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.robokind.api.motion.Robot;
import org.robokind.api.motion.lifecycle.RemoteRobotClientLifecycle;
import org.robokind.api.motion.lifecycle.RemoteRobotLifecycle;
import org.robokind.api.motion.messaging.RobotRequestFactory;
import org.robokind.api.motion.protocol.MotionFrameEvent;
import org.robokind.api.motion.protocol.RobotRequest;
import org.robokind.api.motion.protocol.RobotResponse;
import org.robokind.api.motion.utils.RobotUtils;
import org.robokind.impl.motion.messaging.MotionFrameEventRecord;
import org.robokind.impl.motion.messaging.PortableRobotResponse;
import org.robokind.impl.motion.messaging.PortableMotionFrameEvent;
import org.robokind.impl.motion.messaging.PortableRobotRequest;
import org.robokind.impl.motion.messaging.RobotRequestRecord;

import static org.jflux.impl.messaging.rk.utils.ConnectionUtils.TOPIC;

public final class ConnectRemoteAvatarAction implements ActionListener {
    private final static Logger theLogger = 
            Logger.getLogger(ConnectRemoteAvatarAction.class.getName());
    
    private final static String CONNECTION_ID = "motionConnection";
    private final static String REQUEST_DEST_ID = "robotRequest";
    private final static String RESPONSE_DEST_ID = "robotResponse";
    private final static String MOVE_DEST_ID = "robotMotionFrame";
    private final static Robot.Id ROBOT_ID = new Robot.Id("Avatar_ZenoR50");
    private final static String REQUEST_SENDER_ID = "robotRequestSender";
    private final static String RESPONSE_RECEIVER_ID = "robotResponseReceiver";
    private final static String MOVE_SENDER_ID = "robotFrameSender"; 

    @Override
    public void actionPerformed(ActionEvent e) {
        String ip = JOptionPane.showInputDialog(
                "Remote Avatar IP ?","127.0.0.1"); 
        if(ip == null){
            theLogger.info("User cancelled ConnectRemoteAvatar action.");
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
        
        Set<ManagedService> services = new HashSet<ManagedService>();
        Set<OSGiComponent> components = new HashSet<OSGiComponent>();
        Set<ServiceRegistration> regs = new HashSet<ServiceRegistration>();
        
        try {
            DisconnectAction.addService(
                    RKMessagingConfigUtils.registerConnectionConfig(
                    connectionConfigId, ip, null, 
                    new OSGiComponentFactory(context)));
            DisconnectAction.addComponents(startRemoteRobotClientServices(
                    context, CONNECTION_ID, 
                    REQUEST_DEST_ID, RESPONSE_DEST_ID, 
                    MOVE_DEST_ID, REQUEST_SENDER_ID, 
                    RESPONSE_RECEIVER_ID, MOVE_SENDER_ID));
            DisconnectAction.addRegs(connectMotion(
                    context, tcp, CONNECTION_ID, REQUEST_DEST_ID, 
                    RESPONSE_DEST_ID, MOVE_DEST_ID));
            DisconnectAction.addComponents(ConnectAction.loadJointGroup(
                    context, ROBOT_ID, "./resources/jointgroup.xml"));
            DisconnectAction.addComponent(startRobotClientLifecycle(
                    context, "source", "dest", ROBOT_ID, 
                    REQUEST_SENDER_ID, RESPONSE_RECEIVER_ID, MOVE_SENDER_ID));
            DisconnectAction.addComponent(startRemoteRobot(context, ROBOT_ID));
            DisconnectAction.addServices(RobotUtils.startDefaultBlender(
                    context, ROBOT_ID, 40L));
            OSGiComponent mfe = new OSGiComponent(context,
                    new SimpleLifecycle(
                            new PortableMotionFrameEvent.Factory(), 
                            MotionFrameEventFactory.class));
            mfe.start();
            DisconnectAction.addComponent(mfe);
        } catch(Exception ex) {
            DisconnectAction.disconnect();
            theLogger.log(
                    Level.SEVERE, "Can''t connect to robot at {0}: {1}",
                    new Object[]{ip, ex.getMessage()});
            ex.printStackTrace();
            
            JOptionPane.showMessageDialog(
                    null, "Cannot connect to robot at " + ip,
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static OSGiComponent startRobotClientLifecycle(BundleContext context,
            String sourceId, String destId, Robot.Id robotId, 
            String reqSenderId, String respReceiverId, String frameSenderId){
        RemoteRobotClientLifecycle lifecycle = 
                new RemoteRobotClientLifecycle(sourceId, destId, robotId, 
                        reqSenderId, respReceiverId, frameSenderId);
        OSGiComponent comp = new OSGiComponent(context, lifecycle);
        comp.start();
        
        return comp;
    }
    
    private static OSGiComponent startRemoteRobot(
            BundleContext context, Robot.Id robotId){
        RemoteRobotLifecycle lifecycle = 
                new RemoteRobotLifecycle(robotId);
        OSGiComponent comp = new OSGiComponent(context, lifecycle);
        comp.start();
        
        return comp;
    }
    
    private Set<ServiceRegistration> connectMotion(
            BundleContext context, String connectionStr,
            String connectionId, String requestDestId, 
            String responseDestId, String moveDestId){
        Set<ServiceRegistration> regs = new HashSet<ServiceRegistration>(4);
        theLogger.info("Registering Motion Connection and Destinations");
        Connection con = ConnectionManager.createConnection(
                "admin", "admin", "client1", "test", 
                connectionStr);
        if(con == null){
            return null;
        }
        try{
            con.start();
        }catch(JMSException ex){
            theLogger.log(Level.SEVERE, 
                    "Unable to start connection: " + connectionId, ex);
            return null;
        }
        regs.add(ConnectionUtils.ensureSession(context, 
                connectionId, con, null));
        regs.addAll(ConnectionUtils.ensureDestinations(context, 
                requestDestId, "robotAvatarZenoR50hostrobotRequest", TOPIC, null, 
                responseDestId, "robotAvatarZenoR50hostrobotResponse", TOPIC, null,
                moveDestId, "robotAvatarZenoR50hostmotionFrame", TOPIC, null));
        theLogger.info("Motion Connection and Destinations Registered");
        
        return regs;
    }
    
    private static Set<OSGiComponent> startRemoteRobotClientServices(
            BundleContext context, String connectionId, 
            String requestDestId, String responseDestId, 
            String moveDestId, String requestSenderId, 
            String responseReceiverId, String moveSenderId){
        Set<OSGiComponent> comps = new HashSet<OSGiComponent>(4);
        BytesMessageBlockingReceiverLifecycle<RobotResponse> respRecLifecycle = 
                new BytesMessageBlockingReceiverLifecycle<RobotResponse>(
                        new PortableRobotResponse.RecordMessageAdapter(), 
                        RobotResponse.class, responseReceiverId, 
                        connectionId, responseDestId);
        OSGiComponent respSender = new OSGiComponent(context, respRecLifecycle);
        respSender.start();
        comps.add(respSender);
        
        JMSAvroMessageSenderLifecycle reqSenderLifecycle = 
                new JMSAvroMessageSenderLifecycle(
                        new PortableRobotRequest.MessageRecordAdapter(), 
                        RobotRequest.class, RobotRequestRecord.class, 
                        requestSenderId, connectionId, requestDestId);
        OSGiComponent reqRec = new OSGiComponent(context, reqSenderLifecycle);
        reqRec.start();
        comps.add(reqRec);
        
        JMSAvroMessageSenderLifecycle moveSenderLifecycle = 
                new JMSAvroMessageSenderLifecycle(
                        new PortableMotionFrameEvent.MessageRecordAdapter(), 
                        MotionFrameEvent.class, MotionFrameEventRecord.class, 
                        moveSenderId, connectionId, moveDestId);
        OSGiComponent moveRec = new OSGiComponent(context, moveSenderLifecycle);
        moveRec.start();
        comps.add(moveRec);
        
        RobotRequestFactory reqFact = new PortableRobotRequest.Factory();
        OSGiComponent reqFactComp = new OSGiComponent(context, 
                new SimpleLifecycle(reqFact, RobotRequestFactory.class));
        reqFactComp.start();
        comps.add(reqFactComp);
        
        return comps;
    }
}
