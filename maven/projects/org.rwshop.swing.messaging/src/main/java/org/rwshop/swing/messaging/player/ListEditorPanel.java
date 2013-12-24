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
package org.rwshop.swing.messaging.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.Schema.Type;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;

/**
 *
 * @author jgpallack
 */
public class ListEditorPanel extends javax.swing.JPanel {
    private IndexedRecord myRecord;
    private int myIndex;
    private List myList;
    private JButton addButton;
    /**
     * Creates new form EditorListPanel
     */
    public ListEditorPanel(IndexedRecord record, int index) {
        initComponents();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        myRecord = record;
        myIndex = index;
        myList = (List)myRecord.get(myIndex);
        
        addButton = new JButton("Add");
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                remove(addButton);
                
                GenericData.Array trueList = (GenericData.Array)myList;
                Schema listSchema = trueList.getSchema();
                Schema schema = listSchema.getElementType();
                Object item = marshal(schema);
                
                myList.add(item);
                addItem(myList.size() - 1);
                
//                add(addButton);
                revalidate();
            }
        });
        
        add(addButton);
        
        for(int i = 0; i < myList.size(); i++) {
            addItem(i);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void addItem(final int i) {
        Object o = myList.get(i);
        
        if(IndexedRecord.class.isAssignableFrom(o.getClass())) {
            addEditorPanel(o);
        } else {
            addEditor(i);
        }
    }
    
    private void pushValue(String val, int i) {
        Class cls = ((List)myRecord.get(myIndex)).get(i).getClass();
        
        try {
            Object parsedVal = parseValue(cls, val);
            ((List)myRecord.get(myIndex)).set(i, parsedVal);
        } catch(Exception ex) {
            String message = "Failed to set " + val + " on index " + i;
            String longMessage = message + ":\n\n" + ex.getMessage();
            
            JOptionPane.showMessageDialog(
                    this, longMessage, "Error", JOptionPane.ERROR_MESSAGE);
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

    private void addEditorPanel(Object o) {
        EditorPanel panel = new EditorPanel();
        
        panel.setRecord((IndexedRecord)o);
        add(panel);
    }

    private void addEditor(final int i) {
        Object o = myList.get(i);
        final JTextField editor = new JTextField(o.toString());
        
        editor.setDropTarget(null);
        
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    String val = editor.getText();
                    pushValue(val, i);
                }
            }
        });

        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent evt) {
                String val = editor.getText();
                pushValue(val, i);
            }

            @Override
            public void focusGained(FocusEvent evt) {
            }
        });
        
        add(editor);
    }
    
    private Object marshal(Schema schema) {
        Type type = schema.getType();
        
        if(type == Type.STRING) {
            return "";
        } else if(type == Type.BOOLEAN) {
            return Boolean.FALSE;
        } else if(type == Type.DOUBLE) {
            return new Double(0.0);
        } else if(type == Type.FLOAT) {
            return new Float(0.0);
        } else if(type == Type.INT) {
            return new Integer(0);
        } else if(type == Type.LONG) {
            return new Long(0);
        } else if(type == Type.RECORD) {
            IndexedRecord record = new GenericData.Record(schema);
            
            List<Field> fields = schema.getFields();
            for(int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                Schema fieldSchema = field.schema();
                record.put(i, marshal(fieldSchema));
            }
            
            return record;
        } else if(type == Type.ARRAY) {
            return new GenericData.Array(schema, new ArrayList());
        } else {
            return new Object();
        }
    }
}
