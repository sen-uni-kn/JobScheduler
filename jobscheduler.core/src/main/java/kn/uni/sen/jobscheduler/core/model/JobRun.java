/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.model;

import java.util.List;

import kn.uni.sen.jobscheduler.common.model.JobLibrary;
import kn.uni.sen.jobscheduler.common.model.JobResult;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;

/**
 * Run contains a single instance of JobScheduler 
 * 
 * @author Martin Koelbl
 */
public interface JobRun
{
	/**
	 * Start the run of the actual jobgraph.
	 */
	public JobState start(String executionType);

	/**
	 * Stops a running session
	 */
	public void stop();

	/**
	 * @return result of called session (only valid when JobState is on state
	 *         ended);
	 */
	public JobResult getResult();

	public ResourceFolder getFolder();

	/**
	 * @return The result list of the actual jobgraph.
	 */
	public List<ResourceAbstract> getResultList();

	public JobState getStatus();

	public Integer getRunID();

	public void setJobGraph(ResourceFileXml file);
	
	void addLibrary(JobLibrary library);
}
