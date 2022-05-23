/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.resource.ResourceFile;

public class LoggerFile extends EventLoggerAbstract
{
	String file = null;

	public LoggerFile(String file)
	{
		this(file, 1);
	}

	public LoggerFile(String file, int verbose)
	{
		this.setVerbose(verbose);
		this.file = file;
	}

	@Override
	public boolean logEvent(EventHandler handler, JobEvent event)
	{
		String text = event.getText();
		return ResourceFile.appendText2File(file, text);
	}

	@Override
	public void savePersistent()
	{
	}
}
