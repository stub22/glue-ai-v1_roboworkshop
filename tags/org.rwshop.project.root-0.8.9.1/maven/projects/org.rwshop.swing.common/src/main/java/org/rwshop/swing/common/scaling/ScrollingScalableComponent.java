/*
 * Copyright 2011 Hanson Robokind LLC.
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
package org.rwshop.swing.common.scaling;

import org.rwshop.swing.common.scaling.ScalableComponent;
import javax.swing.JScrollPane;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class ScrollingScalableComponent implements ScalableComponent{
    private ScalableComponent myScalablePanel;
    private JScrollPane myScrollPane;

    public ScrollingScalableComponent(
            ScalableComponent innerPanel, JScrollPane scrollPane){
        if(innerPanel == null || scrollPane == null){
            throw new NullPointerException();
        }
        myScalablePanel = innerPanel;
        myScrollPane = scrollPane;
    }
    
    @Override
    public void setScalar(CoordinateScalar scalar) {
        myScalablePanel.setScalar(scalar);
    }

    @Override
    public int getMinWidth() {
        return myScalablePanel.getMinWidth();
    }

    @Override
    public int getMinHeight() {
        return myScalablePanel.getMinHeight();
    }

    @Override
    public void rescale(int minWidth, int minHeight) {
        myScalablePanel.rescale(minWidth, minHeight);
    }

    @Override
    public void setFocusPosition(Integer x, Integer y) {
        if(x == null){
            return;
        }
        myScrollPane.getHorizontalScrollBar().setValue(x);
    }
}
