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

package org.rwshop.nb.common;

import java.lang.reflect.InvocationTargetException;
import org.jflux.api.common.rk.config.VersionProperty;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class VersionPropertySheet {
    public static Sheet.Set getVersionPropertySheetSet(String propName, String displayName, Object instance) throws NoSuchMethodException{
        Sheet.Set set = Sheet.createPropertiesSet();
        set.put(getVersionPropertyParts(propName, displayName, instance));
        set.setName(displayName + " Version");
        set.setDisplayName(displayName + " Version");
        return set;
    }

    public static Property[] getVersionPropertyParts(String propName, String displayName, Object instance) throws NoSuchMethodException {
        Property versionProp = new PropertySupport.Reflection(instance, VersionProperty.class, propName);
        Property nameProp = getNameProp(versionProp, displayName);
        Property numProp = getNumProp(versionProp, displayName);
        return new Property[]{nameProp, numProp};
    }
    
    private static Property getNameProp(final Property versionProp, String displayName) throws NoSuchMethodException {
        String nameStr = VersionProperty.PROP_NAME;
        String dispStr = displayName + " Name";
        Property name = new PropertySupport.ReadWrite<String>(nameStr, String.class, dispStr, "") {
            private Property myVersionProp = versionProp;
            
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return getVersion(myVersionProp).getName();
            }

            @Override
            public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                VersionProperty prop = getVersion(myVersionProp);
                VersionProperty newProp = new VersionProperty(val, prop.getNumber());
                myVersionProp.setValue(newProp);
            }
        };
        return name;
    }
    
    private static Property getNumProp(final Property versionProp, String displayName) throws NoSuchMethodException {
        String nameStr = VersionProperty.PROP_NUMBER;
        String dispStr = displayName + " Version";
        Property name = new PropertySupport.ReadWrite<String>(nameStr, String.class, dispStr, "") {
            private Property myVersionProp = versionProp;
            
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return getVersion(myVersionProp).getNumber();
            }

            @Override
            public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                VersionProperty prop = getVersion(myVersionProp);
                VersionProperty newProp = new VersionProperty(prop.getName(), val);
                myVersionProp.setValue(newProp);
            }
        };
        return name;
    }
    
    private static VersionProperty getVersion(Property p) throws IllegalAccessException, InvocationTargetException{
        if(p == null){
            return new VersionProperty("", "");
        }
        Object val = p.getValue();
        if(val instanceof VersionProperty){
            return (VersionProperty)val;
        }
        return new VersionProperty("", "");
    }
}
