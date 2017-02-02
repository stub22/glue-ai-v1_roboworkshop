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

import org.jflux.api.common.rk.osgi.lifecycle.ConfiguredServiceLifecycle;
import org.jflux.api.common.rk.osgi.lifecycle.ConfiguredServiceParams;
import org.jflux.api.common.rk.services.Constants;
import org.jflux.extern.utils.apache_commons_configuration.rk.ConfigUtils;
import org.jflux.impl.messaging.rk.config.RKMessagingConfigUtils;
import org.jflux.impl.services.rk.lifecycle.ManagedService;
import org.jflux.impl.services.rk.lifecycle.ServiceLifecycleProvider;
import org.jflux.impl.services.rk.lifecycle.utils.SimpleLifecycle;
import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponent;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponentFactory;
import org.mechio.api.motion.Robot;
import org.mechio.api.motion.lifecycle.RobotJointGroupLifecycle;
import org.mechio.api.motion.servos.ServoRobot;
import org.mechio.api.motion.servos.config.ServoRobotConfig;
import org.mechio.api.motion.servos.utils.ServoRobotLifecycle;
import org.mechio.api.motion.utils.RobotUtils;
import org.mechio.impl.motion.config.RobotConfigXMLReader;
import org.mechio.impl.motion.jointgroup.RobotJointGroupConfigXMLReader;
import org.mechio.impl.motion.lifecycle.RemoteRobotHostServiceGroup;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.swing.*;

/**
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public final class ConnectAction implements ActionListener {
	private static final Logger theLogger = LoggerFactory.getLogger(ConnectAction.class);

	private final static long theDefaultBlenderInterval = 40L;

	@Override
	public void actionPerformed(ActionEvent e) {
		BundleContext context = OSGiUtils.getBundleContext(Robot.class);
		if (context == null) {
			theLogger.warn(
					"Unable to load Robot.  Could not find BundleContext.");
			return;
		}
		Robot.Id robotId = new Robot.Id("myRobot");
		launchRobot(context, robotId, "./resources/robot.xml");
		RobotUtils.startDefaultBlender(
				context, robotId, theDefaultBlenderInterval);
		loadJointGroup(context, robotId, "./resources/jointgroup.xml");
		String connectionConfigId = "remoteRobotConnectionConfig";
		RKMessagingConfigUtils.registerConnectionConfig(
				connectionConfigId, "127.0.0.1",
				null, new OSGiComponentFactory(context));
		new RemoteRobotHostServiceGroup(
				context, robotId, "host", "client",
				connectionConfigId, null, null, 100).start();
	}

	private File chooseFile() {
		JFileChooser chooser = new JFileChooser();
		int ret = chooser.showOpenDialog(null);
		if (ret != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		return chooser.getSelectedFile();
	}

	protected static Set<OSGiComponent> loadJointGroup(
			BundleContext context, Robot.Id robotId, String configPath) {
		Set comps = new HashSet<>(2);
		File file = ConfigUtils.getFileSystemAdapter().openFile(configPath);
		if (file == null) {
			return null;
		}
		String paramId = "robot/" + robotId + "/jointgroup/config/param/xml";
		comps.add(launchJointGroupConfig(context, file, paramId));
		RobotJointGroupLifecycle<File> lifecycle =
				new RobotJointGroupLifecycle<>(robotId, File.class,
						paramId, RobotJointGroupConfigXMLReader.VERSION);
		OSGiComponent jointGroupComp = new OSGiComponent(context, lifecycle);
		jointGroupComp.start();
		comps.add(jointGroupComp);
		return comps;
	}

	private static OSGiComponent launchJointGroupConfig(
			BundleContext context, File file, String paramId) {
		Properties props = new Properties();
		props.put(Constants.CONFIG_PARAM_ID, paramId);
		props.put(Constants.CONFIG_FORMAT_VERSION,
				RobotJointGroupConfigXMLReader.VERSION.toString());
		ServiceLifecycleProvider lifecycle =
				new SimpleLifecycle(file, File.class, props);
		OSGiComponent paramComp = new OSGiComponent(context, lifecycle);
		paramComp.start();
		return paramComp;
	}

	private ManagedService launchRobot(
			BundleContext context, Robot.Id robotId, String path) {
		File file = ConfigUtils.getFileSystemAdapter().openFile(path);
		if (file == null) {
			return null;
		}
		String paramId = "robot/" + robotId + "/config/param/xml";
		launchRobotConfig(context, file, paramId);
		return loadRobotService(context, robotId, paramId);
	}

	protected static ManagedService loadRobotService(
			BundleContext context, Robot.Id robotId, String paramId) {
		ConfiguredServiceParams<Robot, ServoRobotConfig, File> params =
				new ConfiguredServiceParams(
						Robot.class,
						ServoRobotConfig.class,
						File.class,
						null, null, paramId,
						ServoRobot.VERSION,
						RobotConfigXMLReader.VERSION);
		ConfiguredServiceLifecycle lifecycle =
				new ServoRobotLifecycle(
						params, new OSGiComponentFactory(context));

		Properties props = new Properties();
		props.put(Robot.PROP_ID, robotId.getRobtIdString());
		OSGiComponent robotComponent =
				new OSGiComponent(context, lifecycle, props);
		robotComponent.start();
		return robotComponent;
	}

	private static OSGiComponent launchRobotConfig(
			BundleContext context, File file, String paramId) {
		Properties props = new Properties();
		props.put(Constants.CONFIG_PARAM_ID, paramId);
		props.put(Constants.CONFIG_FORMAT_VERSION,
				RobotConfigXMLReader.VERSION.toString());
		ServiceLifecycleProvider lifecycle =
				new SimpleLifecycle(file, File.class, props);
		OSGiComponent paramComp = new OSGiComponent(context, lifecycle);
		paramComp.start();
		return paramComp;
	}
}
