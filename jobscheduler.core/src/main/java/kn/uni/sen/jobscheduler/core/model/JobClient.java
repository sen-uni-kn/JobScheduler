/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.model;

/**
 * The client can be another JobScheduler as well as a user communicating to a JobServer.
 * 
 * @author Martin Koelbl
 */
public interface JobClient
{
	/**
	 * @param server
	 *            notifies the client for some event.
	 * @param event
	 *            event that has happened.
	 */
	void notify(JobSession session, String event);
}
