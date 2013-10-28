/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rwshop.swing.common.config;

import org.jflux.api.core.config.Configuration;
import org.jflux.impl.services.rk.osgi.ServiceClassListener;
import org.osgi.framework.BundleContext;

/**
 *
 * @author Jason G. Pallack <jgpallack@gmail.com>
 */
public class ConfigurationTracker extends ServiceClassListener<Configuration> {

    private ConfigListPanel myList;

    public ConfigurationTracker(BundleContext context) {
        super(Configuration.class, context, null);
        myList = null;
    }

    public void setList(ConfigListPanel list) {
        myList = list;
    }

    @Override
    protected void addService(Configuration t) {
        if(myList != null) {
            myList.pushService(t);
        }
    }

    @Override
    protected void removeService(Configuration t) {
        if(myList != null) {
            myList.popService(t);
        }
    }
}
