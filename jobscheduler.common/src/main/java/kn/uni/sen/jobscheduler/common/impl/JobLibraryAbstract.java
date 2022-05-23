/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.JobLibrary;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.model.RunContext;

/**
 * First abstract implementation of interface JobLibrary like an instance of
 * JobLibrary is normally used.
 */
public abstract class JobLibraryAbstract implements JobLibrary
{
	protected List<Class<? extends Job>> JobList;
	protected List<Class<? extends ResourceInterface>> ResourceList;

	public JobLibraryAbstract()
	{
		JobList = new Vector<Class<? extends Job>>();
		ResourceList = new Vector<Class<? extends ResourceInterface>>();
	}

	public String getLibraryName()
	{
		return this.getClass().getSimpleName();
	}

	public List<Class<? extends Job>> getJobList()
	{
		return JobList;
	}

	@Override
	public Job createJob(String jobName, String version, RunContext father)
	{
		for (Iterator<Class<? extends Job>> it = JobList.iterator(); it.hasNext();)
		{
			Class<? extends Job> jobClass = it.next();
			try
			{
				String name = jobClass.getSimpleName();
				if (name.compareTo(jobName) != 0)
					continue;

				Constructor<? extends Job> con = jobClass.getDeclaredConstructor(RunContext.class);
				Job job = null;
				if (con != null)
					job = con.newInstance(father);
				else
				{
					con = jobClass.getDeclaredConstructor(EventHandler.class);
					if (con != null)
						job = con.newInstance();
				}
				if ((version == null) || (version.compareTo("") == 0))
					return job;

				if (version.compareTo(job.getJobVersion()) == 0)
					return job;
			} catch (Exception ex)
			{
				// todo: make an jobevent of this
				System.out.println("JobLibraryAbstract: CreateJob Error");
			}
		}
		return null;
	}

	@Override
	public List<Class<? extends ResourceInterface>> getResourceList()
	{
		return ResourceList;
	}

	@Override
	public ResourceInterface createResource(String resourceClassName, String version)
	{
		for (Iterator<Class<? extends ResourceInterface>> it = ResourceList.iterator(); it.hasNext();)
		{
			Class<? extends ResourceInterface> jobClass = it.next();
			try
			{
				String name = jobClass.getSimpleName();
				if (name.compareTo(resourceClassName) != 0)
					continue;

				ResourceInterface res = jobClass.getDeclaredConstructor().newInstance();
				return res;
			} catch (Exception ex)
			{
				// todo: make an jobevent of this
				System.out.println("JobLibraryAbstract: CreateResource Error");
			}
		}
		return null;
	}

}
