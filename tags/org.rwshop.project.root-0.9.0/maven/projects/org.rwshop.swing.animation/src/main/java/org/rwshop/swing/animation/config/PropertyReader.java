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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.rwshop.swing.common.utils.UIConfigHelper;
import org.rwshop.swing.animation.config.UIProperties.PropertyHelper;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class PropertyReader {
    /**
     *
     * @return
     */
    public static PropertySet getAnimationProperties(){
        return getAnimationProperties(null, "logicalid", null);
    }
    
    /**
     *
     * @param defaultPathKey
     * @param channelPrefix
     * @param pointsKey
     * @return
     */
    public static PropertySet getAnimationProperties(String defaultPathKey, String channelPrefix, String pointsKey){
        PropertySet ps = new PropertySet(null, null);
        Configuration config = UIConfigHelper.getDisplayConfig();
        PropertySet controlPoints = null;
        if(pointsKey != null){
            getPointProperties(pointsKey);
            ps.putChildProperties("controlpoint", controlPoints);
        }
        int i = 0;
        while(!config.subset(channelPrefix + i).isEmpty()){
            String key = channelPrefix + i + ".channel";
            String cKey = "logicalid" + i + ".channel";
            PropertySet p = getPathProperties(key, defaultPathKey);
            if(pointsKey != null){
                p.putChildProperties("controlpoint", controlPoints);
            }
            ps.putChildProperties(cKey, p);
            i++;
        }
        PropertySet p = getPathProperties(defaultPathKey, defaultPathKey);
        p.setPrimaryColor(Color.red);
        if(pointsKey != null){
            p.putChildProperties("controlpoint", controlPoints);
        }
        ps.putChildProperties("default", p);
        return ps;
    }

    /**
     *
     * @param key
     * @param defaultKey
     * @return
     */
    public static PropertySet<PathProperties> getPathProperties(String key, String defaultKey){
        return buildPropertiesCache(UIConfigHelper.getDisplayConfig(), key, defaultKey, PathProperties.Helper);
    }
    
    /**
     *
     * @param key
     * @return
     */
    public static PropertySet<PointProperties> getPointProperties(String key){
        return buildPropertiesCache(UIConfigHelper.getDisplayConfig(), key, null, PointProperties.Helper);
    }

    /**
     *
     * @param <T>
     * @param config
     * @param key
     * @param def
     * @param helper
     * @return
     */
    public static <T extends UIProperties>PropertySet<T> buildPropertiesCache(
            Configuration config, String key, String def,
            PropertyHelper<T> helper){
        Configuration keyConfig = config.subset(key);
        UIStateProperties<T> p = readStateProperties(keyConfig, helper);
        PropertySet<T> props = new PropertySet<T>(p, helper);
        String statesKey = keyConfig.getString("states", null);
        Configuration statesConfig = null;
        if(statesKey == null){
            statesConfig = keyConfig.subset("states");
            if(statesConfig.isEmpty() && def != null){
                statesConfig = config.subset(def + ".states");
            }
        }else{
            statesConfig = config.subset(statesKey);
        }
        loadSettings(props, statesConfig, helper);
        return props;
    }

    private static <T extends UIProperties> void loadSettings(
            PropertySet<T> props, Configuration c, PropertyHelper<T> helper){
        List<String> keys = new ArrayList<String>();
        Iterator<String> it = c.getKeys();
        while(it.hasNext()){
            String k = it.next().split("[.]")[0];
            if(k != null && !k.isEmpty() && !keys.contains(k)){
                keys.add(k);
            }
        }
        loadSettings(props, c,keys, helper);
    }

    private static <T extends UIProperties> void loadSettings(
            PropertySet<T> props, Configuration config, List<String> keys,
            PropertyHelper<T> helper){
        for(String s : keys){
            UIStateProperties<T> sp = readStateProperties(config.subset(s), helper);
            props.mySettings.put(s, sp);
        }
    }

    /**
     *
     * @param <T>
     * @param config
     * @param helper
     * @return
     */
    public static <T extends UIProperties> UIStateProperties<T> readStateProperties(
            Configuration config, PropertyHelper<T> helper){
        T path = helper.read(config);
        List<T> before = readLayers(config.subset("before"), helper);
        List<T> after = readLayers(config.subset("after"), helper);
        return new UIStateProperties<T>(path, before, after);
    }

    private static <T extends UIProperties> List<T> readLayers(
            Configuration config, PropertyHelper<T> helper){
        List<T> properties = new ArrayList();
        String pre = helper.getPrefix();
        Configuration pathConfig = config.subset(pre);
        if(!pathConfig.isEmpty()){
            properties.add(helper.read(config.subset(pre)));
            return properties;
        }
        int i = 1;
        Configuration c = config.subset("layer" + i + "." + pre);
        while(!c.isEmpty()){
            properties.add(helper.read(config.subset("layer" + i + "." + pre)));
            i++;
            c = config.subset("layer" + i + "." + pre);
        }
        return properties;
    }
}
