/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import java.time.Instant;

import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.EventLogger;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.JobResult;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.model.RunContext;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceDate;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.common.resource.ResourceEnum;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.common.resource.ResourceList;
import kn.uni.sen.jobscheduler.common.resource.ResourceString;
import kn.uni.sen.jobscheduler.common.resource.ResourceTag;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

/**
 * First abstract implementation of interface job like an instance of job is
 * normally used.
 */
public abstract class JobAbstract extends RunContextAbstract implements Job
{
	protected String version = null;

	// actual state of the job
	JobState jobState = JobState.UNDEFINED;

	// command is an request for the job to go to JobState
	JobState command;

	// change this number regularly to show that job is still active
	int watchdogNumber;

	// result of an job
	// will be set at return
	private JobResult jobResult = null;

	EventHandler eventHandler = null;

	ResourceFolder folder;

	protected Instant timeStart = null;
	protected Instant timeEnd = null;

	public static final String INPUTDESCRIPTION = "InputDescription";
	public static final String RESULTDESCRIPTION = "ResultDescription";
	public static final String EVENTHANDLER = "EventHandler";
	public static final String FOLDER = "Folder";
	public static final String JOBVERSION = "JobVersion";
	public static final String JOBNAME = "JobName";
	public static final String JOBSTATE = "JobState";
	public static final String JOBRESULT = "JobResult";
	public static final String STARTTIME = "StartTime";
	public static final String STOPTIME = "StopTime";

	/**
	 * List with description of all input resources that is used to start the
	 * job.
	 */
	ResourceInterface inputDescription = new ResourceList(INPUTDESCRIPTION, ResourceType.DESCRIPTION);

	/**
	 * List with description of all result resources that are returned by the
	 * job.
	 */
	ResourceInterface resultDescription = new ResourceList(RESULTDESCRIPTION, ResourceType.DESCRIPTION);

	{
		addChild(inputDescription);
		addChild(resultDescription);

		ResourceDescription folderDescr = createInputDescr(FOLDER, ResourceType.FOLDER);
		folderDescr.addTag(ResourceTag.NECESSARY);

		String val = ResourceFolder.getUniqueFolderDateTime("result" + ResourceFolder.getSplitSign() + "result");
		folderDescr.setDefault(new ResourceFolder(val));

		createResultDescr(JOBVERSION, ResourceType.STRING);
		createResultDescr(JOBSTATE, ResourceType.ENUM);
		createResultDescr(JOBRESULT, ResourceType.ENUM);
		createResultDescr(STARTTIME, ResourceType.INTEGER);
		createResultDescr(STOPTIME, ResourceType.INTEGER);
	}

	public JobAbstract(RunContext parent)
	{
		setParent(parent);
		setName(getJobName());
		command = JobState.UNDEFINED;
		jobState = JobState.START;

		addChild(getEventHandler());
	}

	public JobAbstract()
	{
		this(null);
	}

	@Override
	public ResourceFolder getFolder()
	{
		return getResourceWithType(FOLDER, false);
	}

	@Override
	public boolean setResource(String name, ResourceInterface res)
	{
		return false;
	}

	protected ResourceDescription createInputDescr(String name, ResourceType type)
	{
		ResourceDescription descr = new ResourceDescription(name, type);
		inputDescription.addChild(descr);
		return descr;
	}

	protected ResourceDescription createResultDescr(String name, ResourceType type)
	{
		ResourceDescription descr = new ResourceDescription(name, type);
		descr.setResult(true);
		descr.setOwner(this);
		resultDescription.addChild(descr);
		return descr;
	}

	/**
	 * Return results and variables during a job run
	 */
	protected ResourceInterface getResultResource(String name)
	{
		if (FOLDER.equals(name))
			return folder;
		else if (JOBNAME.equals(name))
		{
			return new ResourceString(getJobName());
		} else if (JOBVERSION.equals(name))
		{
			return new ResourceString(version);
		} else if (JOBRESULT.equals(name) && (jobResult != null))
		{
			return new ResourceEnum(jobResult);
		} else if (JOBSTATE.equals(name) && (jobState != null))
		{
			return new ResourceEnum(jobState);
		} else if (STARTTIME.equals(name) && (timeStart != null))
		{
			ResourceDate date = new ResourceDate();
			date.setData(timeStart.toString());
			date.setName(STARTTIME);
			return date;
		} else if (STOPTIME.equals(name) && (timeEnd != null))
		{
			ResourceDate date = new ResourceDate();
			date.setData(timeEnd.toString());
			date.setName(STOPTIME);
			return date;
		}
		return null;
	}

