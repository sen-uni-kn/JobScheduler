/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.joblibrary.test.job_bye;

import kn.uni.sen.jobscheduler.common.impl.JobAbstract;
import kn.uni.sen.jobscheduler.common.model.JobResult;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.model.RunContext;
import kn.uni.sen.jobscheduler.common.resource.ResourceString;
import kn.uni.sen.jobscheduler.common.resource.ResourceTag;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

/**
 * Simple job that appends a bye to a string and return the new string as result
 */
public class JobBye extends JobAbstract
{
	public static String INPUT1 = "Input1";
	public static String RESULT1 = "Result1";

	ResourceString result1 = new ResourceString();

	public JobBye(RunContext parent)
	{
		super(parent);
		this.version = "3.2.0";

		createInputDescr(INPUT1, ResourceType.STRING).addTag(ResourceTag.NECESSARY);
		createResultDescr(RESULT1, ResourceType.STRING).addTag(ResourceTag.NECESSARY);
	}

	@Override
	public JobState task()
	{
		setJobState(JobState.RUNNING);
		// get and create folder
		// descrFolder.getResource();

		ResourceString input = getResourceWithType(INPUT1, false);
		if (input == null)
			return endError("Input value missing");

		System.out.println("----------------");
		System.out.println("Input(" + input.getName() + "):" + input.getData());

		String value = input.getData();
		value += "Bye";
		result1.setData(value);
		try
		{
			Thread.sleep(3000);
		} catch (InterruptedException e)
		{
		}

		System.out.println("Output(" + result1.getName() + "):" + result1.getData());
		System.out.println("----------------");

		return end(JobResult.OK);
	}

	@Override
	public ResourceInterface getResultResource(String name)
	{
		if (RESULT1.equals(name))
		{
			String value = result1.getData();
			value += "Bye";
			result1.setData(value);
			return result1;
		}
		return super.getResultResource(name);
	}

	@Override
	public String getJobName()
	{
		return this.getClass().getSimpleName();
	}
}
