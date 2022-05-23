/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.EventLogger;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.resource.ResourceImpl;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

public class EventHandlerAbstract extends ResourceImpl implements EventHandler
{
	String name = null;
	EventHandler father = null;
	List<EventLogger> logList = new ArrayList<>();

	public EventHandlerAbstract(EventHandler father, String name)
	{
		this.name = name;
		setFather(father);
		if ((name != null) && (name.isEmpty()))
		{
			// todo: log to any logger
			System.out.println("Error: EventHandler with empty name");
			this.name = "UNKOWN";
		}
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public EventHandler getFather()
	{
		return father;
	}

	@Override
	public String getPath()
	{
		String path = name;
		EventHandler f = father;
		while (f != null)
		{
			path = f.getName() + "/" + path;
			f = f.getFather();
		}
		return path;
	}

	@Override
	public boolean isEvent(JobEvent event)
	{
		for (EventLogger logger : logList)
			if (logger.isEvent(event))
				return true;
		return false;
	}

	@Override
	public boolean logEvent(JobEvent event)
	{
		boolean ret = false;
		for (EventLogger logger : logList)
			ret |= logger.logEvent(this, event);
		return ret;
	}

	@Override
	public JobEvent requestMsg(EventLogger logger, String name)
	{
		return null;
	}

	@Override
	public void subscribe(EventLogger logger)
	{
		if (logList.contains(logger))
			return;
		logList.add(logger);
	}

	@Override
	public void unsubscribe(EventLogger logger)
	{
		while (logList.remove(logger))
			;
	}

	@Override
	public List<EventLogger> getChildLogList()
	{
		return logList;
	}

	@Override
	public void setFather(EventHandler father)
	{
		if (father == null)
			return;
		this.father = father;
		for (EventLogger log : father.getChildLogList())
		{
			subscribe(log);
		}
	}

	@Override
	public void savePersistent()
	{
		for (EventLogger logger : logList)
			logger.savePersistent();
	}

	@Override
	public ResourceType getType()
	{
		return ResourceType.EVENT_HANDLER;
	}
}
