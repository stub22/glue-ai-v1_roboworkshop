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
 * ChannelTable.java
 *
 * Created on Feb 16, 2011, 2:31:52 AM
 */

package org.rwshop.swing.animation.table.channel;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.robokind.api.interpolation.InterpolatorDirectory;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.animation.editor.ChannelEditor;
import org.robokind.api.animation.editor.MotionPathEditor;
import org.rwshop.swing.animation.InterpolatorRenderer;
import org.rwshop.swing.common.TableHelper;
import org.rwshop.swing.common.utils.SettingsRepository;
import static org.robokind.api.common.localization.Localizer.*;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class ChannelTable extends javax.swing.JPanel {
    private ChannelTableModel myTableModel;
    private ChannelTableMotionPathListener myPathListener;
    private AnimationEditor myAnimationEditor;
    private ChannelEditor myChannelEditor;
    private ChannelTableChannelListener myChannelListener;
    private ChannelTableListener myTableListener;
    private ChannelTableLayoutListener myLayoutListener;
    private ChannelTableContextMenu myContextMenu;
    private ChannelTableAnimationListener myAnimationListener;
    private boolean myIsInitialized;

    /** Creates new form ChannelTable */
    public ChannelTable() {
        initComponents();
        myTableModel = new ChannelTableModel();
        myPathListener = new ChannelTableMotionPathListener();
        myChannelListener = new ChannelTableChannelListener();
        myLayoutListener = new ChannelTableLayoutListener();
        myTableListener = new ChannelTableListener(myTable);
        myContextMenu = new ChannelTableContextMenu(myTable);
        myAnimationListener = new ChannelTableAnimationListener();
        myAnimationListener.init(this);
        
        myPathListener.init(myTableModel);
        myTable.setModel(myTableModel);
        myTable.getSelectionModel().addListSelectionListener(myTableListener);
        myTable.addComponentListener(myLayoutListener);
        myChannelListener.init(myPathListener, myTableModel);
        myChannelListener.setTable(myTable);
        myIsInitialized = false;
        jScrollPane1.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    /**
     *
     * @param ac
     */
    public void setAnimationController(AnimationEditor ac){
        if(myAnimationEditor == ac){
            return;
        }
        myAnimationEditor = ac;
        myAnimationListener.cleanConsumer();
        if(ac == null || ac.getChildren().isEmpty()){
            setController(null);
        }else{
            setController(ac.getSelected());
        }
        if(ac != null){
            ac.addConsumer(myAnimationListener);
        }
    }

    /**
     *
     * @param controller
     */
    public void setController(ChannelEditor controller){
        if(myChannelEditor == controller){
            return;
        }
        myPathListener.cleanConsumer();
        myChannelListener.cleanConsumer();
        
        myChannelEditor = controller;
        myPathListener.setController(myChannelEditor);
        myTableListener.setController(myChannelEditor);
        myTableModel.setController(myChannelEditor);
        myContextMenu.setController(myChannelEditor);
        if(!myIsInitialized){
            setTableLayout();
            myIsInitialized = true;
        }
        if(myChannelEditor == null){
            return;
        }
        for(MotionPathEditor mpc : myChannelEditor.getChildren()){
            mpc.addConsumer(myPathListener);
        }
        myChannelEditor.addConsumer(myChannelListener);
    }

    private void setTableLayout(){
        Icon[] i = SettingsRepository.getAnimationTableIcons();
        TableColumn colEnabled = myTable.getColumn("Enabled");
        TableHelper.setColumnCheckBoxIcons(colEnabled, null, null, $("enable.motion.path"));
        TableColumn colLocked = myTable.getColumn("Locked");
        TableHelper.setColumnCheckBoxIcons(colLocked, i[0], i[1], $("lock.motion.path"));
        TableColumn colVis = myTable.getColumn("Visible");
        TableHelper.setColumnCheckBoxIcons(colVis, i[2], i[3], $("show.motion.path"));

        TableColumn colType = myTable.getColumn("Type");
        TableCellRenderer typeRenderer = new InterpolatorRenderer();
        colType.setCellRenderer(typeRenderer);
        JComboBox cb = new JComboBox(InterpolatorDirectory.instance().buildFactoryList().toArray());
        cb.setRenderer(new InterpolatorRenderer());
        DefaultCellEditor dce = new DefaultCellEditor(cb);
        colType.setCellEditor(dce);
        //((JComponent)myTable.getDefaultRenderer(String.class)).setToolTipText("Channel Name");
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
        myTable = new javax.swing.JTable();/*new javax.swing.JTable(){
            public boolean getScrollableTracksViewportHeight() {
                return getPreferredSize().height < getParent().getHeight();
            }
        }*/;

        myTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        myTable.setShowVerticalLines(false);
        myTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(myTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable myTable;
    // End of variables declaration//GEN-END:variables

}
