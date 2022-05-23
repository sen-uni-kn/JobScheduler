/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.model;

public interface JobServer
{
	/**
	 * creates a new session for a job
	 * 
	 * @param name
	 * @param user
	 * @param group
	 * @param password
	 * @return
	 */
	public JobSession createSession(String user);

	/**
	 * deletes a new session for a job
	 * 
	 * @param session
	 * @return
	 */
	public boolean deleteSession(JobSession session);

	/**
	 * @param runID
	 * @return new run if id does not exists xet
	 */
	public JobRun createRun(Integer runID);
}
