/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

public interface EventFilter
{
	/**
	 * @param event
	 * @return value for importance of event
	 */
	int isEvent(JobEvent event);

	/**
	 * @param event
	 * @return value for unimportance of event
	 */
	int isNotEvent(JobEvent event);
}
