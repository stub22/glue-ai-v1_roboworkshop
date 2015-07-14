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

import java.io.File;
import java.io.IOException;
import org.jflux.api.common.rk.utils.RKSource.SourceImpl;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.filesystems.FileObject;
import org.openide.loaders.SaveAsCapable;
import org.openide.util.lookup.InstanceContent;
import org.rwshop.swing.animation.actions.FileAction;

/**
 *
 * @author krystal
 */

public class MySavable extends AbstractSavable implements SaveAsCapable{
	private AnimationDataObject myAnimDObj;
	private InstanceContent myContent;

	/**
	 *
	 * @param animDob
	 * @param content
	 */
	public MySavable(AnimationDataObject animDob, InstanceContent content){
		myAnimDObj = animDob;
		myContent = content;
		register();
	}

	/**
	 *
	 * @return Animation name
	 */
	@Override
	protected String findDisplayName() {
		return myAnimDObj.getController().getName();
	}

	/**
	 *
	 * @throws IOException	 *
	 */
	@Override
	protected void handleSave() throws IOException {
		new FileAction.Save(new SourceImpl(myAnimDObj.getController()), false).actionPerformed(null);
		unregister();
		myContent.remove(this);
		myAnimDObj.setModified(false);
	}

	@Override
	public boolean equals(Object obj) {
		Class save = MySavable.class;
		if(save.isInstance(obj)){
			return ((MySavable)obj).myAnimDObj.equals(myAnimDObj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return myAnimDObj.hashCode();
	}

	/**
	 *
	 * @param folder
	 * @param name
	 * @throws IOException
	 */
	@Override
	public void saveAs(FileObject directory, String name) throws IOException {
		String path = directory.getPath() + File.separator + name;
		if (!path.endsWith(".rkanim")) {
			path = path.concat(".rkanim");
		}

		new FileAction.SaveAs(path, name, myAnimDObj.getController());
		unregister();
		myContent.remove(this);
		myAnimDObj.setModified(false);
	}
}

