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

package org.rwshop.swing.common.scaling;

import java.awt.geom.Point2D;
import org.jflux.api.common.rk.property.PropertyChangeSource;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public interface CoordinateScalar extends PropertyChangeSource{
    public final static String PROP_SCALE_X = "scaleX";
    public final static String PROP_OFFSET_X = "offsetX";
    public final static String PROP_SCALE_Y = "scaleY";
    public final static String PROP_OFFSET_Y = "offsetY";
    
    public final static double MAX_SCALE_AMOUNT = 5.0;
    public final static double MIN_SCALE_AMOUNT = 0.0;
    
    public double getScaleX();
    public void setScaleX(double s);

    public void setXOffset(double val);

    public double getScaleY();
    public void setScaleY(double s);

    public void setYOffset(double val);

    public double scaleX(double x);
    public double scaleX(Point2D p);

    public double scaleY(double y);
    public double scaleY(Point2D p);

    public double unscaleX(double x);
    public double unscaleX(Point2D p);

    public double unscaleY(double y);
    public double unscaleY(Point2D p);

    public static interface ScalarSource{
        public CoordinateScalar getScalar();
    }
}
