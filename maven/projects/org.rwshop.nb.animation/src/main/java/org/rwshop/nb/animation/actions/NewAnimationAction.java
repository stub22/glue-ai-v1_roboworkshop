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

package org.rwshop.nb.animation.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JOptionPane;
import org.jflux.api.common.rk.utils.RKSource.SourceImpl;
import org.rwshop.nb.animation.AnimationTimelineEditor;
import org.rwshop.nb.animation.history.UndoRedoFactory;
import org.rwshop.swing.animation.actions.FileAction;
import org.mechio.api.animation.editor.AnimationEditor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.rwshop.nb.animation.AnimationDataObject;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.scaling.DefaultCoordinateScalar;
import org.rwshop.swing.common.scaling.ScalingManager;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public final class NewAnimationAction implements ActionListener {

    @Override
   public void actionPerformed(ActionEvent e) {
		SourceImpl<AnimationEditor> cS = new SourceImpl<AnimationEditor>();
		new FileAction.New(cS, new UndoRedoFactory()).actionPerformed(null);
		if (cS.getValue() == null) {
			return;
		}
		String path = cS.getValue().getFilePath();

		Path file = FileSystems.getDefault().getPath(path);
		if (!Files.notExists(file)) {
			JOptionPane.showMessageDialog(null, "File Already Exists", null, JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			Files.createFile(file);
		} catch (IOException ex) {
			Exceptions.printStackTrace(ex);
		}
		FileObject fob = FileUtil.toFileObject(new File(path));

		DataObject dob = null;
		try {
			dob = DataObject.find(fob);

		} catch (DataObjectNotFoundException ex) {
			Exceptions.printStackTrace(ex);
		}
		if (dob == null || !(AnimationDataObject.class).isInstance(dob)) {
			return;
		}
		AnimationDataObject animDob = (AnimationDataObject) dob;
		animDob.setController(cS.getValue());
		AnimationTimelineEditor editor = new AnimationTimelineEditor(animDob);

		String name = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf(".rkanim"));
		cS.getValue().setName(name);

		editor.open();
		editor.requestActive();
		CoordinateScalar scaler = new DefaultCoordinateScalar(0.02, 400, true);
		ScalingManager sm = new ScalingManager(scaler);
		editor.init(sm);
		editor.setController(cS.getValue());
	}
}
