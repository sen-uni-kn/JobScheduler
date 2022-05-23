/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common;

import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.impl.JobDataResult;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.resource.OwnerResource;

public class JobDataTest extends JobDataResult implements OwnerResource
{
	List<TestResult> testList = new ArrayList<>();

	public void addTest(TestResult test)
	{
		testList.add(test);
	}

	public boolean checkResult(Job job)
	{
		if (job == null)
			return false;
		boolean good = true;
		for (TestResult test : testList)
		{
			good &= test.checkResult(job);
		}
		return good;
	}
}
