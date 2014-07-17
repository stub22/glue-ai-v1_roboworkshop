/*
 * Copyright 2014 the Friendularity Project
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

package org.rwshop.swing.motion.connection;

import java.util.ArrayList;
import java.util.List;
import org.jflux.api.core.Source;
import org.jflux.spec.discovery.Discoverer;
import org.jflux.spec.discovery.UniqueService;

/**
 *
 * @author Amy Jessica Book <jgpallack@gmail.com>
 */
public class SelectorFrame extends javax.swing.JFrame {
    private static SelectorFrame theInstance;
    private List<Source<UniqueService>> mySelectors;
    
    /**
     * Creates new form SelectorFrame
     */
    public SelectorFrame() {
        initComponents();
        
        mySelectors = new ArrayList<Source<UniqueService>>(2);
        mySelectors.add(robotSelector1);
        mySelectors.add(iPPanel1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        robotSelector1 = new org.rwshop.swing.motion.connection.RobotSelector();
        iPPanel1 = new org.rwshop.swing.motion.connection.IPPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Select a Robot");
        setAlwaysOnTop(true);

        jTabbedPane1.addTab("Discovery", robotSelector1);
        jTabbedPane1.addTab("IP Address", iPPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                SelectorFrame frame = new SelectorFrame();
                Discoverer discoverer = new Discoverer();
                Thread thread = new Thread(discoverer);
                
                frame.initialize(discoverer);
                thread.start();
                frame.setVisible(true);
            }
        });
    }
    
    public void initialize(Discoverer discoverer) {
        robotSelector1.setDiscoverer(discoverer);
    }
    
    public static void instantiate() {
        if(theInstance == null) {
            theInstance = new SelectorFrame();
            theInstance.setVisible(false);
            
            Discoverer discoverer = new Discoverer();
            Thread thread = new Thread(discoverer); 
            theInstance.initialize(discoverer);
            thread.start();
        }
    }
    
    public static SelectorFrame getInstance() {
        return theInstance;
    }
    
    public List<Source<UniqueService>> getSelectors() {        
        return mySelectors;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.rwshop.swing.motion.connection.IPPanel iPPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private org.rwshop.swing.motion.connection.RobotSelector robotSelector1;
    // End of variables declaration//GEN-END:variables
}
