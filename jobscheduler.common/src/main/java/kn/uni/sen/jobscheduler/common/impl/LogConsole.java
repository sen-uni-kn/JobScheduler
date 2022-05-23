/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import kn.uni.sen.jobscheduler.common.impl.LogConsole;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.EventHandler;

/**
 * JobLogger that output all messages to the console.
 * 
 * @author Martin Koelbl
 */
public class LogConsole extends EventLoggerAbstract
{
	public LogConsole()
	{
		this(2);
	}

	public LogConsole(int verbose)
	{
		super(verbose);
		addFilter(filterVer);
		filterVer.setVerbose(verbose);
	}

	@Override
	public boolean logEvent(EventHandler handler, JobEvent event)
	{
		if (!!!isEvent(event))
			return false;;
		String prefix = getPrefix(handler);
		System.out.println(prefix + event.getText());
		return true;
	}

	public String getPrefix(EventHandler handler)
	{
		String path = handler.getPath();
		if (path == null)
			return "";
		int c = path.split("/").length - 1;
		String pre = "";
		for (int i = 0; i < c; i++)
			pre += "  ";
		return pre;
	}
}
