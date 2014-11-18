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
 * AnimationTable.java
 *
 * Created on Feb 16, 2011, 2:31:52 AM
 */

package org.rwshop.swing.animation.table.animation;

import javax.swing.border.EmptyBorder;
import javax.swing.Icon;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.editor.ChannelEditor;
import org.rwshop.swing.common.ColorEditor;
import org.rwshop.swing.common.ColorRenderer;
import org.rwshop.swing.common.TableHelper;
import org.rwshop.swing.common.utils.SettingsRepository;

import static org.jflux.api.common.rk.localization.Localizer.$;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationTable extends javax.swing.JPanel {
    private AnimationTableModel myTableModel;
    private AnimationTableChannelListener myChannelListener;
    private AnimationTableContextMenu myContextMenu;
    private AnimationTableAnimationListener myAnimationListener;
    private AnimationTableListener myTableListener;
    private AnimationTableLayoutListener myLayoutListener;
    private AnimationEditor myController;

    /** Creates new form AnimationTable */
    public AnimationTable() {
        initComponents();
        myTableModel = new AnimationTableModel();
        myTable.setModel(myTableModel);

        myChannelListener = new AnimationTableChannelListener();
        myChannelListener.setTableModel(myTableModel);

        myAnimationListener = new AnimationTableAnimationListener();
        myAnimationListener.setChannelListener(myChannelListener);
        myAnimationListener.setTableModel(myTableModel);
        myAnimationListener.setTable(myTable);

        myTableListener = new AnimationTableListener();
        myTableListener.setTable(myTable);
        myTable.getSelectionModel().addListSelectionListener(myTableListener);

        myLayoutListener = new AnimationTableLayoutListener();
        myTable.addComponentListener(myLayoutListener);
        myContextMenu = new AnimationTableContextMenu(myTable);
        //setTableHeader($("animation.channels"));
        jScrollPane1.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    /**
     *
     * @param controller
     */
    public void setController(AnimationEditor controller){
        if(myController == controller){
            return;
        }
        myController = controller;
        myChannelListener.cleanConsumer();
        myAnimationListener.cleanConsumer();

        myTableModel.setController(myController);
        myChannelListener.setController(myController);
        myTableListener.setController(myController);
        myContextMenu.setController(myController);

        if(myController != null){
            myController.addConsumer(myAnimationListener);
            for(ChannelEditor cc : myController.getChildren()){
                cc.addConsumer(myChannelListener);
            }
        }
        
        setTableLayout();
        myLayoutListener.setTableLayout(myTable);
    }

    private void setTableLayout(){
        Icon[] i = SettingsRepository.getAnimationTableIcons();
        TableColumn colEnabled = myTable.getColumn("Enabled");
        TableHelper.setColumnCheckBoxIcons(colEnabled, null, null, $("enable.channel"));
        TableColumn colLocked = myTable.getColumn("Locked");
        TableHelper.setColumnCheckBoxIcons(colLocked, i[0], i[1], $("lock.channel"));
        TableColumn colVis = myTable.getColumn("Visible");
        TableHelper.setColumnCheckBoxIcons(colVis, i[2], i[3], $("show.channel"));

        TableColumn colColor = myTable.getColumn("Color");
        colColor.setCellRenderer(new ColorRenderer());
        colColor.setCellEditor(new ColorEditor());
    }
    
    /**
     *
     */
    public void hideHeader(){
        myTable.setTableHeader(null);
    }

    /**
     *
     * @param name
     */
    public void setTableHeader(String name) {
        JTableHeader th = myTable.getTableHeader();
        TableColumnModel tcm = new DefaultTableColumnModel();
        TableColumn tc = new TableColumn();
        tcm.addColumn(tc);
        th.setColumnModel(tcm);
        tc.setHeaderValue(name);
        tc.setWidth(myTable.getWidth());
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
                {null},
                {null}
            },
            new String [] {
                "nulldf"
            }
        ));
        myTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        myTable.setShowVerticalLines(false);
        myTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(myTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable myTable;
    // End of variables declaration//GEN-END:variables

}
