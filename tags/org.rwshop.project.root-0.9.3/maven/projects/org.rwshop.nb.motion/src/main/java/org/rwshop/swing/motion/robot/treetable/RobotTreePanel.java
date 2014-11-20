/*
 * Copyright 2011 Hanson Robokind LLC.
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
 * RobotTreePanel.java
 *
 * Created on Oct 20, 2011, 12:35:39 AM
 */
package org.rwshop.swing.motion.robot.treetable;

import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.jflux.impl.services.rk.osgi.ServiceClassListener;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.OutlineModel;
import org.osgi.framework.BundleContext;
import org.robokind.api.motion.Robot;
import org.rwshop.swing.motion.jointgroup.JointTreeRenderer;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class RobotTreePanel extends javax.swing.JPanel {
    private Robot myRobot;
    private RobotTreeRowModel myRowModel;
    private RobotTreeModel myTreeModel;
    private ServiceClassListener<Robot> myRobotTracker;
    /** Creates new form JointTreePanel */
    public RobotTreePanel() {
        initComponents();
        BundleContext context = OSGiUtils.getBundleContext(Robot.class);
        if(context == null){
            return;
        }
        myRobotTracker = new ServiceClassListener<Robot>(Robot.class, context, null) {

            @Override
            protected void addService(Robot t) {
                setRobot();
            }

            @Override
            protected void removeService(Robot t) {
                setRobot();
            }
            
            private void setRobot(){
                Robot group = getTopService();
                if(group == null){
                    return;
                }else if(group == myRobot){
                    return;
                }
                setModel(group);
            }
        };
        myRobotTracker.start();
    }
    
    protected void setModel(Robot rootGroup){
        myRobot = rootGroup;
        myRowModel = new RobotTreeRowModel();
        myTreeModel = new RobotTreeModel(rootGroup);
        OutlineModel mdl = DefaultOutlineModel.createOutlineModel(myTreeModel, myRowModel);
        myRobotTreeOutline.setRootVisible(true);
        myRobotTreeOutline.setModel(mdl);
        myRobotTreeOutline.setRenderDataProvider(new JointTreeRenderer());
        jScrollPane1.setViewportView(myRobotTreeOutline);
        
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
        myRobotTreeOutline = new org.netbeans.swing.outline.Outline();

        jScrollPane1.setViewportView(myRobotTreeOutline);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private org.netbeans.swing.outline.Outline myRobotTreeOutline;
    // End of variables declaration//GEN-END:variables
}