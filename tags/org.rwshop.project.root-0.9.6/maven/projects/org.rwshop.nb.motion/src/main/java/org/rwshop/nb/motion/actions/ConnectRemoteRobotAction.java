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
package org.rwshop.nb.motion.actions;

import org.mechio.api.motion.protocol.MotionFrameEvent.MotionFrameEventFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.swing.JOptionPane;
import org.jflux.api.common.rk.utils.TimeUtils;
import org.jflux.api.core.Source;
import org.jflux.impl.messaging.rk.config.RKMessagingConfigUtils;
import org.jflux.impl.messaging.rk.lifecycle.BytesMessageBlockingReceiverLifecycle;
import org.jflux.impl.messaging.rk.lifecycle.JMSAvroAsyncReceiverLifecycle;
import org.jflux.impl.messaging.rk.lifecycle.JMSAvroMessageSenderLifecycle;
import org.jflux.impl.messaging.rk.utils.ConnectionManager;
import org.jflux.impl.messaging.rk.utils.ConnectionUtils;
import org.jflux.impl.services.rk.lifecycle.utils.SimpleLifecycle;
import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponent;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponentFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.mechio.api.motion.Robot;
import org.mechio.api.motion.lifecycle.RemoteRobotClientLifecycle;
import org.mechio.api.motion.lifecycle.RemoteRobotLifecycle;
import org.mechio.api.motion.messaging.RobotRequestFactory;
import org.mechio.api.motion.protocol.MotionFrameEvent;
import org.mechio.api.motion.protocol.RobotDefinitionResponse;
import org.mechio.api.motion.protocol.RobotRequest;
import org.mechio.api.motion.protocol.RobotResponse;
import org.mechio.api.motion.utils.RobotUtils;
import org.mechio.impl.motion.messaging.MotionFrameEventRecord;
import org.mechio.impl.motion.messaging.PortableRobotResponse;
import org.mechio.impl.motion.messaging.PortableMotionFrameEvent;
import org.mechio.impl.motion.messaging.PortableRobotDefinitionResponse;
import org.mechio.impl.motion.messaging.PortableRobotRequest;
import org.mechio.impl.motion.messaging.RobotDefinitionResponseRecord;
import org.mechio.impl.motion.messaging.RobotRequestRecord;

import static org.jflux.impl.messaging.rk.utils.ConnectionUtils.TOPIC;
import org.jflux.spec.discovery.UniqueService;
import org.rwshop.swing.motion.connection.SelectorFrame;

public final class ConnectRemoteRobotAction implements ActionListener {
    private final static Logger theLogger = 
            Logger.getLogger(ConnectRemoteRobotAction.class.getName());
    
    private final static String CONNECTION_ID = "motionConnection";
    private final static String REQUEST_DEST_ID = "robotRequest";
    private final static String RESPONSE_DEST_ID = "robotResponse";
    private final static String MOVE_DEST_ID = "robotMotionFrame";
    private final static String REQUEST_SENDER_ID = "robotRequestSender";
    private final static String RESPONSE_RECEIVER_ID = "robotResponseReceiver";
    private final static String MOVE_SENDER_ID = "robotFrameSender"; 
    private final static String DEF_RECEIVER_ID = "robotDefinitionReceiver";
    private final static String DEF_DEST_ID = "robotDefinition";

