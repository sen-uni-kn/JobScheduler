/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.experiment;

import static org.junit.Assert.assertTrue;

import java.net.URL;

import kn.uni.sen.jobscheduler.common.JobAbstractTest;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceFile;
import kn.uni.sen.jobscheduler.common.resource.ResourceString;


public class Test_Job_Experiment extends JobAbstractTest
{
	@Override
	protected Job createJob()
	{
		return new Job_Experiment(this);
	}

	@Override
	protected ResourceInterface getResourceByName(String name, boolean out)
	{
		// add inputs
		if (name.compareTo("Experiment") == 0)
		{
			ClassLoader classLoader = getClass().getClassLoader();
			URL res = classLoader.getResource("experiment_test.xml");
			assertTrue(res != null);
			// add inputs
			return new ResourceFile(JobAbstractTest.getPath(res));
		} else if (name.compareTo("Library") == 0)
		{
			ClassLoader classLoader = getClass().getClassLoader();
			URL res = classLoader.getResource("test.joblibrary-1.5.0.jar");
			assertTrue(res != null);
			// add inputs
			return new ResourceFile(JobAbstractTest.getPath(res));
		} else if (name.compareTo("Pre") == 0)
		{
			// add inputs
			// return new ResourceString("LibTarTar_Console");
		} else if (name.compareTo(Job_Experiment.RUN) == 0)
		{
			return new ResourceString("yes");
		}
		return null;
	}

	public static void main(String[] args)
	{
		Test_Job_Experiment test = new Test_Job_Experiment();
		test.testAll();
	}
}
