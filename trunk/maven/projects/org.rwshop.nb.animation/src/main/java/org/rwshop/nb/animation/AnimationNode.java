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

package org.rwshop.nb.animation;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.mechio.api.animation.editor.AbstractEditor;
import org.mechio.api.animation.editor.AnimationEditor;
import org.rwshop.nb.common.VersionPropertySheet;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationNode extends AbstractNode implements PropertyChangeListener{
    private AnimationEditor myController;

    public AnimationNode(AnimationEditor controller) {
        super(Children.LEAF);
        //super(new AnimationControllerChildren(controller), Lookups.singleton(controller));
        if(controller == null){
            throw new NullPointerException("Cannot create JointNode with null Joint.");
        }
        myController = controller;
        setDisplayName(myController.getName());
        myController.addPropertyChangeListener(WeakListeners.propertyChange(this, myController));
    }

    public AnimationEditor getAnimationController(){
        return myController;
    }

    @Override
    public String getHtmlDisplayName() {
        if(myController == null){
            return null;
        }
        String name = myController.getName();
        if (name == null) {
            return null;
        }
        return "<font color='!textText'>" + name + "</font>";
    }

    @Override
    public Image getIcon(int type) {
        return Utilities.loadImage("org/myorg/myeditor/icon.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{};
    }

    protected Sheet.Set getDefaultSet(){
        Sheet.Set set = Sheet.createPropertiesSet();
        Property pathProp = getPathProperty();
        set.put(pathProp);
        return set;
    }

	private Property getPathProperty(){
        /* Changed Path property from read/write to read only since changing the
         * could be expected to move or copy the animation to the new path.
         * I don't want to spend time supporting this since I plan to change
         * from a file path to a URI first. - Matt 6/14/2011
         */
        /*Property pathProp = new PropertySupport.ReadWrite<File>("Path", File.class, "Path", "") {
            @Override
            public File getValue() throws IllegalAccessException, InvocationTargetException {
                String path = myController.getFilePath();
                if(path == null || path.isEmpty()){
                    return null;
                }
                return new File(myController.getFilePath());
            }
            @Override
            public void setValue(File val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                String path = val.getAbsolutePath();
                myController.setFilePath(path);
            }
        };*/
        Property pathProp = new PropertySupport.ReadOnly<String>("Path", String.class, "Path", "") {
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                if(myController == null){
                    return "";
                }
                return myController.getFilePath();
            }
        };
        return pathProp;
    }

    protected Sheet.Set[] getPropertySheetSets(){
        List<Sheet.Set> sets = new ArrayList(2);
        try{
            sets.add(VersionPropertySheet.getVersionPropertySheetSet("Version", "Animation", myController));
        }catch(NoSuchMethodException ex){}
        sets.add(getDefaultSet());
        return sets.toArray(new Sheet.Set[0]);
    }

    //TODO: add SaveCookie to properties window
    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set[] sets = getPropertySheetSets();
        for(Sheet.Set set : sets){
            sheet.put(set);
        }
        return sheet;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (AbstractEditor.PROP_NAME.equals(evt.getPropertyName())) {
            setDisplayName((String)evt.getNewValue());
        }
    }

	/*public static class AnimationControllerChildren extends Children.Keys{
        private AnimationEditor myController;

        public AnimationControllerChildren(AnimationEditor controller){
            myController = controller;
        }

        @Override
        protected void addNotify(){
            super.addNotify();
            List keys = new LinkedList();
            keys.addAll(myController.getChildren());
            setKeys(keys);
        }

        @Override
        protected Node[] createNodes(Object key) {
            if(key instanceof Joint){
                return new Node[]{new JointNode((Joint)key)};
            }else if(key instanceof JointControllerConfig){
                return new Node[]{new ControllerConfigNode((JointControllerConfig)key)};
            }
            return null;
        }
    }*/
}
