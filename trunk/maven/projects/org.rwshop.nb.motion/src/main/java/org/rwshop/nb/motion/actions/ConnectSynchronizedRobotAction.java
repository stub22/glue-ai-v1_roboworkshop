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

import org.apache.qpid.client.AMQQueue;
import org.jflux.api.common.rk.services.ServiceConnectionDirectory;
import org.jflux.extern.utils.apache_commons_configuration.rk.ConfigUtils;
import org.jflux.impl.messaging.rk.utils.ConnectionManager;
import org.jflux.impl.messaging.rk.utils.ConnectionUtils;
import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.mechio.api.motion.Robot;
import org.mechio.api.motion.sync.SynchronizedRobot;
import org.mechio.api.motion.utils.RobotUtils;
import org.mechio.impl.motion.sync.SynchronizedRobotConfigLoader;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.swing.*;

public final class ConnectSynchronizedRobotAction implements ActionListener {
	private static final Logger theLogger = LoggerFactory.getLogger(ConnectSynchronizedRobotAction.class);

	private final static long theDefaultBlenderInterval = 40L;

	@Override
	public void actionPerformed(ActionEvent e) {
		File file = chooseFile();
		if (file == null) {
			return;
		}
		BundleContext context = OSGiUtils.getBundleContext(Robot.class);
		if (context == null) {
			theLogger.warn(
					"Unable to load Robot.  Could not find BundleContext.");
			return;
		}
		Robot robot;
		try {
			robot = ServiceConnectionDirectory.buildService(context,
					SynchronizedRobot.VERSION, SynchronizedRobotConfigLoader.VERSION,
					file, File.class, Robot.class);
			if (robot == null) {
				return;
			}
		} catch (Exception ex) {
			theLogger.warn("Unable to load Robot.  Could not build service.", ex);
			return;
		}
		robot.connect();
		ServiceRegistration reg =
				RobotUtils.registerRobot(context, robot, null);
		if (reg == null) {
			theLogger.info("Failed registering Robot service.");
			return;
		}
		theLogger.info("Robot Service Registered.");
		RobotUtils.startDefaultBlender(
				context, robot.getRobotId(), theDefaultBlenderInterval);
		createAndRegisterServer(context, robot);
	}

	private File chooseFile() {
		JFileChooser chooser = new JFileChooser();
		int ret = chooser.showOpenDialog(null);
		if (ret != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		return chooser.getSelectedFile();
	}

	private File predfinedFile(String path) {

		return ConfigUtils.getFileSystemAdapter().openFile(path);
	}

	private static void createAndRegisterServer(
			BundleContext bundleCtx, Robot robot) {
		Connection connection = ConnectionManager.createConnection(
				ConnectionUtils.getUsername(), ConnectionUtils.getPassword(),
				"client1", "test", "tcp://127.0.0.1:5672");
		if (connection == null) {
			return;
		}
		try {
			connection.start();
		} catch (JMSException ex) {
			theLogger.warn("Could not start connection.", ex);
			return;
		}
		if (connection == null) {
			return;
		}
		String queue =
				"robotMotionFrame; {create: always, node: {type: queue}}";
		Session session;
		Destination destination;
		try {
			session = connection.createSession(
					false, Session.CLIENT_ACKNOWLEDGE);
			destination = new AMQQueue(queue);
		} catch (URISyntaxException ex) {
			theLogger.warn("Error creating destination.", ex);
			return;
		} catch (JMSException ex) {
			theLogger.warn("Error creating session.", ex);
			return;
		}

		try {
			//startRobotServer(bundleCtx, robot, session, destination);
		} catch (Exception ex) {
			theLogger.warn("Error starting Robot Server.", ex);
		}
	}

    /*private static JMSMotionFrameAsyncReceiver startRobotServer(
			BundleContext context, Robot robot,
            Session session, Destination destination) throws Exception{
        JMSMotionFrameAsyncReceiver receiver = 
                new JMSMotionFrameAsyncReceiver(session, destination);
        Robot.Id id = robot.getRobotId();
        RobotMoverFrameSource frameSource = new RobotMoverFrameSource(robot);
        RobotMoveHandler moveHandler = new RobotMoveHandler();
        ServiceRegistration reg = 
                RobotUtils.registerFrameSource(context, id, frameSource);
        moveHandler.setRobotFrameSource(frameSource);
        receiver.addMessageListener(moveHandler);
        receiver.start();
        return receiver;
        return null;
    }*/
}
