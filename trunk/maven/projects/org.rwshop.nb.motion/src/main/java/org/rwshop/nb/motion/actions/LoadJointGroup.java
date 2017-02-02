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
import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponent;
import org.mechio.api.motion.jointgroup.JointGroup;
import org.mechio.api.motion.jointgroup.RobotJointGroup;
import org.mechio.impl.motion.jointgroup.RobotJointGroupConfigXMLReader;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

/**
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public final class LoadJointGroup implements ActionListener {
	private static final Logger theLogger = LoggerFactory.getLogger(LoadJointGroup.class);

	@Override
	public void actionPerformed(ActionEvent e) {
		File file = getFile();
		if (file == null) {
			return;
		}
		BundleContext context = OSGiUtils.getBundleContext(JointGroup.class);
		if (context == null) {
			return;
		}
		launchService(context, file);
	}

	private void launchService(BundleContext context, File file) {
		ConfiguredServiceParams params =
				new ConfiguredServiceParams(JointGroup.class, null, File.class,
						null, file, null,
						RobotJointGroup.VERSION,
						RobotJointGroupConfigXMLReader.VERSION);
		OSGiComponent jointGroupComp = new OSGiComponent(context,
				new ConfiguredServiceLifecycle(params));
		jointGroupComp.start();
		theLogger.info("JointGroup Service Registered.");
	}

	private File getFile() {
		JFileChooser chooser = new JFileChooser();
		int ret = chooser.showOpenDialog(null);
		if (ret == JFileChooser.CANCEL_OPTION) {
			return null;
		}
		return chooser.getSelectedFile();
	}
}
