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

package org.rwshop.integration.netbeans;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jflux.api.common.rk.config.FileSystemAdapter;
import org.jflux.api.common.rk.utils.RKConstants;
import org.jflux.extern.utils.apache_commons_configuration.rk.ConfigUtils;
import org.jflux.impl.services.rk.lifecycle.utils.ManagedServiceFactory;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponentFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.mechio.api.motion.Robot;
import org.mechio.integration.motion_speech.VisemeMotionUtils;
import org.rwshop.swing.common.utils.SettingsRepository;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class Activator implements BundleActivator {
    private final static Logger theLogger = Logger.getLogger(Activator.class.getName());
//    private BundleChildren myBundleChildren;
//    private ServiceChildren myServiceChildren;

    @Override
    public void start(BundleContext context) throws Exception {
        Logger.getLogger("org.netbeans.core.netigso.Netigso").setLevel(Level.WARNING);
        theLogger.log(Level.INFO, "Activating NetBeans Adapter Bundle");
        setFileSystem(context);
        initSettings();
        startVisemes(context);
        theLogger.log(Level.INFO, "NetBeans Adapter Bundle Activation Complete");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }

    private void setFileSystem(BundleContext context){
        String cluster = "robokind";
        FileSystemAdapter fs = 
                new NetBeansRelativeFileAdapter(
                        getRoot() + "/" + cluster + "/");
        ConfigUtils.setFileSystemAdapter(fs);
    }
    
    private String getRoot(){
        String brandingToken = "robokindcontroller";
        File f = new File(".");
        String path = f.getAbsolutePath();
        theLogger.info(path);
        int len = path.length();
        if(len >= 5){
            String dir = path.substring(len-5, len-2).toLowerCase();
            if(dir.equals("bin")){
                return "..";
            }
        }
        int blen = brandingToken.length() + 2;
        if(len >= blen){
            String dir = path.substring(len-blen, len-2).toLowerCase();
            if(dir.equals(brandingToken)){
                return "." ;
            }
        }
        return "./target/" + brandingToken;
    }

    private void startVisemes(BundleContext context){
        ManagedServiceFactory fact = new OSGiComponentFactory(context);
        String speechServiceId = RKConstants.DEFAULT_SPEECH_ID;
        String path = getRoot() + "/robokind/resources/VisemeConf.json";
		// Does "myRobot" indicate "use default"?	If so, can we pull that as a constant from somewhere?
        // String robotName = "myRobot";
        // 2012-03-30 :  Stu temporarily set viseme-target robot name to match the Avatar.
        // 2012-11-19 :  Matt is removing visemes for avatar.  This is to be started in cogchar.
//        VisemeMotionUtils.startVisemeFrameSourceGroup(
//                fact, new Robot.Id(RKConstants.VIRTUAL_R50_ID), 
//                speechServiceId, path);
        //"myRobot" is hardcoded for the physical robot
        //2012-04-09 : Matt is registering visemes for both
        VisemeMotionUtils.startVisemeFrameSourceGroup(
                fact, new Robot.Id(RKConstants.PHYSICAL_R50_ID), 
                speechServiceId, path);
    }
    
    private void initSettings(){
        SettingsRepository.initSettings();
    }

//    private void registerBundleChildren(BundleContext context){
//        myBundleChildren = new BundleChildren(context);
//        myBundleChildren.addNotify();
//        Properties props = new Properties();
//        props.put("OSGi Bundle", "true");
//        props.put("NetBeans Module", "true");
//        context.registerService(BundleChildren.class.getName(), myBundleChildren, props);
//        theLogger.log(Level.INFO, "BundleChildren successfuly registers.");
//    }
//
//    private void registerServiceChildren(BundleContext context){
//        myServiceChildren = new ServiceChildren(context);
//        myServiceChildren.addNotify();
//        Properties props = new Properties();
//        props.put("OSGi Services", "true");
//        context.registerService(ServiceChildren.class.getName(), myServiceChildren, props);
//        theLogger.log(Level.INFO, "ServiceChildren successfuly registers.");
//    }
}
