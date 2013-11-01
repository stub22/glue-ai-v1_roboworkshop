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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.rwshop.swing.animation.config.UIProperties.PropertyHelper;
import org.robokind.api.common.utils.Utils;

/**
 *
 * @param <T>
 * @author Matthew Stevenson <www.robokind.org>
 */
public class PropertySet<T extends UIProperties> {
    private UIStateProperties<T> myDefault;
    private PropertyHelper<T> myHelper;
    /**
     *
     */
    protected Map<String,UIStateProperties<T>> mySettings;
    private Map<String,UIStateProperties<T>> myCache;
    private Map<String,PropertySet> mySets;

    /**
     *
     * @param props
     * @param pc
     */
    public PropertySet(UIStateProperties<T> props, PropertyHelper<T> pc){
        if(props == null && pc != null){
            T prop = pc.createEmptyProperties();
            props = new UIStateProperties<T>(prop, null, null);
        }
        myDefault = props;
        mySettings = new HashMap();
        myCache = new HashMap();
        myHelper = pc;
        mySets = new HashMap();
    }

    /**
     *
     * @return
     */
    public UIStateProperties<T> getDefaultProperties(){
        return myDefault;
    }

    /**
     *
     * @return
     */
    public Color getPrimaryColor(){
        if(myDefault == null){
            return null;
        }
        T props = myDefault.getDefaultProperties();
        if(props == null){
            return null;
        }
        return props.getColor();
    }

    /**
     *
     * @param col
     */
    public void setPrimaryColor(Color col){
        if(myDefault == null){
            return;
        }
        T props = myDefault.getDefaultProperties();
        if(props == null){
            return;
        }
        props.setColor(col);
        myCache.clear();
    }

    /**
     *
     * @param setting
     * @return
     */
    public boolean hasSetting(String setting){
        return mySettings.containsKey(setting);
    }

    /**
     *
     * @param settings
     * @return
     */
    public UIStateProperties<T> getProperties(List<String> settings){
        String key = Arrays.toString(settings.toArray());
        if(!myCache.containsKey(key)){
            myCache.put(key, buildProperties(settings));
        }
        return myCache.get(key);
    }

    /**
     *
     * @param settings
     * @return
     */
    public UIStateProperties<T> getProperties(String... settings){
        String key = Arrays.toString(settings);
        if(!myCache.containsKey(key)){
            myCache.put(key, buildProperties(Arrays.asList(settings)));
        }
        return myCache.get(key);
    }

    /**
     *
     * @param key
     * @param ps
     */
    public void putChildProperties(String key, PropertySet ps){
        mySets.put(key, ps);
    }

    /**
     *
     * @param key
     * @return
     */
    public PropertySet getChildProperties(String key){
        return mySets.get(key);
    }

    /**
     *
     */
    public void clearCache(){
        myCache.clear();
    }

    private UIStateProperties<T> buildProperties(List<String> settings){
        List<UIStateProperties<T>> props = new ArrayList();
        props.add(myDefault.copy());
        for(String s : settings){
            props.add(mySettings.get(s));
        }
        return UIStateProperties.combine(props, myHelper);
    }

    /**
     *
     * @return
     */
    public PropertySet copy(){
        PropertySet ps = new PropertySet(myDefault.copy(), myHelper);
        for(Entry<String, UIStateProperties<T>> e : mySettings.entrySet()){
            ps.mySettings.put(e.getKey(), e.getValue().copy());
        }
        boolean addSelf = false;
        String selfKey = null;
        for(Entry<String, PropertySet> e : mySets.entrySet()){
            if(e.getValue() == this){
                addSelf = true;
                selfKey = e.getKey();
            }else{
                ps.mySets.put(e.getKey(), e.getValue().copy());
            }
        }
        if(addSelf){
            ps.mySets.put(selfKey, ps);
        }
        return ps;
    }
    /**
     * Merges two property sets without creating copies of the underlying objects.
     * Changes in the original PropertySets will be reflected in the new sets
     * and changes in the new PropertySet will be seen in the original.
     *
     * The resulting PropertySet contains all the settings from a, and the settings
     * from b which do not belong to a.
     * @param a
     * @param b
     * @return
     */
    public static PropertySet merge(PropertySet a, PropertySet b){
        if(b == null){
            return a;
        }
        if(a == null){
            return b;
        }
        UIStateProperties sp = a.myDefault;
        PropertyHelper ph = a.myHelper;
        if(sp == null){
            sp = b.myDefault;
            ph = b.myHelper;
        }
        PropertySet ps = new PropertySet(sp, ph);
        Utils.mergeMaps(ps.mySettings, a.mySettings, b.mySettings);
        Utils.mergeMaps(ps.mySets, a.mySets);
        for(Entry<String, PropertySet> e : ((Map<String,PropertySet>)b.mySets).entrySet()){
            PropertySet pb = e.getValue();
            PropertySet pa = (PropertySet)ps.mySets.get(e.getKey());
            String k = e.getKey();
            if(ps.mySets.containsKey(k)){
                if(pa == a && pb == b){
                    ps.mySets.put(k, ps);
                }else if(pa == a){
                    ps.mySets.put(k, merge(ps,pb));
                }else if(pb == b){
                    ps.mySets.put(k, merge(pa,ps));
                }else{
                    ps.mySets.put(k, merge(pa,pb));
                }
            }else{
                ps.mySets.put(k,pb);
            }
        }
        for(Entry<String, PropertySet> e : ((Map<String,PropertySet>)ps.mySets).entrySet()){
            PropertySet p = e.getValue();
            if(p == a || p == b){
                ps.mySets.put(e.getKey(), ps);
            }
        }
        return ps;
    }
}
