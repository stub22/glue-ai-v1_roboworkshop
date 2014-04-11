/*
 * Copyright 2014 the Friendularity Project
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

package org.rwshop.swing.motion.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import org.apache.http.entity.mime.content.StringBody;
import org.jflux.api.core.Listener;
import org.jflux.spec.discovery.Discoverer;
import org.jflux.spec.discovery.UniqueService;

/**
 *
 * @author Amy Jessica Book <jgpallack@gmail.com>
 */
public class RobotComboBoxModel extends DefaultComboBoxModel
    implements Listener<UniqueService> {
    private Map<String, String> myRobotServiceMap;
    private List<UniqueService> myRobots;
    private String mySelectedItem;
    
    // format is robotId @ IP / UUID
    
    public RobotComboBoxModel(Discoverer discoverer) {
        myRobotServiceMap = new HashMap<String, String>();
        myRobots = new ArrayList<UniqueService>();
        mySelectedItem = null;
        discoverer.addListener(this);
    }
    
    @Override
    public void setSelectedItem(Object o) {
        mySelectedItem = o.toString();
    }
    
    @Override
    public Object getSelectedItem() {
        return mySelectedItem;
    }
    
    @Override
    public int getSize() {
        return myRobots.size();
    }
    
    @Override
    public Object getElementAt(int i) {
        if(myRobots.isEmpty()) {
            return null;
        } else {
            return format(myRobots.get(i));
        }
    }
    
    public String getSelectedIP() {
        return myRobotServiceMap.get(mySelectedItem);
    }
    
    public UniqueService getSelectedRobot() {
        for (UniqueService robot : myRobots) {
            if (robot.getSerial().equals(deformat(mySelectedItem))) {
                return robot;
            }
        }
        
        return null;
    }

    @Override
    public void handleEvent(UniqueService t) {
        String serial = t.getSerial();
        String ipAddress = t.getIPAddress();
        
        if(myRobotServiceMap.containsKey(serial) &&
                myRobotServiceMap.get(serial).equals(ipAddress)) {
            return;
        }
        
        if(myRobotServiceMap.containsKey(serial)) {
            // This means the IP changed.
            
            int index = 0;
            
            for(int i = 0; i < myRobots.size(); i++) {
                if(myRobots.get(i).getSerial().equals(serial)) {
                    index = i;
                    break;
                }
            }
            
            myRobots.remove(index);
            myRobots.add(index, t);
        } else {
            // We have a new robot.
            
            myRobots.add(t);
        }

        myRobotServiceMap.put(serial, ipAddress);
        fireContentsChanged(this, 0, myRobots.size() - 1);
    }
    
    private static String format(UniqueService service) {
        StringBuilder sb = new StringBuilder();
        sb.append(service.getProperties().get("robotId"));
        sb.append(" @ ");
        sb.append(service.getIPAddress());
        sb.append(" / ");
        sb.append(service.getSerial());
        
        return sb.toString();
    }
    
    private static String deformat(String formatted) {
        return formatted.split("/")[1].trim();
    }
}
