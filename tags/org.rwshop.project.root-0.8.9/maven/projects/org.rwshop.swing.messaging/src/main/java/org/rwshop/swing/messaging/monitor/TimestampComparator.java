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
package org.rwshop.swing.messaging.monitor;

import java.util.Comparator;
import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;

/**
 *
 * @author jgpallack
 */
public class TimestampComparator implements Comparator<IndexedRecord> {

    @Override
    public int compare(IndexedRecord o1, IndexedRecord o2) {
        Schema o1Schema = o1.getSchema();
        Schema o2Schema = o2.getSchema();
        int index1 = -1;
        int index2 = -1;
        
        for(int i = 0; i < o1Schema.getFields().size(); i++) {
            if(o1Schema.getFields().get(i).name().equals(
                    "timestampMillisecUTC")) {
                index1 = i;
                break;
            }
        }
        
        for(int i = 0; i < o2Schema.getFields().size(); i++) {
            if(o2Schema.getFields().get(i).name().equals(
                    "timestampMillisecUTC")) {
                index2 = i;
                break;
            }
        }
        
        if(index1 < 0 || index2 < 0) {
            return 0;
        }
        
        Long o1Timestamp = (Long)o1.get(index1);
        Long o2Timestamp = (Long)o2.get(index2);
        
        return o1Timestamp.compareTo(o2Timestamp);
    }
}
