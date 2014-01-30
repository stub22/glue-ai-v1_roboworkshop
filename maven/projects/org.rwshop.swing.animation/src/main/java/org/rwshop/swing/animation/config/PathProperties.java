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

package org.rwshop.swing.animation.config;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.jflux.extern.utils.apache_commons_configuration.rk.ConfigUtils;
import org.rwshop.swing.common.utils.UIHelpers;
import org.rwshop.swing.common.scaling.CoordinateScalar;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class PathProperties extends UIProperties {
    /**
     *
     */
    public final static PathHelper Helper = new PathHelper();
    
    /**
     *
     * @param stroke
     * @param c
     * @param a
     */
    public PathProperties(StrokeProperties stroke, Color c, Float a){
        super(stroke, c, a);
    }

    /**
     *
     * @return
     */
    @Override
    public UIProperties copy(){
        StrokeProperties stroke = myStroke == null ? null : myStroke.copy();
        return new PathProperties(stroke, myColor, myOpacity);
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
        UIHelpers.drawLines(g, points, s);
    }
    
    /**
     *
     */
    public static class PathHelper extends PropertyHelper<PathProperties>{
        /**
         *
         * @return
         */
        @Override
        public String getPrefix(){
            return "path";
        }
        /**
         *
         * @param config
         * @return
         */
        @Override
        public PathProperties read(Configuration config){
            StrokeProperties props = readStrokeProperties(config.subset("stroke"));
            Color col = ConfigUtils.readColor(config, "color");
            Float opacity = config.getFloat("opacity", null);
            return new PathProperties(props, col, opacity);
        }
        /**
         * Combines the properties in the list with the elements at the end 
         * having precedence over those in front.
         * @param a
         * @return
         */
        @Override
        public PathProperties combine(List<PathProperties> a){
            if(a == null){
                return null;
            }
            List<StrokeProperties> strokes = new ArrayList<StrokeProperties>();
            Color c = null;
            Float alpha = 1.0f;
            for(UIProperties p : a){
                if(p == null){
                    continue;
                }
                StrokeProperties stroke = p.getStroke();
                if(p != null){
                    strokes.add(stroke);
                }
                if(p.getColor() != null){
                    c = p.getColor();
                }
                if(p.getOpacity() != null){
                    alpha *= p.getOpacity();
                }
            }
            return new PathProperties(StrokeProperties.combine(strokes), c, alpha);
        }

        @Override
        public PathProperties createEmptyProperties() {
            StrokeProperties props = new StrokeProperties(1.0f, null, null);
            return new PathProperties(props, Color.BLACK, 1.0f);
        }
    }

    /**
     *
     */
    public static class PathConfigRenderer extends UIConfigLayer<PathProperties> {
        /**
         *
         * @param config
         * @param key
         */
        public PathConfigRenderer(Configuration config, String key){
            super(config, key, "default.channel", PathProperties.Helper);
        }
        /**
         *
         * @param ps
         */
        public PathConfigRenderer(PropertySet ps){
            super(ps, PathProperties.Helper);
        }
    }
    
}
