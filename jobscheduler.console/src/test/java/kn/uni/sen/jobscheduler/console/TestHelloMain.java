/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.console;

import java.util.List;

import kn.uni.sen.joblibrary.test.joblibrary.JobLibrary_Test;
import kn.uni.sen.jobscheduler.experiment.Experiment_Console;

public class TestHelloMain
{
	static Console_Library createLibSes()
	{
		JobLibrary_Test testLib = new JobLibrary_Test();

		Console_Library ses = new Console_Library(testLib)
		{
			@Override
			protected Experiment_Console createExperiment(String name, List<String> args)
			{
				return null;
			}
		};
		return ses;
	}

	public static void main(String[] args)
	{
		Console_Library ses = createLibSes();
		if (args.length == 0)
		{
			// return;
			// args = new String[] { "-lib", "Hello.jar", "-destiny",
			// "results.txt", "-store",
			// "$job2/Result1", "-store", "$job2/EndTime", "JobBye", "-Folder",
			// "1_Peter/JobBye" };
			ses.run(new String[] { "-lib", "Hello.jar", "-destiny", "result/results.txt", "-store", "Result1", "-run",
					"run1", "-id", "job1", "JobHello", "-Input1", "Peter", "-Folder", "1_Peter/JobHello" });

			Console_Library ses2 = createLibSes();
			ses2.run(new String[] { "-lib", "Hello.jar", "-destiny", "result/results.txt", "-store", "*", "-run",
					"run1", "-id", "job2", "JobBye", "-Folder", "1_Peter/JobBye", "-Input1", "{run1/job1/Result1}" });

			Console_Library ses3 = createLibSes();
			ses3.run(new String[] { "-lib", "Hello.jar", "-destiny", "result/results.txt", "-store", "Result1", "-run",
					"run2", "-id", "job1", "JobHello", "-Input1", "Petra", "-Folder", "1_Peter/JobHello" });
			return;
		}
		ses.run(args);
	}
}
