/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

//import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import kn.uni.sen.jobscheduler.core.model.JobServer;

public class Test_JobServer
{
	JobServer Server;

	@Before
	public void RunServer()
	{
		// Server = new JobServerDirect();
	}

	@Test()
	public void startScheduler()
	{
		/*
		 * assertTrue(Server != null);
		 * 
		 * JobSession session = Server.createSession("Test", "Test", "Test",
		 * ""); assertTrue(session != null);
		 * 
		 * ResourceFileXml jobGraph = new ResourceFileXml("JobGraph");
		 * jobGraph.setData("TestGraph.xml"); session.setJobGraph(jobGraph);
		 * session.start(); session.stop();
		 */
	}

	public static void main(String[] args)
	{
		Test_JobServer test = new Test_JobServer();
		test.RunServer();
		test.startScheduler();
	}
}
