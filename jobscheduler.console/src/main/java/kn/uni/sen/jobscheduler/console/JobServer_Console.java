/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.console;

import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.core.impl.JobServerAbstract;
import kn.uni.sen.jobscheduler.core.model.JobRun;
import kn.uni.sen.jobscheduler.core.model.JobSession;

public class JobServer_Console extends JobServerAbstract
{
	public JobServer_Console(String name, String folder)
	{
		super(name, folder);
	}

	@Override
	protected JobSession createSessionInstance(String user)
	{
		return null;
	}

	@Override
	protected JobRun createJobRun(Integer id)
	{
		ResourceFolder fol = createSessionFolder("result");
		return new JobRun_Console(id, this.getEventHandler(), fol);
	}

}
