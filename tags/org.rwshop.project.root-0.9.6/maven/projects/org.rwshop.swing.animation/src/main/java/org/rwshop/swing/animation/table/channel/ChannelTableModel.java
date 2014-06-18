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

package org.rwshop.swing.animation.table.channel;

import javax.swing.table.AbstractTableModel;
import org.mechio.api.interpolation.InterpolatorFactory;
import org.mechio.api.animation.editor.EditState;
import org.mechio.api.animation.editor.ChannelEditor;
import org.mechio.api.animation.editor.MotionPathEditor;

import static org.jflux.api.common.rk.localization.Localizer.$_;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class ChannelTableModel  extends AbstractTableModel {
    private static String[] myColumnNames = new String[]{"Enabled", "Name", "Type", "Locked", "Visible"};
    private static Class[] myColumnClasses = new Class[]{Boolean.class, String.class, InterpolatorFactory.class, Boolean.class, Boolean.class};
    private ChannelEditor myController;

    /**
     *
     * @param c
     */
    public void setController(ChannelEditor c){
        myController =  c;
        fireTableDataChanged();
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
        MotionPathEditor mpc = myController.getChild(rowIndex);
        switch(columnIndex){
            case 0: mpc.setState(this, EditState.DISABLED, !(Boolean)aValue, myController.getSharedHistory()); break;
            case 2: mpc.setInterpolatorFactory(this, (InterpolatorFactory)aValue, myController.getSharedHistory()); break;
            case 3: mpc.setState(this, EditState.LOCKED, (Boolean)aValue, myController.getSharedHistory()); break;
            case 4: mpc.setState(this, EditState.VISIBLE, (Boolean)aValue, myController.getSharedHistory()); break;
        }
    }

    @Override
    public int getRowCount() {
        if(myController == null){
            return 0;
        }
        if(myController.getChildren() == null){
            return 0;
        }
        return myController.getChildren().size();
    }

    @Override
    public int getColumnCount() {
        return myColumnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(myController == null){
            return 0;
        }
        MotionPathEditor mpc = myController.getChild(rowIndex);
        if(mpc == null){
            return 0;
        }
        switch(columnIndex){
            case 0: return !EditState.hasFlag(mpc.getStateFlags(), EditState.DISABLED);
            case 1: return $_("motion.path.short")  + rowIndex;
            case 2: return mpc.getInterpolatorFactory();
            case 3: return EditState.hasFlag(mpc.getStateFlags(), EditState.LOCKED);
            case 4: return EditState.hasFlag(mpc.getStateFlags(), EditState.VISIBLE);
            default: return 0;
        }
    }

}
