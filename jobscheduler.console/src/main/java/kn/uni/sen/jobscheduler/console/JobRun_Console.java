/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.console;

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

public class JobRun_Console extends JobRunAbstract
{
	// hack is used to try this implementation
	JobGraph Graph;

	public JobRun_Console(Integer id, EventHandler father, ResourceFolder folder)
	{
		super(id, father, folder);
	}

	@Override
	protected JobScheduler createScheduler(ResourceFileXml jobFile, ResourceFolder folder, String schedulerType)
	{
		if (Graph == null)
		{
			Graph = new JobGraph_File("", jobFile);
			Graph.load();
		}

		return new JobScheduler_Easy(Graph, createHandler(folder), folder);
	}

	protected JobScheduler createScheduler(JobGraph jobGraph, ResourceFolder folder, String schedulerType)
	{
		return new JobScheduler_Easy(jobGraph, createHandler(folder), folder);
	}

	EventHandler createHandler(ResourceFolder folder)
	{
		String logFile = folder.getData() + "/" + "tmp.log";
		LoggerFile log = new LoggerFile(logFile);
		EventHandlerAbstract handler = new EventHandlerAbstract(null, "session");
		handler.subscribe(log);
		return handler;
	}

	protected void setJobGraph(JobGraph graph)
	{
		this.Graph = graph;
	}
}
