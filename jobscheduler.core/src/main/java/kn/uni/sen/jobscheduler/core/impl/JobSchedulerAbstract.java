/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kn.uni.sen.jobscheduler.common.impl.JobAbstract;
import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.JobLibrary;
import kn.uni.sen.jobscheduler.common.model.JobResult;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.model.RunContext;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.common.resource.ResourceTag;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;
import kn.uni.sen.jobscheduler.core.model.JobControl;
import kn.uni.sen.jobscheduler.core.model.JobDescription;
import kn.uni.sen.jobscheduler.core.model.JobGraph;
import kn.uni.sen.jobscheduler.core.model.JobScheduler;

/**
 * First functions are implemented Each JobScheduler will need this attribute
 * and implemented functions
 */
public abstract class JobSchedulerAbstract extends JobContainer implements JobScheduler
{
	// list of libraries to search jobs in
	List<JobLibrary> libraryList = new LinkedList<JobLibrary>();

	boolean simulate = false;

	JobGraph jobGraph;
	// tree with jobs and their controlling class
	JobControl jobTree;
	// actual list of active jobs
	List<JobControl> jobActiveList = new LinkedList<JobControl>();

	class StoreRefList
	{
		public List<ResourceAbstract> list;
		public ResourceAbstract ref;

		public StoreRefList(List<ResourceAbstract> list, ResourceAbstract ref)
		{
			this.list = list;
			this.ref = ref;
		}
	}

	// set if a job notify this scheduler that a job has ended
	boolean JobNotification = false;

	ResourceDescription jobGraphDescr = new ResourceDescription("JobGraph", ResourceType.JOB);

	public JobSchedulerAbstract(JobGraph graph, EventHandler father, ResourceFolder folder)
	{
		this.version = "3.0.0";

		setJobState(JobState.START);
		this.jobGraph = graph;
		setJobState(JobState.IDLE);
		setResource(JobAbstract.FOLDER, folder);

		// jobGraphDescr.addTag(ResourceTag.NECESSARY);
		addInputDescription(jobGraphDescr);
		jobGraphDescr.setOwner(this);
	}

	public JobState getState()
	{
		return JobState.UNDEFINED;
	}

	public JobState simulate()
	{
		simulate = true;
		// todo: implement simulation;
		// return start();
		return end(JobResult.OK);
	}

	boolean checkActiveList()
	{
		for (JobControl control : jobActiveList)
			// job is no more active
			if (control.checkActive())
				return false;
		return true;
	}

	@Override
	public boolean check(boolean val)
	{
		boolean ret = super.check(val);
		if (ret == false)
			return ret;

		// todo: extend check
		if (jobGraph == null)
			return false;
		return true;
	}

	@Override
	public JobState task()
	{
		// reset all virtually calculated jobs
		reset();
		// create all jobs
		jobTree = createJobTree(jobGraph, this, true);
		connectResources(jobTree);
		if (jobTree == null)
		{
			setJobResult(JobResult.ERROR);
			return endLogger();
		}

		// start all jobs that are possible to start
		startJob(jobTree);

		// Scheduler goto sleep an wait until notification or timeout
		boolean bRun = true;
		while (bRun)
		{
			synchronized (this)
			{
				try
				{
					wait(3000);
					if (JobNotification != false)
					{
						JobNotification = false;
						startJob(jobTree);
					} else if (!checkActiveList())
						bRun = false;
					// check if all jobs are still running
					else if (jobActiveList.isEmpty())
					{
						// no jobs are running
						if (!!!startJob(jobTree))
						{
							// and no jobs can be start -> end
							bRun = false;
						}
					}

				} catch (InterruptedException e)
				{
					logError("Error during scheduling.");
				}
			}
			for (JobControl control : jobActiveList)
				control.sendCommand(JobState.FINISHED);
		}
		return endLogger();
	}

	private void connectResources(JobControl jobTree)
	{
		List<JobControl> controlList = jobTree.getControlList();
		for (JobControl control : controlList)
		{
			connectResources(control);

			Job job = control.getJob();
			ResourceInterface d = job.getInputDescription();
			while (d != null)
			{
				if ((d instanceof ResourceDescription))
				{
					ResourceDescription descr = (ResourceDescription) d;
					if (descr.getOwner() == null)
					{
						String resOwner = descr.getOwnerName();
						if ((resOwner != null) && !!!resOwner.isEmpty())
						{
							JobControl conOwner = jobTree.getControlByName(resOwner);
							if (conOwner != null)
								descr.setOwner(conOwner.getJob());
						}
						if (descr.getOwner() == null)
							// still no owner -> father container is owner
							descr.setOwner(jobTree.getJob());
					}
				}
				d = d.getNext();
			}
		}
	}

	protected JobState endLogger()
	{
		setJobState(JobState.ENDING);
		getEventHandler().savePersistent();
		return JobState.ENDING;
	}

	/**
	 * Correct sequence of starting the job of the jobGraph is the work of a
	 * scheduler.
	 * 
	 * @param control
	 * @return true if Job of control was started
	 */
	protected abstract boolean startJob(JobControl control);

	protected void reset()
	{
		if (jobTree == null)
			return;
		jobTree = null;
		JobNotification = false;
		// todo: reset maybe easier if simple all resourcer owners are reseted
	}

