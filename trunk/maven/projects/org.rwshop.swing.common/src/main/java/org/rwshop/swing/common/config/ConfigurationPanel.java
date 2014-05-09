/*
 * Copyright 2012 The JFlux Project (www.jflux.org).
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
package org.rwshop.swing.common.config;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.jflux.api.core.Listener;
import org.jflux.api.core.Notifier;
import org.jflux.api.core.config.Configuration;

/**
 *
 * @author Amy Jessica Book <jgpallack@gmail.com>
 */
public class ConfigurationPanel extends javax.swing.JPanel {

    private Configuration<String> myConfig;
    private final static Logger theLogger =
            Logger.getLogger(ConfigurationPanel.class.getName());
    private GridBagConstraints myConstraints;
    private JTextField jTextField1;
    private JCheckBox jCheckBox1;
    private JCheckBox jCheckBox2;
    private List<String> myFilters;
    private List<String> myFilteredKeys;
    private boolean myKeys;
    private boolean myValues;
    private String myFilterStr;

    /**
     * Creates new form ConfigurationPanel
     */
    public ConfigurationPanel() {
        initComponents();

        myConstraints = new GridBagConstraints();
        myConstraints.fill = GridBagConstraints.HORIZONTAL;
    }

    public void setConfig(Configuration<String> config, boolean inner) {
        myConstraints.gridy = 0;
        removeAll();

        myConfig = config;
        setLayout(new GridBagLayout());
        setFilters("", true, true);

        jTextField1 = new JTextField();
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        
        jTextField1.setDropTarget(null);

        jCheckBox1 = new JCheckBox("Keys", true);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        
        jCheckBox2 = new JCheckBox("Values", true);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        if(!inner) {
            myConstraints.gridwidth = 2;
            myConstraints.gridx = 0;
            myConstraints.weightx = 1.0;
            add(jTextField1, myConstraints);

            myConstraints.weightx = 0.0;
            myConstraints.gridwidth = 1;
            myConstraints.gridy = 1;
            add(jCheckBox1, myConstraints);

            myConstraints.gridx = 1;
            add(jCheckBox2, myConstraints);

            myConstraints.gridx = 0;
            myConstraints.gridy = 2;
        } else {
            myConstraints.gridx = 0;
            myConstraints.gridy = 0;
            myConstraints.weightx = 0.0;
            myConstraints.gridwidth = 1;
        }

        if(config == null) {
            return;
        }

        for(String s : config.getKeySet()) {
            JLabel label = new JLabel(s);
            label.setName(s);
            myConstraints.gridx = 0;
            myConstraints.weightx = 0.0;
            add(label, myConstraints);

            myConstraints.gridx = 1;
            myConstraints.weightx = 1.0;

            if(Configuration.class.isAssignableFrom(config.getPropertyClass(s))) {
                addConfigurationPanel(s);
            } else if(!isParseable(config.getPropertyClass(s))
                    || config.getPropertySetter(s) == null) {
                addReadOnlyField(s);
            } else {
                addEditor(s);
            }

            myConstraints.gridy++;
        }

        revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public Configuration getConfig() {
        return myConfig;
    }

    private void pushValue(String key, String val) {
        Listener setter = myConfig.getPropertySetter(key);
        Class cls = myConfig.getPropertyClass(key);

        try {
            Object parsedVal = parseValue(cls, val);
            setter.handleEvent(parsedVal);
        } catch(Exception e) {
            String message = "Failed to set " + val + " on " + key;
            String longMessage = message + ":\n\n" + e.getMessage();

            theLogger.log(Level.SEVERE, "{0}.", message);
            theLogger.log(Level.SEVERE, e.getMessage());

            JOptionPane.showMessageDialog(
                    this, longMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isParseable(Class cls) {
        if(Integer.class.isAssignableFrom(cls)) {
            return true;
        } else if(Long.class.isAssignableFrom(cls)) {
            return true;
        } else if(Short.class.isAssignableFrom(cls)) {
            return true;
        } else if(Byte.class.isAssignableFrom(cls)) {
            return true;
        } else if(Float.class.isAssignableFrom(cls)) {
            return true;
        } else if(Double.class.isAssignableFrom(cls)) {
            return true;
        } else if(String.class.isAssignableFrom(cls)) {
            return true;
        } else {
            return false;
        }
    }

    private Object parseValue(Class cls, String val) {
        if(Integer.class.isAssignableFrom(cls)) {
            return Integer.parseInt(val);
        } else if(Long.class.isAssignableFrom(cls)) {
            return Long.parseLong(val);
        } else if(Short.class.isAssignableFrom(cls)) {
            return Short.parseShort(val);
        } else if(Byte.class.isAssignableFrom(cls)) {
            return Byte.parseByte(val);
        } else if(Float.class.isAssignableFrom(cls)) {
            return Float.parseFloat(val);
        } else if(Double.class.isAssignableFrom(cls)) {
            return Double.parseDouble(val);
        } else if(String.class.isAssignableFrom(cls)) {
            return val;
        } else {
            throw new IllegalArgumentException(val);
        }
    }

    private void addConfigurationPanel(String s) {
        Configuration<String> newConfig = myConfig.getPropertyValue(s);
        ConfigurationPanel panel = new ConfigurationPanel();
        
        if(newConfig.equals(myConfig)) {
            JTextField field = new JTextField("This configuration");
            field.setName(s);
            field.setEditable(false);
            add(field, myConstraints);
            
            theLogger.log(
                    Level.WARNING, "Cannot add a configuration to itself.");
            return;
        }
        
        panel.setName(s);
        panel.setConfig(newConfig, true);

        add(panel, myConstraints);
    }

    private void addReadOnlyField(String s) {
        JTextField field;
        
        if(myConfig.getPropertyValue(s) != null) {
            field = new JTextField(myConfig.getPropertyValue(s).toString());
        } else {
            field = new JTextField("null");
        }

        field.setEditable(false);
        field.setName(s);
        field.setDropTarget(null);

        add(field, myConstraints);
    }

    private void addEditor(String key) {
        final JTextField field = new JTextField();
        final String finalKey = key;
        final Configuration fConfig = myConfig;
        
        if(myConfig.getPropertyValue(key) != null) {
            field.setText(myConfig.getPropertyValue(key).toString());
        }

        field.setName(key);
        field.setDropTarget(null);

        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    String val = field.getText();
                    pushValue(finalKey, val);
                    
                    filterCache();
                }
            }
        });
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent evt) {
                String val = field.getText();
                pushValue(finalKey, val);
                
                filterCache();
            }

