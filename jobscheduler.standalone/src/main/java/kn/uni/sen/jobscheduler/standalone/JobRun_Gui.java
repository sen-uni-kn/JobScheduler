/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import kn.uni.sen.jobscheduler.common.impl.EventHandlerAbstract;
import kn.uni.sen.jobscheduler.common.impl.LoggerFile;
import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.core.impl.JobGraph_File;
import kn.uni.sen.jobscheduler.core.impl.JobScheduler_Easy;
import kn.uni.sen.jobscheduler.core.impl.JobRunAbstract;
import kn.uni.sen.jobscheduler.core.model.JobGraph;
import kn.uni.sen.jobscheduler.core.model.JobScheduler;
import kn.uni.sen.jobscheduler.core.model.JobRun;

public class JobRun_Gui extends JobRunAbstract implements JobRun
{
	public JobRun_Gui(Integer id, EventHandler handler, ResourceFolder folder)
	{
		super(id, handler, folder);
	}

	@Override
	protected JobScheduler createScheduler(ResourceFileXml jobFile, ResourceFolder folder, String schedulerType)
	{
		String logFile = folder.getData() + "/";
		JobGraph jobGraph = new JobGraph_File("", jobFile);
		jobGraph.load();

		EventHandler handler = new EventHandlerAbstract(null, "Session");
		handler.subscribe(new LoggerFile(logFile));
		return new JobScheduler_Easy(jobGraph, handler, folder);
	}
}
