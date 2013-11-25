/*
 * Copyright 2013 Hanson Robokind LLC.
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
package org.rwshop.nb.motion.actions;

import edu.emory.mathcs.backport.java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jflux.impl.services.rk.lifecycle.ManagedService;
import org.jflux.impl.services.rk.osgi.lifecycle.OSGiComponent;
import org.osgi.framework.ServiceRegistration;

/**
 *
 * @author Jason G. Pallack <jgpallack@gmail.com>
 */
public class DisconnectAction implements ActionListener {
    private static Set<ManagedService> theServices =
            new HashSet<ManagedService>();
    private static Set<OSGiComponent> theComponents =
            new HashSet<OSGiComponent>();
    private static Set<ServiceRegistration> theRegs =
            new HashSet<ServiceRegistration>();
    
    @Override
    public void actionPerformed(ActionEvent e) {
        disconnect();
    }
    
    public static void disconnect() {
        for(ManagedService service: theServices) {
            service.dispose();
        }
        
        for(OSGiComponent component: theComponents) {
            component.dispose();
        }
        
        for(ServiceRegistration reg: theRegs) {
            reg.unregister();
        }
        
        theServices = new HashSet<ManagedService>();
        theComponents = new HashSet<OSGiComponent>();
        theRegs = new HashSet<ServiceRegistration>();
    }
    
    public static void addService(ManagedService service) {
        if(service != null) {
            theServices.add(service);
        }
    }
    
    public static void addServices(Collection<ManagedService> services) {
        if(services != null && !services.isEmpty()) {
            theServices.addAll(services);
        }
    }
    
    public static void addServices(ManagedService[] services) {
        if(services != null && services.length > 0) {
            theServices.addAll(Arrays.asList(services));
        }
    }
    
    public static void addComponent(OSGiComponent component) {
        if(component != null) {
            theComponents.add(component);
        }
    }
    
    public static void addComponents(Collection<OSGiComponent> components) {
        if(components != null && !components.isEmpty()) {
            theComponents.addAll(components);
        }
    }
    
    public static void addComponents(OSGiComponent[] components) {
        if(components != null && components.length > 0) {
            theComponents.addAll(Arrays.asList(components));
        }
    }
    
    public static void addReg(ServiceRegistration reg) {
        if(reg != null) {
            theRegs.add(reg);
        }
    }
    
    public static void addRegs(Collection<ServiceRegistration> regs) {
        if(regs != null && !regs.isEmpty()) {
            theRegs.addAll(regs);
        }
    }
    
    public static void addRegs(ServiceRegistration[] regs) {
        if(regs != null && regs.length > 0) {
            theRegs.addAll(Arrays.asList(regs));
        }
    }
}