	public boolean sendCommand(JobState request)
	{
		switch (request)
		{
		case FINISHED:
		case ENDING:
			// TODO: end job
			break;
		case PAUSE:
			// TODO: goto pause
			break;

		case RESUMING:
			if (getState() != JobState.PAUSE)
				return false;
			// TODO: goto resume
			break;

		case IDLE:
			if (getState() != JobState.RUNNING)
				return false;
			// TODO: goto IDLE
			break;

		case CHECKING:
			if (getState() != JobState.IDLE)
				return false;
			setJobState(JobState.CHECKING);
			if (check())
				setJobState(JobState.START);
			break;

		case START:
		case RUNNING:
			start();
			break;

		case RESET:
			// TODO: reset Job to start
			break;
		case UNDEFINED:
		default:
			return false;
		}
		return true;
	}

	public void addLibrary(JobLibrary library)
	{
		if (library == null)
			return;

		libraryList.add(library);
	}

	/**
	 * check all ids of jobs and resources are unique
	 * 
	 * @param job
	 */
	protected boolean checkResourceUnique(Map<String, ResourceDescription> resourceList, JobDescription job)
	{
		boolean bGood = true;
		if (resourceList == null)
			resourceList = new HashMap<>();

		ResourceInterface d = job.getInputList();
		while (d != null)
		{
			if (d instanceof ResourceDescription)
				bGood &= checkResourceUnique(resourceList, (ResourceDescription) d);
			d = d.getNext();
		}

		resourceList.clear();
		d = job.getResultList();
		while (d != null)
		{
			if (d instanceof ResourceDescription)
				bGood &= checkResourceUnique(resourceList, (ResourceDescription) d);
			d = d.getNext();
		}

		resourceList.clear();
		d = job.getJobList();
		while (d != null)
		{
			if (d instanceof JobDescription)
				bGood &= checkResourceUnique(resourceList, (JobDescription) d);
			d = d.getNext();
		}
		return bGood;
	}

	/**
	 * Checks if JobDescription is consistent. Means if all resource are unique.
	 * Resource from other job are check by list.
	 * 
	 * @return true if consistent
	 */
	public boolean checkResourceUnique(Map<String, ResourceDescription> resourceList, ResourceDescription res)
	{
		if ((res.getName() == null) || (res.getName().isEmpty()))
			return true;
		if (!!!resourceList.containsKey(res.getName()))
		{
			resourceList.put(res.getName(), res);
			return true;
		}
		logEventStatus(JobEvent.WARNING, "ResourceIdNotUnique",
				"Resource " + res.getName() + " was found a second time");
		return false;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	synchronized public void notify(ResourceInterface job)
	{
		if (jobActiveList.remove(job))
		{
			JobNotification = true;
			notify();
		}
	}

	/**
	 * check if all necessary input variables are present - for all missing
	 * input parameters without tag send warning
	 * 
	 * @param job
	 * @throws Exception
	 */
	protected JobControl createJobTree(JobDescription description, RunContext father, boolean root)
	{
		// create job and resources that are no references
		Job job = createJob(description, father);
		if (job == null)
		{
			logError("Job not found: " + description.getName() + ":" + description.getJobName());
			setJobResult(JobResult.ERROR);
			return null;
		}

		// todo: String jobName = description.getName().isEmpty() ?
		// description.getJobName() : description.getName();
		// child logger event for new Job
		// String eventName = "JobEvent" + (jobName != null &&
		// !!!jobName.isEmpty() ? "_" + jobName : "");
		// job.setName(eventName);

		JobControl control = new JobThread(this, description, job, getEventHandler());
		// control.SetParameters(InputList, ResultList);

		// create child jobs
		ResourceInterface j = description.getJobList();
		while (j != null)
		{
			if (j instanceof JobDescription)
				control.addJob(createJobTree((JobDescription) j, job, false));
			j = j.getNext();
		}
		return control;
	}

	/**
	 * Create new job after description and set all input variables.
	 * 
	 * @param description
	 *            of job
	 * @return created job
	 */
	protected Job createJob(JobDescription description, RunContext father)
	{
		Job job = null;
		if (description instanceof JobGraphAbstract)
			job = new JobContainer();
		else if (description.getJobName().isEmpty())
			job = new JobContainer();
		else
			for (JobLibrary lib : libraryList)
			{
				String jobName = description.getJobName();
				Job job2 = lib.createJob(jobName, description.getJobVersion(), father);
				if (job == null)
					job = job2;
				else if (description.getJobVersion().compareTo("") == 0)
					logMessage(JobEvent.WARNING, "Found two possible jobs " + jobName);
				else if (job2.getJobVersion().compareTo(job.getJobVersion()) >= 0)
					job = job2;
			}
		if (job == null)
			return job;

		ResourceInterface d = job.getResultDescription();
		while (d != null)
		{
			if (d instanceof ResourceDescription)
				((ResourceDescription) d).setOwner(job);
			d = d.getNext();
		}

		// todo: necessary to add input from scheduler to some jobs?
		return job;
	}

	/**
	 * check if all necessary results are available - for all missing result
	 * parameters without tag send warning
	 * 
	 * @return
	 */
	// check if all necessary results are calculated
	protected boolean isResourceAvailable(Map<String, List<ResourceAbstract>> resList)
	{
		boolean bGood = true;
		for (Entry<String, List<ResourceAbstract>> entry : resList.entrySet())
			for (ResourceAbstract res : entry.getValue())
				if (res.isAvailable())
					continue;
				else if (res.hasTag(ResourceTag.IGNORE) || res.hasTag(ResourceTag.POSSIBLE))
					continue;
				else if (res.hasTag(ResourceTag.NECESSARY))
				{
					logError("Resource is missing " + res.getName() + " " + entry.getValue());
					bGood = false;
				} else if (!res.hasTag(ResourceTag.IGNORE))
					logMessage(JobEvent.WARNING, "Resource is missing " + res.getName() + " " + entry.getValue());
		return bGood;
	}
}
