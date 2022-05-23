/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;

public interface RunContext extends ResourceInterface
{
	ResourceFolder getFolder();

	EventHandler getEventHandler();

	String getFolderText();

	JobEvent logEventStatus(String type, String text);

	JobEvent logEventStatus(String type, String name, String text);
}
