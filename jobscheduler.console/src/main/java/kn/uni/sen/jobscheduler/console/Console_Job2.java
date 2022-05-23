/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.console;

import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.impl.LogConsole;
import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.JobLibrary;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.model.Version;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceStub;
import kn.uni.sen.jobscheduler.core.impl.JobThread;

/***
 * Parses the job arguments from command line and starts a job
 * 
 * @author Martin Koelbl
 */
public class Console_Job2 extends ConsoleAbstract
{
	protected List<JobLibrary> LibList = new ArrayList<>();
	protected Job job = null;
	// protected ResourceList inputList = new ResourceList("input");

	// EventHandler eventHandler = new EventHandlerAbstract(null, "Console");

	public Console_Job2(EventHandler father, String name, String folder)
	{
		super(father, name, folder);
		if (father == null)
			eventHandler.subscribe(new LogConsole());
	}

	@Override
	void printVersion()
	{
		System.out.println(Version.getVersion());
	}

	@Override
	void printHelp()
	{
		printVersion();
		System.out.println("-h / --help: this help text");
		System.out.println("-v / --version: version of job");
		System.out.println("-l / --list: list of parameters in job\n");
	}

	@Override
	void printList()
	{
		// for(JobLibrary lib: LibList)
		if (job == null)
			return;
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

	public void addLibrary(JobLibrary lib)
	{
		LibList.add(lib);
	}

	protected void run(String[] args)
	{
		if (args.length == 0)
		{
			printHelp();
			return;
		}
		String jobName = args[0];
		job = createJob(jobName, null);
		if (job == null)
		{
			logEventStatus(JobEvent.ERROR, "Error with job " + jobName);
			printHelp();
			return;
		}
		// parse job arguments
		parseJobInput(args, 0, job.getInputDescription());
		if (runSimple())
			// exclusively print information or run job
			return;
		// parseJobArguments(inputParameter);
		// event.setVerbose(verbose);
		// ResourceDescription.setOwner(d, inputList);

		// EventHandler handler = new EventHandlerAbstract(null, "console");
		// handler.subscribe(new LogConsole(verbose));
		JobThread thread = new JobThread(this, null, job, eventHandler);
		thread.startJobSingle();
		while ((thread.getState() != JobState.FINISHED) && (thread.getState() != JobState.ENDING))
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
			}
	}

	protected Job createJob(String name, String version)
	{
		for (JobLibrary lib : LibList)
		{
			return lib.createJob(name, version, this);
		}
		return null;
	}

	/*
	 * private void parseJobArguments(List<ResourceStub> resList) {
	 * ResourceInterface d = job.getInputDescription(); for (ResourceStub res :
	 * resList) { ResourceInterface descr = d.getNextByName(res.getName()); if
	 * ((descr == null) || !!!(descr instanceof ResourceDescription)) continue;
	 * logEventStatus(JobEvent.DEBUG, "Input " + res.getName() + " " +
	 * res.getData()); ((ResourceDescription) descr).setData(res); } }
	 */

	private ResourceInterface parseJobInput(String[] args, int start, ResourceInterface descr)
	{
		ResourceInterface first = null;
		for (int j = start; j < args.length; j++)
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
			String par = name.substring(1);
			ResourceStub res = new ResourceStub(par);
			if (!!!val.isEmpty())
				res.setData(val);

			ResourceInterface r = descr.getNextByName(par);
			if (r != null)
				r.addChild(res);
			else if (job != null)
				System.out.println("Unkown parameter: " + par);
			// inputList.addResource(res);
			// else
		}
		return first;
	}

	public void setVerbose(int verbose)
	{
		this.verbose = verbose;
	}

	public ResourceInterface getResult(String store)
	{
		ResourceInterface d = job.getResultDescription();
		while (d != null)
		{
			if (store.equals(d.getName()) || (store.equals("*")))
			{
				ResourceInterface res = job.getResource(d.getName(), true);
				if (res instanceof ResourceAbstract)
					((ResourceAbstract) res).setName(d.getName());
				return res;
			}
		}
		return null;
	}
}
