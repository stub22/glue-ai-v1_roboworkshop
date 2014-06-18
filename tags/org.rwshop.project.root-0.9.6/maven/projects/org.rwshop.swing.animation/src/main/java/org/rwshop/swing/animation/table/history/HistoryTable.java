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
 * HistoryTable.java
 *
 * Created on Mar 8, 2011, 2:30:32 PM
 */

package org.rwshop.swing.animation.table.history;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.mechio.api.animation.editor.history.HistoryStack;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class HistoryTable extends javax.swing.JPanel {
    private HistoryTableModel myModel;
    private HistoryStack myHistory;
    private HistoryTableHistoryStackListener myHistoryListener;
    private HistoryTableListener myTableListener;
    /** Creates new form HistoryTable */
    public HistoryTable() {
        initComponents();
        myModel = new HistoryTableModel();
        myTableListener = new HistoryTableListener();
        myHistoryListener = new HistoryTableHistoryStackListener(myTable);
        myTable.setModel(myModel);
        myTable.getSelectionModel().addListSelectionListener(myTableListener);
        myTableListener.setTable(myTable);
        myHistoryListener.setModel(myModel);
        myModel.addTableModelListener(myTableListener);
        jScrollPane1.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    /**
     *
     */
    public void hideHeader(){
        myTable.setTableHeader(null);
    }

    /**
     *
     * @param hist
     */
    public void setHistory(HistoryStack hist){
        if(myHistory != null){
            myHistory.removeListener(myHistoryListener);
        }
        myHistory = hist;
        myModel.setHistory(myHistory);
        myTableListener.setHistory(myHistory);
        if(myHistory != null){
            myHistory.addListener(myHistoryListener);
        }
    }

    /**
     *
     * @param name
     */
    public void setTableHeader(String name) {
        JTableHeader th = new JTableHeader();
        TableColumnModel tcm = new DefaultTableColumnModel();
        TableColumn tc = new TableColumn();
        tcm.addColumn(tc);
        th.setColumnModel(tcm);
        tc.setHeaderValue(name);
        tc.setWidth(myTable.getWidth());
        myTable.setTableHeader(th);
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
        myTable = new javax.swing.JTable();

        myTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        myTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(myTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable myTable;
    // End of variables declaration//GEN-END:variables

}
