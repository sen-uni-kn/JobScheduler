/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.console;

import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.impl.EventHandlerAbstract;
import kn.uni.sen.jobscheduler.common.impl.LogConsole;
import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.common.resource.ResourceStub;
import kn.uni.sen.jobscheduler.core.impl.JobThread;

/***
 * Parses the arguments from command line and starts a job
 * 
 * @author Martin Koelbl
 */
@Deprecated
public class Console_Job extends ConsoleAbstract
{
	protected Job job = null;

	protected String[] Args;

	public Console_Job(Job job, String[] args)
	{
		super(null, "console", "result");
		this.job = job;
		this.Args = args;
	}

	@Override
	void printVersion()
	{
		System.out.println(job.getJobName() + ": " + job.getJobVersion());
	}

	@Override
	void printHelp()
	{
		printVersion();
		System.out.println("-h / --help: this help text");
		System.out.println("-v / --version: version of job");
		System.out.println("-destiny: file to store results");
		System.out.println("-store: reference to values to store");
		System.out.println("-l / --list: list of parameters in job\n");
	}

	@Override
	void printList()
	{
		ResourceInterface d = job.getInputDescription();
		System.out.println("Input Parameters for Job " + job.getJobName() + ":");
		if (d == null)
			System.out.println("No input parameters!");
		while (d != null)
		{
			System.out.println("" + d.getName());
			d = d.getNext();
		}
	}

	protected void run()
	{
		parseArguments(Args);
		if (runSimple())
			// exclusively print information or run job
			return;
		// event.setVerbose(verbose);
		EventHandler handler = new EventHandlerAbstract(null, "console");
		handler.subscribe(new LogConsole(verbose));
		JobThread thread = new JobThread(this, null, job, handler);
		thread.startJob();
	}

	private void parseArguments(String[] jobArgs)
	{
		List<ResourceStub> resList = parseResources(jobArgs);

		ResourceInterface dFirst = job.getInputDescription();
		for (ResourceStub res : resList)
		{
			ResourceInterface d = dFirst.getNextByName(res.getName());
			if ((d == null) || !!!(d instanceof ResourceDescription))
				continue;
			if (verbose == 2)
			{
				System.out.println("Input " + res.getName() + " " + res.getData());
			}
			((ResourceDescription) d).setData(res);
		}
	}

	private List<ResourceStub> parseResources(String[] args)
	{
		List<ResourceStub> resList = new ArrayList<>();
		for (int j = 0; j < args.length; j++)
		{
			String arg = args[j];
			if (parseArg(arg))
				continue;

			// get resource name
			String name = args[j];
			if (!!!name.startsWith("-"))
				continue;
			String val = "";
			// compute whole argument for resource
			for (j++; j < args.length; j++)
			{
				String s = args[j];
				if (s.startsWith("-"))
				{
					j--;
					break;
				}
				if (!!!val.isEmpty())
					val += " ";
				val += s;
			}
			ResourceStub res = new ResourceStub(name.substring(1));
			if (!!!val.isEmpty())
				res.setData(val);
			resList.add(res);
		}
		return resList;
	}

	public void setVerbose(int verbose)
	{
		this.verbose = verbose;
	}
}
