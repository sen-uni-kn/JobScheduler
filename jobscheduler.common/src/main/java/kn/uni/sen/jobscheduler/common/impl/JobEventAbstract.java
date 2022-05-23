/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.impl.JobEventAbstract;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;

/**
 * JobEvent are events that happens during a JobScheduler is running JobEvent
 * can happen inside of the JobScheduler itself or inside of Job and
 * JobLibraries
 */
public class JobEventAbstract implements JobEvent
{
	List<String> tagList = null;
	List<ResourceInterface> dataList = null;
	String eventText = "";

	public JobEventAbstract(String text)
	{
		eventText = text;
	}

	@Override
	public void addTag(String tag)
	{
		if (tag == null)
			return;
		if (tagList == null)
			tagList = new ArrayList<>();
		tagList.add(tag);
	}

	@Override
	public List<String> getTagList()
	{
		return tagList;
	}

	@Override
	public boolean hasTag(String tag)
	{
		if (tagList == null)
			return false;
		for (String t : tagList)
			if (t.equals(tag))
				return true;
		return false;
	}

	@Override
	public String getText()
	{
		return eventText;
	}

	@Override
	public void addData(ResourceInterface res)
	{
		if (dataList == null)
			dataList = new ArrayList<>();
		dataList.add(res);
	}

	@Override
	public List<ResourceInterface> getData()
	{
		if (dataList == null)
			return dummyList;
		return dataList;
	}

	static List<ResourceInterface> dummyList = new ArrayList<>();
}