	public String getName()
	{
		return this.getJobName().replace("Job_", "");
	}

	@Override
	public String getJobName()
	{
		return getClass().getSimpleName();
	}

	/**
	 * @return String with static version of Job
	 */
	public String getJobVersion()
	{
		return "JV_" + version;
	}

	public JobResult getResult()
	{
		return jobResult;
	}

	@Override
	public EventHandler getEventHandler()
	{
		if (eventHandler != null)
			return eventHandler;
		EventHandler fHand = null;
		if (parent != null)
		{
			ResourceInterface res = parent.getChildByName(JobAbstract.EVENTHANDLER);
			if (res instanceof EventHandler)
				fHand = (EventHandler) res;
		}
		this.eventHandler = new EventHandlerAbstract(fHand, JobAbstract.EVENTHANDLER);
		return eventHandler;
	}

	@Override
	public void setEventHandler(EventHandler handler)
	{
		eventHandler = handler;
	}

	public void addLogger(EventLogger logger)
	{
		getEventHandler().subscribe(logger);
	}

	protected void setJobResult(JobResult result)
	{
		this.jobResult = result;
	}

	public void addInputDescription(ResourceDescription descr)
	{
		inputDescription = ResourceAbstract.addList(inputDescription, descr);
	}

	public void addResultDescription(ResourceDescription descr)
	{
		resultDescription = ResourceAbstract.addList(resultDescription, descr);
	}

	public JobEvent logError(String description)
	{
		JobEvent event = logEventStatus(JobEvent.ERROR, description);
		setJobResult(JobResult.ERROR);
		return event;
	}

	protected JobState endError(String description)
	{
		logError(description);
		setJobState(JobState.ENDING);
		return JobState.ENDING;
	}

	public JobEvent logMessage(String tag, String description)
	{
		JobEvent event = logEventStatus(tag, description);
		return event;
	}

	public boolean sendCommand(JobState request)
	{
		// TODO: check if this command is actually okay and the do the command
		// only set the Command if job is doing something and do it next time
		if (command != JobState.UNDEFINED)
			return false;

		command = request;
		return true;
	}

	protected boolean checkResult()
	{
		boolean bGood = true;
		ResourceInterface descr = resultDescription;
		while (descr != null)
		{
			ResourceInterface res = getResultResource(descr.getName());
			if (descr.hasTag(ResourceTag.NECESSARY) && (res == null))
				bGood = false;
			descr = descr.getNext();
		}
		return bGood;
	}

	protected boolean setJobState(JobState state)
	{
		this.jobState = state;
		if (this.jobState == JobState.ENDING)
			return false;
		return true;
	}

	@Override
	public JobState getJobState()
	{
		return jobState;
	}

	@Override
	public ResourceInterface getInputDescription()
	{
		return inputDescription.getChild();
	}

	@Override
	public ResourceDescription getResultDescription()
	{
		ResourceInterface res = resultDescription.getChild();
		if (res instanceof ResourceDescription)
			return (ResourceDescription) res;
		return res.getNextByType();
	}

	private void init()
	{
	}

	@Override
	public JobState start()
	{
		init();
		setJobResult(JobResult.OK);
		if (!!!check(false))
			return end(JobResult.ERROR);
		setJobState(JobState.RUNNING);
		timeStart = Instant.now();

		while (getJobState() != JobState.ENDING)
		{
			JobState state = task();
			if (getJobState() != state)
				setJobState(state);
		}

		if (!!!checkResult())
			setJobResult(JobResult.ERROR);

		setJobState(JobState.FINISHED);
		timeEnd = Instant.now();

		if (parent != null)
			parent.notify(this);

		return getJobState();
	}

