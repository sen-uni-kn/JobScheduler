/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.se.joblibrary.test.job_hello;

import kn.uni.sen.joblibrary.test.job_hello.JobHello;
import kn.uni.sen.jobscheduler.common.JobAbstractTest;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceString;

/**
 * Unit test for JobBye
 */
public class Test_JobHello extends JobAbstractTest
{
	protected Job createJob()
	{
		checkOnlineResults = true;
		return new JobHello(this);
	}

	@Override
	protected ResourceInterface getResourceByName(String name, boolean out)
	{
		// add inputs
		if ("Input1".equals(name))
		{
			ResourceString val = new ResourceString();
			val.setName(name);
			val.setData("Friend ");
			return val;
		}
		return null;
	}

	public static void main(String[] args)
	{
		Test_JobHello test = new Test_JobHello();
		test.testAll();
	}
}
