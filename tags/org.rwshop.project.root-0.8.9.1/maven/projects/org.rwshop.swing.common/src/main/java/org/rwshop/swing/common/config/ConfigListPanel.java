/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rwshop.swing.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jflux.api.core.config.Configuration;

/**
 *
 * @author Jason G. Pallack <jgpallack@gmail.com>
 */
public class ConfigListPanel extends javax.swing.JPanel {

    private Configuration<String> currConfig;
    private Vector<String> listData;
    private int myCount;
    private Map<String, Configuration<String>> myConfigs;
    private ConfigFrame myConfigFrame;

    /**
     * Creates new form ConfigListPanel
     */
    public ConfigListPanel() {
        initComponents();

        listData = new Vector<String>();
        jList1.setListData(listData);
        myCount = 1;
        myConfigs = new HashMap<String, Configuration<String>>();
        currConfig = null;

        ListSelectionModel lsm = jList1.getSelectionModel();
        final JList finalList = jList1;
        lsm.addListSelectionListener(new ListSelectionListener() {
            @Override
            public synchronized void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    if(finalList.getSelectedIndex() != -1) {
                        String panelName = (String)finalList.getSelectedValue();
                        Configuration<String> config = myConfigs.get(panelName);

                        setConfig(config);
                    } else {
                        setConfig(null);
                    }
                }
            }
        });
    }

    public void setConfigFrame(ConfigFrame frame) {
        myConfigFrame = frame;

        frame.setConfig(currConfig);
    }

    public void pushService(Configuration<String> config) {
        String configName = "Configuration " + myCount;
        
        if(config.containsKey("configurationName")) {
            configName = config.getPropertyValue("configurationName");
        } else {
            myCount++;
        }

        myConfigs.put(configName, config);

        listData = new Vector<String>(myConfigs.keySet());
        jList1.setListData(listData);
    }

    public void popService(Configuration<String> config) {
        if(config == currConfig) {
            setConfig(null);
        }

        for(String key : myConfigs.keySet()) {
            if(myConfigs.get(key) == config) {
                myConfigs.remove(key);
                break;
            }
        }

        listData = new Vector<String>(myConfigs.keySet());
        jList1.setListData(listData);
    }

    private synchronized void setConfig(Configuration<String> config) {
        currConfig = config;

        if(myConfigFrame != null) {
            myConfigFrame.setConfig(config);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}