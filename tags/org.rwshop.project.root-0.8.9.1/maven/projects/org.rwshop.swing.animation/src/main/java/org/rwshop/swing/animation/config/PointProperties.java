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

package org.rwshop.swing.animation.config;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.rwshop.swing.common.utils.UIHelpers;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.robokind.extern.utils.apache_commons_configuration.ConfigUtils;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class PointProperties extends UIProperties {
    /**
     *
     */
    public final static PointHelper Helper = new PointHelper();
    private Integer myWidth;
    private Integer myHeight;
    private String myStyle;

    /**
     *
     * @param width
     * @param height
     * @param stroke
     * @param c
     * @param a
     * @param style
     */
    public PointProperties(Integer width, Integer height, StrokeProperties stroke, Color c, Float a, String style){
        super(stroke, c, a);
        myWidth = width;
        myHeight = height;
        myStyle = style;
    }

    /**
     *
     * @return
     */
    public Integer getHeight(){
        return myHeight;
    }

    /**
     *
     * @return
     */
    public Integer getWidth(){
        return myWidth;
    }

    /**
     *
     * @return
     */
    public String getStyle(){
        return myStyle;
    }

    /**
     *
     * @return
     */
    @Override
    public UIProperties copy(){
        return new PointProperties(getWidth(), getHeight(), myStroke.copy(), myColor, myOpacity, myStyle);
    }

    /**
     *
     * @param g
     * @param points
     * @param s
     */
    @Override
    public void paint(Graphics2D g, List<Point2D> points, CoordinateScalar s){
        UIProperties.setStroke(this, g);
        UIProperties.setColor(this, g);
        int w = myWidth == null ? 4 : myWidth;
        int h = myHeight == null ? 4 : myHeight;
        if(myStyle != null && myStyle.equals("FILL")){
            for(Point2D p : points){
                int x = (int)s.scaleX(p) - w/2;
                int y = (int)s.scaleY(p) - h/2;
                g.fillOval(x, y, w, h);
            }
        }else{
            for(Point2D p : points){
                int x = (int)s.scaleX(p) - w/2;
                int y = (int)s.scaleY(p) - h/2;
                g.drawOval(x, y, w, h);
            }
        }
        UIHelpers.drawLines(g, points, s);
    }

    /**
     *
     */
    public static class PointHelper extends PropertyHelper<PointProperties>{
        /**
         *
         * @return
         */
        @Override
        public String getPrefix(){
            return "point";
        }

        /**
         *
         * @param config
         * @return
         */
        @Override
        public PointProperties read(Configuration config){
            StrokeProperties props = readStrokeProperties(config.subset("stroke"));
            Color col = ConfigUtils.readColor(config, "color");
            Float opacity = config.getFloat("opacity", null);
            Integer width = config.getInteger("width", null);
            Integer height = config.getInteger("height", null);
            String style = config.getString("style", null);
            return new PointProperties(width, height, props, col, opacity, style);
        }
    
        /**
         *
         * @param a
         * @return
         */
        @Override
        public PointProperties combine(List<PointProperties> a){
            if(a == null){
                return null;
            }
            List<StrokeProperties> strokes = new ArrayList<StrokeProperties>();
            Color c = null;
            Float alpha = 1.0f;
            Integer w = null;
            Integer h = null;
            String style = null;
            for(PointProperties p : a){
                if(p == null){
                    continue;
                }
                strokes.add(p.getStroke());
                if(p.getColor() != null){
                    c = p.getColor();
                }
                if(p.getOpacity() != null){
                    alpha *= p.getOpacity();
                }
                if(p.getWidth() != null){
                    w = p.getWidth();
                }
                if(p.getHeight() != null){
                    h = p.getHeight();
                }
                if(p.getStyle() != null){
                    style = p.getStyle();
                }
            }
            return new PointProperties(w, h, StrokeProperties.combine(strokes), c, alpha, style);
        }

        @Override
        public PointProperties createEmptyProperties() {
            StrokeProperties props = new StrokeProperties(1.0f, null, null);
            return new PointProperties(10, 10, props, Color.BLACK, 1.0f, null);
        }
    }

    /**
     *
     */
    public static class PointConfigRenderer extends UIConfigLayer<PointProperties>{
        /**
         *
         * @param config
         * @param key
         */
        public PointConfigRenderer(Configuration config, String key){
            super(config, key, null, PointProperties.Helper);
        }
        /**
         *
         * @param ps
         */
        public PointConfigRenderer(PropertySet ps){
            super(ps, PointProperties.Helper);
        }
    }
}
