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

package org.rwshop.swing.common.utils;

import java.awt.Component;
import javax.swing.JOptionPane;

import static org.jflux.api.common.rk.localization.Localizer.$;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class MessageAlerter {
    private static Component myComponent;

    /**
     *
     * @param c
     */
    public static void setComponent(Component c){
        myComponent = c;
    }
    /**
     *
     * @param error
     * @param innerError
     * @param t
     */
    public static void Error(String error, String innerError, Throwable t){
        JOptionPane.showMessageDialog(myComponent, error, $("error"), JOptionPane.ERROR_MESSAGE);
    }
    /**
     *
     * @param title
     * @param message
     */
    public static void Ok(String title, String message){
        JOptionPane.showMessageDialog(myComponent, message, title, JOptionPane.PLAIN_MESSAGE);
    }
}
