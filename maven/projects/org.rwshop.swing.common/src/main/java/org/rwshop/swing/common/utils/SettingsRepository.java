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

import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.UIManager;
import org.jflux.extern.utils.apache_commons_configuration.rk.ConfigUtils;
import org.osgi.framework.BundleContext;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class SettingsRepository {
    private final static Logger theLogger = Logger.getLogger(SettingsRepository.class.getName());
    /**
     *
     */
    public static double theHoverDistance = 8.0;
    /**
     *
     */
    public static final int theAnimationWidthOffset = 20;
    private static BundleContext theContext;
    private static String theLookAndFeelDir = "win7/";
    private static boolean myInitializedFlag = false;
    
    public static boolean isInitialized(){
        return myInitializedFlag;
    }

    /**
     *
     * @return
     */
    public static Icon[] getAnimationTableIcons(){
        Icon[] icons = new Icon[4];
        String path = UIConfigHelper.getImagePath();
        icons[0] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "not_locked.png");
        icons[1] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "locked.png");
        icons[2] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "not_visible.png");
        icons[3] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "visible.png");
        return icons;
    }

    /**
     *
     * @return
     */
    public static Icon[] getFileIcons(){
        Icon[] icons = new Icon[3];
        String path = UIConfigHelper.getImagePath();
        icons[0] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "new24.png");
        icons[1] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "open24.png");
        icons[2] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "save24.png");
        return icons;
    }
    
    /**
     *
     * @return
     */
    public static Icon[] getHistoryIcons(){
        Icon[] icons = new Icon[2];
        String path = UIConfigHelper.getImagePath();
        icons[0] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "undo24.gif");
        icons[1] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "redo24.gif");
        return icons;
    }

    /**
     *
     * @return
     */
    public static Icon[] getPlayerIcons(){
        Icon[] icons = new Icon[3];
        String path = UIConfigHelper.getImagePath();
        icons[0] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "Play24.gif");
        icons[1] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "Pause24.gif");
        icons[2] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "Stop24.gif");
        return icons;
    }

    /**
     *
     * @return
     */
    public static Icon[] getDefaultHeaderIcons(){
        Icon[] icons = new Icon[4];
        String path = UIConfigHelper.getImagePath();
        icons[0] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "DefaultHeader12.png");
        path += theLookAndFeelDir;
        icons[1] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "close_enabled.png");
        icons[2] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "close_rollover.png");
        icons[3] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "close_pressed.png");
        return icons;
    }

    /**
     *
     * @return
     */
    public static Icon[] getPlayIcons24(){
        Icon[] icons = new Icon[4];
        String path = UIConfigHelper.getImagePath();
        icons[0] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "Play24.gif");
        icons[1] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "Pause24.gif");
        icons[2] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "Continue24.gif");
        icons[3] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "Stop24.gif");
        return icons;
    }

    /**
     *
     * @return
     */
    public static Icon[] getPlayStatusIcons(){
        Icon[] icons = new Icon[4];
        String path = UIConfigHelper.getImagePath();
        icons[0] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "PlayStatus12.png");
        icons[1] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "PauseStatus12.png");
        icons[2] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "StopStatus12.png");
        icons[3] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "CompleteStatus12.png");
        return icons;
    }

    /**
     *
     * @return
     */
    public static Icon[] getAllJointIcons(){
        Icon[] icons = new Icon[6];
        String path = UIConfigHelper.getImagePath();
        icons[0] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "PlayStatus12.png");
        icons[1] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "PauseStatus12.png");
        icons[2] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "StopStatus12.png");
        icons[3] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "Play24.gif");
        icons[4] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "Pause24.gif");
        icons[5] = ConfigUtils.getFileSystemAdapter().openImageIcon(path + "Reset16.gif");
        return icons;
    }

    /**
     *
     * @return
     */
    public static Icon getDefaultHeaderIcon(){
        String path = UIConfigHelper.getImagePath();
        return ConfigUtils.getFileSystemAdapter().openImageIcon(path + "DefaultHeader12.png");
    }

    /**
     *
     * @return
     */
    public static Icon getCompletedHeaderIcon(){
        String path = UIConfigHelper.getImagePath();
        return ConfigUtils.getFileSystemAdapter().openImageIcon(path + "CompleteStatus12.png");
    }

    /**
     *
     * @return
     */
    public static boolean getAntiAliasing(){
        return false;
    }

    /**
     *
     * @return
     */
    public static boolean getAntiAliasingRenderer(){
        return false;
    }
    

    /**
     *
     */
    public static void initSettings(){
        if(myInitializedFlag){
            return;
        }
        UIConfigHelper.setDefaultLocale();
        //AnimationPlayer.setServos(servos);
        //AnimationPlayer.setStepLength(20);
        myInitializedFlag = true;
    }

    /**
     *
     * @param context
     */
    public static void setContext(BundleContext context){
        theContext = context;
    }

    /**
     *
     * @param logicalId
     * @param pos
     */
    public static void move(int logicalId, double pos){
        
    }

    /**
     *
     */
    public static void setUIManager(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Throwable t){
            t.printStackTrace();
        }
    }
}
