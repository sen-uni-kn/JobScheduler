/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

import java.util.List;

public interface EventHandler extends ResourceInterface
{
	/**
	 * @return name of current handler instance
	 */
	String getName();

	/**
	 * @return tree path of current handler
	 */
	String getPath();

	/**
	 * @return father of current handler
	 */
	EventHandler getFather();

	/**
	 * @param event
	 * @return true is event will be logged
	 */
	boolean isEvent(JobEvent event);

	/**
	 * request all loggers to log message
	 * 
	 * @return true if at least one logger logs the message
	 */
	boolean logEvent(JobEvent event);

	/**
	 * Loggers can request to get an event from an handler
	 * 
	 * @param name
	 * @return requested event if possible
	 */
	JobEvent requestMsg(EventLogger logger, String name);

	List<EventLogger> getChildLogList();

	/**
	 * Subscribes a logger to the current handler
	 * 
	 * @param logger
	 */
	void subscribe(EventLogger logger);

	/**
	 * Unsubscribes a logger to the current handler
	 * 
	 * @param logger
	 */
	void unsubscribe(EventLogger logger);

	void setFather(EventHandler father);

	void savePersistent();
}
