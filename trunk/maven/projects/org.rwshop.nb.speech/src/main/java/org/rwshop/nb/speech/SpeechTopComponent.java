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

package org.rwshop.nb.speech;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.slf4j.LoggerFactory;

//import org.openide.util.ImageUtilities;

//import org.openide.util.ImageUtilities;

//import org.openide.util.ImageUtilities;

//import org.openide.util.ImageUtilities;

//import org.openide.util.ImageUtilities;

//import org.openide.util.ImageUtilities;

//import org.openide.util.ImageUtilities;

//import org.openide.util.ImageUtilities;

//import org.openide.util.ImageUtilities;

//import org.openide.util.ImageUtilities;

/**
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
@ConvertAsProperties(dtd = "-//org.rwshop.speech//Speech//EN",
		autostore = false)
public final class SpeechTopComponent extends TopComponent {

	private static SpeechTopComponent instance;
	/**
	 * path to the icon used by the component and its open action
	 */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
	private static final String PREFERRED_ID = "SpeechTopComponent";

	public SpeechTopComponent() {
		initComponents();
		setName(NbBundle.getMessage(SpeechTopComponent.class, "CTL_SpeechTopComponent"));
		setToolTipText(NbBundle.getMessage(SpeechTopComponent.class, "HINT_SpeechTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		aQTTSPanel1 = new org.rwshop.swing.speech.AQTTSPanel();

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(aQTTSPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(aQTTSPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
		);
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private org.rwshop.swing.speech.AQTTSPanel aQTTSPanel1;
	// End of variables declaration//GEN-END:variables

	/**
	 * Gets default instance. Do not use directly: reserved for *.settings files only,
	 * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
	 * To obtain the singleton instance, use {@link #findInstance}.
	 */
	public static synchronized SpeechTopComponent getDefault() {
		if (instance == null) {
			instance = new SpeechTopComponent();
		}
		return instance;
	}

	/**
	 * Obtain the SpeechTopComponent instance. Never call {@link #getDefault} directly!
	 */
	public static synchronized SpeechTopComponent findInstance() {
		TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
		if (win == null) {
			LoggerFactory.getLogger(SpeechTopComponent.class.getName()).warn(
					"Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
			return getDefault();
		}
		if (win instanceof SpeechTopComponent) {
			return (SpeechTopComponent) win;
		}
		LoggerFactory.getLogger(SpeechTopComponent.class.getName()).warn(
				"There seem to be multiple components with the '" + PREFERRED_ID
						+ "' ID. That is a potential source of errors and unexpected behavior.");
		return getDefault();
	}

	@Override
	public int getPersistenceType() {
		return TopComponent.PERSISTENCE_ALWAYS;
	}

	@Override
	public void componentOpened() {
	}

	@Override
	public void componentClosed() {
	}

	void writeProperties(java.util.Properties p) {
		// better to version settings since initial version as advocated at
		// http://wiki.apidesign.org/wiki/PropertyFiles
		p.setProperty("version", "1.0");
	}

	Object readProperties(java.util.Properties p) {
		if (instance == null) {
			instance = this;
		}
		instance.readPropertiesImpl(p);
		return instance;
	}

	private void readPropertiesImpl(java.util.Properties p) {
		String version = p.getProperty("version");
	}

	@Override
	protected String preferredID() {
		return PREFERRED_ID;
	}
}
