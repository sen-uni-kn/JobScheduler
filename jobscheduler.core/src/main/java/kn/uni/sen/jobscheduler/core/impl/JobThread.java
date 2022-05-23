/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import java.util.LinkedList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.impl.RunContextSimple;
import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.JobResult;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.core.model.JobControl;
import kn.uni.sen.jobscheduler.core.model.JobDescription;

/**
 * This class handles communication to a job for every instance
 *
 */
public class JobThread extends RunContextSimple implements JobControl
{
	// protected InternalThread ThreadInstance;

	protected class InternalThread implements Runnable
	{
		@Override
		public void run()
		{
			jobHandle.start();
		}
	}

	// has old state for checking if state has changed since last time
	JobState StateLast;
	// last watchdog number of job
	int WatchdogNumberLast;

	// handle to a job instance
	Job jobHandle;

	JobDescription jobDescript;

	EventHandler EventNode;

	List<JobControl> ControlList = new LinkedList<JobControl>();
	// handle of thread if job is started
	Thread ThreadJob;

	public JobThread(ResourceInterface parent, JobDescription jobDescript, Job jobHandle, EventHandler fatherEvent)
	{
		super(fatherEvent, "JobThread", "result");
		this.jobDescript = jobDescript;
		this.jobHandle = jobHandle;
		EventNode = fatherEvent;
		this.parent = parent;

		if (this.jobHandle != null)
		{
			this.jobHandle.setParent(this);
		}
	}

	// helper class to reset all resource marked as available
	public void resetJob()
	{
		if (jobHandle == null)
			return;
		System.out.println("Warning: JobThread.ResetJob() not tested");

		jobHandle.sendCommand(JobState.RESET);
		for (JobControl childJob : ControlList)
			childJob.resetJob();

		// wait until job is reseted
		while (jobHandle.getJobState() != JobState.UNDEFINED)
			;
	}

	@Override
	/**
	 * Check if an job can be calculated
	 * 
	 * @param description
	 * @return true if another resource was calculcated
	 */
	public boolean simulateJob()
	{
		boolean bChange = false;
		// check nested jobs if possible to calculate
		for (JobControl jobChild : ControlList)
			bChange |= jobChild.checkJob();

		// check by job itself if all input parameters are given and monitor and
		// results are available
		jobHandle.check();
		bChange |= (jobHandle.getResult() == JobResult.OK);
		return bChange;
	}

	public void startJob()
	{
		if (ThreadJob != null)
			return;
		ThreadJob = new Thread(new InternalThread());
		ThreadJob.start();
	}

	public void startJobSingle()
	{
		(new InternalThread()).run();
	}

	public void endJob()
	{
		jobHandle.sendCommand(JobState.FINISHED);
		while (jobHandle.getJobState() != JobState.FINISHED)
			;
	}

	private void notifyByJob(Job job)
	{
		if (job == jobHandle)
			StateLast = jobHandle.getJobState();
		if (parent != null)
			parent.notify(this);
		ThreadJob = null;
	}

	@Override
	public void notify(ResourceInterface res)
	{
		if (jobHandle != res)
			return;
		notifyByJob(jobHandle);
	}

	@Override
	public void addJob(JobControl job)
	{
		if (job == null)
			return;
		ControlList.add(job);
	}

	@Override
	public JobState getState()
	{
		StateLast = jobHandle.getJobState();
		return StateLast;
	}

	@Override
	public boolean checkActive()
	{
		if (jobHandle.getJobState() == JobState.RUNNING)
		{
			// check active numbers of job once a minute
			int number = jobHandle.getWatchdogNumber();
			if (number == WatchdogNumberLast)
				return false;
		}
		return true;
	}

	@Override
	public List<JobControl> getControlList()
	{
		return ControlList;
	}

	@Override
	public JobDescription getDescription()
	{
		return jobDescript;
	}

	@Override
	public void sendCommand(JobState state)
	{
		jobHandle.sendCommand(state);
	}

	@Override
	public boolean checkJob()
	{
		return jobHandle.check();
	}

	@Override
	public Job getJob()
	{
		return jobHandle;
	}

	@Override
	public JobControl getControlByName(String resOwner)
	{
		if (resOwner == null)
			return null;
		for (JobControl control : getControlList())
		{
			Job job = control.getJob();
			if (job == null)
				continue;
			String name = job.getJobName();
			if ((name != null) && (resOwner.compareTo(name) == 0))
				return control;
		}
		return null;
	}
}
