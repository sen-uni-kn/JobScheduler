/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.job_logsystem;

import kn.uni.sen.jobscheduler.job_logsystem.model.SystemResource;

public class SystemResource_Processor implements SystemResource
{

	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}

	@Override
	public String checkLogData()
	{
		return null;
	}

	@Override
	public String logData()
	{
		return null;
	}

}
