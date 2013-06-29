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
package org.rwshop.nb.animation.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import org.openide.util.Exceptions;
import org.rwshop.nb.common.cookies.SaveAsCookie;

/**
 * 
 * @author Matthew Stevenson <www.robokind.org>
 */
public final class SaveAsAction implements ActionListener {

    private final SaveAsCookie context;

    public SaveAsAction(SaveAsCookie context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try{
            context.saveAs();
        }catch(IOException ex){
            Exceptions.printStackTrace(ex);
        }
    }
}
