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

import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.animation.config.PathProperties.PathConfigRenderer;
import org.rwshop.swing.animation.config.PointProperties.PointConfigRenderer;

/**
 * Contains settings for drawing UIElements.
 * Passing around a single instance of ElementSettings allows for easy global
 * changes.
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class ElementSettings {
    private CoordinateScalar myScalar;
    private PathConfigRenderer myPathRenderer;
    private PointConfigRenderer myPointRenderer;

    /**
     *
     */
    public ElementSettings(CoordinateScalar scalar, 
            PathConfigRenderer pathRenderer,
            PointConfigRenderer pointRenderer){
        myScalar = scalar;
        myPathRenderer = pathRenderer;
        myPointRenderer = pointRenderer;
    }

    /**
     *
     * @param s
     */
    public void setScaler(CoordinateScalar s){
        myScalar = s;
    }
    /**
     *
     * @return
     */
    public CoordinateScalar getScalar(){
        return myScalar;
    }
    
    public void setPathRenderer(PathConfigRenderer renderer){
        myPathRenderer = renderer;
    }
    
    public PathConfigRenderer getPathRenderer(){
        return myPathRenderer;
    }
    
    public void setPointRenderer(PointConfigRenderer renderer){
        myPointRenderer = renderer;
    }
    
    public PointConfigRenderer getPointRenderer(){
        return myPointRenderer;
    }
}
