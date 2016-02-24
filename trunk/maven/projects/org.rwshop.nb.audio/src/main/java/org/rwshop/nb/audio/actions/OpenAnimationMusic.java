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
package org.rwshop.nb.audio.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import org.jflux.api.common.rk.playable.Playable;
import org.jflux.api.common.rk.services.addon.ServiceAddOn;
import org.jflux.api.common.rk.utils.RKSource;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.audio.WavPlayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.windows.Mode;
import org.openide.windows.WindowManager;
import org.rwshop.nb.animation.AnimationDataObject;
import org.rwshop.nb.animation.AnimationTimelineEditor;
import org.rwshop.nb.animation.history.UndoRedoFactory;
import org.rwshop.nb.audio.AnimationMusicTopComponent;
import org.rwshop.swing.animation.actions.FileAction;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.scaling.DefaultCoordinateScalar;
import org.rwshop.swing.common.scaling.ScalingManager;

/**
 *
 * @author matt
 */
public class OpenAnimationMusic implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		RKSource.SourceImpl<AnimationEditor> cS = new RKSource.SourceImpl<AnimationEditor>();
		new FileAction.Open(cS, new UndoRedoFactory(), null).actionPerformed(null);
		if (cS.getValue() == null) {
			return;
		}

		String path = cS.getValue().getFilePath();
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

		List<ServiceAddOn<Playable>> addons = cS.getValue().getAnimation().getAddOns();
		WavPlayer player = (WavPlayer) addons.get(0).getAddOn();
		CoordinateScalar scalar = new DefaultCoordinateScalar(0.02, 400, true);
		ScalingManager sm = new ScalingManager(scalar);
		AnimationTimelineEditor editor = new AnimationTimelineEditor(animDob);
		editor.init(sm);
		editor.setController(cS.getValue());
		editor.open();
		editor.requestActive();
		WindowManager wm = WindowManager.getDefault();
		Mode bottom = wm.findMode("output");
		AnimationMusicTopComponent music = new AnimationMusicTopComponent();
		bottom.dockInto(music);
		music.init(player, sm);
		music.open();
	}
}
