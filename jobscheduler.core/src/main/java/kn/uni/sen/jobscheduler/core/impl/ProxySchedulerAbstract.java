/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;
import kn.uni.sen.jobscheduler.core.model.ProxyScheduler;
import kn.uni.sen.jobscheduler.core.model.ProxyServer;

public class ProxySchedulerAbstract implements ProxyScheduler
{
	ProxyServer Server;
	String SchedulerID;

	public ProxySchedulerAbstract(ProxyServer server)
	{
		Server = server;
	}

	@Override
	public boolean sendData(String resourceID, ResourceType type, ResourceAbstract resource)
	{
		return Server.sendData(Server.getSessionID() + "/" + resourceID + "/" + type.name() + "/" + resource.getData());
	}

	@Override
	public String getSendReponse(String resourceID)
	{
		return Server.sendResult(Server.getSessionID() + "/" + resourceID);
	}

	@Override
	public boolean requestData(String resourceID, ResourceType type)
	{
		return Server.sendRequest(Server.getSessionID() + "/" + resourceID);
	}

	@Override
	public ResourceAbstract getData(String resourceID, ResourceType type)
	{
		return Server.requestResult(Server.getSessionID() + "/" + resourceID);
	}
}
