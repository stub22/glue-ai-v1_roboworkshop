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
 * AnimationJobListPanel.java
 *
 * Created on Apr 28, 2011, 1:33:08 PM
 */

package org.rwshop.swing.animation.joblist;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import org.jflux.api.common.rk.utils.TimeUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.mechio.api.animation.player.AnimationJob;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationJobListPanel extends javax.swing.JPanel {
    private final static Border theBorder = new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY);
    private List<AnimationJobPanel> myAnimationJobPanels;
    private Map<AnimationJob,AnimationJobPanel> myAnimationJobMap;
    private AnimationJobServiceListener myServiceListener;
    /** Creates new form AnimationJobListPanel */
    public AnimationJobListPanel() {
        initComponents();
        myAnimationJobPanels = new ArrayList();
        myAnimationJobMap = new HashMap();
        myAnimationJobsPanel.setLayout(new BoxLayout(myAnimationJobsPanel,BoxLayout.Y_AXIS));
        startListening();
    }

    /**
     *
     * @param job
     */
    public void addAnimationJob(AnimationJob job){
        addJob(job);
        myAnimationJobsPanel.revalidate();
    }

    /**
     *
     * @param jobs
     */
    public void addAnimationJobs(List<AnimationJob> jobs){
        for(AnimationJob job : jobs){
            addJob(job);
        }
        myAnimationJobsPanel.revalidate();
    }

    /**
     *
     */
    public void startListening(){
        Bundle bundle = FrameworkUtil.getBundle(AnimationJob.class);
        if(bundle == null){
            return;
        }
        BundleContext context = bundle.getBundleContext();
        startListening(context);
    }

    /**
     *
     * @param context
     */
    public void startListening(BundleContext context){
        if(context == null || myServiceListener != null){
            return;
        }
        myServiceListener = new AnimationJobServiceListener(context, this);
    }

    private void addJob(AnimationJob job){
        if(job == null){
            return;
        }
        if(myAnimationJobMap.containsKey(job)){
           return;
        }
        AnimationJobPanel panel = new AnimationJobPanel();
        panel.setBorder(theBorder);
        panel.addCloseListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closePanel(e);
            }
        });
        panel.setAnimationJob(job);
        myAnimationJobMap.put(job, panel);
        myAnimationJobPanels.add(panel);
        myAnimationJobsPanel.add(panel);
		btnClear.setEnabled(true);
    }

    /**
     *
     * @param job
     */
    public void removeAnimationJob(AnimationJob job){
        AnimationJobPanel panel = myAnimationJobMap.remove(job);
        if(panel == null){
            return;
        }
        myAnimationJobPanels.remove(panel);
        myAnimationJobsPanel.remove(panel);
        myAnimationJobsPanel.revalidate();
        myAnimationJobsPanel.repaint();
    }

	public void removeAllAnimationJobs() {
		if (myAnimationJobPanels.isEmpty()) {
			return;
		}
		AnimationJob job;
		for (int i = myAnimationJobPanels.size(); i > 0; i--) {
			job = myAnimationJobPanels.get(i - 1).getAnimationJob();
			removeAnimationJob(job);
		}
		btnClear.setEnabled(false);
	}

    private void closePanel(ActionEvent e){
        Object obj = e.getSource();
        if(obj == null || !(obj instanceof AnimationJobPanel)){
            return;
        }
        AnimationJobPanel panel = (AnimationJobPanel)obj;
        if(panel == null){
            return;
        }
        AnimationJob job = panel.getAnimationJob();
        if(job == null){
            return;
        }
        job.stop(TimeUtils.now());
        removeAnimationJob(job);
        if(myServiceListener != null){
            myServiceListener.unregisterService(job);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myAnimationJobsScrollPane = new javax.swing.JScrollPane();
        myAnimationJobsPanel = new javax.swing.JPanel();
        btnClear = new javax.swing.JButton();

        myAnimationJobsScrollPane.setBorder(null);

        javax.swing.GroupLayout myAnimationJobsPanelLayout = new javax.swing.GroupLayout(myAnimationJobsPanel);
        myAnimationJobsPanel.setLayout(myAnimationJobsPanelLayout);
        myAnimationJobsPanelLayout.setHorizontalGroup(
            myAnimationJobsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );
        myAnimationJobsPanelLayout.setVerticalGroup(
            myAnimationJobsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        myAnimationJobsScrollPane.setViewportView(myAnimationJobsPanel);

        btnClear.setText("Clear");
        btnClear.setEnabled(false);
        btnClear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnClear.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClear)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(myAnimationJobsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myAnimationJobsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        removeAllAnimationJobs();
    }//GEN-LAST:event_btnClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JPanel myAnimationJobsPanel;
    private javax.swing.JScrollPane myAnimationJobsScrollPane;
    // End of variables declaration//GEN-END:variables

}
