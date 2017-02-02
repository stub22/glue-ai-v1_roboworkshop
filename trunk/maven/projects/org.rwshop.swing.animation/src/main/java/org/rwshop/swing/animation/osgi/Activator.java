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

package org.rwshop.swing.animation.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rwshop.swing.common.utils.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class Activator implements BundleActivator {
	private static final Logger theLogger = LoggerFactory.getLogger(Activator.class);

	@Override
	public void start(BundleContext context) throws Exception {
		theLogger.info("AnimationEditor Activation Begin.");
/*
		try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Throwable t){
            t.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterpolationFrame().setVisible(true);
            }
        });*/
		//SettingsRepository.initSettings();
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }catch(Throwable t){
                    t.printStackTrace();
                }
                new AnimationFrame().setVisible(true);
            }
        });*/
		SettingsRepository.setContext(context);
		theLogger.info("AnimationEditor Activation Complete.");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
