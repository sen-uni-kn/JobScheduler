/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.job_logsystem;

import kn.uni.sen.jobscheduler.common.impl.JobAbstract;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

public class Job_LogSystemLoad extends JobAbstract
{
	Logger_SystemLoad Logger;

	ResourceDescription descrLog = new ResourceDescription("LogFile", ResourceType.FILE);

	public Job_LogSystemLoad()
	{
		this.version = "2.0.0";
		Logger = new Logger_SystemLoad();

		// log memory use of program and log memory use whole system
		Logger.addElement(new SystemResource_Memory());
		// log use of processor use and log each processor of whole system
		Logger.addElement(new SystemResource_Processor());

		addInputDescription(descrLog);
	}

	@Override
	public JobState task()
	{
		// let thread sleep and but check log regularly
		Logger.checkLogData();
		return getJobState();
	}

	@Override
	public ResourceInterface getResource(String name, boolean out)
	{
		return null;
	}
}
