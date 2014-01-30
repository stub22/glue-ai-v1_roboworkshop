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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.mechio.api.animation.editor.EditState;
import org.rwshop.swing.animation.config.UIProperties.PropertyHelper;
import org.rwshop.swing.common.scaling.CoordinateScalar;

/**
 *
 * @param <T>
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public abstract class UIConfigLayer<T extends UIProperties> {
    private static final EditState[] theDefaultStateOrder = {
        EditState.HOVER, EditState.SELECTED, EditState.DISABLED,  EditState.LOCKED
    };

    /**
     *
     */
    protected PropertySet<T> myProperties;
    /**
     *
     */
    protected EditState[] myStateOrder;
    
    protected PropertyHelper<T> myPropertyHelper;

    /**
     *
     * @param config
     * @param key
     * @param def
     * @param helper
     */
    public UIConfigLayer(Configuration config, String key, String def, PropertyHelper<T> helper){
        if(helper == null){
            throw new NullPointerException("PropertyHelper cannot be null.");
        }
        myPropertyHelper = helper;
        myProperties = PropertyReader.buildPropertiesCache(config, key, def, myPropertyHelper);
    }

    /**
     *
     * @param props
     */
    public UIConfigLayer(PropertySet<T> props, PropertyHelper<T> helper){
        if(helper == null){
            throw new NullPointerException("PropertyHelper cannot be null.");
        }
        myProperties = props;
        myPropertyHelper = helper;
    }

    /**
     *
     * @return
     */
    public PropertySet<T> getProperties(){
        return myProperties;
    }

    /**
     *
     * @param states
     */
    public void setStateOrder(EditState...states){
        myStateOrder = states;
    }

    /**
     *
     * @param g
     * @param points
     * @param flags
     * @param scalar
     */
    public void paint(Graphics g, List<Point2D> points, int flags, 
            CoordinateScalar scalar, T optionalDrawProperties){
        UIStateProperties<T> props = getProperties(flags);
        paint(g, points, props, scalar, optionalDrawProperties);
    }

    /**
     *
     * @param g
     * @param points
     * @param scalar
     * @param settings
     */
    public void paint(Graphics g, List<Point2D> points, CoordinateScalar scalar, 
            String settings, T optionalDrawProperties){
        if(myProperties == null || !myProperties.hasSetting(settings)){
            return;
        }
        UIStateProperties<T> props = myProperties.getProperties(settings);
        if(props == null){
            props = myProperties.getDefaultProperties();
            if(props == null){
                return;
            }
        }
        paint(g, points, props, scalar, optionalDrawProperties);
    }

    private void paint(Graphics g, List<Point2D> points, 
            UIStateProperties<T> props, CoordinateScalar scalar, 
            T optional) {
        if(props == null || points == null || g == null || scalar == null){
            return;
        }
        T def = props.getDefaultProperties();
        T prop;
        if(def == null){
            prop = optional;
        }else if(optional == null){
            prop = def;
        }else{
            prop = myPropertyHelper.combine(Arrays.asList(def,optional));
        }
        Graphics2D g2 = (Graphics2D)g;
        Stroke stroke = g2.getStroke();
        paintPoints(g2, points, props.getBefore(), scalar);
        if(prop != null){
            prop.paint(g2, points, scalar);
        }
        paintPoints(g2, points, props.getAfter(), scalar);
        g2.setStroke(stroke);
    }

    /**
     *
     * @param g
     * @param points
     * @param props
     * @param scalar
     */
    public void paintPoints(Graphics2D g, List<Point2D> points, List<T> props, 
            CoordinateScalar scalar){
        if(props == null){
            return;
        }
        for(T p : props){
            p.paint(g, points, scalar);
        }
    }

    /**
     *
     * @param flags
     * @return
     */
    protected UIStateProperties<T> getProperties(int flags){
        List<String> settings = new ArrayList();
        EditState[] order = myStateOrder == null ? theDefaultStateOrder : myStateOrder;
        for(EditState s : order){
            addStateString(flags, settings, s);
        }
        if(myProperties == null){
            return null;
        }
        return myProperties.getProperties(settings);
    }

    /**
     *
     * @param flags
     * @param settings
     * @param state
     */
    protected void addStateString(int flags, List<String> settings, EditState state) {
        if (EditState.hasFlag(flags, state)) {
            settings.add(state.toString().toLowerCase());
        }
    }
}
