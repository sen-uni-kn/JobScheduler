/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kn.uni.sen.jobscheduler.common.impl.LogConsole;
import kn.uni.sen.jobscheduler.common.impl.RunContextSimple;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.core.model.JobSession;
import kn.uni.sen.jobscheduler.core.model.JobRun;
import kn.uni.sen.jobscheduler.core.model.JobServer;

public abstract class JobServerAbstract extends RunContextSimple
		implements /* Runnable, */ JobServer
{
	Map<Integer, JobRun> runMap = new HashMap<>();
	List<JobSession> sessionList = new LinkedList<>();

	private Integer RunCounter = 1;

	/**
	 * Folder where all JobSchedulers are running Syntax:
	 * Folder/group/JobGraphName_unique_data_time(_number)
	 */

	public JobServerAbstract(String name, String folder)
	{
		super(null, name, folder);
		eventHandler.subscribe(new LogConsole());
	}

	@Override
	public JobRun createRun(Integer runID)
	{
		if ((runID == null) || (runID > RunCounter))
		{
			runID = RunCounter;
			RunCounter++;
		}

		JobRun run = getRun(runID);
		if (run == null)
		{
			run = createJobRun(runID);
			runMap.put(runID, run);
		}
		return run;
	}

	public int getMaxRunID()
	{
		return RunCounter;
	}

	public JobRun getRun(Integer runID)
	{
		if (runID == null)
			return null;
		return runMap.get(runID);
	}

	protected String getFirstSubString(String text, String val)
	{
		if (text == null)
			return null;
		int pos = text.indexOf(val);
		String ses = text;
		if (pos != -1)
			ses = text.substring(0, pos);
		return ses;
	}

	protected abstract JobRun createJobRun(Integer id);

	@Override
	public JobSession createSession(String user)
	{
		JobSession session = createSessionInstance(user);
		sessionList.add(session);
		return session;
	}

	protected abstract JobSession createSessionInstance(String user);

	@Override
	public boolean deleteSession(JobSession session)
	{
		return sessionList.remove(session);
	}

	public ResourceFolder createSessionFolder(String name)
	{
		if (name == null)
			return null;
		// get new unique folder name
		String folderName = getFolderText();
		folderName = ResourceFolder.getUniqueFolderDateTime(folderName + "/" + name);

		// create folder
		ResourceFolder folderProject = new ResourceFolder("Folder");
		folderProject.setData(folderName);
		if (!!!folderProject.createFolder())
			return null;

		return folderProject;
	}
}
