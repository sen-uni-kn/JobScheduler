/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.model;

import kn.uni.sen.jobscheduler.common.model.ResourceInterface;

public interface JobSession
{
	String getUser();

	String getGroup();

	JobRun createRun(ResourceInterface graph);
}
