/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import java.util.List;

import kn.uni.sen.jobscheduler.core.impl.JobScheduler_Easy;
import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.core.model.JobControl;
import kn.uni.sen.jobscheduler.core.model.JobGraph;
import kn.uni.sen.jobscheduler.core.model.JobScheduler;

/**
 * Simple test class to test scheduler
 *
 */
public class JobScheduler_Easy extends JobSchedulerAbstract
{
	public static final String TYPE = "JobScheduler_Easy";

	public JobScheduler_Easy(JobGraph jobGraph, EventHandler handler, ResourceFolder folder)
	{
		super(jobGraph, handler, folder);
	}

	public boolean sendCommand(JobState cmd)
	{
		logEventStatus(JobEvent.DEBUG, "JobScheduler:Command", "Sended command " + cmd);
		getEventHandler().savePersistent();
		return false;
		// return super.SendCommand(cmd);
	}

	protected boolean startJob(JobControl control)
	{
		boolean bMissing = false;
		List<JobControl> listControls = control.getControlList();
		// first run all child jobs
		for (JobControl controlChild : listControls)
		{
			if (controlChild.getState() == JobState.FINISHED)
				continue;
			bMissing = true;
			if (!!!controlChild.checkJob())
				continue;
			startJob(controlChild);
		}

		// at least one new job inside of children is not finished
		if (bMissing)
			return false;

		// control is actually not waiting to start
		if (control.getState() != JobState.START)
			return false;
		// check job
		if (!control.checkJob())
			return false;
		//
		control.startJob();
		jobActiveList.add(control);
		return true;
	}

	public static void main(String[] args)
	{
		if (args.length == 0)
			return;
		String graphFile = "";
		String folderUrl = "result";
		for (String arg : args)
		{
			if (arg.startsWith("-f"))
				// todo: parse folder
				continue;
			if (arg.startsWith("-h"))
				// todo: write help
				return;
			if (arg.startsWith("-"))
				continue;
			else
				graphFile = arg;
		}
		ResourceFileXml xmlFile = new ResourceFileXml();
		xmlFile.setData(graphFile);
		ResourceFolder folder = new ResourceFolder();
		folder.setData(folderUrl);
		JobGraph graph = new JobGraph_File("", xmlFile);
		JobScheduler schedul = new JobScheduler_Easy(graph, null, folder);

		/*
		 * JobLibrary lib1 = new JobLibrary_SpinCause("LibraryFirst");
		 * scheduler.AddLibrary(lib1);
		 */
		System.out.println("Starting...");
		schedul.start();
		while (schedul.getJobState() != JobState.FINISHED)
			;
	}
}
