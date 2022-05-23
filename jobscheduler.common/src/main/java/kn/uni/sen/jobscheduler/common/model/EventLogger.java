/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

/**
 * Interface for element that can store JobEvent Tree persistent
 */
public interface EventLogger
{
	void addFilter(EventFilter filter);

	/**
	 * Add new JobEvent event to the event list
	 * 
	 * @param event
	 */
	boolean logEvent(EventHandler handler, JobEvent event);

	/**
	 * @param event
	 * @return if event will be stored
	 */
	boolean isEvent(JobEvent event);

	/**
	 * All events are persitently stored
	 */
	public void savePersistent();
}
