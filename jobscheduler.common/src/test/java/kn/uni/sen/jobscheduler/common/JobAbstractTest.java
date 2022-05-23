/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Timer;

import org.junit.Test;

import kn.uni.sen.jobscheduler.common.helper.Helper;
import kn.uni.sen.jobscheduler.common.impl.EventLoggerAbstract;
import kn.uni.sen.jobscheduler.common.impl.JobAbstract;
import kn.uni.sen.jobscheduler.common.impl.JobDataInput;
import kn.uni.sen.jobscheduler.common.impl.LogConsole;
import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.JobResult;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.OwnerResource;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.common.resource.ResourceFile;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.common.resource.ResourceTag;

/**
 * Unit test for JobScheduler JobScheduler_Easy
 */
public abstract class JobAbstractTest extends JobAbstract implements OwnerResource
{
	protected Job jobTest;
	protected boolean checkOnlineResults = false;
	protected String testName = "";
	protected int monitorTime = 2000;
	JobDataInput inData = null;

	//EventHandler eventHandler2 = new EventHandlerAbstract(null, "Test");
	protected EventLoggerAbstract logger = new LogConsole(2);
	{
		EventHandler eH = getEventHandler();
		if(eH !=null)
			eH.subscribe(logger);
		
		//eventHandler.subscribe(logger);
		//addChild(eventHandler);
	}

	protected boolean ignoreTest = false;
	protected boolean expectFail = false;

	/**
	 * @return new job class of actual test
	 */
	protected abstract Job createJob();

	Timer timer = null;

	public static String getPath(URL fileUrl)
	{
		return Helper.getPath(fileUrl);
	}

	public static ResourceFile loadFile(String fileName)
	{
		return Helper.loadFile(fileName);
	}

	// @Before
	public void beforeTest()
	{
		logEventStatus(JobEvent.INFO, this.getClass().getSimpleName() + " - Start");
	}

	// @After
	public void afterTest()
	{
		logEventStatus(JobEvent.INFO, this.getClass().getSimpleName() + " - End");
	}

	public JobDataTest createTest(int index)
	{
		jobTest = createJob();
		JobDataTest test = createTest(jobTest, index);
		if (test == null)
			return null;
		if (timer != null)
			timer.cancel();
		timer = new Timer();
		if (checkOnlineResults)
			timer.schedule(new Test_ResultOnline(jobTest), 1000, monitorTime);

		jobTest.addLogger(new LogConsole(2));

		ResourceInterface d = jobTest.getInputDescription();
		while (d != null)
		{
			if (d instanceof ResourceDescription)
			{
				ResourceDescription descr = (ResourceDescription) d;
				if (descr.getOwner() == null)
					descr.setOwner(this);
			}
			d = d.getNext();
		}
		return test;
	}

	public void endTest(JobDataTest test)
	{
		endTest();
		if (timer != null)
			timer.cancel();
		jobTest = null;
	}

	protected void endTest()
	{
	}

	@Override
	public final ResourceInterface getResource(String name, boolean out)
	{
		if ((name == null) || name.isEmpty())
			return null;

		ResourceInterface res = inData.getResource(name, out);
		if (res != null)
			return res;

		res = getResourceByName(name, out);
		if ((res == null) && JobAbstract.FOLDER.equals(name))
			res = new ResourceFolder("result");
		return res;
	}

	protected abstract ResourceInterface getResourceByName(String name, boolean out);

	void setInputOwner()
	{
		if (jobTest == null)
			return;

		ResourceInterface d = jobTest.getInputDescription();
		while (d != null)
		{
			if (d instanceof ResourceDescription)
			{
				ResourceDescription descr = (ResourceDescription) d;
				if (descr.getOwner() == null)
					descr.setOwner(this);
			}
			d = d.getNext();
		}
	}

	protected void runTest(int indexStart, boolean single)
	{
		testName = "RunPositive";

		// do check on job
		int index = indexStart;
		while (true)
		{
			JobDataTest test = createTest(index++);
			// test = createTest(jobTest, index++);
			if (test == null)
				break;
			if (ignoreTest)
			{
				endTest(test);
				return;
			}
			inData = test;
			assertTrue(jobTest.check());
			// do run of job
			jobTest.start();
			if (expectFail)
			{
				assertTrue(jobTest.getResult() == JobResult.ERROR);
			} else
			{
				assertTrue(jobTest.getResult() == JobResult.OK);
				assertTrue(checkResult());
				assertTrue(test.checkResult(jobTest));
			}
			endTest(test);
			if (single)
				break;
		}
	}

	protected JobDataTest createTest(Job jobTest2, int index)
	{
		if (index != 1)
			return null;
		// setInputOwner();
		return new JobDataTest();
	}

	protected boolean checkResult()
	{
		boolean bGood = true;
		ResourceInterface d = jobTest.getResultDescription();
		while (d != null)
		{
			if (d.hasTag(ResourceTag.NECESSARY))
			{
				ResourceInterface res = d.getChild();
				if (res == null)
					bGood = false;
			}
			d = d.getNext();
		}
		return bGood;
	}

	@Override
	public boolean setResource(String name, ResourceInterface res)
	{
		return false;
	}

	@Test()
	public void testAll()
	{
		beforeTest();
		runTest(1, false);
		afterTest();
	}

	public void testSingle(int index)
	{
		beforeTest();
		runTest(index, true);
		afterTest();
	}
}
