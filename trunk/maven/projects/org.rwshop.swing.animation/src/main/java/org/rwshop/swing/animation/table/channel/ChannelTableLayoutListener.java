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

package org.rwshop.swing.animation.table.channel;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.rwshop.swing.common.TableHelper;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class ChannelTableLayoutListener implements ComponentListener{
    @Override
    public void componentResized(ComponentEvent e) {
        setTableLayout((JTable)e.getComponent());
    }

    @Override
    public void componentShown(ComponentEvent e) {
        setTableLayout((JTable)e.getComponent());
    }

    @Override public void componentHidden(ComponentEvent e) {}
    @Override public void componentMoved(ComponentEvent e) {}
    
    private void setTableLayout(JTable table){
        JTableHeader th = table.getTableHeader();
        if(th != null){
            TableColumn tc = th.getColumnModel().getColumn(0);
            tc.setWidth(table.getWidth());
        }
        TableColumn colEnabled = table.getColumn("Enabled");
        TableHelper.setColumnWidth(colEnabled, 26);
        
        TableColumn colLocked = table.getColumn("Locked");
        TableHelper.setColumnWidth(colLocked, 26);
        
        TableColumn colVis = table.getColumn("Visible");
        TableHelper.setColumnWidth(colVis, 26);

        int w = table.getWidth();
        w -= 3*26;
        w /= 2;
        TableColumn colColor = table.getColumn("Type");
        TableHelper.setColumnWidth(colColor, w);

        TableColumn colName = table.getColumn("Name");
        TableHelper.setColumnWidth(colName, w);
    }

}
