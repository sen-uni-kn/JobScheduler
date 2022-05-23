/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import kn.uni.sen.joblibrary.test.job_bye.JobBye;
import kn.uni.sen.joblibrary.test.job_hello.JobHello;
import kn.uni.sen.joblibrary.test.joblibrary.JobLibrary_Test;
import kn.uni.sen.jobscheduler.common.JobAbstractTest;
import kn.uni.sen.jobscheduler.common.JobDataTest;
import kn.uni.sen.jobscheduler.common.impl.EventHandlerAbstract;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.common.resource.ResourceString;
import kn.uni.sen.jobscheduler.core.model.JobGraph;
import kn.uni.sen.jobscheduler.core.model.JobScheduler;

/**
 * Unit test for JobScheduler JobScheduler_Easy
 */
public class Test_JobSchedulerEasy extends JobAbstractTest
{
	JobGraph jobGraph;

	JobDescriptionStruct jobDescrHello = new JobDescriptionStruct(JobHello.class.getSimpleName());
	JobDescriptionStruct jobDescrBye = new JobDescriptionStruct(JobBye.class.getSimpleName());

	JobGraph createTestGraph()
	{
		// resultList = new LinkedList<>();
		JobGraphAbstract graph = new JobGraphAbstract("");
		graph.addJob(jobDescrHello);
		graph.addJob(jobDescrBye);

		jobDescrHello.setName("JobHello1");
		ResourceAbstract inputText = new ResourceString();
		inputText.setData("result");
		graph.addResource(inputText, "InputHello1");

		ResourceInterface d = jobDescrHello.getInputList();
		while (d != null)
		{
			d = d.getNext();
		}

		d = jobDescrHello.getInputList();
		while (d != null)
		{
			if (d instanceof ResourceDescription)
				((ResourceDescription) d).setOwnerName(jobDescrHello.getName());
			d = d.getNext();
		}
		return graph;
	}

	@Override
	public JobDataTest createTest(int index)
	{
		ignoreTest = true;
		return super.createTest(index);
	}

	/**
	 * @return new job class of actual test
	 */
	protected Job createJob()
	{
		EventHandlerAbstract handler = new EventHandlerAbstract(null, "TestScheduler");
		ResourceFolder folder = new ResourceFolder();
		folder.setData("result");

		jobGraph = createTestGraph();
		JobScheduler scheduler = new JobScheduler_Easy(jobGraph, handler, folder);
		scheduler.addLibrary(new JobLibrary_Test());
		return scheduler;
	}

	public static void main(String[] args)
	{
		Test_JobSchedulerEasy test = new Test_JobSchedulerEasy();
		test.testAll();
	}

	@Override
	protected ResourceInterface getResourceByName(String name, boolean out)
	{
		return null;
	}
}
