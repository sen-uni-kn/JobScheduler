/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.job_logsystem;

import java.util.LinkedList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.resource.ResourceFile;
import kn.uni.sen.jobscheduler.job_logsystem.model.SystemResource;

public class Logger_SystemLoad //implements Logger_JobEvent
{
	List<SystemResource> SystemElementsList;
	ResourceFile File;

	public Logger_SystemLoad()
	{
		File = new ResourceFile("LogResource.xmi");
		SystemElementsList = new LinkedList<SystemResource>();
	}

	public void setLogFile(String fileName)
	{
		if (fileName == null)
			return;
		File = new ResourceFile("LogResource.xmi");
	}

	public String getLogFile()
	{
		return File.getFilePath();
	}

	public void checkLogData()
	{
		// TODO: write log at big drift of resource
		// for each resource check dirft

		// TODO: write log at special timing
		// Logger.LogData();
	}

	public void logData()
	{
	}

	public void addElement(SystemResource logElement)
	{
		if (logElement == null)
			return;
		SystemElementsList.add(logElement);
	}
}
