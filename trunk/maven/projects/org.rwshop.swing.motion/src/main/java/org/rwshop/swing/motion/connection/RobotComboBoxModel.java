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
import org.jflux.api.core.Listener;
import org.jflux.spec.discovery.Discoverer;
import org.jflux.spec.discovery.UniqueService;

/**
 *
 * @author Amy Jessica Book <jgpallack@gmail.com>
 */
public class RobotComboBoxModel extends DefaultComboBoxModel<UniqueService>
    implements Listener<UniqueService> {
    private Map<String, UniqueService> myRobotServiceMap;
    
    // format is robotId @ IP / UUID
    
    public RobotComboBoxModel(Discoverer discoverer) {
        myRobotServiceMap = new HashMap<String, UniqueService>();
        discoverer.addListener(this);
    }
    
    public String getSelectedIP() {
        UniqueService s = (UniqueService)getSelectedItem();
        if(s == null){
            return "none";
        }
        return s.getIPAddress();
    }

    @Override
    public void handleEvent(UniqueService t) {
        if(getIndexOf(t) >= 0){
            return;
        }
        
        String serial = t.getSerial();
        if(myRobotServiceMap.containsKey(serial)) {
            // This means the IP changed.
            UniqueService u = myRobotServiceMap.get(serial);
            int i = getIndexOf(u);
            removeElement(u);
            insertElementAt(t, i);
        } else {
            // We have a new robot.
            addElement(t);
        }
        myRobotServiceMap.put(serial, t);
        fireContentsChanged(this, 0, getSize());
    }
}
