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

package org.rwshop.swing.common.utils;

import org.apache.commons.configuration.Configuration;
import org.jflux.api.common.rk.localization.LanguageLocale;
import org.jflux.api.common.rk.localization.Localizer;
import org.jflux.extern.utils.apache_commons_configuration.rk.ConfigUtils;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class UIConfigHelper {
    private static String myImagePath = null;

    /**
     *
     * @return
     */
    public static Configuration getDisplayConfig(){
        return ConfigUtils.getLinkedConfig("layout");
    }

    /**
     *
     * @return
     */
    public static String getImagePath(){
        if(myImagePath != null){
            return myImagePath;
        }
        return ConfigUtils.getConfiguration().getString("images", null);
    }

    /**
     *
     */
    public static void setDefaultLocale(){
        Configuration config = getLanguageConfig();
        LanguageLocale locale = ConfigUtils.loadLanguage(config);
        Localizer.setLocale(locale);
    }

    private static Configuration getLanguageConfig(){
        return ConfigUtils.getLinkedConfig("language");
    }
}