    @Override
    public void actionPerformed(ActionEvent e) {
        Thread connectThread = new Thread(new Runnable() {

            @Override
            public void run() {
                SelectorFrame ipFrame = SelectorFrame.getInstance();
                ipFrame.setLocationRelativeTo(null);
                ipFrame.setVisible(true);

                UniqueService robot = null;

                while(robot == null) {
                    for(Source<UniqueService> selector:
                            ipFrame.getSelectors()) {
                        robot = selector.getValue();
                        
                        if(robot != null) {
                            break;
                        }
                    }

                    TimeUtils.sleep(1000);
                }
                
                String ip = robot.getIPAddress();

                if(ip.equals("CANCEL")) {
                    theLogger.info("User cancelled ConnectRemoteRobot action.");
                    return;
                }
                
                String robotId = robot.getProperties().get("robotId");
                
                if(robotId == null) {
                    robotId = "Avatar_ZenoR50";
                }
                
                theLogger.log(Level.INFO, "Connecting to robot {0}", robotId);
                
                Robot.Id mainRobotId = new Robot.Id(robotId);
                String smallRobotId = robotId.replaceAll("_", "");

                String tcp = "tcp://" + ip + ":5672";
                theLogger.info(tcp);
                BundleContext context = OSGiUtils.getBundleContext(Robot.class);
                if(context == null){
                    theLogger.warning(
                            "Unable to load Robot.  Could not find BundleContext.");
                    return;
                }

                try {
                    DisconnectAction.addService(
                            RKMessagingConfigUtils.registerConnectionConfig(
                                    "robotReceiverConnectionConfig", ip, null, 
                                    new OSGiComponentFactory(context)));
                    DisconnectAction.addComponents(
                            startRemoteRobotClientServices(
                            context, CONNECTION_ID, 
                            REQUEST_DEST_ID, RESPONSE_DEST_ID, 
                            MOVE_DEST_ID, REQUEST_SENDER_ID, 
                            RESPONSE_RECEIVER_ID, MOVE_SENDER_ID,
                            DEF_RECEIVER_ID, DEF_DEST_ID));
                    DisconnectAction.addRegs(connectMotion(
                            context, tcp, CONNECTION_ID, REQUEST_DEST_ID, 
                            RESPONSE_DEST_ID, MOVE_DEST_ID, DEF_DEST_ID,
                            smallRobotId));
                    if(robotId.equals("myRobot")) {
                        DisconnectAction.addComponents(
                                ConnectAction.loadJointGroup(
                                        context, mainRobotId,
                                        "./resources/jointgroup.xml"));
                    } else if(robotId.equals("Avatar_ZenoR50")) {
                        DisconnectAction.addComponents(
                                ConnectAction.loadJointGroup(
                                        context, mainRobotId,
                                        "./resources/jointgroup-avatar.xml"));
                    }
                    DisconnectAction.addComponent(startRobotClientLifecycle(
                            context, "source", "dest", mainRobotId, 
                            REQUEST_SENDER_ID, RESPONSE_RECEIVER_ID,
                            MOVE_SENDER_ID));
                    DisconnectAction.addComponent(
                            startRemoteRobot(
                                    context, mainRobotId, DEF_RECEIVER_ID));
                    DisconnectAction.addServices(RobotUtils.startDefaultBlender(
                            context, mainRobotId, 40L));
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
        });
        
        connectThread.start();
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
            BundleContext context, Robot.Id robotId, String defReceiverId){
        RemoteRobotLifecycle lifecycle = 
                new RemoteRobotLifecycle(robotId, defReceiverId);
        OSGiComponent comp = new OSGiComponent(context, lifecycle);
        comp.start();
        
        return comp;
    }
    
    private Set<ServiceRegistration> connectMotion(
            BundleContext context, String connectionStr, String connectionId,
            String requestDestId, String responseDestId, String moveDestId,
            String defDestId, String robotId){
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
                requestDestId, "robot" + robotId + "hostrobotRequest", TOPIC, null, 
                responseDestId, "robot" + robotId + "hostrobotResponse", TOPIC, null,
                moveDestId, "robot" + robotId + "hostmotionFrame", TOPIC, null,
                defDestId, "robot" + robotId + "hostrobotDefinition", TOPIC, null));
        theLogger.info("Motion Connection and Destinations Registered");
        
        return regs;
    }
    
    private static Set<OSGiComponent> startRemoteRobotClientServices(
            BundleContext context, String connectionId, 
            String requestDestId, String responseDestId, 
            String moveDestId, String requestSenderId, 
            String responseReceiverId, String moveSenderId,
            String defReceiverId, String defDestId){
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
        
        JMSAvroAsyncReceiverLifecycle<RobotDefinitionResponse,
                RobotDefinitionResponseRecord> defRecLifecycle = 
                new JMSAvroAsyncReceiverLifecycle<RobotDefinitionResponse,
                        RobotDefinitionResponseRecord>(
                                new PortableRobotDefinitionResponse.RecordMessageAdapter(), 
                                RobotDefinitionResponse.class,
                                RobotDefinitionResponseRecord.class,
                                RobotDefinitionResponseRecord.SCHEMA$,
                                defReceiverId, connectionId, defDestId);
        OSGiComponent defReceiver = new OSGiComponent(context, defRecLifecycle);
        defReceiver.start();
        comps.add(defReceiver);
        
        return comps;
    }
}
