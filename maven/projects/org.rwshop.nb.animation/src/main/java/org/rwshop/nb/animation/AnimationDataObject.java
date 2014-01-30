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

import org.rwshop.nb.animation.cookies.AnimationSaveCookie;
import java.io.IOException;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.mechio.api.animation.editor.AnimationEditor;
import org.rwshop.nb.animation.cookies.AnimationSaveAsCookie;
import org.rwshop.nb.common.cookies.SaveAsCookie;

/**
 * 
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationDataObject extends MultiDataObject {
    private InstanceContent myInstance;
    private Lookup myLookup;
    private AnimationEditor myController;
    
    public AnimationDataObject(AnimationEditor controller, FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        myController = controller;
        myInstance = new InstanceContent();
        myLookup = new AbstractLookup(myInstance);
        myInstance.add(myController);
        setSaveCookie();
    }
    
    private void setSaveCookie(){
        if(getLookup().lookup(SaveCookie.class) == null){
            SaveCookie cookie = new AnimationSaveCookie(myController);
            myInstance.add(cookie);
        }
        
        if(getLookup().lookup(SaveAsCookie.class) == null){
            SaveAsCookie saCookie = new AnimationSaveAsCookie(myController);
            myInstance.add(saCookie);
        }
    }

    @Override
    protected Node createNodeDelegate() {
        return new AnimationNode(getLookup().lookup(AnimationEditor.class));
    }

    @Override
    public Lookup getLookup() {
        return myLookup;
    }
    
    
}
