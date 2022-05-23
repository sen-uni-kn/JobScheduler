/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.console;

import java.util.List;

import kn.uni.sen.joblibrary.test.joblibrary.JobLibrary_Test;
import kn.uni.sen.jobscheduler.experiment.Experiment_Console;

public class LibraryTestHello_Main
{
	public static void main(String[] args)
	{
		JobLibrary_Test testLib = new JobLibrary_Test();
		Console_Library lib = new Console_Library(testLib)
		{
			@Override
			protected Experiment_Console createExperiment(String name, List<String> args)
			{
				return null;
			}
		};
		lib.run(args);
	}
}
