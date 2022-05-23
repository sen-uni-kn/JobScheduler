/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

public class RunContextSimple extends RunContextAbstract
{
	protected EventHandler eventHandler = null;
	ResourceFolder folder;

	public RunContextSimple(EventHandler father, String name, String folder)
	{
		this(father, name, new ResourceFolder(folder));
	}

	public RunContextSimple(EventHandler father, String name, ResourceFolder folder)
	{
		setName(name);
		this.eventHandler = new EventHandlerAbstract(father, JobAbstract.EVENTHANDLER);
		addChild(this.eventHandler);
		this.folder = folder;
	}

	public void setFolder(ResourceFolder folder)
	{
		this.folder = folder;
	}

	@Override
	public ResourceFolder getFolder()
	{
		return folder;
	}

	@Override
	public EventHandler getEventHandler()
	{
		return eventHandler;
	}

	@Override
	public ResourceType getType()
	{
		return ResourceType.RUN;
	}

	public static RunContextSimple createDummy(String name)
	{
		return new RunContextSimple(null, name, "result");
	}
}
