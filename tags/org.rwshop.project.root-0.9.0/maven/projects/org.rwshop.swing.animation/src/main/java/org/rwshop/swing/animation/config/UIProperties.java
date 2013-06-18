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
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.robokind.extern.utils.apache_commons_configuration.ConfigUtils;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public abstract class UIProperties {
    /**
     *
     */
    protected StrokeProperties myStroke;
    /**
     *
     */
    protected Color myColor;
    /**
     *
     */
    protected Float myOpacity;

    /**
     *
     * @param stroke
     * @param c
     * @param a
     */
    public UIProperties(StrokeProperties stroke, Color c, Float a){
        myStroke = stroke;
        myColor = c;
        myOpacity = a;
    }

    /**
     *
     * @return
     */
    public StrokeProperties getStroke(){
        return myStroke;
    }

    /**
     *
     * @return
     */
    public Color getColor(){
        return myColor;
    }

    /**
     *
     * @param col
     */
    public void setColor(Color col){
        myColor = col;
    }

    /**
     *
     * @return
     */
    public Float getOpacity(){
        return myOpacity;
    }

    /**
     *
     * @return
     */
    public abstract UIProperties copy();

    /**
     *
     * @param g
     * @param points
     * @param s
     */
    public abstract void paint(Graphics2D g, List<Point2D> points, CoordinateScalar s);

    
    /**
     *
     * @param props
     * @param g
     */
    public static void setStroke(UIProperties props, Graphics2D g) {
        if (props != null && props.getStroke() != null) {
            g.setStroke(props.getStroke().getStroke());
        }
    }

    /**
     *
     * @param props
     * @param g
     */
    public static void setColor(UIProperties props, Graphics2D g) {
        Color c = props.getColor();
        if (c == null) {
            c = g.getColor();
        }
        if (props.getOpacity() != null) {
            float a = ((float)c.getAlpha())/255.0f;
            a *= props.getOpacity();
            c = new Color(c.getColorSpace(), c.getComponents(null), a);
        }
        g.setColor(c);
    }
    /**
     *
     * @param <T>
     */
    public abstract static class PropertyHelper<T extends UIProperties>{
        /**
         *
         * @return
         */
        public abstract String getPrefix();
        /**
         *
         * @param config
         * @return
         */
        public abstract T read(Configuration config);
        /**
         *
         * @param properties
         * @return
         */
        public abstract T combine(List<T> properties);

        /**
         *
         * @param config
         * @return
         */
        public StrokeProperties readStrokeProperties(Configuration config){
            Float width = config.getFloat("width", null);
            float[] dash = ConfigUtils.readFloats(config, "dash");
            Float phase = config.getFloat("dash.phase", null);
            return new StrokeProperties(width, dash, phase);
        }
        
        public abstract T createEmptyProperties();
    }
}