	public JobEvent logInfo(String text)
	{
		return logEventStatus(JobEvent.INFO, text);
	}

	protected JobState end(JobResult result)
	{
		if (this.jobResult != JobResult.ERROR)
			setJobResult(result);
		setJobState(JobState.ENDING);
		return getJobState();
	}

	@Override
	public boolean check()
	{
		return check(false);
	}

	public boolean check(boolean logError)
	{
		boolean bGood = true;

		if ((inputDescription == null) || (resultDescription == null))
		{
			if (logError)
				logEventStatus(JobEvent.ERROR, "JobDescription is missing");
			bGood = false;
		} else
		{
			// check if every input has an corresponding resource
			bGood &= checkInputAvailable(true);
		}

		ResourceFolder folderIn = getFolder();
		if ((folderIn == null) || (folderIn.getData() == null))
		{
			bGood = false;
			if (logError)
				logEventStatus(JobEvent.ERROR, "ErrorFolder", "Folder is missing!");
		} else
		{
			ResourceFolder folder = (ResourceFolder) folderIn;
			folder.createFolder();
			if (!!!folder.isFolder())
			{
				bGood = false;
				if (logError)
					logEventStatus(JobEvent.ERROR, "ErrorFolder", "Folder " + folder.getData() + " is not a folder!");
			} else if (!!!folder.exists())
			{
				bGood = false;
				if (logError)
					logEventStatus(JobEvent.ERROR, "ErrorFolder", "Folder " + folder.getData() + " is not a folder!");
			}
		}
		return bGood;
	}

	/**
	 * @param listAll
	 *            all resources needed
	 * @param list
	 *            all resources available
	 * @return true if all resources from listAll are in list
	 */
	protected boolean checkInputAvailable(boolean logError)
	{
		boolean bGood = true;
		ResourceInterface descr = inputDescription.getChild();
		while (descr != null)
		{
			boolean bNecessary = descr.hasTag(ResourceTag.NECESSARY);
			if (bNecessary)
			{
				ResourceInterface r = descr.getChild();
				if (r == null)
				{
					String text = "Warning: ";
					if (bNecessary)
					{
						bGood = false;
						text = "Error: ";
					}
					if (logError)
						logEventStatus(JobEvent.ERROR, text + "ResourceMissing", "Job " + getJobName()
								+ " has missing resource " + descr.getName() + ":" + " of type " + descr.getType());
				}
			}
			descr = descr.getNext();
		}
		return bGood;
	}

	/**
	 * 
	 * @param listAll
	 * @param list
	 * @return
	 */
	protected void setResourceAvailable(ResourceDescription descrList)
	{
		// todo: create each element of result description list
	}

	@Override
	public void getResourceEstimation(ResourceDescription inputParameters, ResourceDescription estimation)
	{
		// idea: create for each element in estimation an estimation value and
		// put it inside input values can be taken into account for better
		// estimations

		// empty resource estimation means don't know
	}

	public ResourceInterface getResource(String name, boolean result)
	{
		if (name == null)
			return null;
		ResourceInterface res = null;
		if (result)
			res = getResultResource(name);
		else
		{
			ResourceInterface descr = inputDescription.getChildByName(name);
			if (descr != null)
				return descr.getChild();
		}
		if (res != null)
			res.setName(name);
		return res;
	}

	@Override
	public <T extends ResourceInterface> T getResourceWithType(String name, boolean out)
	{
		ResourceInterface res = getResource(name, out);
		try
		{
			@SuppressWarnings("unchecked")
			T t = (T) res;
			return t;
		} catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Task that does the real job. Normally for a job it is enough to overwrite
	 * this task
	 */
	public JobState task()
	{
		// do this regularly
		changeWatchdogNumber();
		// end of task
		return JobState.ENDING;
	}

	@Override
	public int getWatchdogNumber()
	{
		return watchdogNumber;
	}

	protected void changeWatchdogNumber()
	{
		watchdogNumber++;
		watchdogNumber %= 100;
	}

	@Override
	public ResourceType getType()
	{
		return ResourceType.JOB;
	}
}
