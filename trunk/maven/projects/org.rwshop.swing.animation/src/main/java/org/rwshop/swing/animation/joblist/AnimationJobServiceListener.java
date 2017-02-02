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

package org.rwshop.swing.animation.joblist;

import org.jflux.api.common.rk.utils.TimeUtils;
import org.mechio.api.animation.player.AnimationJob;
import org.mechio.api.animation.player.AnimationPlayer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public class AnimationJobServiceListener implements ServiceListener {
	private static final Logger theLogger = LoggerFactory.getLogger(AnimationJobServiceListener.class);
	private AnimationJobListPanel myList;
	private BundleContext myContext;
	private Map<AnimationJob, ServiceReference> myReferenceMap;

	/**
	 *
	 * @param context
	 * @param list
	 */
	public AnimationJobServiceListener(BundleContext context, AnimationJobListPanel list) {
		myContext = context;
		myList = list;
		myReferenceMap = new HashMap();
		if (myContext != null) {
			String filter = "(" + Constants.OBJECTCLASS + "=" + AnimationJob.class.getName() + ")";
			try {
				myContext.addServiceListener(this, filter);
			} catch (InvalidSyntaxException ex) {
				theLogger.warn("Could not register ServiceListener.  Invalid filter syntax.", ex);
			}
		}
		if (myList == null || myContext == null) {
			return;
		}
		try {
			ServiceReference[] refs = context.getServiceReferences(AnimationJob.class.getName(), null);
			if (refs == null || refs.length == 0) {
				return;
			}
			List<AnimationJob> jobs = new ArrayList(refs.length);
			for (ServiceReference se : refs) {
				AnimationJob job = (AnimationJob) context.getService(se);
				jobs.add(job);
				myReferenceMap.put(job, se);
			}
			myList.addAnimationJobs(jobs);
		} catch (InvalidSyntaxException ex) {
			theLogger.error("There was an error fetching service references.", ex);
		}
	}

	@Override
	public void serviceChanged(ServiceEvent se) {
		switch (se.getType()) {
			case ServiceEvent.REGISTERED:
				addService(se);
				break;
			case ServiceEvent.MODIFIED_ENDMATCH:
			case ServiceEvent.UNREGISTERING:
				removeService(se);
				break;
		}
	}

	private void addService(ServiceEvent se) {
		AnimationJob job = getService(se);
		if (job == null) {
			return;
		}
		myList.addAnimationJob(job);
		myReferenceMap.put(job, se.getServiceReference());
	}

	private void removeService(ServiceEvent se) {
		AnimationJob job = getService(se);
		myReferenceMap.remove(job);
		myList.removeAnimationJob(job);
	}

	private AnimationJob getService(ServiceEvent se) {
		ServiceReference ref = se.getServiceReference();
		if (ref == null || myContext == null) {
			return null;
		}
		Object obj = myContext.getService(ref);
		if (!(obj instanceof AnimationJob)) {
			return null;
		}
		AnimationJob job = (AnimationJob) obj;
		return job;
	}

	/**
	 *
	 * @param job
	 */
	public void unregisterService(AnimationJob job) {
		if (job == null) {
			return;
		}
		job.stop(TimeUtils.now());
		AnimationPlayer source = job.getSource();
		if (source == null) {
			return;
		}
		source.removeAnimationJob(job);
	}

}
