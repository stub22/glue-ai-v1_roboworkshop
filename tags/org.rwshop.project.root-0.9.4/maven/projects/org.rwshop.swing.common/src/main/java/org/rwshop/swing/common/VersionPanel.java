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
 * VersionPanel.java
 *
 * Created on May 13, 2011, 6:28:41 PM
 */

package org.rwshop.swing.common;

import org.jflux.api.common.rk.config.VersionProperty;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class VersionPanel extends javax.swing.JPanel {
    
    /** Creates new form VersionPanel */
    public VersionPanel() {
        initComponents();
    }

    /**
     *
     * @param version
     */
    public void setVersion(VersionProperty version){
        setVersionName(version.getName());
        setVersionNumber(version.getNumber());
    }
    
    /**
     *
     * @param name
     */
    public void setVersionName(String name){
        myVersionNameTxtBox.setText(name);
    }
    
    /**
     *
     * @param number
     */
    public void setVersionNumber(String number){
        myVersionNumberTxtBox.setText(number);
    }

    /**
     *
     * @return
     */
    public VersionProperty getVersion(){
        String name = myVersionNameTxtBox.getText();
        String number = myVersionNumberTxtBox.getText();
        VersionProperty version = new VersionProperty(name, number);
        return version;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myVersionNameLabel = new javax.swing.JLabel();
        myVersionNameTxtBox = new javax.swing.JTextField();
        myVersionNumberLabel = new javax.swing.JLabel();
        myVersionNumberTxtBox = new javax.swing.JTextField();

        myVersionNameLabel.setText("Name:");

        myVersionNameTxtBox.setMaximumSize(new java.awt.Dimension(200, 200));
        myVersionNameTxtBox.setMinimumSize(new java.awt.Dimension(40, 20));

        myVersionNumberLabel.setText("Version");

        myVersionNumberTxtBox.setMinimumSize(new java.awt.Dimension(20, 20));
        myVersionNumberTxtBox.setPreferredSize(new java.awt.Dimension(55, 20));
        myVersionNumberTxtBox.setRequestFocusEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(myVersionNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myVersionNameTxtBox, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(myVersionNumberLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myVersionNumberTxtBox, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(myVersionNameLabel)
                .addComponent(myVersionNumberTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(myVersionNameTxtBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(myVersionNumberLabel))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel myVersionNameLabel;
    private javax.swing.JTextField myVersionNameTxtBox;
    private javax.swing.JLabel myVersionNumberLabel;
    private javax.swing.JTextField myVersionNumberTxtBox;
    // End of variables declaration//GEN-END:variables

}