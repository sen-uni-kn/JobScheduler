/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.model;

import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;

public interface ProxyServer
{
	enum ConnectionState
	{
		UNDEF, CONNECTED, LOGIN, CLOSED
	}

	/**
	 * @param user
	 * @param password
	 * @return SessionID if already available
	 */
	boolean login(String user, String group, String password);
	
	/**
	 * @return SessionID if available
	 */
	String getSessionID();
	
	/**
	 * 
	 * @param active
	 * @return all active/inactive schedulers of server
	 */
	String[] getSchedulerList(boolean active);

	/**
	 * @param user
	 *            to logout
	 */
	boolean logout(String sessionID);
	
	boolean sendData(String url);
	
	String sendResult(String url);
	
	boolean sendRequest(String url);
	
	ResourceAbstract requestResult(String url);

	public ConnectionState getConnectionState();
}
