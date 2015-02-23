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

package org.rwshop.swing.common;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class TableHelper {
    /**
     *
     * @param tc
     * @param icon
     * @param selIcon
     * @param tooltip
     */
    public static void setColumnCheckBoxIcons(TableColumn tc, Icon icon, Icon selIcon, String tooltip){
        TableCellRenderer tcr = new CheckBoxIconCellRenderer(icon, selIcon, tooltip);
        TableCellEditor tce = buildCheckBoxCellEditor(icon, selIcon, tooltip);
        tc.setCellRenderer(tcr);
        tc.setCellEditor(tce);
    }

    /**
     *
     * @param tc
     * @param width
     */
    public static void setColumnWidth(TableColumn tc, int width){
        tc.setPreferredWidth(width);
        tc.setWidth(width);
    }

    /**
     *
     * @param icon
     * @param selIcon
     * @param tooltip
     * @return
     */
    public static TableCellEditor buildCheckBoxCellEditor(Icon icon, Icon selIcon, String tooltip){
        JCheckBox cb = new JCheckBox();
        makeCellCheckBox(cb, icon, selIcon, tooltip);
        return new DefaultCellEditor(cb);
    }

    private static void makeCellCheckBox(JCheckBox cb, Icon icon, Icon selIcon, String tooltip){
        cb.setHorizontalAlignment(JCheckBox.CENTER);
        if(icon != null){
            cb.setIcon(icon);
        }
        if(selIcon != null){
            cb.setSelectedIcon(selIcon);
        }
        cb.setOpaque(true);
        cb.setToolTipText(tooltip);
    }

    /**
     *
     */
    public static class CheckBoxIconCellRenderer extends JCheckBox implements TableCellRenderer {
        /**
         *
         * @param icon
         * @param selIcon
         * @param tooltip
         */
        public CheckBoxIconCellRenderer(Icon icon, Icon selIcon, String tooltip) {
            makeCellCheckBox(this, icon, selIcon, tooltip);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
            if(isSelected){
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }else{
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            setSelected((value != null && ((Boolean) value).booleanValue()));
            return this;
        }
    }
}
