/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.model.EventFilter;
import kn.uni.sen.jobscheduler.common.model.EventLogger;
import kn.uni.sen.jobscheduler.common.model.JobEvent;

public abstract class EventLoggerAbstract implements EventLogger
{
	List<EventFilter> filterList = new ArrayList<>();
	FilterVerbose filterVer = null;

	public EventLoggerAbstract()
	{
		filterVer = new FilterVerbose();
		addFilter(filterVer);
	}

	public EventLoggerAbstract(int verbose)
	{
		filterVer = new FilterVerbose(verbose);
		addFilter(filterVer);
	}

	public class FilterVerbose implements EventFilter
	{
		int verbose = 2;

		public FilterVerbose()
		{
		}

		public FilterVerbose(int ver)
		{
			verbose = ver;
		}

		@Override
		public int isEvent(JobEvent event)
		{
			if ((verbose >= 1) && (event.hasTag(JobEvent.ERROR) || event.hasTag(JobEvent.WARNING)))
				return 1;
			if ((verbose >= 2) && (event.hasTag(JobEvent.INFO)))
				return 1;
			if ((verbose >= 3) && (event.hasTag(JobEvent.DEBUG)))
				return 1;
			return 0;
		}

		@Override
		public int isNotEvent(JobEvent event)
		{
			return 1;
		}

		public void setVerbose(int verbose)
		{
			this.verbose = verbose;
		}
	}

	@Override
	public void addFilter(EventFilter eventFilter)
	{
		filterList.add(eventFilter);
	}

	public List<EventFilter> getFilterList()
	{
		return filterList;
	}

	public void removeFilter(EventFilter filter)
	{
		filterList.remove(filter);
	}

	@Override
	public boolean isEvent(JobEvent event)
	{
		int yes = 0;
		int no = 0;
		for (EventFilter filter : filterList)
		{
			int y = filter.isEvent(event);
			if (yes < y)
				yes = y;
			int n = filter.isNotEvent(event);
			if (no < n)
				no = n;
		}
		return yes >= no;
	}

	public void setVerbose(int verbose)
	{
		if (filterVer == null)
			return;
		filterVer.setVerbose(verbose);
	}

	@Override
	public void savePersistent()
	{
	}
}
