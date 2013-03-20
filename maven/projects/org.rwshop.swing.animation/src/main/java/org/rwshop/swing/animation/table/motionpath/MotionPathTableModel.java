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

package org.rwshop.swing.animation.table.motionpath;

import java.awt.geom.Point2D;
import javax.swing.table.AbstractTableModel;
import org.robokind.api.animation.editor.MotionPathEditor;
import static org.robokind.api.common.localization.Localizer.*;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class MotionPathTableModel extends AbstractTableModel {
    private static String[] myColumnNames = new String[]{$("time"), $("position")};
    private static Class[] myColumnClasses = new Class[]{Long.class, Double.class};
    private MotionPathEditor myController;

    /**
     *
     * @param c
     */
    public void setController(MotionPathEditor c){
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
        return !myController.isLocked();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(myController == null|| myController.isLocked()){
            return;
        }
        Point2D p = myController.getChild(rowIndex).getSelected();
        Point2D nP = new Point2D.Double();
        if(columnIndex == 0){
            nP.setLocation((Long)aValue, p.getY());
        }else if(columnIndex == 1){
            nP.setLocation(p.getX(), (Double)aValue);
        }
        myController.movePoint(aValue, rowIndex, (long)nP.getX(), nP.getY(), myController.getSharedHistory());
    }

    public int getRowCount() {
        return myController == null ? 0 : myController.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(myController == null){
            return null;
        }
        Point2D p = myController.getChild(rowIndex).getSelected();
        if(columnIndex == 0){
            return (long)p.getX();
        }else{
            return p.getY();
        }
    }

}
