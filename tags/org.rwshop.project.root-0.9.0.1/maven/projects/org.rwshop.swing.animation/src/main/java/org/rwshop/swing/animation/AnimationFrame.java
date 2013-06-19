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

/*
 * AnimationFrame.java
 *
 * Created on Feb 2, 2011, 10:35:35 PM
 */

package org.rwshop.swing.animation;

import javax.swing.JFileChooser;
import org.robokind.api.animation.xml.AnimationXML;
import org.robokind.api.animation.editor.AnimationEditor;
import org.robokind.api.animation.editor.history.HistoryStack;
import org.robokind.api.animation.Animation;
import org.robokind.api.animation.editor.history.HistoryFactoryImpl;
import org.rwshop.swing.animation.menus.MenuBarManager;
import org.rwshop.swing.animation.osgi.Activator;
import org.rwshop.swing.common.scaling.ScalingManager;
import org.rwshop.swing.common.utils.MessageAlerter;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class AnimationFrame extends javax.swing.JFrame {
    /** Creates new form AnimationFrame */
    
    public AnimationFrame() {
        initComponents();
    }
    
    public void init(ScalingManager scalingManager){
        animationEditor1.init(scalingManager);
        AnimationEditor editor = loadAnimation(null);
        animationEditor1.set(editor);
        MenuBarManager menu = new MenuBarManager(myMenuBard, animationEditor1, new HistoryFactoryImpl());
        menu.setupMenu();
    }

    private AnimationEditor loadAnimation(String path) {
        Animation anim;
        path = "./resources/anim.xml";
        if(path == null || path.isEmpty()){
            JFileChooser fileChooser = new JFileChooser();
            int i = fileChooser.showSaveDialog(null);
            if(i == JFileChooser.CANCEL_OPTION){
                path = null;
            }else{
                path = fileChooser.getSelectedFile().getPath();
            }
        }
        if(path != null){
            try{
                anim = AnimationXML.loadAnimation(path);
            }catch(Throwable t){
                MessageAlerter.Error("animation.load.error", null, t);
                anim = new Animation();
            }
        }else{
            anim = new Animation();
        }
        AnimationEditor editor = new AnimationEditor(anim, path, 
                new HistoryStack());
        return editor;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        animationEditor1 = new org.rwshop.swing.animation.AnimationEditorPanel();
        myMenuBard = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setJMenuBar(myMenuBard);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(animationEditor1, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(animationEditor1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        try{
            new Activator().start(null);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.rwshop.swing.animation.AnimationEditorPanel animationEditor1;
    private javax.swing.JMenuBar myMenuBard;
    // End of variables declaration//GEN-END:variables

}
