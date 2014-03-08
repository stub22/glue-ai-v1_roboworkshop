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
 * JointTreePanel.java
 *
 * Created on Aug 20, 2011, 1:23:31 AM
 */
package org.rwshop.swing.motion.jointgroup;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.RepaintManager;
import org.jflux.impl.services.rk.osgi.ServiceClassListener;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.OutlineModel;
import org.osgi.framework.BundleContext;
import org.mechio.api.motion.Joint;
import org.mechio.api.motion.jointgroup.JointGroup;
import org.mechio.api.motion.JointProperty;
import org.mechio.api.motion.joint_properties.ReadCurrentPosition;
import org.mechio.api.motion.joint_properties.ReadLoad;
import org.mechio.api.motion.joint_properties.ReadTemperature;
import org.mechio.api.motion.joint_properties.ReadVoltage;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class JointTreePanel extends javax.swing.JPanel {
    private JointGroup myRootGroup;
    private JointTreeRowModel myRowModel;
    private JointGroupTreeModel myTreeModel;
    private JointTreeJointGroupListener myListenerTree;
    private List<String> myPropertyNames;
    private ServiceClassListener<JointGroup> myGroupTracker;
    private boolean myInitializedFlag;
    /** Creates new form JointTreePanel */
    public JointTreePanel() {
        initComponents();
        myPropertyNames = Arrays.asList(
                "absGoalPosition", 
                ReadCurrentPosition.PROPERTY_NAME,
                "absCurrentPosition", 
                ReadLoad.PROPERTY_NAME, 
                ReadTemperature.PROPERTY_NAME, 
                ReadVoltage.PROPERTY_NAME);
        myInitializedFlag = false;
    }
    
    public boolean isInitialized(){
        return myInitializedFlag;
    }
    
    public void initialize(BundleContext context){
        if(context == null || myInitializedFlag){
            return;
        }
        myGroupTracker = new ServiceClassListener<JointGroup>(JointGroup.class, context, null) {

            @Override
            protected void addService(JointGroup t) {
                setJointGroup();
            }

            @Override
            protected void removeService(JointGroup t) {
                setJointGroup();
            }
            
            private void setJointGroup(){
                JointGroup group = getTopService();
                if(group == null){
                    return;
                }else if(group == myRootGroup){
                    return;
                }
                setModel(group, myPropertyNames);
            }
        };
        myGroupTracker.start();
        myInitializedFlag = true;
    }
    
    protected void setModel(JointGroup rootGroup, List<String> propNames){
        myRootGroup = rootGroup;
        myPropertyNames = propNames;
        myRowModel = new JointTreeRowModel(myPropertyNames);
        myTreeModel = new JointGroupTreeModel(rootGroup);
        OutlineModel mdl = DefaultOutlineModel.createOutlineModel(myTreeModel, myRowModel);
        myJointTreeOutline.setRootVisible(true);
        myJointTreeOutline.setModel(mdl);
        myJointTreeOutline.setRenderDataProvider(new JointTreeRenderer());
        jScrollPane1.setViewportView(myJointTreeOutline);
        if(myListenerTree != null){
            myListenerTree.stopListening();
        }
        myListenerTree = JointTreeJointGroupListener.buildListenerTree(myRootGroup, myJointTreeOutline, myPropertyNames);
        
    }
    
    private ScheduledExecutorService s = null;
    public void startUpdates(long interval){
        final JointTreePanel panel = this;
        if(s != null){  
            return;
        }
        s = new ScheduledThreadPoolExecutor(1);
        s.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            panel.readJointProperties();
                            RepaintManager.currentManager(panel)
                                    .markCompletelyDirty(panel);
                        } catch(Exception e) {
                            // fix for autoupdate problem
                        }
                    }
                }, 0, interval, TimeUnit.MILLISECONDS);
    }
    
    public void stopUpdates(){
        if(s == null){
            return;
        }
        if(!s.isShutdown() || !s.isTerminated()){
            s.shutdown();
        }
        s = null;
    }
    
    public void readJointProperties(){
        readJointPropertiesRec(myRootGroup, myPropertyNames);
    }
    
    private void readJointPropertiesRec(JointGroup<?,JointGroup,Joint> group, List<String> props){
        for(Joint j : group.getJoints()){
            if(j == null){
                continue;
            }
            for(String s : props){
                JointProperty jp = j.getProperty(s);
                if(jp == null){
                    continue;
                }
                jp.getValue();
            }
        }
        for(JointGroup g : group.getJointGroups()){
            readJointPropertiesRec(g, props);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        myJointTreeOutline = new org.netbeans.swing.outline.Outline();

        jScrollPane1.setViewportView(myJointTreeOutline);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private org.netbeans.swing.outline.Outline myJointTreeOutline;
    // End of variables declaration//GEN-END:variables
}
