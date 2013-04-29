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

import java.util.ArrayList;
import java.util.List;
import org.rwshop.swing.animation.config.UIProperties.PropertyHelper;

/**
 *
 * @param <T>
 * @author Matthew Stevenson <www.robokind.org>
 */
public class UIStateProperties<T extends UIProperties>{
    private T myDefaultProperties;
    private List<T> myBefore;
    private List<T> myAfter;

    /**
     *
     * @param p
     * @param b
     * @param a
     */
    public UIStateProperties(T p, List<T> b, List<T> a){
        myDefaultProperties = p;
        if(b == null){
            b = new ArrayList();
        }
        myBefore = b;
        if(a == null){
            a = new ArrayList();
        }
        myAfter = a;
    }

    /**
     *
     * @return
     */
    public T getDefaultProperties(){
        return myDefaultProperties;
    }
    
    public void setDefaultProperties(T properties){
        myDefaultProperties = properties;
    }
    
    /**
     *
     * @return
     */
    public List<T> getBefore(){
        return myBefore;
    }

    /**
     *
     * @return
     */
    public List<T> getAfter(){
        return myAfter;

    }

    /**
     *
     * @return
     */
    public UIStateProperties<T> copy(){
        return new UIStateProperties(myDefaultProperties == null ? null : myDefaultProperties.copy(),
                copyList(getBefore()), copyList(getAfter()));
    }

    /**
     *
     * @param <T>
     * @param a
     * @param combiner
     * @return
     */
    public static <T extends UIProperties> UIStateProperties<T> combine(List<UIStateProperties<T>> a, PropertyHelper<T> combiner){
        if(a == null || combiner == null){
            return null;
        }
        List<T> props = new ArrayList();
        List<List<T>> before = new ArrayList();
        List<List<T>> after = new ArrayList();
        for(UIStateProperties<T> p : a){
            if(p == null){
                    continue;
            }
            props.add(p.getDefaultProperties());
            before.add(p.getBefore());
            after.add(p.getAfter());
        }
        return new UIStateProperties(combiner.combine(props),
                copyLists(before), copyLists(after));
    }

    private static <T extends UIProperties>List<T> copyLists(List<List<T>> a){
        if(a == null){
            return null;
        }
        List<T> ret = new ArrayList();
        for(List<T> p : a){
            if(p != null){
                ret.addAll(p);
            }
        }
        return copyList(ret);
    }
    
    private static <T extends UIProperties>List<T> copyList(List<T> props){
        List<T> ret = new ArrayList(props.size());
        for(T p : props){
            ret.add((T)p.copy());
        }
        return ret;
    }
}