            @Override
            public void focusGained(FocusEvent evt) {
            }
        });

        Notifier notifier = myConfig.getPropertyNotifier(key);
        notifier.addListener(new Listener() {
            @Override
            public void handleEvent(Object input) {
                field.setText(fConfig.getPropertyValue(finalKey).toString());
                filterCache();
            }
        });

        add(field, myConstraints);
    }

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {
        setFilters(
                jTextField1.getText(), jCheckBox1.isSelected(),
                jCheckBox2.isSelected());
    }
    
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        setFilters(
                jTextField1.getText(), jCheckBox1.isSelected(),
                jCheckBox2.isSelected());
    }                                          

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        setFilters(
                jTextField1.getText(), jCheckBox1.isSelected(),
                jCheckBox2.isSelected());
    }             

    public void setFilters(String filterStr, boolean keys, boolean values) {
        myFilters = new ArrayList<String>();
        myFilterStr = filterStr;
        myKeys = keys;
        myValues = values;

        String[] filters = filterStr.split(",");
        for(String s : filters) {
            s = s.trim();
            if(!s.isEmpty()) {
                myFilters.add(s);
            }
        }
        filterCache();
    }

    private void filterCache() {
        if(myConfig == null || myConfig.getKeySet().isEmpty()) {
            return;
        }

        if(myFilters == null || myFilters.isEmpty()) {
            myFilteredKeys = new ArrayList<String>();

            for(String key : myConfig.getKeySet()) {
                myFilteredKeys.add(key);
            }

            refresh();

            return;
        }

        myFilteredKeys = new ArrayList<String>();
        for(String thals : myConfig.getKeySet()) {
            if(filterList(thals)) {
                myFilteredKeys.add(thals);
            }
        }

        refresh();
    }

    public boolean refresh() {
        boolean anything = false;
        
        for(Component comp : getComponents()) {
            if(comp == null || comp == jTextField1 || comp == jCheckBox1
                    || comp == jCheckBox2) {
                continue;
            }
            
            if(ConfigurationPanel.class.isAssignableFrom(comp.getClass())) {
                ConfigurationPanel inner = (ConfigurationPanel)comp;
                inner.setFilters(myFilterStr, myKeys, myValues);
                boolean keepInner = inner.refresh();
                
                comp.setVisible(keepInner);
                
                if(keepInner) {
                    continue;
                }
            }

            if(myFilteredKeys.contains(comp.getName())) {
                comp.setVisible(true);
                anything = true;
            } else {
                comp.setVisible(false);
            }
        }

        revalidate();

        try {
            ((JFrame)getTopLevelAncestor()).pack();
        } catch(Exception e) {
            theLogger.log(Level.WARNING, e.getMessage());
        }
        
        return anything;
    }

    private boolean filterList(String thals) {
        if(myKeys) {
            for(String f : myFilters) {
                Pattern p = Pattern.compile(
                        ".*" + f + ".*", Pattern.MULTILINE | Pattern.DOTALL);
                if(p.matcher(thals).matches()) {
                    return true;
                }
            }
        }

        if(myValues) {
            for(String f : myFilters) {
                Pattern p = Pattern.compile(
                        ".*" + f + ".*", Pattern.MULTILINE | Pattern.DOTALL);
                if(p.matcher(myConfig.getPropertyValue(thals).toString()).matches()) {
                    return true;
                }
            }
        }
        
        if(Configuration.class.isAssignableFrom(myConfig.getPropertyClass(thals))) {
            Configuration<String> newConfig =
                    (Configuration<String>)(myConfig.getPropertyValue(thals));
            
            if(newConfig.equals(myConfig)) {
                return false;
            }
            
            for(Component comp: getComponents()) {
                if(ConfigurationPanel.class.isAssignableFrom(comp.getClass())) {
                    ConfigurationPanel inner = (ConfigurationPanel)comp;
                    if(inner.getConfig() == newConfig) {
                        inner.setFilters(myFilterStr, myKeys, myValues);
                        return inner.refresh();
                    }
                }
            }
        }

        return false;
    }
}
