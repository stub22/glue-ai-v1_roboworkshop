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
package org.rwshop.swing.common.config;

import org.jflux.api.core.config.Configuration;

/**
 *
 * @author jgpallack
 */
public class ConfigFrame extends javax.swing.JFrame {

    /**
     * Creates new form ConfigTestFrame
     */
    public ConfigFrame() {
        initComponents();
        pack();
    }
    
    public void setConfig(Configuration<String> config) {
        configurationPanel1.setConfig(config, false);
        pack();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        configurationPanel1 = new org.rwshop.swing.common.config.ConfigurationPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout configurationPanel1Layout = new javax.swing.GroupLayout(configurationPanel1);
        configurationPanel1.setLayout(configurationPanel1Layout);
        configurationPanel1Layout.setHorizontalGroup(
            configurationPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
        );
        configurationPanel1Layout.setVerticalGroup(
            configurationPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 302, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(configurationPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(configurationPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for(javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch(ClassNotFoundException ex) {
            org.slf4j.LoggerFactory.getLogger(ConfigFrame.class).error(ex.getMessage(), ex);
        } catch(InstantiationException ex) {
            org.slf4j.LoggerFactory.getLogger(ConfigFrame.class).error(ex.getMessage(), ex);
        } catch(IllegalAccessException ex) {
            org.slf4j.LoggerFactory.getLogger(ConfigFrame.class).error(ex.getMessage(), ex);
        } catch(javax.swing.UnsupportedLookAndFeelException ex) {
            org.slf4j.LoggerFactory.getLogger(ConfigFrame.class).error(ex.getMessage(), ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ConfigFrame ctf = new ConfigFrame();
                ctf.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.rwshop.swing.common.config.ConfigurationPanel configurationPanel1;
    // End of variables declaration//GEN-END:variables
}
