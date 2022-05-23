/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

import java.util.List;

import kn.uni.sen.jobscheduler.common.model.JobEvent;

/**
 * Interface for JobEvent to save inside a tree structure. JobEventLogger is
 * needed as extension to give the nodes the possibility to notify the root to
 * store events.
 */
public interface JobEvent
{
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	public static final String WARNING = "WARNING";
	public static final String DEBUG = "DEBUG";
	// void addData(ResourceInterface res);

	void addTag(String tag);

	boolean hasTag(String tag);

	String getText();

	List<String> getTagList();

	void addData(ResourceInterface res);

	List<ResourceInterface> getData();

	// ResourceInterface getData(String name);
	// List<ResourceInterface> getData();

	// public enum EventType
	// {
	// UNKOWN, LOGGER, INFO, USER, WARNING, ERROR, DEBUG
	// }

	/**
	 * An JobEvent event is a added to the Logger The actual event becomes an
	 * own logger and other events can be added to it
	 * 
	 * @param event
	 *            that has appeared
	 * @return new LogEventNode
	 */
	// @Deprecated
	// JobEvent logEvent(String name);

	/**
	 * An JobEvent event is a added to the Logger The actual event becomes an
	 * own logger and other events can be added to it
	 * 
	 * @param event
	 *            Name of event that has appeared.
	 * @param desription
	 *            Description of event or output text.
	 * @return new LogEventNode
	 */
	// @Deprecated
	// JobEvent logEvent(String name, String desription);

	/**
	 * Certain event types may not be important at runtim (e.g. debug
	 * information).
	 * 
	 * @param type
	 * @return true if event type will be logged
	 */
	// boolean isEvent(EventType type);

	// JobEvent logEvent(EventType type, String name);

	// JobEvent logEvent(EventType type, String name, String desription);

	/**
	 * An JobEvent event is a added to the Logger The actual event becomes an
	 * own logger and other events can be added to it
	 * 
	 * @param event
	 *            that has appeared
	 * 
	 * @param tag
	 *            for the event e.g. error, warning
	 * 
	 * @return new LogEventNode JobEvent LogEvent(String name, String
	 *         desription, String tag);
	 */

	/**
	 * Append some more text to old description.
	 * 
	 * @param desription
	 *            Text to append
	 */
	// public void appendDescription(String desription);

	/**
	 * @return name of the event
	 */
	// String getName();

	/**
	 * @return description of the event
	 */
	// String getEventDescription();

	/**
	 * @return description of the event
	 */
	// List<JobEvent> getEventChildren();

	// List<String> getEventTagList();

	/**
	 * Add some tags to an event. Used when it is necessary to mark an event
	 * e.g. 'error' or 'warning'
	 * 
	 * @param name
	 *            name of tag
	 */
	// public void addEventTag(String value);

	/**
	 * Add some tags to an event. Used when it is necessary to give some meaning
	 * to an event like an JobState (e.g. name='JobState' value='Start')
	 * 
	 * @param name
	 *            name of the attribute
	 * @param value
	 *            value of the attribute
	 */
	// void addAttribute(String name, String value);

	/**
	 * Save all data inside the logger. Perhaps when it is necessary to have an
	 * event stored for sure.
	 */
	// public void savePersistent();
}
