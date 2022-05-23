/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.RunContext;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.common.resource.ResourceImpl;

public abstract class RunContextAbstract extends ResourceImpl implements RunContext
{
	@Override
	public String getFolderText()
	{
		ResourceFolder folder = getFolder();
		return folder.getData();
	}

	@Override
	public JobEvent logEventStatus(String type, String text)
	{
		EventHandler eH = getEventHandler();
		if (eH == null)
			return null;
		JobEvent event = new JobEventStatus(type, text);
		return eH.logEvent(event) ? event : null;
	}

	@Override
	public JobEvent logEventStatus(String type, String name, String text)
	{
		EventHandler eH = getEventHandler();
		if (eH == null)
			return null;
		JobEvent event = new JobEventStatus(type, name, text);
		return eH.logEvent(event) ? event : null;
	}
}
