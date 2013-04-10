/*
 * Copyright 2012 Hanson Robokind LLC.
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

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.osgi.framework.BundleContext;
import org.robokind.api.common.lifecycle.DependencyDescriptor;
import org.robokind.api.common.lifecycle.ManagedService;
import org.robokind.api.common.osgi.ServiceClassListener;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class ManagedServiceListPanel extends JPanel{
    private final static Logger theLogger = Logger.getLogger(ManagedServiceListPanel.class.getName());
    private Map<ManagedService,ManagedServicePanel> myPanelMap;
    private ManagedServiceListener myServiceChangeListener;
    private ServiceClassListener<ManagedService> myServiceTracker;
    private List<String> myFilters;
    private Map<ManagedService,ManagedServicePanel> myFilteredCache;
    private boolean myClassNames;
    private boolean myPropertyKeys;
    private boolean myPropertyValues;
    private boolean myDependencies;
    
    public ManagedServiceListPanel(){
        myServiceChangeListener = new ManagedServiceListener(this);
        myPanelMap = new HashMap<ManagedService, ManagedServicePanel>();
        
        setFilters("", true, true, true, true);
    }

    public void setBundleContext(BundleContext context){
        initLayout();
        myServiceTracker = new ServiceClassListener<ManagedService>(
                ManagedService.class, context, null);
        myServiceTracker.addPropertyChangeListener(
                myServiceChangeListener);
        myServiceTracker.start();
    }
    
    public void initLayout(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    
    public synchronized void addService(ManagedService service){
        if(service == null || myPanelMap.containsKey(service)){
           return;
        }
        ManagedServicePanel panel = new ManagedServicePanel();
        panel.setVisible(false);
        panel.setManagedService(service);
        myPanelMap.put(service, panel);
        add(panel);
        resize(panel.getPreferredSize().height);
        panel.setVisible(true);
        
        filterCache();
    }
    
    private void resize(int heightChange){
        Dimension prefSize = getPreferredSize();
        Dimension newSize = 
                new Dimension(prefSize.width, prefSize.height+heightChange);
        //setPreferredSize(newSize);
    }

    public synchronized void removeService(ManagedService service){
        if(service == null){
            return;
        }
        ManagedServicePanel panel = myPanelMap.remove(service);
        if(panel == null){
            return;
        }
        myPanelMap.remove(service);
        remove(panel);
        resize(-(panel.getPreferredSize().height));
        revalidate();
        repaint();
        
        refresh();
    }
    
    public synchronized void clearDependencies(){
        myPanelMap.clear();
        removeAll();
    }
    
    public synchronized void setFilters(
            String filterStr, boolean classNames, boolean propertyKeys,
            boolean propertyValues, boolean dependencies){
        myFilters = new ArrayList<String>();
        myClassNames = classNames;
        myPropertyKeys = propertyKeys;
        myPropertyValues = propertyValues;
        myDependencies = dependencies;
        
        String[] filters = filterStr.split(",");
        for(String s : filters){
            s = s.trim();
            if(!s.isEmpty()){
                myFilters.add(s);
            }
        }
        filterCache();
    }
    
    private void filterCache(){
        if (myPanelMap == null){
            return;
        }
        if(myFilters == null || myFilters.isEmpty()){
            myFilteredCache = myPanelMap;
            refresh();
            
            return;
        }
        myFilteredCache = new HashMap<ManagedService,ManagedServicePanel>();
        for(ManagedService thals : myPanelMap.keySet()){
            if(filterList(thals)){
                myFilteredCache.put(thals, myPanelMap.get(thals));
            }
        }
        
        refresh();
    }
    
    private void refresh(){
        for(ManagedService ms : myPanelMap.keySet()){
            if(myFilteredCache.containsKey(ms)){
                myPanelMap.get(ms).setVisible(true);
            }else{
                myPanelMap.get(ms).setVisible(false);
            }
        }
        
        updateUI();
    }
    
    private boolean filterList(ManagedService thals){
        if(myClassNames){
            for(String s : thals.getServiceClassNames()){
                for(String f : myFilters){
                    Pattern p = Pattern.compile(
                            ".*" + f + ".*", Pattern.MULTILINE | Pattern.DOTALL);
                    if(s != null && p.matcher(s).matches()){
                        return true;
                    }
                }
            }
        }
        
        if(myPropertyKeys){
            for(String s : thals.getRegistrationProperties().stringPropertyNames()){
                for(String f : myFilters){
                    Pattern p = Pattern.compile(
                            ".*" + f + ".*", Pattern.MULTILINE | Pattern.DOTALL);
                    if(s != null && p.matcher(s).matches()){
                        return true;
                    }
                }
            }
        }
        
        if(myPropertyValues){
            for(String s : thals.getRegistrationProperties().stringPropertyNames()){
                for(String f : myFilters){
                    Pattern p = Pattern.compile(
                            ".*" + f + ".*", Pattern.MULTILINE | Pattern.DOTALL);
                    if(thals.getRegistrationProperties().getProperty(s) != null
                            && p.matcher(thals.getRegistrationProperties().getProperty(s)).matches()){
                        return true;
                    }
                }
            }
        }
        
        if(myDependencies){
            for(DependencyDescriptor d : (List<DependencyDescriptor>)thals.getDependencies()){
                for(String f : myFilters){
                    Pattern p = Pattern.compile(
                            ".*" + f + ".*", Pattern.MULTILINE | Pattern.DOTALL);
                    if(d.getDependencyName() != null &&
                            p.matcher(d.getDependencyName()).matches()){
                        return true;
                    } else if(d.getServiceFilter() != null &&
                            p.matcher(d.getServiceFilter()).matches()){
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    class ManagedServiceListener implements PropertyChangeListener{
        private Component myComp;

        public ManagedServiceListener(Component comp) {
            myComp = comp;
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt == null){
                theLogger.warning("Received null property change event.");
                return;
            }else if(evt.getNewValue() == null ||
                    !(evt.getNewValue() instanceof ManagedService)){
                theLogger.log(Level.WARNING, 
                        "Received property change with bad value: {0}", 
                        evt.getPropertyName());
                return;
            }
            if(ServiceClassListener.PROP_SERVICE_ADDED.equals(
                    evt.getPropertyName())){
                addService((ManagedService)evt.getNewValue());
            }else if(ServiceClassListener.PROP_SERVICE_REMOVED.equals(
                    evt.getPropertyName())){
                removeService((ManagedService)evt.getNewValue());
            }
        }
        
    }
}
