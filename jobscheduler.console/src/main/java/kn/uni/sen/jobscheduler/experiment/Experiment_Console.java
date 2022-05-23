/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.experiment;

import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.console.Console_Job2;

/**
 * Creates an experiment by calling Job_Experiment
 * 
 * @author Martin Koelbl
 */
public class Experiment_Console extends Console_Job2
{
	public Experiment_Console()
	{
		super(null, "ExperimentConsole", "result");
	}

	@Override
	protected Job createJob(String name, String version)
	{
		return new Job_Experiment(null);
	}

	@Override
	public void run(String[] args)
	{
		if (args.length <= 0)
		{
			logEventStatus(JobEvent.ERROR, "Experiment_Console called without input parameters");
			return;
		}

		if (!!!args[0].startsWith("-"))
		{
			String[] args2 = new String[args.length + 1];
			System.arraycopy(args, 0, args2, 1, args.length);
			args2[0] = "-" + Job_Experiment.EXPERIMENT;
			args = args2;
		}

		/*
		 * String val = ResourceFile.getFilenameOnly(args[1]).toLowerCase(); if
		 * (!!!val.startsWith("exp")) { logEventStatus(JobEvent.ERROR,
		 * "Experiment_Console missing experiment file"); return; }
		 * setName(val);
		 */
		super.run(args);
	}

	public static void main(String args[])
	{
		(new Experiment_Console()).run(args);
	}
}
