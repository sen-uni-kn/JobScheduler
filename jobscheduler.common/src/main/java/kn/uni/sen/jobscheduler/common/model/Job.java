/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

import kn.uni.sen.jobscheduler.common.resource.OwnerResource;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;

/**
 * Job is a general interface how a job must be implemented in all libraries so
 * a JobScheduler can call the job just by this interface
 */
public interface Job extends OwnerResource, RunContext
{
	/**
	 * @param cmd
	 *            Send command to the job to can e.g. pause or break it
	 * @return true if command was accepted
	 */
	public boolean sendCommand(JobState cmd);

	/**
	 * Checks if all parameters are set correctly and job can be started.
	 * 
	 * @return Current state of job
	 */
	public boolean check();

	/**
	 * Starts run of the job.
	 * 
	 * @return Current state of job
	 */
	public JobState start();

	/**
	 * @return list of inputs from static part of a JOB
	 */
	public ResourceInterface getInputDescription();

	/**
	 * @return list of results from static part of a JOB
	 */
	public ResourceDescription getResultDescription();

	/**
	 * @return actual state of the job
	 */
	public JobState getJobState();

	/**
	 * JobResult is valid when job has ended.
	 * 
	 * @return The result of the job or the default value.
	 */
	public JobResult getResult();

	/**
	 * Job
	 * 
	 * @return name of job class: done by getSimpleName()
	 */
	public String getJobName();

	/**
	 * Each job has a number that is changed regularly (perhaps once a minute).
	 * So if job is inside in a deadlock this number is no more changed and Job
	 * can be killed.
	 * 
	 * @return random number
	 */
	public int getWatchdogNumber();

	/**
	 * @return name of job instance
	 */
	public String getName();

	/**
	 * @return version of job instance
	 */
	public String getJobVersion();

	/**
	 * @param logger
	 *            for new events
	 */
	public void addLogger(EventLogger logger);

	/**
	 * @param logger
	 *            for new events
	 */
	public void setEventHandler(EventHandler handler);

	/**
	 * if no estimation can be given, parameter will not be available input
	 * gives the normal input parameters necessary to run the job
	 * 
	 * @return an estimation for each parameter in list estimation
	 */
	public void getResourceEstimation(ResourceDescription inputParameters, ResourceDescription estimation);

	<T extends ResourceInterface> T getResourceWithType(String name, boolean out);

	public EventHandler getEventHandler();
}
