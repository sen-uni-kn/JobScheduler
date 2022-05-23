/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.model;

import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.JobLibrary;

/**
 * Does the scheduling of the JobGraph. It computes in which sequence the jobs
 * are done.
 */
public interface JobScheduler extends Job
{
	/**
	 * Adds a library with jobs to the scheduler.
	 * 
	 * @param library
	 */
	public void addLibrary(JobLibrary library);
}
