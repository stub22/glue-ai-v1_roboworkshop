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
package org.rwshop.swing.common.lifecycle;

import javax.swing.JLabel;
import javax.swing.UIManager;
import org.jflux.api.core.config.Configuration;
import org.jflux.api.core.config.DefaultConfiguration;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rwshop.swing.common.config.ConfigListPanel;
import org.rwshop.swing.common.config.ConfigListFrame;
import org.rwshop.swing.common.config.ConfigFrame;
import org.rwshop.swing.common.config.ConfigurationTracker;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class Activator implements BundleActivator {
    @Override
    public void start(final BundleContext context) throws Exception {
//        try{
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        }catch(Exception ex){}
//        
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                ServicesFrame svcFrame = new ServicesFrame();
//                svcFrame.setBundleContext(context);
//                svcFrame.setVisible(true);
//                ConfigFrame sf = new ConfigFrame();
//                ConfigurationTracker ct = new ConfigurationTracker(context);
//                ConfigListFrame cltf = new ConfigListFrame();
//                ConfigListPanel list = cltf.getList();
//                
//                ct.setList(list);
//                list.setConfigFrame(sf);
//                
//                sf.setVisible(true);
//                ct.start();
//                cltf.setVisible(true);
//            }
//        });
//        addConfigs(context);
    }
   
    private void addConfigs(BundleContext context){
        
        DefaultConfiguration<String> inConfig =
                new DefaultConfiguration<String>();
        inConfig.addProperty(Integer.class, "intProp", 4);
        inConfig.addProperty(Double.class, "dblProp", 4.0);
        inConfig.addProperty(Byte.class, "byteProp", (byte)4);
        inConfig.addProperty(String.class, "strProp2", "four");
        inConfig.addProperty(JLabel.class, "otherProp", new JLabel("four"));
        inConfig.addProperty(DefaultConfiguration.class, "confProp", inConfig);
        
        DefaultConfiguration<String> config = new DefaultConfiguration<String>();
        config.addProperty(Integer.class, "intProp", 5);
        config.addProperty(Double.class, "dblProp", 5.0);
        config.addProperty(Byte.class, "byteProp", (byte)5);
        config.addProperty(String.class, "strProp", "five");
        config.addProperty(DefaultConfiguration.class, "confProp", inConfig);
        config.addProperty(JLabel.class, "otherProp", new JLabel("five"));
        
        context.registerService(Configuration.class.getName(), config, null);
        context.registerService(Configuration.class.getName(), inConfig, null);
    }
    
    public void stop(BundleContext context) throws Exception {
        //TODO add deactivation code here
    }
}
