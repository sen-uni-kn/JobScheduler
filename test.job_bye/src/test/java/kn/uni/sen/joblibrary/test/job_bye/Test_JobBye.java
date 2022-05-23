/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.joblibrary.test.job_bye;

import kn.uni.sen.jobscheduler.common.JobAbstractTest;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceString;

/**
 * Unit test for JobBye
 */
public class Test_JobBye extends JobAbstractTest
{
	@Override
	protected Job createJob()
	{
		checkOnlineResults = true;
		return new JobBye(this);
	}

	@Override
	protected ResourceInterface getResourceByName(String name, boolean out)
	{
		// add inputs
		if ("Input1".equals(name))
		{
			ResourceString val = new ResourceString();
			val.setName(name);
			val.setData("Good ");
			return val;
		}
		return null;
	}

	public static void main(String[] args)
	{
		Test_JobBye test = new Test_JobBye();
		test.testAll();
	}
}
