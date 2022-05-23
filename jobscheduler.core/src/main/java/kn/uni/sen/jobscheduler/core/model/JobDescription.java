/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.model;

import kn.uni.sen.jobscheduler.core.model.JobDescription;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.common.resource.ResourceUrl;

/**
 * Is the structure to save the jobs of the JobGraph. Also the some static
 * input data can be contained.
 * 
 * @author Martin Koelbl
 */
public interface JobDescription extends ResourceInterface
{
	/**
	 * @param name
	 *            of this job
	 */
	public void setName(String name);

	/**
	 * @return name of this job
	 */
	public String getName();

	/**
	 * @return the name of this job class.
	 */
	public String getJobName();

	/**
	 * @return version of the job
	 */
	public String getJobVersion();

	/**
	 * @param url
	 *            of the job scheduler doing the work
	 */
	public void setWho(ResourceUrl url);

	/**
	 * @return the job scheduler doing the work
	 */
	public ResourceUrl getWho();

	/**
	 * @param job
	 *            Add job to the list of job inside this job
	 * 
	 * @return true when job could be added
	 */
	public boolean addJob(JobDescription job);

	/**
	 * @param res
	 *            Add input to the list of inputs.
	 * @return true then input could be added
	 */
	public boolean addInput(ResourceDescription res);

	/**
	 * @param res
	 *            Add result to the list of results.
	 * @return true then input could be added
	 */
	public boolean addResult(ResourceDescription res);

	/**
	 * @return list of jobs done inside this job
	 */
	public ResourceInterface getJobList();

	/**
	 * @return input resource with name name of input list
	 */
	public ResourceInterface getInputList();

	/**
	 * @return result resource with name name of input list
	 */
	public ResourceInterface getResultList();
}
