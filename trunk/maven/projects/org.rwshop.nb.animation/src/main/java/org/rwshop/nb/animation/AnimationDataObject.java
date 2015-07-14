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

import java.io.IOException;
import org.mechio.api.animation.editor.AnimationEditor;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.rwshop.nb.animation.cookies.*;
import org.rwshop.nb.common.cookies.*;

/**
 *
 * @author krystal
 */
@Messages({
	"LBL_Animation_LOADER=Files of Animation"
})
@MIMEResolver.ExtensionRegistration(
		displayName = "#LBL_Animation_LOADER",
		mimeType = "text/rkanim+xml",
		extension = {"rkanim", "xml", "anim"}
)
@DataObject.Registration(
		mimeType = "text/rkanim+xml",
		iconBase = "org/rwshop/nb/animation/web.png",
		displayName = "#LBL_Animation_LOADER",
		position = 300
)
@ActionReferences({
	@ActionReference(
			path = "Loaders/text/rkanim+xml/Actions",
			id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
			position = 100,
			separatorAfter = 200
	),
	@ActionReference(
			path = "Loaders/text/rkanim+xml/Actions",
			id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
			position = 300
	),
	@ActionReference(
			path = "Loaders/text/rkanim+xml/Actions",
			id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
			position = 400,
			separatorAfter = 500
	),
	@ActionReference(
			path = "Loaders/text/rkanim+xml/Actions",
			id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
			position = 600
	),
	@ActionReference(
			path = "Loaders/text/rkanim+xml/Actions",
			id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
			position = 700,
			separatorAfter = 800
	),
	@ActionReference(
			path = "Loaders/text/rkanim+xml/Actions",
			id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
			position = 900,
			separatorAfter = 1000
	),
	@ActionReference(
			path = "Loaders/text/rkanim+xml/Actions",
			id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
			position = 1100,
			separatorAfter = 1200
	),
	@ActionReference(
			path = "Loaders/text/rkanim+xml/Actions",
			id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
			position = 1300
	),
	@ActionReference(
			path = "Loaders/text/rkanim+xml/Actions",
			id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
			position = 1400
	)
})
public class AnimationDataObject extends MultiDataObject {
	private InstanceContent myContent;
	private Lookup lookup;
	private AnimationEditor myController;

	/**
	 *
	 * @param pf
	 * @param loader
	 * @throws DataObjectExistsException
	 * @throws IOException	 *
	 */
	public AnimationDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
		super(pf, loader);
		registerEditor("text/rkanim+xml", true);

		myContent = new InstanceContent();
		lookup = new AbstractLookup(myContent);
		//myController = Lookup.getDefault().lookup(AnimationEditor.class);
		myController = null;
		myContent.add(this);
	}

	/**
	 *
	 * @param controller
	 */
	public void setController(AnimationEditor controller){
		if(myController != null){
			myContent.remove(myController);
		}
		myController = controller;
		myContent.add(myController);

		getCookieSet().add(new PlayAnimationCookie(myController));
        getCookieSet().add(new LoopAnimationCookie(myController));
        getCookieSet().add(new StopAnimationCookie(myController));
        getCookieSet().add(new StopAllAnimationsCookie(myController));
		registerCookies(myContent, lookup);
	}

	/**
	 *
	 * @return AnimationEditor controller
	 */
	public AnimationEditor getController(){
		return myController;
	}

	/**
	 *
	 * @return
	 */
	@Override
	protected int associateLookup() {
		return 1;
	}

	/**
	 *
	 * @param lkp
	 * @return
	 */
	@MultiViewElement.Registration(
			displayName = "#LBL_Animation_EDITOR",
			iconBase = "org/rwshop/nb/animation/web.png",
			mimeType = "text/rkanim+xml",
			persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
			preferredID = "Animation",
			position = 1000
	)
	@Messages("LBL_Animation_EDITOR=Source")
	public static MultiViewEditorElement createEditor(Lookup lkp) {
		return new MultiViewEditorElement(lkp);
	}

	/**
	 *
	 * @return new AnimationNode
	 */
	@Override
	protected Node createNodeDelegate(){
		return new AnimationNode(getLookup().lookup(AnimationEditor.class));
	}

	/**
	 *
	 * @return AnimationDataObject lookup
	 */
	@Override
	public Lookup getLookup(){
		return lookup;
	}

	/**
	 *
	 * @param content
	 * @param l
	 */
	public void registerCookies(InstanceContent content, Lookup l){
        registerCookies(content, l, getCookieSet(),
                PlayCookie.class, LoopCookie.class, StopCookie.class, StopAllCookie.class);
    }

    private static void registerCookies(InstanceContent i, Lookup l, CookieSet cs, Class<? extends Node.Cookie>...types){
        for(Class<? extends Node.Cookie> c : types){
            removeCookie(c, l, i);
            addCookie(c, cs, i);
        }
    }

    private static <T extends Node.Cookie> void removeCookie(Class<T> c, Lookup l, InstanceContent i){
        T t = l.lookup(c);
        if(t != null){
            i.remove(t);
        }
    }

    private static <T extends Node.Cookie> void addCookie(Class<T> c, CookieSet cs, InstanceContent i){
        T t = cs.getCookie(c);
        if(t != null){
            i.add(t);
        }
    }
}
