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

package org.rwshop.swing.common.utils;

import org.rwshop.swing.common.scaling.CoordinateScalar;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class UIHelpers {


    /**
     *
     * @param g
     * @param cp
     * @param s
     */
    /*public static void drawCompiledPath(Graphics g, CompiledPath cp, CoordinateScalar s) {
        if (cp == null || cp.isEmpty()) {
            return;
        }
        Graphics2D g2 = (Graphics2D)g;
        Long time = cp.getStartTime();
        Long stepLength = cp.getStepLength();
        int len = cp.size();
        int[] x = new int[len];
        int[] y = new int[len];
        int i=0;
        for (Double d : cp) {
            if(d == -1.0){
                g2.drawPolyline(x, y, i);
                i=0;
            }else{
                x[i] = s.scaleX(time);
                y[i] = s.scaleY(d);
                i++;
            }
            time += stepLength;
        }
        g2.drawPolyline(x, y, i);
    }*/

    /**
     *
     * @param g
     * @param points
     * @param s
     */
    public static void drawLines(Graphics g, List<Point2D> points, CoordinateScalar s){
        if(points == null){
            return;
        }
        for(List<Point2D> path : splitList(points)){
            drawLinePath(g, path, s);
        }
    }
    
    /**
     * Splits a List of Points by those with a y-value of -1.
     * @param points List of points to split
     * @return A resulting List of sublists
     */
    public static List<List<Point2D>> splitList(List<Point2D> points){
        List<List<Point2D>> lists = new ArrayList();
        if(points == null){
            return lists;
        }
        int len = points.size();
        if(len < 2){
            lists.add(points);
            return lists;
        }
        List<Integer> breaks = new ArrayList<Integer>();
        if(points.get(0).getY() == -1.0){
            return splitList(points.subList(1, len));
        }
        if(points.get(len-1).getY() == -1.0){
            return splitList(points.subList(0, len-1));
        }
        for(int i=0; i< points.size(); i++){
            Point2D p = points.get(i);
            if(p.getY() == -1.0){
                breaks.add(i);
            }
        }
        if(breaks.isEmpty()){
            lists.add(points);
            return lists;
        }
        int prev = 0;
        for(int i : breaks){
            lists.add(points.subList(prev, i));
            prev = i+1;
        }
        lists.add(points.subList(prev, len));
        return lists;
    }

    /**
     *
     * @param g
     * @param points
     * @param s
     */
    public static void drawLinePath(Graphics g, List<Point2D> points, CoordinateScalar s){
        if(points == null){
            return;
        }
        int len = points.size();
        if(len < 2){
            return;
        }
        int[] x = new int[len];
        int[] y = new int[len];
        for(int i=0; i<len; i++){
            Point2D p = points.get(i);
            x[i] = (int)s.scaleX(p);
            y[i] = (int)s.scaleY(p);
            
        }
        Graphics2D g2 = (Graphics2D)g;
        g2.drawPolyline(x, y, len);
    }
}
