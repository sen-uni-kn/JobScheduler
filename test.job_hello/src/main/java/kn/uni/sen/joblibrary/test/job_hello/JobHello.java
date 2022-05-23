/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.joblibrary.test.job_hello;

import java.text.SimpleDateFormat;
import java.util.Date;

import kn.uni.sen.jobscheduler.common.impl.JobAbstract;
import kn.uni.sen.jobscheduler.common.model.JobResult;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.model.RunContext;
import kn.uni.sen.jobscheduler.common.resource.ResourceString;
import kn.uni.sen.jobscheduler.common.resource.ResourceTag;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

/**
 * Simple job that appends a hello to a string and return the new string as
 * result
 */
public class JobHello extends JobAbstract
{
	public static String INPUT1 = "Input1";
	public static String RESULT1 = "Result1";

	ResourceString result1 = new ResourceString();

	public JobHello(RunContext parent)
	{
		super(parent);
		this.version = "3.1.0";

		createInputDescr(INPUT1, ResourceType.STRING).addTag(ResourceTag.NECESSARY);
		createResultDescr(RESULT1, ResourceType.STRING).addTag(ResourceTag.NECESSARY);
	}

	@Override
	public JobState task()
	{
		ResourceString input = getResourceWithType(INPUT1, false);
		if (input == null)
			return endError("Input value missing");

		try
		{
			// wait a short time to simulate processing time of a job
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
		}

		System.out.println("----------------");
		System.out.println("Input(" + input.getName() + "):" + input.getData());

		String value = "Hello " + input.getData() + " ";
		result1.setData(value);

		System.out.println("Output(" + result1.getName() + "):" + result1.getData());
		System.out.println("----------------");

		return end(JobResult.OK);
	}

	public static String getTimeText()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}

	@Override
	public ResourceInterface getResultResource(String name)
	{
		if (RESULT1.equals(name))
		{
			String value = result1.getData();
			// value += "Bye";
			result1.setData(value);
			return result1;
		}
		return super.getResultResource(name);
	}

	@Override
	public ResourceType getType()
	{
		return ResourceType.JOB;
	}

	@Override
	public boolean hasTag(ResourceTag tag)
	{
		return false;
	}
}
