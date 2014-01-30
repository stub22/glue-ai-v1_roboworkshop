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

package org.rwshop.swing.animation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.mechio.api.animation.MotionPath;
import org.mechio.api.interpolation.InterpolatorFactory;
import org.rwshop.swing.common.utils.UIHelpers;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.scaling.DefaultCoordinateScalar;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class InterpolatorRenderer extends JPanel implements ListCellRenderer, TableCellRenderer{
        private CoordinateScalar myScalar;
        private List<Point2D> myPoints;
        private MotionPath myMotionPath;
        private Map<InterpolatorFactory,MotionPath> myPathMap;
        private int myLastWidth;

        /**
         *
         */
        public InterpolatorRenderer(){
            myPoints = new ArrayList();
            myPoints.add(new Point2D.Double(150, 0.15));
            myPoints.add(new Point2D.Double(383, 0.8));
            myPoints.add(new Point2D.Double(617, 0.15));
            myPoints.add(new Point2D.Double(850, 0.8));
            myPathMap = new HashMap();
            myScalar = new DefaultCoordinateScalar(0.13, 16, true);
            setSize(getWidth(), 16);
            setPreferredSize(new Dimension(getPreferredSize().width, 16));
            setOpaque(true);
        }

    @Override
        public Component getListCellRendererComponent(JList list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            int width = list.getWidth();
            int height = 30;
            if(index == -1){
                height = 16;
                width -= 12;
            }
            myScalar.setScaleY(height);
            setSize(width, height);
            setPreferredSize(new Dimension(width, height));
            return renderComponent(list, value, isSelected, width,
                    list.getSelectionBackground(), list.getSelectionForeground());
        }

    @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            int width = table.getColumnModel().getColumn(column).getWidth();
            int height = table.getRowHeight(row);
            myScalar.setScaleY(height-1);
            return renderComponent(table, value, isSelected, width,
                    table.getSelectionBackground(), table.getSelectionForeground());

        }

        private Component renderComponent(JComponent component, Object value, boolean isSelected, int width, Color selBack, Color selFore){
            if(myLastWidth != width){
                myLastWidth = width;
                double w = myLastWidth;
                double sX = w/1000.0;
                myScalar.setScaleX(sX);
            }
            if(isSelected){
                setBackground(selBack);
                setForeground(selFore);
            }else{
                setBackground(component.getBackground());
                setForeground(component.getForeground());
            }
            if(!(value instanceof InterpolatorFactory)){
                return this;
            }
            myMotionPath = getPath((InterpolatorFactory)value);
            return this;
        }

        private MotionPath getPath(InterpolatorFactory factory){
            if(!myPathMap.containsKey(factory)){
                MotionPath path = new MotionPath(factory);
                path.addPoints(myPoints);
                myPathMap.put(factory, path);
                return path;
            }
            return myPathMap.get(factory);
        }

        @Override public void paint(Graphics g){
            super.paint(g);
            if(true){//SettingsRepository.getAntiAliasing()){
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            }
            g.setColor(getForeground());
            UIHelpers.drawLinePath(g, myMotionPath.getInterpolatedPoints(), myScalar);
        }
    }
