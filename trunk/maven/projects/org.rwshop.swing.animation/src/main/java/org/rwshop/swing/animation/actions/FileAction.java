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
package org.rwshop.swing.animation.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.*;
import javax.swing.JFileChooser;
import org.apache.commons.configuration.ConfigurationException;
import org.jflux.api.common.rk.utils.RKSource;
import org.jflux.api.core.Source;
import org.mechio.api.animation.Animation;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.editor.history.HistoryStack;
import org.mechio.api.animation.utils.AnimationUtils;
import org.mechio.api.animation.xml.AnimationXML;
import org.rwshop.swing.common.utils.MessageAlerter;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class FileAction {

	/**
	 *
	 */
	public static class New implements ActionListener {

		private RKSource<AnimationEditor> mySource;
		private Source<? extends HistoryStack> myHistoryFactory;

		/**
		 *
		 * @param source
		 * @param histFact
		 */
		public New(RKSource<AnimationEditor> source, Source<? extends HistoryStack> histFact) {
			mySource = source;
			myHistoryFactory = histFact;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser(Settings.getLastDirectory());
			int i = fileChooser.showOpenDialog(null);
			if (i == JFileChooser.CANCEL_OPTION) {
				mySource.set(null);
				return;
			}
			String path = fileChooser.getSelectedFile().getPath();
			if (!path.endsWith(".rkanim")) {
				path = path.concat(".rkanim");
			}
			Settings.saveLastDirectory(path);

			Animation anim = new Animation();
			AnimationEditor editor = new AnimationEditor(anim, path,
					myHistoryFactory.getValue());
			mySource.set(editor);
		}
	}

	/**
	 *
	 */
	public static class Open implements ActionListener {

		private RKSource<AnimationEditor> mySource;
		private Source<? extends HistoryStack> myHistoryFactory;
		private String myPath;

		/**
		 *
		 * @param source
		 * @param histFact
		 * @param path
		 */
		public Open(RKSource<AnimationEditor> source, Source<? extends HistoryStack> histFact, String path) {
			mySource = source;
			myPath = path;
			myHistoryFactory = histFact;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String path = myPath;
			if (path == null || path.isEmpty()) {
				JFileChooser fileChooser = new JFileChooser(Settings.getLastDirectory());
				int i = fileChooser.showOpenDialog(null);
				if (i == JFileChooser.CANCEL_OPTION) {
					mySource.set(null);
					return;
				}
				path = fileChooser.getSelectedFile().getPath();
			}
			String name = path.substring(path.lastIndexOf(File.separator) + File.separator.length());
			if (!path.endsWith(".rkanim")) {
				path = path.concat(".rkanim");
			}
			Settings.saveLastDirectory(path);
			AnimationEditor temp = mySource.getValue();
			mySource.set(null);

			try {
				Animation anim = AnimationXML.loadAnimation(path);
				anim.setVersion(name, anim.getVersion().getNumber());
				AnimationEditor editor = new AnimationEditor(anim, path,
						myHistoryFactory.getValue());
				mySource.set(editor);
			} catch (Throwable t) {
				mySource.set(temp);
				MessageAlerter.Error("Unable to load Animation", null, t);
				return;
			}
		}
	}

	/**
	 * Handles save in MySavable and various other files in
	 * org.rwshop.swing.animation
	 */
	public static class Save implements ActionListener {

		private RKSource<AnimationEditor> mySource;
		private boolean myOpenDialog;

		/**
		 *
		 * @param source
		 * @param dialog
		 */
		public Save(RKSource<AnimationEditor> source, boolean dialog) {
			mySource = source;
			myOpenDialog = dialog;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AnimationEditor editor = mySource.getValue();
			String path = editor.getFilePath();
			if (myOpenDialog || path == null || path.isEmpty()) {
				JFileChooser fileChooser = new JFileChooser(Settings.getLastDirectory());
				int i = fileChooser.showSaveDialog(null);
				if (i == JFileChooser.CANCEL_OPTION) {
					return;
				}
				path = fileChooser.getSelectedFile().getPath();
			}
			Settings.saveLastDirectory(path);
			String name = path.substring(path.lastIndexOf(File.separator) + File.separator.length());
			if (!path.endsWith(".rkanim")) {
				path = path.concat(".rkanim");
			}

			bumpVersionNumber(editor);

			boolean error = true;
			String innerError = "";
			Throwable innerException = null;
			try {
				AnimationXML.saveAnimation(path, editor.getAnimation(),
						AnimationUtils.getChannelsParameterSource(),
						editor.collectSynchronizedPointGroups());
				error = false;
				editor.setFilePath(path);
				editor.setName(name);
			} catch (ConfigurationException ex) {
				innerError = "There was an error writing the Animation to XML";
				innerException = ex;
			} catch (Throwable ex) {
				innerException = ex;
			}
			if (error) {
				MessageAlerter.Error("Unable to save Animation", innerError, innerException);
			} else {
				MessageAlerter.Ok("File Saved", "The Animation was successfully saved.");
			}
		}

	}

	/**
	 * Handles SaveAs for SaveAsCapable implementation in MySavable
	 */
	public static class SaveAs {

		/**
		 *
		 * @param path
		 * @param name
		 * @param controller
		 */
		public SaveAs(String path, String name, AnimationEditor controller) {

			boolean error = true;
			String innerError = "";
			Throwable innerException = null;
			AnimationEditor editor = controller;

			bumpVersionNumber(editor);
			editor.setName(name);
			try {
				AnimationXML.saveAnimation(path, editor.getAnimation(),
						AnimationUtils.getChannelsParameterSource(),
						editor.collectSynchronizedPointGroups());
				error = false;
				editor.setFilePath(path);
				Settings.saveLastDirectory(path);
				editor.setName(name);
			} catch (ConfigurationException ex) {
				innerError = "There was an error writing the Animation to XML";
				innerException = ex;
			} catch (Throwable ex) {
				innerException = ex;
			}
			if (error) {
				MessageAlerter.Error("Unable to save Animation", innerError, innerException);
			} else {
				MessageAlerter.Ok("File Saved", "The Animation was successfully saved.");
			}
		}
	}

	/**
	 * Saves and retrieves most recently accessed directory.
	 */
	public static class Settings {

		private final static Preferences prefs = Preferences.userNodeForPackage(Settings.class);

		private void Settings() {
		}

		/**
		 *
		 * @param path
		 */
		public static void saveLastDirectory(String path) {
			prefs.put("LAST_DIRECTORY", path);
		}

		/**
		 *
		 * @return most recently accessed directory
		 */
		public static String getLastDirectory() {
			if (prefs.get("LAST_DIRECTORY", "") == null || prefs.get("LAST_DIRECTORY", "").isEmpty()) {
				return System.getProperty("user.home");
			}
			return prefs.get("LAST_DIRECTORY", "");
		}
	}

	/**
	 * This method will ensure the version number is 3 parts, ex. 0.1.12, and
	 * will increase the version number by 1
	 *
	 * @param versionNumber
	 *
	 * @return
	 *
	 * @throws NumberFormatException
	 */
	private static String getNextVersion(String versionNumber) throws NumberFormatException {
		if (versionNumber != null && versionNumber.equals("1.0")) {
			return "1.0.1";
		}

		ArrayList<String> versionParts = new ArrayList<String>(Arrays.asList(versionNumber.split("\\.")));
		if (versionParts.isEmpty()) {
			versionParts.add("0");
		}
		if (versionParts.size() == 1) {
			versionParts.add(0, "0");
		}
		if (versionParts.size() == 2) {
			versionParts.add(0, "1");
		}

		for (int i = versionParts.size() - 1; i >= 0; i--) {
			int number;
			try {
				number = Integer.parseInt(versionParts.get(i));
			} catch (Exception e) {
				number = 0;
			}
			if (i == versionParts.size() - 1) {
				number++;
			}
			int div = number / 20;
			int rem = number % 20;
			versionParts.set(i, "" + rem);

			if (i == 0 && div != 0) {
				versionParts.add(0, "0");
				i++;
			}

			if (div != 0) {
				int prevNum;
				try {
					prevNum = Integer.parseInt(versionParts.get(i - 1));
				} catch (Exception e) {
					prevNum = 0;
				}
				prevNum += div;
				versionParts.set(i - 1, "" + prevNum);
			}
		}
		String newVersionNumber = "";
		for (int i = 0; i < versionParts.size(); i++) {
			newVersionNumber += versionParts.get(i);
			if (i != versionParts.size() - 1) {
				newVersionNumber += ".";
			}
		}
		return newVersionNumber;
	}

	private static void bumpVersionNumber(AnimationEditor editor) throws NumberFormatException {
		String currentVersionNumber = editor.getVersion().getNumber();
		currentVersionNumber = getNextVersion(currentVersionNumber);
		editor.setVersionNumber(currentVersionNumber);
	}
}
