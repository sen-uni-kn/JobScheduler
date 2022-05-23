/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.job_logsystem;

import kn.uni.sen.jobscheduler.job_logsystem.model.SystemResource;

/**
 * idea: show use of resources of computer (ram, cpu)
 */
public class SystemResourceAbstract implements SystemResource
{	
	public String checkSaveData()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String checkLogData()
	{
		//TODO: use some timing/conditions
		//if(false)
		//	return null;
		return logData();
	}

	public String logData()
	{
		return "";
	}
}
