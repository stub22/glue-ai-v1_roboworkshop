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

package org.rwshop.swing.animation;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.List;
import org.robokind.api.animation.editor.EditState;
import org.rwshop.swing.animation.config.PropertySet;
import org.rwshop.swing.animation.config.PathProperties.PathConfigRenderer;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.common.DrawableLayer;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class PathRenderer implements DrawableLayer{
    private CoordinateScalar myScalar;
    private List<Point2D> myPoints;
    private PathConfigRenderer myRenderer;

    /**
     *
     * @param points
     * @param props
     * @param scalar
     */
    public PathRenderer(List<Point2D> points, PropertySet props, CoordinateScalar scalar){
        myScalar = scalar;
        myPoints = points;
        myRenderer = new PathConfigRenderer(props);
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        if(myPoints.size() < 2){
            return;
        }
        myRenderer.paint(g, myPoints, EditState.VISIBLE.getFlag(), myScalar, null);
    }
}
