/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import kn.uni.sen.jobscheduler.common.JobAbstractTest;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;

public class Test_JobGraph
{
	@Test
	public void checkGraph()
	{
		ClassLoader classLoader = getClass().getClassLoader();
		String filePath = JobAbstractTest.getPath(classLoader.getResource("JobGraphHello.xmi"));
		assertTrue(!filePath.isEmpty());

		ResourceFileXml file = new ResourceFileXml();
		file.setData(filePath);

		JobGraph_File jobGraph = new JobGraph_File("JobGraph", file);
		jobGraph.load();

		ResourceFileXml fileTo = new ResourceFileXml();
		fileTo.setData("test/" + file.getFileName());
		jobGraph.saveTo(fileTo);

		// todo: compare files
		// if (!file.compareContent(fileTo))
		// assert (true);
	}

	public static void main(String[] args)
	{
		Test_JobGraph test = new Test_JobGraph();
		test.checkGraph();
	}
}
