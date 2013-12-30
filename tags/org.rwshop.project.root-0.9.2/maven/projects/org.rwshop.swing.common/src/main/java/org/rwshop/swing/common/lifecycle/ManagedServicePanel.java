/*
 * Copyright 2012 Hanson Robokind LLC.
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
 * ManagedServicePanel.java
 *
 * Created on Feb 13, 2012, 10:51:13 PM
 */
package org.rwshop.swing.common.lifecycle;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.RepaintManager;
import javax.swing.table.DefaultTableModel;
import org.robokind.api.common.lifecycle.DependencyDescriptor;
import org.robokind.api.common.lifecycle.ManagedService;
import org.rwshop.swing.common.InnerScrollPaneWheelListener;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class ManagedServicePanel extends AbstractServicePanel<ManagedService> {
    private final static Logger theLogger = 
            Logger.getLogger(ManagedServicePanel.class.getName());
    private ManagedService<?> myService;
    private ServiceChangeListener myServiceChangeListener;
    private boolean myPropertiesVisible;
    private boolean myDependenciesVisible;
    
    /** Creates new form ManagedServicePanel */
    public ManagedServicePanel() {
        initComponents();
        pnlDependencyList.setLayout(
                new BoxLayout(pnlDependencyList, BoxLayout.Y_AXIS));
        myServiceChangeListener = new ServiceChangeListener();
        myPropertiesVisible = true;
        tblProperties.setTableHeader(null);
        lblType.setOpaque(false);
        jScrollPane2.setOpaque(false);
        jScrollPane2.getViewport().setOpaque(false);
        jScrollPane2.addMouseWheelListener(new InnerScrollPaneWheelListener());
    }
    
    @Override
    public void setService(ManagedService service){
        if(myService == service){
            updateServiceInfo();
            return;
        }
        if(myService != null){
            myService.removePropertyChangeListener(myServiceChangeListener);
        }
        myService = service;
        updateServiceInfo();
        setVals();
        setDependencies();
        myService.addPropertyChangeListener(myServiceChangeListener);
        markRepaint();
    }
    
    private void updateServiceInfo(){
        updateRegistrationStatus();
        if(myService == null){
            lblType.setText("--");
            lblDependencyCount.setText("(--)");
            return;
        }
        String names = "";
        for(String name : myService.getServiceClassNames()){
            if(!names.isEmpty()){
                names += "\n";
            }
            names += name;
        }
        lblType.setText(names);
    }
    
    private void setVals(){
        Properties props = myService.getRegistrationProperties();
        Object[][] objs = new Object[props.size()][2];
        int i = 0;
        for(Entry e : props.entrySet()){
            objs[i][0] = e.getKey();
            objs[i][1] = e.getValue();
            i++;
        }
        tblProperties.setModel(new DefaultTableModel(
                objs, new String []{"Property Name", "Value"}));
        int h = tblProperties.getRowCount() * 
                (tblProperties.getRowHeight()+tblProperties.getRowMargin());
        Dimension dim = pnlProperties.getSize();
        dim.setSize(dim.width, h);
        Dimension dim2 = tblProperties.getSize();
        Dimension d2 = new Dimension(dim2.width, h);
        //tblProperties.setSize(d2);
        pnlProperties.setPreferredSize(d2);
    }
    
    private void setDependencies(){
        pnlDependencyList.clearDependencies();
        if(myService != null){
            for(DependencyDescriptor dep : myService.getDependencies()){
                Boolean status = 
                        myService.getDependencyStatus(dep.getDependencyName());
                pnlDependencyList.addDependency(dep, status);
            }
        }
        updateDependencyCount();
        //resizeDependencies();
    }
    
    private void resizeDependencies(){
        Dimension d = pnlDependencyList.getDependenciesSize();
        pnlDependencyList.setPreferredSize(d);
    }
    
    private void updateDependencyStatus(String dependencyId){
        if(dependencyId == null){
            return;
        }
        Boolean status;
        if(myService == null){
            status = null;
        }else{
            status = myService.getDependencyStatus(dependencyId);
        }
        
        pnlDependencyList.updateDependnecyStatus(dependencyId, status);
        updateDependencyCount();
    }
    
    private void updateDependencyCount(){
        if(myService == null){
            lblDependencyCount.setText("(--/--)");
            return;
        }
        int total = myService.getDependencyCount();
        int available = myService.getAvailableDependencyCount();
        lblDependencyCount.setText("(" + available + "/" + total + ")");
    }
    
    private void updateRegistrationStatus(){
        if(myService == null){
            btnRegister.setEnabled(false);
            btnUnregister.setEnabled(false);
            lblStatus.setText("--");
            return;
        }
        boolean registered = myService.isRegistered();
        boolean available = myService.isAvailable();
        if(registered){
            btnRegister.setEnabled(false);
            btnUnregister.setEnabled(true);
            lblStatus.setText("Available");
        }else if(available){
            btnRegister.setEnabled(true);
            btnUnregister.setEnabled(false);
            lblStatus.setText("Unregistered");
        }else{
            btnRegister.setEnabled(false);
            btnUnregister.setEnabled(false);
            lblStatus.setText("Unavailable");
        }
    }
    
    @Override
    public PropertyChangeListener getServiceChangeListener(){
        return myServiceChangeListener;
    }    
    
    class ServiceChangeListener implements PropertyChangeListener{
        @Override
        public synchronized void propertyChange(PropertyChangeEvent evt) {
            if(evt == null){
                return;
            }
            String name = evt.getPropertyName();
            if(ManagedService.PROP_SERVICE_CHANGED.equals(name)){
                setService((ManagedService)evt.getNewValue());
            }else if(ManagedService.PROP_DEPENDENCY_CHANGED.equals(name)){
                Object obj = evt.getNewValue();
                if(!(obj instanceof String)){
                    return;
                }
                updateServiceInfo();
                updateDependencyStatus((String)obj);
            }
        }
    }
    private void markRepaint(){
        RepaintManager.currentManager(this).markCompletelyDirty(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lblType = new javax.swing.JTextArea();
        pnlCollapsable = new javax.swing.JPanel();
        pnlDepsCollapse = new javax.swing.JPanel();
        lblDependencies = new javax.swing.JLabel();
        lblDependencyCount = new javax.swing.JLabel();
        pnlDependencyList = new org.rwshop.swing.common.lifecycle.DependencyListPanel();
        pnlPropsCollapse = new javax.swing.JPanel();
        lblRegistrationProperties = new javax.swing.JLabel();
        pnlProperties = new javax.swing.JPanel();
        tblProperties = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnUnregister = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnDispose = new javax.swing.JButton();

        jLabel1.setText("Service Type:");

        lblStatus.setText("--");

        jScrollPane2.setBorder(null);
        jScrollPane2.setOpaque(false);

        lblType.setColumns(20);
        lblType.setEditable(false);
        lblType.setLineWrap(true);
        lblType.setRows(1);
        lblType.setWrapStyleWord(true);
        lblType.setBorder(null);
        lblType.setOpaque(false);
        jScrollPane2.setViewportView(lblType);

        javax.swing.GroupLayout pnlTitleLayout = new javax.swing.GroupLayout(pnlTitle);
        pnlTitle.setLayout(pnlTitleLayout);
        pnlTitleLayout.setHorizontalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus)
                .addContainerGap())
        );
        pnlTitleLayout.setVerticalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblStatus, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel1))
        );

        pnlDepsCollapse.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblDependencies.setText("[-]Dependencies");
        lblDependencies.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDependenciesMouseClicked(evt);
            }
        });

        lblDependencyCount.setText("(--/--)");
        lblDependencyCount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDependencyCountMouseClicked(evt);
            }
        });

        pnlDependencyList.setMaximumSize(new java.awt.Dimension(175, 600));

        javax.swing.GroupLayout pnlDependencyListLayout = new javax.swing.GroupLayout(pnlDependencyList);
        pnlDependencyList.setLayout(pnlDependencyListLayout);
        pnlDependencyListLayout.setHorizontalGroup(
            pnlDependencyListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 175, Short.MAX_VALUE)
        );
        pnlDependencyListLayout.setVerticalGroup(
            pnlDependencyListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlDepsCollapseLayout = new javax.swing.GroupLayout(pnlDepsCollapse);
        pnlDepsCollapse.setLayout(pnlDepsCollapseLayout);
        pnlDepsCollapseLayout.setHorizontalGroup(
            pnlDepsCollapseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDepsCollapseLayout.createSequentialGroup()
                .addComponent(lblDependencies)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDependencyCount)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnlDependencyList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlDepsCollapseLayout.setVerticalGroup(
            pnlDepsCollapseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDepsCollapseLayout.createSequentialGroup()
                .addGroup(pnlDepsCollapseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDependencies)
                    .addComponent(lblDependencyCount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDependencyList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlPropsCollapse.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRegistrationProperties.setText("[-]Registration Properties");
        lblRegistrationProperties.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegistrationPropertiesMouseClicked(evt);
            }
        });

        tblProperties.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Property Name", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProperties.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        tblProperties.setCellSelectionEnabled(true);
        tblProperties.setFillsViewportHeight(true);
        tblProperties.setMinimumSize(new java.awt.Dimension(0, 0));
        tblProperties.getTableHeader().setReorderingAllowed(false);

        javax.swing.GroupLayout pnlPropertiesLayout = new javax.swing.GroupLayout(pnlProperties);
        pnlProperties.setLayout(pnlPropertiesLayout);
        pnlPropertiesLayout.setHorizontalGroup(
            pnlPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tblProperties, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
        );
        pnlPropertiesLayout.setVerticalGroup(
            pnlPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tblProperties, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlPropsCollapseLayout = new javax.swing.GroupLayout(pnlPropsCollapse);
        pnlPropsCollapse.setLayout(pnlPropsCollapseLayout);
        pnlPropsCollapseLayout.setHorizontalGroup(
            pnlPropsCollapseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblRegistrationProperties, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlProperties, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlPropsCollapseLayout.setVerticalGroup(
            pnlPropsCollapseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPropsCollapseLayout.createSequentialGroup()
                .addComponent(lblRegistrationProperties)
                .addGap(8, 8, 8)
                .addComponent(pnlProperties, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlCollapsableLayout = new javax.swing.GroupLayout(pnlCollapsable);
        pnlCollapsable.setLayout(pnlCollapsableLayout);
        pnlCollapsableLayout.setHorizontalGroup(
            pnlCollapsableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPropsCollapse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDepsCollapse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlCollapsableLayout.setVerticalGroup(
            pnlCollapsableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCollapsableLayout.createSequentialGroup()
                .addComponent(pnlPropsCollapse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDepsCollapse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnUnregister.setText("Unregister");
        btnUnregister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnregisterActionPerformed(evt);
            }
        });

        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        btnDispose.setText("Dispose");
        btnDispose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisposeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlButtonsLayout = new javax.swing.GroupLayout(pnlButtons);
        pnlButtons.setLayout(pnlButtonsLayout);
        pnlButtonsLayout.setHorizontalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUnregister, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDispose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlButtonsLayout.setVerticalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addComponent(btnRegister)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUnregister)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDispose))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(pnlCollapsable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCollapsable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnUnregisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnregisterActionPerformed
        if(myService == null){
            btnUnregister.setEnabled(false);
            btnRegister.setEnabled(false);
            return;
        }
        myService.setRegistrationEnabled(false);
    }//GEN-LAST:event_btnUnregisterActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        if(myService == null){
            btnUnregister.setEnabled(false);
            btnRegister.setEnabled(false);
            return;
        }
        myService.setRegistrationEnabled(true);
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void lblRegistrationPropertiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegistrationPropertiesMouseClicked
        if(myPropertiesVisible){
            lblRegistrationProperties.setText("[+]Registration Properties");
            myPropertiesVisible = false;
            pnlProperties.setVisible(false);
        }else{
            lblRegistrationProperties.setText("[-]Registration Properties");
            myPropertiesVisible = true;
            pnlProperties.setVisible(true);
        }
    }//GEN-LAST:event_lblRegistrationPropertiesMouseClicked

    private void lblDependenciesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDependenciesMouseClicked
        toggleDependenciesVisible();
    }//GEN-LAST:event_lblDependenciesMouseClicked

    private void lblDependencyCountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDependencyCountMouseClicked
        toggleDependenciesVisible();
    }//GEN-LAST:event_lblDependencyCountMouseClicked

    private void btnDisposeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisposeActionPerformed
        disposeService();
    }//GEN-LAST:event_btnDisposeActionPerformed

    private void toggleDependenciesVisible(){
        if(myDependenciesVisible){
            lblDependencies.setText("[+]Dependencies");
            myDependenciesVisible = false;
            pnlDependencyList.setVisible(false);
        }else{
            lblDependencies.setText("[-]Dependencies");
            myDependenciesVisible = true;
            pnlDependencyList.setVisible(true);
        }
    }
    
    private void disposeService(){
        myService.dispose();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDispose;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnUnregister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDependencies;
    private javax.swing.JLabel lblDependencyCount;
    private javax.swing.JLabel lblRegistrationProperties;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextArea lblType;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlCollapsable;
    private org.rwshop.swing.common.lifecycle.DependencyListPanel pnlDependencyList;
    private javax.swing.JPanel pnlDepsCollapse;
    private javax.swing.JPanel pnlProperties;
    private javax.swing.JPanel pnlPropsCollapse;
    private javax.swing.JPanel pnlTitle;
    private javax.swing.JTable tblProperties;
    // End of variables declaration//GEN-END:variables
}