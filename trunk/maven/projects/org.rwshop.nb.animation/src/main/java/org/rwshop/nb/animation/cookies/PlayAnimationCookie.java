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

package org.rwshop.nb.animation.cookies;

import org.jflux.api.common.rk.utils.RKSource.SourceImpl;
import org.jflux.impl.services.rk.osgi.OSGiUtils;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.player.AnimationPlayer;
import org.mechio.api.animation.utils.AnimationUtils;
import org.osgi.framework.BundleContext;
import org.rwshop.nb.common.cookies.PlayCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class PlayAnimationCookie
		extends SourceImpl<AnimationEditor> implements PlayCookie {
	private static final Logger theLogger = LoggerFactory.getLogger(PlayAnimationCookie.class);

	public PlayAnimationCookie(AnimationEditor controller) {
		super(controller);
	}

	@Override
	public void play() {
		BundleContext context =
				OSGiUtils.getBundleContext(AnimationPlayer.class);
		if (context == null) {
			theLogger.error("Unable to find BundleContext for AnimationPlayer");
			return;
		}
		AnimationUtils.playAnimation(
				context, null, getValue().getEnabledAnimation());
	}
}
