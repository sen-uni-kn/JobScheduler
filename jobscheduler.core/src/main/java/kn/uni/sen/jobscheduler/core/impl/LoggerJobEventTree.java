/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import kn.uni.sen.jobscheduler.common.impl.JobEventAbstract;
import kn.uni.sen.jobscheduler.common.impl.NodeXmlHandler_Event;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.EventLogger;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;

/**
 * logs event as a tree for this each job has an own child in an xml tree where
 * he can add events
 * 
 * Each hour some the eventlist is saved to the harddisk Also the first failure
 * is saved and when the Logger is closed
 */
@Deprecated
public class LoggerJobEventTree extends JobEventAbstract implements JobEvent
{
	ResourceFileXml xmlFile;

	public LoggerJobEventTree(String fileName)
	{
		super("LogFile");
		xmlFile = new ResourceFileXml();
		xmlFile.setData(fileName);
		xmlFile.createFile(false);
	}

	public LoggerJobEventTree(EventLogger father, String eventName, String description, String fileName)
	{
		super(description);
		xmlFile = new ResourceFileXml();
		xmlFile.setData(fileName);
		xmlFile.createFile(false);
	}

	public void savePersistent()
	{
		NodeXmlHandler_Event handler = new NodeXmlHandler_Event(this);
		// todo: xmlFile.deleteFile();
		xmlFile.setHandler(handler);
		xmlFile.writeFile();
	}

}
