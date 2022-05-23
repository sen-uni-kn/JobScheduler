/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.model;

import java.util.List;

import kn.uni.sen.jobscheduler.core.model.JobControl;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.JobState;

/**
 * JobControl has control of an job. It can be checked with the description of
 * an job and the job itself if it is possible to run the job.
 */
public interface JobControl
{
	/**
	 * checks if job has all input variables
	 * 
	 * @return job states inside of tree has changed
	 */
	public boolean checkJob();

	/**
	 * Starts job, when it is done check should already have been successful
	 */
	public void startJob();

	/**
	 * checks if job has all input variables and set output as available
	 * 
	 * @return job states inside of tree has changed
	 */
	public boolean simulateJob();

	/**
	 * Will end the job as soon as possible.
	 */
	public void sendCommand(JobState state);

	public JobState getState();

	public JobDescription getDescription();

	/**
	 * reset job, e.g. mark result variables as not set
	 */
	public void resetJob();

	public void addJob(JobControl job);

	public List<JobControl> getControlList();

	public JobControl getControlByName(String resOwner);

	public Job getJob();

	/**
	 * Regular checks of all JobControls shall help to detect deadlock and
	 * something like this. Check Timeouts, give possibility to check if job is
	 * still running, ...
	 * 
	 * @return false if there is a problem and scheduler has to end
	 */
	public boolean checkActive();
}
