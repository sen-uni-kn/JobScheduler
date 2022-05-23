/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import kn.uni.sen.jobscheduler.core.impl.JobDescriptionStruct;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.common.resource.ResourceList;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;
import kn.uni.sen.jobscheduler.common.resource.ResourceUrl;
import kn.uni.sen.jobscheduler.common.resource.ResourceXmlNode;
import kn.uni.sen.jobscheduler.core.model.JobDescription;

/**
 * Contains all data of a job TODO: check if this should be named ResourceJob
 */
public class JobDescriptionStruct extends ResourceXmlNode implements JobDescription
{
	public static

	// name of this instance of a job
	String Name = "";

	// name of this instance/ID of a job
	String JobID = null;

	// name of the job (->Job-class) to call
	String JobName = "";

	// version of the job to call
	String JobVersion = "";

	// tag of the job
	String JobTag = null;

	// url can point to another instance of a jobScheduler
	// this instance could then run the job
	ResourceUrl Who;

	// parent node
	JobDescriptionStruct ParentJob = null;

	ResourceList jobList = null;
	ResourceList inputList = null;
	ResourceList resultList = null;

	/**
	 * Empty JobName means just a job that contains other jobs.
	 * 
	 * @param jobName
	 *            searched in libraries for
	 */
	public JobDescriptionStruct(String jobName)
	{
		super("Job");
		JobName = jobName;
	}

	/*
	 * public JobDescriptionStruct(String jobName, Map<String, List<Resource>>
	 * inputList, Map<String, List<Resource>> resultList) { JobName = jobName;
	 * 
	 * for (Entry<String, List<Resource>> entry : inputList.entrySet()) for
	 * (Resource res : entry.getValue()) addInput(res, entry.getKey());
	 * 
	 * for (Entry<String, List<Resource>> entry : resultList.entrySet()) for
	 * (Resource res : entry.getValue()) addResult(res, entry.getKey()); }
	 */

	@Override
	public void setName(String name)
	{
		Name = name;
	}

	@Override
	public String getName()
	{
		return Name;
	}

	public String getJobName()
	{
		return JobName;
	}

	public String getJobVersion()
	{
		return JobVersion;
	}

	public void setWho(ResourceUrl url)
	{
		Who = url;
	}

	public ResourceUrl getWho()
	{
		return Who;
	}

	@Override
	public ResourceInterface getJobList()
	{
		if (jobList == null)
			return null;
		return jobList.getChild();
	}

	@Override
	public ResourceInterface getInputList()
	{
		if (inputList == null)
			return null;
		return inputList.getChild();
	}

	@Override
	public ResourceInterface getResultList()
	{
		if (resultList == null)
			return null;
		return resultList.getChild();
	}

	public boolean addJob(JobDescription job)
	{
		if (job == null)
			return false;
		if (jobList == null)
			jobList = new ResourceList("JobList", ResourceType.LIST);

		// TODO: do some checks

		jobList.addChild(job);
		System.out.println(job.getJobName() + " added");
		return true;
	}

	public JobDescriptionStruct getParentResource()
	{
		return ParentJob;
	}

	public void setParentJob(JobDescriptionStruct parentJob)
	{
		if (parentJob != null && parentJob.JobName != null)
			System.out.println("Setting parent job " + parentJob.JobName);
		ParentJob = parentJob;
	}

	@Override
	public boolean addInput(ResourceDescription res)
	{
		if (inputList == null)
			inputList = new ResourceList("JobList", ResourceType.LIST);
		inputList.addChild(res);
		return true;
	}

	@Override
	public boolean addResult(ResourceDescription res)
	{
		if (resultList == null)
			resultList = new ResourceList("JobList", ResourceType.LIST);
		resultList.addChild(res);
		return true;
	}

	public String getJobTag()
	{
		return JobTag;
	}

	public void setJobTag(String jobTag)
	{
		this.JobTag = jobTag;
	}

	public String getJobId()
	{
		return JobID;
	}

	public void setJobId(String jobId)
	{
		JobID = jobId;
	}
}
