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
package org.rwshop.swing.animation.timeline.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import org.mechio.api.animation.editor.AbstractEditor;
import org.mechio.api.animation.editor.EditState;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.editor.ChannelEditor;
import org.mechio.api.animation.editor.ControlPointEditor;
import org.mechio.api.animation.editor.MotionPathEditor;
import org.mechio.api.animation.editor.actions.MotionPathActions;
import org.mechio.api.animation.editor.features.SynchronizedPointGroup;
import org.mechio.api.animation.factory.ControlPointFactory;
import org.rwshop.swing.animation.timeline.TimelineAnimation;
import org.rwshop.swing.animation.timeline.TimelineChannel;
import org.rwshop.swing.animation.timeline.TimelineMotionPath;
import org.rwshop.swing.animation.timeline.TimelineControlPoint;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.utils.SettingsRepository;
import org.rwshop.swing.animation.EditorElement;
import org.rwshop.swing.animation.timeline.position.PositionSource;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class TimelineMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

	private final static Logger theLogger = Logger.getLogger(TimelineMouseListener.class.getName());
	private AnimationEditor myController;
	private TimelineAnimation myTimeline;
	private CoordinateScalar myScalar;
	private TimelineContextMenuManager myContextMenu;
	private TimelineControlPoint myControlPoint;
	private int myLastButton;
	private JPanel myPanel;

	private boolean myPathChanging;
	private Double myLastClickX;
	private Double myLastClickY;
	private Double cpX = 0.0;
	private Double cpY = 0.0;
	private PositionSource myPositionSource;

	/**
	 *
	 * @param s
	 * @param contextMenu
	 */
	public TimelineMouseListener(CoordinateScalar s, TimelineContextMenuManager contextMenu) {
		myScalar = s;
		myContextMenu = contextMenu;
		myLastButton = 0;
		myPathChanging = false;
	}

	/**
	 *
	 * @param controller
	 * @param timeline
	 */
	public void setAnimation(AnimationEditor controller, TimelineAnimation timeline) {
		myController = controller;
		myTimeline = timeline;
		myLastButton = 0;
		myPathChanging = false;
	}

	public void setPositionSource(PositionSource ps) {
		myPositionSource = ps;
	}

	public void setPanel(JPanel panel) {
		myPanel = panel;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		myLastButton = e.getButton();
		setLastClick(e);
		if (myContextMenu.tryShowPopup(e)) {
			return;
		}
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}
		MotionPathEditor motionPath = getSelectedPath();
		if (motionPath == null) {
			selectChannelOrPath(e);
			return;
		}
		int i = TimelineMotionPath.findControlPoint(motionPath, e.getX(), e.getY(), myScalar, SettingsRepository.theHoverDistance);
		if (i >= 0) {
			myControlPoint = getLastControlPoint(i);
			cpX = myScalar.unscaleX(myControlPoint.getX());
			cpY = myScalar.unscaleY(myControlPoint.getY());
			motionPath.select(this, i, motionPath.getSharedHistory());
			myPathChanging = true;
			return;
		} else if (motionPath.getSelected() != null) {
			motionPath.deselect(this);
		}
		//if(addPointToLine(motionPath, e))return;
		boolean selected = selectChannel(e);
		selected = selectMotionPath(e) || selected;
		if (!selectChannelOrPath(e) && e.getClickCount() == 2) {
			double x = myScalar.unscaleX(e.getX());
			addFeedback(x);
		}
	}

	private void addFeedback(double x) {
		if (myPositionSource == null) {
			return;
		}
		myPositionSource.addPositions((long) x);
	}

	private boolean selectChannelOrPath(MouseEvent e) {
		//separate lines to be sure both functions are called
		boolean selected = selectChannel(e);
		return selectMotionPath(e) || selected;
	}

	private boolean addPointToLine(MotionPathEditor motionPath, MouseEvent e) {
		if (myTimeline == null) {
			return false;
		}
		TimelineChannel selected = myTimeline.getSelected();
		if (selected == null) {
			return false;
		}
		TimelineMotionPath tmp = selected.getSelected();
		if (tmp != null && tmp.contains(e.getX(), e.getY(), SettingsRepository.theHoverDistance)) {
			//addPoint(motionPath, e);
			return true;
		}
		return false;
	}

	private boolean selectChannel(MouseEvent e) {
		if (myTimeline == null || myController == null) {
			return false;
		}
		for (int j = 0; j < myTimeline.getChannels().size(); j++) {
			TimelineChannel cc = myTimeline.getChannels().get(j);
			if (!cc.getController().hasFlag(EditState.VISIBLE)) {
				continue;
			}
			if (cc.contains(e.getX(), e.getY(), SettingsRepository.theHoverDistance)) {
				myController.select(this, j, myController.getSharedHistory());
				return true;
			}
		}
		return false;
	}

	private boolean selectMotionPath(MouseEvent e) {
		if (myTimeline == null || myController == null) {
			return false;
		}
		TimelineChannel selected = myTimeline.getSelected();
		if (selected == null) {
			return false;
		}
		for (int j = 0; j < selected.getMotionPaths().size(); j++) {
			TimelineMotionPath sel = selected.getMotionPaths().get(j);
			if (sel.contains(e.getX(), e.getY(), SettingsRepository.theHoverDistance)) {
				AbstractEditor editor = selected.getController();
				if (editor == null) {
					continue;
				}
				editor.select(this, j, editor.getSharedHistory());
				return true;
			}
		}
		return false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		myLastButton = 0;
		myContextMenu.tryShowPopup(e);
		MotionPathEditor motionPath = getSelectedPath();
		endPathChange();
		if (motionPath == null) {
			return;
		}
		motionPath.deselect(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		setLastClick(e);
		if (e.isShiftDown()) {
			if (myController == null) {
				return;
			}
			ChannelEditor channel = myController.getSelected();
			if (channel == null) {
				return;
			}
			MotionPathEditor motionPath = channel.getSelected();
			if (motionPath == null) {
				return;
			}
			DecimalFormat dForm = new DecimalFormat("#.##");
			double y = Double.valueOf(dForm.format(myScalar.unscaleY(e.getY())));
			Point2D p = new Point2D.Double(myScalar.unscaleX(e.getX()), y);
			TimelineMotionPath tmp = getSelectedTimelinePath();
			MotionPathEditor controller = tmp.getController();
			ControlPointFactory factory = new ControlPointFactory(p);
			int id = controller.addChild(e, factory.getValue(), controller.getSharedHistory());


		} else {
			myContextMenu.tryShowPopup(e);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (myLastButton != MouseEvent.BUTTON1) {
			return;
		}
		if (myController == null) {
			return;
		}
		ChannelEditor channel = myController.getSelected();
		if (channel == null) {
			return;
		}
		MotionPathEditor motionPath = channel.getSelected();
		if (motionPath == null) {
			return;
		}
		long x = (long) myScalar.unscaleX(e.getX());
		double y = myScalar.unscaleY(e.getY());

		double xDiff = x - myLastClickX;
		double yDiff = y - myLastClickY;

		double xDiff2 = e.getX() - myScalar.scaleX(myLastClickX);
		double yDiff2 = e.getY() - myScalar.scaleY(myLastClickY);

		DecimalFormat dForm = new DecimalFormat("#.##");
		y = Double.valueOf(dForm.format(y));
		yDiff = Double.valueOf(dForm.format(yDiff));

		if (e.isShiftDown()) {
			if (Math.abs(xDiff2) >= Math.abs(yDiff2)) {
				y = cpY;
			} else {
				x = cpX.longValue();
			}
		}

		if (e.isAltDown()) {
			y = gridY(y);
			x = gridX(x);
		}

		if (myPathChanging && e.isControlDown()) {
			motionPath.movePath(this, (long) xDiff, yDiff, motionPath.getSharedHistory());
			return;
		}
		int sel = motionPath.dragSelectedPoint(this, x, y, motionPath.getSharedHistory());
		if (sel != -1) {
			move(channel, motionPath, sel, x, y);
			return;
		}
		drag(e);
	}

	private void drag(MouseEvent e) {

	}

	private boolean move(
			ChannelEditor channel, MotionPathEditor motionPath, int sel, long x,
			double y) {
		if (myPositionSource == null) {
			return false;
		}
		boolean move = !channel.hasFlag(EditState.DISABLED)
				&& !motionPath.hasFlag(EditState.DISABLED);
		if (!move) {
			return false;
		}
		if (sel == -1) {
			return false;
		}
		ControlPointEditor point = motionPath.getChild(sel);
		Map<Integer, Double> positions = getChannelPositions(point, (long) x, y);
		if (positions == null || positions.isEmpty()) {
			return false;
		}
		myPositionSource.handlePositionEdit(x, positions);
		return true;
	}

	private Map<Integer, Double> getChannelPositions(ControlPointEditor point, long time, Double pos) {
		if (myTimeline == null || myController == null) {
			return null;
		}
		Map<Integer, Double> positions = new HashMap<Integer, Double>();
		for (int j = 0; j < myTimeline.getChannels().size(); j++) {
			TimelineChannel timelineChannel = myTimeline.getChannels().get(j);
			ChannelEditor chan = timelineChannel.getController();
			if (chan.hasFlag(EditState.DISABLED)) {
				continue;
			}
			Double y = timelineChannel.getY(time);
			if (y == null) {
				continue;
			}
			positions.put(timelineChannel.getController().getId(), y);
		}
		if (point.isGrouped()) {
			SynchronizedPointGroup group = point.getPointGroup();
			group.addPositions(positions);
		} else if (pos != null) {
			TimelineChannel sel = myTimeline.getSelected();
			if (sel != null) {
				positions.put(sel.getController().getId(), pos);
			}
		}
		return positions;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (myTimeline == null) {
			return;
		}
		hover(e, SettingsRepository.theHoverDistance, myTimeline.getChannels());
		TimelineChannel selectedChannel = myTimeline.getSelected();
		if (selectedChannel == null) {
			return;
		}
		hover(e, SettingsRepository.theHoverDistance, selectedChannel.getMotionPaths());
		TimelineMotionPath selectedTMP = selectedChannel.getSelected();
		if (selectedTMP == null) {
			return;
		}
		hover(e, SettingsRepository.theHoverDistance, selectedTMP.getControlPoints());
	}

	private void hover(MouseEvent e, double dist, List<? extends EditorElement> elems) {
		for (EditorElement el : elems) {
			boolean h = el.contains(e.getX(), e.getY(), dist);
			AbstractEditor editor = el.getController();
			if (editor == null) {
				continue;
			}
			editor.setState(this, EditState.HOVER, h, editor.getSharedHistory());
		}
	}

	private MotionPathEditor getSelectedPath() {
		if (myController == null) {
			return null;
		}
		ChannelEditor channel = myController.getSelected();
		if (channel == null) {
			return null;
		}
		return channel.getSelected();
	}

	private TimelineMotionPath getSelectedTimelinePath() {
		if (myTimeline == null) {
			return null;
		}
		TimelineChannel channel = myTimeline.getSelected();
		if (channel == null) {
			return null;
		}
		return channel.getSelected();
	}

	private void setLastClick(MouseEvent e) {
		myLastClickX = myScalar.unscaleX(e.getX());
		myLastClickY = myScalar.unscaleY(e.getY());
		if (e.isControlDown()) {
			TimelineMotionPath mp = getSelectedTimelinePath();
			if (mp != null && mp.contains(e.getX(), e.getY(), SettingsRepository.theHoverDistance)) {
				myPathChanging = true;
			}
		}
	}

	private TimelineControlPoint getLastControlPoint(int i) {
		if (i < 0) {
			return null;
		}
		TimelineMotionPath tmp = getSelectedTimelinePath();
		List<TimelineControlPoint> tcpList = tmp.getControlPoints();
		return tcpList.get(i);
	}

	private void endPathChange() {
		if (!myPathChanging) {
			return;
		}
		myPathChanging = false;
		MotionPathEditor mp = getSelectedPath();
		if (mp != null) {
			mp.endPathChange(false, mp.getSharedHistory());
		}
	}

	private double gridY(double y){
		if(y > .75){
				if(y >= .97)
					y = 1;
				else if(y >= .91)
					y = .94;
				else if(y >= .84)
					y = .88;
				else
					y = .81;
			}
			else if(y > .5){
				if(y >= .72)
					y = .75;
				else if(y >= .66)
					y = .69;
				else if(y >= .59)
					y = .63;
				else
					y = .56;
			}
			else if(y > .25){
				if(y >= .47)
					y = .5;
				else if(y >= .41)
					y = .44;
				else if(y >= .34)
					y = .38;
				else
					y = .31;
			} else {
				if(y > .22)
					y = .25;
				else if(y >= .16)
					y = .19;
				else if(y >= .09)
					y = .13;
				else if(y >= .03)
					y = .06; 
				else
					y = 0;
			}
		return y;
	}

	private long gridX(long x){
		double lowX = -1;
		double highX = -1;
		double interval = myContextMenu.getPanel().getTimeInterval();
		double max = myContextMenu.getPanel().getMax();

		for(int i = 0; i < max; i+=interval){
			if(x <= i){
				highX = i;
				if(i == 0){
					lowX = i;
				}
				else{
					lowX = (i-interval);
				}
				break;
			}
		}

		double block = .25*interval;

		if(highX == lowX){
			return 0;
		}
		if(x >= lowX + 3.5*block)
			x = (long)highX;
		else if( x >= lowX + 2.5*block)
			x = (long)(highX - block);
		else if(x >= lowX + 1.5*block)
			x = (long)(highX - 2*block);
		else if(x >= lowX + .5*block)
			x = (long)(lowX + block);
		else
			x = (long)lowX;

		return x;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!e.isShiftDown()) {
			return;
		}
		double scroll = e.getWheelRotation();
		scroll = Math.pow(0.95, scroll);
		MotionPathEditor mp = getSelectedPath();
		if (mp != null) {
			myPathChanging = true;
			mp.scalePath(this, scroll, (long) myScalar.unscaleX(e.getX()), mp.getSharedHistory());
		}
		e.consume();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (myPositionSource == null) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F) {
			boolean val = !myPositionSource.getAddEnabled();
			myPositionSource.setAddEnabled(val);
			repaint();
		} else if (e.getKeyCode() == KeyEvent.VK_M) {
			boolean val = !myPositionSource.getEditEnabled();
			myPositionSource.setEditEnabled(val);
			repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_SHIFT:
				endPathChange();
				break;
			case KeyEvent.VK_F:
				//myFeedbackFlag = false;
				break;
			case KeyEvent.VK_M:
				//myMoveFlag = false;
				break;
		}
	}

	private void repaint() {
		if (myPanel != null) {
			RepaintManager.currentManager(myPanel).markCompletelyDirty(myPanel);
		}
	}
}

