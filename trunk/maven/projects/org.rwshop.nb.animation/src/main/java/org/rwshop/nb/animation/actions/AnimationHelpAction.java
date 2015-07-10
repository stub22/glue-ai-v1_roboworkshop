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
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author krystal
 */

@ActionID(
		category = "Help",
		id = "org.rwshop.nb.animation.actions.AnimationHelpAction"
)
@ActionRegistration(
		displayName = "#CTL_AnimationHelpAction"
)
@ActionReference(path = "Menu/Help", position = 100)
@Messages("CTL_AnimationHelpAction=Shortcuts")
public final class AnimationHelpAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, "Shift+Click adds a control point\n"
			+ "Shift+Drag locks point movement to one axis\n"
			+ "Alt+Drag snaps point to grid\n"
			+ "Ctrl+Drag moves entire motion path\n"
			+ "Ctrl+Shift+Z or Ctrl+Y moves forward through entries in history tab\n"
			+ "Ctrl+Z moves backwards through entries in history tab\n"
			+ "Ctrl+Z/Ctrl+Y - Undo/Redo\n"
			+ "Ctrl+C+Click copies the motion path (or curve) that was clicked\n"
			+ "Ctrl+V+Click pastes the motion path (or curve). Select desired channel first.\n"
			+ "Shift+Click to 'x' out of tab closes ALL tabs\n"
			+ "Alt+Click to 'x' out of tab closes all OTHER tabs",
			"Help Window", JOptionPane.PLAIN_MESSAGE);
	}
}


