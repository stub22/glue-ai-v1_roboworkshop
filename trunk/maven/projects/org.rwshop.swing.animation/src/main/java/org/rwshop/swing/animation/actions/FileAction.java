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
    public static class New implements ActionListener{
        private RKSource<AnimationEditor> mySource;
        private Source<? extends HistoryStack> myHistoryFactory;

        /**
         *
         * @param label
         * @param source
         * @param histFact
         */
        public New(RKSource<AnimationEditor> source, Source<? extends HistoryStack> histFact){
            mySource = source;
            myHistoryFactory = histFact;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			int i = fileChooser.showOpenDialog(null);
			if(i == JFileChooser.CANCEL_OPTION){
				mySource.set(null);
				return;
			}
			String path = fileChooser.getSelectedFile().getPath();
			if (!path.endsWith(".rkanim")) {
				path = path.concat(".rkanim");
			}

            Animation anim = new Animation();
			AnimationEditor editor = new AnimationEditor(anim, path,
            myHistoryFactory.getValue());
            mySource.set(editor);
        }
    }

    /**
     *
     */
    public static class Open implements ActionListener{
        private RKSource<AnimationEditor> mySource;
        private Source<? extends HistoryStack> myHistoryFactory;
        private String myPath;

        /**
         *
         * @param label
         * @param source
         * @param histFact
         * @param path
         */
        public Open(RKSource<AnimationEditor> source, Source<? extends HistoryStack> histFact, String path){
            mySource = source;
            myPath = path;
            myHistoryFactory = histFact;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String path = myPath;
            if(path == null || path.isEmpty()){
                JFileChooser fileChooser = new JFileChooser();
                int i = fileChooser.showOpenDialog(null);
                if(i == JFileChooser.CANCEL_OPTION){
                    mySource.set(null);
                    return;
                }
                path = fileChooser.getSelectedFile().getPath();
				if (!path.endsWith(".rkanim")) {
					path = path.concat(".rkanim");
				}
            }
            AnimationEditor temp = mySource.getValue();
            mySource.set(null);
			String name = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf(".rkanim"));
            try{
                Animation anim = AnimationXML.loadAnimation(path);
                    anim.setVersion(name, anim.getVersion().getNumber());
                AnimationEditor editor = new AnimationEditor(anim, path,
                        myHistoryFactory.getValue());
                mySource.set(editor);
            }catch(Throwable t){
                mySource.set(temp);
                MessageAlerter.Error("Unable to load Animation", null, t);
                return;
            }
        }
    }

    /**
     *
     */
    public static class Save implements ActionListener{
        private RKSource<AnimationEditor> mySource;
        private boolean myOpenDialog;
        /**
         *
         * @param label
         * @param source
         * @param dialog
         */
        public Save(RKSource<AnimationEditor> source, boolean dialog){
            mySource = source;
            myOpenDialog = dialog;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            AnimationEditor editor = mySource.getValue();
            String path = editor.getFilePath();
            if(myOpenDialog || path == null || path.isEmpty()){
                JFileChooser fileChooser = new JFileChooser();
                int i = fileChooser.showSaveDialog(null);
                if(i == JFileChooser.CANCEL_OPTION){
                    return;
                }
                path = fileChooser.getSelectedFile().getPath();
            }
			String name = path.substring(path.lastIndexOf("\\")+1);
            boolean error = true;
            String innerError = "";
            Throwable innerException = null;
            try{
                AnimationXML.saveAnimation(path, editor.getAnimation(),
                        AnimationUtils.getChannelsParameterSource(),
                        editor.collectSynchronizedPointGroups());
                error = false;
                editor.setFilePath(path);
				editor.setName(name);
            }catch(ConfigurationException ex){
                innerError = "There was an error writing the Animation to XML";
                innerException = ex;
            }catch(Throwable ex){
                innerException = ex;
            }
            if(error){
                MessageAlerter.Error("Unable to save Animation", innerError, innerException);
            }else{
                MessageAlerter.Ok("File Saved", "The Animation was successfully saved.");
            }
        }
    }

	public static class SaveAs {
		public SaveAs(String path, String name, AnimationEditor controller) {
			boolean error = true;
			String innerError = "";
			Throwable innerException = null;
			AnimationEditor editor = controller;
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
}
