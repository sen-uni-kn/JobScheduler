/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.junit.Test;

import kn.uni.sen.joblibrary.test.joblibrary.JobLibrary_Test;
import kn.uni.sen.jobscheduler.common.model.JobLibrary;

public class Test_JobStandanlone
{
	@Test()
	public void RunGui()
	{
		ClassLoader classLoader = getClass().getClassLoader();
		URL res = classLoader.getResource("JobGraphAlone.xmi");
		assertTrue(res != null);

		URL resLib = classLoader.getResource("test.joblibrary-1.5.0.jar");
		assertTrue(resLib != null);

		String filePath = res.getPath();
		if (filePath.contains("%"))
		{
			try
			{
				filePath = URLDecoder.decode(res.getPath(), "UTF-8");
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}

		String[] args = new String[] { filePath };
		JobClient_Gui gui = new JobClient_Gui(args);

		JobLibrary lib = gui.loadJobLibrary(resLib.getPath(), JobLibrary_Test.class.getSimpleName());
		if (lib != null)
			gui.addJobLibrary(lib);
		else
		{
			System.out.println("Warning: Test library could not be loaded");
			gui.addJobLibrary(new JobLibrary_Test());
		}
		gui.run();
	}

	public static void main(String[] args)
	{
		Test_JobStandanlone test = new Test_JobStandanlone();
		test.RunGui();
	}
}
