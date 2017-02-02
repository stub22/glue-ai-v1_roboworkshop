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

/*
 * DynamixelControlLoopPanel.java
 *
 * Created on Mar 20, 2012, 1:54:14 PM
 */
package org.rwshop.swing.motion.dynamixel;

import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.jflux.impl.services.rk.osgi.ServiceClassListener;
import org.mechio.api.motion.servos.ServoController;
import org.mechio.impl.motion.dynamixel.DynamixelController;
import org.mechio.impl.motion.dynamixel.feedback.DynamixelControlLoop;
import org.mechio.impl.motion.dynamixel.feedback.DynamixelControlSettings;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class DynamixelControlLoopPanel extends javax.swing.JPanel {
	private static final Logger theLogger = LoggerFactory.getLogger(DynamixelControlLoopPanel.class);

	private DynamixelControlSettings mySettings;
	private DynamixelControlLoop myLoop;
	private DynamixelListener myListener;

	/**
	 * Creates new form DynamixelControlLoopPanel
	 */
	public DynamixelControlLoopPanel() {
		initComponents();
	}

	public void init(DynamixelControlLoop loop) {
		if (loop == null) {
			myLoop = null;
			mySettings = null;
			clearDisplay();
		}
		myLoop = loop;
		mySettings = myLoop.getSettings();
		updateDisplay();
	}

	public void updateDisplay() {
		tglRun.setSelected(mySettings.getRunFlag());
		chkUpdates.setSelected(mySettings.getUpdateFlag());
		chkMove.setSelected(mySettings.getMoveFlag());
		chkCommands.setSelected(mySettings.getCommandFlag());

		txtReadCount.setText(p(mySettings.getReadCount()));
		txtReturnDelay.setText(p(mySettings.getReturnDelay()));
		txtWriteLag.setText(p(mySettings.getCommandSendDelay()));
		txtMaxTemp.setText(p(mySettings.getMaxRunTemperature()));
		txtCooldownTemp.setText(p(mySettings.getCooldownTemperature()));
	}

	public void clearDisplay() {
		tglRun.setSelected(false);
		chkUpdates.setSelected(false);
		chkMove.setSelected(false);
		chkCommands.setSelected(false);

		txtReadCount.setText("");
		txtReturnDelay.setText("");
		txtWriteLag.setText("");
		txtMaxTemp.setText("");
		txtCooldownTemp.setText("");
	}


	public void initialize(BundleContext context) {
		if (context == null || myListener != null) {
			return;
		}
		myListener = new DynamixelListener(context);
	}

	private String p(Object obj) {
		return obj.toString();
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		tglRun = new javax.swing.JToggleButton();
		pnlLoopFlags = new javax.swing.JPanel();
		chkMove = new javax.swing.JCheckBox();
		chkCommands = new javax.swing.JCheckBox();
		chkUpdates = new javax.swing.JCheckBox();
		jPanel2 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		txtReadCount = new javax.swing.JTextField();
		txtReturnDelay = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		txtWriteLag = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		txtMaxTemp = new javax.swing.JTextField();
		txtCooldownTemp = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		btnApply = new javax.swing.JButton();

		jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		tglRun.setText("Run");
		tglRun.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tglRunActionPerformed(evt);
			}
		});

		chkMove.setText("Move");
		chkMove.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				chkMoveActionPerformed(evt);
			}
		});

		chkCommands.setText("Commands");
		chkCommands.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				chkCommandsActionPerformed(evt);
			}
		});

		chkUpdates.setText("Updates");
		chkUpdates.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				chkUpdatesActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout pnlLoopFlagsLayout = new javax.swing.GroupLayout(pnlLoopFlags);
		pnlLoopFlags.setLayout(pnlLoopFlagsLayout);
		pnlLoopFlagsLayout.setHorizontalGroup(
				pnlLoopFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(chkMove)
						.addComponent(chkCommands)
						.addComponent(chkUpdates)
		);
		pnlLoopFlagsLayout.setVerticalGroup(
				pnlLoopFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(pnlLoopFlagsLayout.createSequentialGroup()
								.addComponent(chkMove)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(chkCommands)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(chkUpdates))
		);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(tglRun, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(pnlLoopFlags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addContainerGap(127, Short.MAX_VALUE))
		);
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(tglRun)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(pnlLoopFlags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(15, Short.MAX_VALUE))
		);

		jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		jLabel1.setText("Read Count");

		jLabel2.setText("Return Delay");

		jLabel3.setText("Write Latency");

		jLabel4.setText("Max Temp");

		jLabel5.setText("Cooldown Temp");

		btnApply.setText("Apply Changes");
		btnApply.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnApplyActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel2Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel2Layout.createSequentialGroup()
												.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jLabel2)
														.addComponent(jLabel3)
														.addComponent(jLabel1)
														.addComponent(jLabel4)
														.addComponent(jLabel5))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
														.addComponent(txtCooldownTemp, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(txtReturnDelay, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(txtWriteLag, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(txtMaxTemp, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(txtReadCount, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addComponent(btnApply))
								.addContainerGap(18, Short.MAX_VALUE))
		);
		jPanel2Layout.setVerticalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel2Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel1)
										.addComponent(txtReadCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel2)
										.addComponent(txtReturnDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel3)
										.addComponent(txtWriteLag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel4)
										.addComponent(txtMaxTemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(txtCooldownTemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel5))
								.addGap(18, 18, 18)
								.addComponent(btnApply)
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
	}// </editor-fold>//GEN-END:initComponents

	private void chkUpdatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUpdatesActionPerformed
		if (mySettings == null) {
			return;
		}
		boolean val = chkUpdates.isSelected();
		mySettings.setUpdateFlag(val);
	}//GEN-LAST:event_chkUpdatesActionPerformed

	private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
		if (mySettings == null) {
			return;
		}
		try {
			int read = Integer.parseInt(txtReadCount.getText());
			int returnDelay = Integer.parseInt(txtReturnDelay.getText());
			double writeLag = Double.parseDouble(txtWriteLag.getText());
			double hot = Double.parseDouble(txtMaxTemp.getText());
			double cool = Double.parseDouble(txtCooldownTemp.getText());

			if (read != mySettings.getReadCount()
					|| returnDelay != mySettings.getReturnDelay()) {
				mySettings.setReadCount(read);
				mySettings.setReturnDelay(returnDelay);
				delayChange();
			}

			mySettings.setCommandSendDelay(writeLag);
			mySettings.setMaxRunTemperature(hot);
			mySettings.setCooldownTemperature(cool);
		} catch (Exception ex) {
			theLogger.warn("Error parsing and setting values.", ex);
		}

	}//GEN-LAST:event_btnApplyActionPerformed

	private void chkCommandsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCommandsActionPerformed
		if (mySettings == null) {
			return;
		}
		boolean val = chkCommands.isSelected();
		mySettings.setCommandFlag(val);
	}//GEN-LAST:event_chkCommandsActionPerformed

	private void chkMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMoveActionPerformed
		if (mySettings == null) {
			return;
		}
		boolean val = chkMove.isSelected();
		mySettings.setMoveFlag(val);
	}//GEN-LAST:event_chkMoveActionPerformed

	private void tglRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglRunActionPerformed
		if (mySettings == null) {
			return;
		}
		boolean val = tglRun.isSelected();
		mySettings.setRunFlag(val);
	}//GEN-LAST:event_tglRunActionPerformed

	private void delayChange() {
		if (!mySettings.getRunFlag()) {
			return;
		}
		boolean update = mySettings.getUpdateFlag();
		boolean move = mySettings.getMoveFlag();
		boolean cmd = mySettings.getCommandFlag();
		mySettings.setCommandFlag(true);
		mySettings.setMoveFlag(false);
		mySettings.setUpdateFlag(false);
		//myLoop.updateReturnDelays();
		mySettings.setCommandFlag(cmd);
		mySettings.setMoveFlag(move);
		mySettings.setUpdateFlag(update);
	}

	class DynamixelListener {
		private BundleContext myContext;
		private ServiceClassListener<ServoController> myControllerTracker;
		private DynamixelController myController;

		public DynamixelListener(BundleContext context) {
			if (context == null) {
				throw new NullPointerException();
			}
			String filter =
					OSGiUtils.createFilter(
							ServoController.PROP_VERSION,
							DynamixelController.VERSION.toString());
			myControllerTracker =
					new ServiceClassListener<ServoController>(
							ServoController.class, context, filter) {

						@Override
						protected void addService(ServoController t) {
							setController();
						}

						@Override
						protected void removeService(ServoController t) {
							setController();
						}

						private void setController() {
							ServoController controller = getTopService();
							if (controller == myController) {
								return;
							} else if (controller == null
									|| !(controller instanceof DynamixelController)) {
								init(null);
							} else {
								myController = (DynamixelController) controller;
								DynamixelControlLoop loop =
										myController.getControlLoop();
								init(loop);
							}
						}
					};
			myControllerTracker.start();
			myContext = context;
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnApply;
	private javax.swing.JCheckBox chkCommands;
	private javax.swing.JCheckBox chkMove;
	private javax.swing.JCheckBox chkUpdates;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel pnlLoopFlags;
	private javax.swing.JToggleButton tglRun;
	private javax.swing.JTextField txtCooldownTemp;
	private javax.swing.JTextField txtMaxTemp;
	private javax.swing.JTextField txtReadCount;
	private javax.swing.JTextField txtReturnDelay;
	private javax.swing.JTextField txtWriteLag;
	// End of variables declaration//GEN-END:variables
}
