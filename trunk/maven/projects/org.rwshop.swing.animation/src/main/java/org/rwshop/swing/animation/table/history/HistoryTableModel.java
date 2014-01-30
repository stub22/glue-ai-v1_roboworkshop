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

package org.rwshop.swing.animation.table.history;

import javax.swing.table.AbstractTableModel;
import org.mechio.api.animation.editor.history.HistoryStack;

import static org.jflux.api.common.rk.localization.Localizer.$;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class HistoryTableModel extends AbstractTableModel {
    private HistoryStack myHistory;

    /**
     *
     * @param hist
     */
    public void setHistory(HistoryStack hist){
        myHistory = hist;
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        if(myHistory == null){
            return 0;
        }
        return myHistory.getHistory().size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return myHistory.getEvent(rowIndex).getName();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return $("history");
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
