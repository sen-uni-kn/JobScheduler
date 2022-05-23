/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import java.util.LinkedList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.impl.RunContextSimple;
import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.JobLibrary;
import kn.uni.sen.jobscheduler.common.model.JobResult;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.common.resource.ResourceFile;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.core.model.JobScheduler;
import kn.uni.sen.jobscheduler.core.model.JobRun;

public abstract class JobRunAbstract extends RunContextSimple implements JobRun
{
	List<JobLibrary> jobLibraryList = new LinkedList<>();

	/**
	 * Unique ID of the run
	 */
	Integer runID;

	/**
	 * JobScheduler to run jobgraph
	 */
	protected JobScheduler jobScheduler;

	/**
	 * folder of current instance of JobScheduler
	 */
	ResourceFileXml jobFile;

	public JobRunAbstract(Integer id, EventHandler father, ResourceFolder folder)
	{
		super(father, "" + id, folder);
		this.runID = id;
	}

	@Override
	public void addLibrary(JobLibrary library)
	{
		jobLibraryList.add(library);
	}

	@Override
	public JobState start(String executionType)
	{
		if (jobFile == null)
		{
			System.out.println("Missing JobGraph");
		}

		// copy all resource data to server
		// JobGraph file to folder
		if ((jobFile == null) || (getFolder() == null))
			return JobState.FINISHED;

		if (getFolder().getData().compareTo(".") != 0)
			ResourceFile.copyFile(jobFile.getData(), getFolder().getData() + "/" + jobFile.getFileName());

		// create and start jobScheduler
		jobScheduler = createScheduler(jobFile, getFolder(), JobScheduler_Easy.TYPE);
		for (JobLibrary library : jobLibraryList)
			jobScheduler.addLibrary(library);

		// if ((executionType != null) && (executionType.compareTo("simulate")
		// == 0))
		// return jobScheduler.simulate();

		return jobScheduler.start();
	}

	abstract protected JobScheduler createScheduler(ResourceFileXml jobFile, ResourceFolder folder,
			String schedulerType);

	protected String getProjectName()
	{
		if (jobFile == null)
			return "";
		return jobFile.getFileName().replace(".xmi", "");
	}

	@Override
	public void stop()
	{
		jobScheduler.sendCommand(JobState.ENDING);
		// jobServer.deleteSession(this);
	}

	@Override
	public List<ResourceAbstract> getResultList()
	{
		if (jobScheduler != null)
		{
			List<ResourceAbstract> list = new LinkedList<>();
			ResourceInterface d = jobScheduler.getResultDescription();
			while (d != null)
			{
				if (d instanceof ResourceDescription)
				{
					ResourceInterface res = jobScheduler.getResource(d.getName(), true);
					if (res == null)
						continue;
					if (res instanceof ResourceAbstract)
						list.add((ResourceAbstract) res);
					// System.out.println(res.getData());
				}
				d = d.getNext();
			}
			return list;
		}
		return new LinkedList<>();
	}

	@Override
	public JobState getStatus()
	{
		if (jobScheduler != null)
			return jobScheduler.getJobState();
		return JobState.UNDEFINED;
	}

	@Override
	public JobResult getResult()
	{
		if (jobScheduler != null)
			return jobScheduler.getResult();
		return JobResult.UNDEFINED;
	}

	@Override
	public Integer getRunID()
	{
		return runID;
	}

	public void setJobGraph(ResourceFileXml file)
	{
		jobFile = file;
	}
}
