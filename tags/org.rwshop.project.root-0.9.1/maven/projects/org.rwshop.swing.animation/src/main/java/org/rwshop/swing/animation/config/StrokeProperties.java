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

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
 public class StrokeProperties{
    private Float myWidth;
    private Float myDashPhase;
    private float[] myDash;

    /**
     *
     * @param width
     * @param dash
     * @param phase
     */
    public StrokeProperties(Float width, float[] dash, Float phase){
        myWidth = width;
        myDash = dash;
        myDashPhase = phase;
    }

    /**
     *
     * @return
     */
    public Float getWidth(){
        return myWidth;
    }

    /**
     *
     * @return
     */
    public float[] getDash(){
        return myDash;
    }

    /**
     *
     * @return
     */
    public Float getDashPhase(){
        return myDashPhase;
    }

    /**
     *
     * @return
     */
    public Stroke getStroke(){
        float width = getWidth() == null ? 1 : getWidth();
        if(getDashPhase() == null || getDash() == null){
            return new BasicStroke(width);
        }
        return new BasicStroke(getWidth(), BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, getDash(), getDashPhase());
    }

    /**
     *
     * @return
     */
    public StrokeProperties copy(){
        return new StrokeProperties(myWidth,
                myDash == null ? null : Arrays.copyOf(myDash, myDash.length),
                myDashPhase);
    }

    /**
     *
     * @param a
     * @return
     */
    public static StrokeProperties combine(List<StrokeProperties> a){
        if(a == null || a.isEmpty()){
            return null;
        }
        StrokeProperties props = new StrokeProperties(1.0f,null,null);
        for(StrokeProperties p : a){
            if(p == null){
                continue;
            }
            if(p.getWidth() != null){
                props.myWidth = p.getWidth();
            }
            if(p.getDash() != null){
                props.myDash = p.getDash();
            }
            if(p.getDashPhase() != null){
                props.myDashPhase = p.getDashPhase();
            }
        }
        return props;
    }
}