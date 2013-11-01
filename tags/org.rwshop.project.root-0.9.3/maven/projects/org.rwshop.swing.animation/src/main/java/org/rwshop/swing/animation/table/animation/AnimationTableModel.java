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

package org.rwshop.swing.animation.table.animation;

import java.awt.Color;
import javax.swing.table.AbstractTableModel;
import org.robokind.api.animation.editor.EditState;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.animation.editor.ChannelEditor;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class AnimationTableModel  extends AbstractTableModel {
    private static String[] myColumnNames = new String[]{"Enabled", "Name", "Color", "Locked", "Visible"};
    private static Class[] myColumnClasses = new Class[]{Boolean.class, String.class, Color.class, Boolean.class, Boolean.class};
    private AnimationEditor myController;

    /**
     *
     * @param c
     */
    public void setController(AnimationEditor c){
        myController =  c;
        this.fireTableStructureChanged();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return myColumnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return myColumnClasses[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 1;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(myController == null){
            return;
        }
        ChannelEditor cc = myController.getChild(rowIndex);
        switch(columnIndex){
            case 0: cc.setState(this, EditState.DISABLED, !(Boolean)aValue, myController.getSharedHistory()); break;
            case 2: cc.setPrimaryColor(this, (Color)aValue); break;
            case 3: cc.setState(this, EditState.LOCKED, (Boolean)aValue, myController.getSharedHistory()); break;
            case 4: cc.setState(this, EditState.VISIBLE, (Boolean)aValue, myController.getSharedHistory()); break;
        }
    }

    public int getRowCount() {
        if(myController == null){
            return 0;
        }
        if(myController.getChildren() == null){
            return 0;
        }
        return myController.getChildren().size();
    }

    public int getColumnCount() {
        return myColumnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(myController == null){
            return 0;
        }
        ChannelEditor cc = myController.getChild(rowIndex);
        if(cc == null){
            return 0;
        }
        switch(columnIndex){
            case 0: return !EditState.hasFlag(cc.getStateFlags(), EditState.DISABLED);
            case 1: return cc.getName();
            case 2: return cc.getPrimaryColor();
            case 3: return EditState.hasFlag(cc.getStateFlags(), EditState.LOCKED);
            case 4: return EditState.hasFlag(cc.getStateFlags(), EditState.VISIBLE);
            default: return 0;
        }
    }

}
