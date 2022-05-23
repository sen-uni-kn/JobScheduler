/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.model;

import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

/**
 * JobScheduler can call other JobServer by ProxyJobGraph to run jobs on them.
 * Also a client gui uses this interface to communicate with an JobScheduler.
 * 
 * Call is done by JobProxy, all input data are copied and the results are
 * copied back. During process of job also show monitor variables and events.
 * 
 * @author Martin Koelbl
 *
 */
public interface ProxyScheduler
{	
	/**
	 * @return unique ID of the scheduler (per default this is the "group/folder" name)
	 */
	//String getSchedulerID();
	
	/**
	 * @param url
	 *            of single data
	 * @param type
	 *            of data
	 * @return true if request could be send
	 */
	boolean requestData(String resourceUrl, ResourceType type);

	/**
	 * @param url
	 * @param type
	 * @return data that where requested else null
	 */
	ResourceAbstract getData(String resourceUrl, ResourceType type);

	/**
	 * Send some data to a resource
	 * 
	 * @param url
	 *            of data
	 * @param type
	 *            of data
	 * @param resource
	 *            data itself
	 * @return true if data could be set
	 */
	boolean sendData(String resourceID, ResourceType type, ResourceAbstract resource);
	
	/**
	 * @param url when sended by SendData
	 * @return url on server, when send was successfull
	 */
	String getSendReponse(String resourceID);
}
