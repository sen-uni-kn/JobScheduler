/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.console;

import static org.junit.Assert.assertTrue;
import java.net.URL;
import org.junit.Test;

import kn.uni.sen.jobscheduler.common.resource.ResourceFile;

public class Test_JobClientConsole
{
	@Test()
	public void RunConsole()
	{
		ClassLoader classLoader = getClass().getClassLoader();
		URL res = classLoader.getResource("JobGraphAlone.xmi");
		assertTrue(res != null);

		URL resLib = classLoader.getResource("test.joblibrary-1.5.0.jar");
		assertTrue(resLib != null);

		String path = ResourceFile.getFolder(res.getPath());
		String graph = ResourceFile.getFilename(res.getFile());
		String lib = ResourceFile.getFilename(resLib.getFile());
		String folder = "test2";

		String[] args = new String[] { "-p ", path, "-l", lib, "-graph ", graph, "-f", folder };
		JobClient_Console.main(args);
	}

	public static void main(String[] args)
	{
		Test_JobClientConsole test = new Test_JobClientConsole();
		test.RunConsole();
	}
}
