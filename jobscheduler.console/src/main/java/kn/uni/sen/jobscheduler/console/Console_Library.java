/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.console;

import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.impl.RunContextSimple;
import kn.uni.sen.jobscheduler.common.impl.LogConsole;
import kn.uni.sen.jobscheduler.common.impl.NodeXmlHandler_Resource;
import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.RunContext;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.JobLibrary;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;
import kn.uni.sen.jobscheduler.common.resource.ResourceString;
import kn.uni.sen.jobscheduler.experiment.Experiment_Console;

/**
 * Runs an arbitrary job by console with context Stores results and loads values
 * from other jobs
 * 
 * @author Martin Koelbl
 */
public abstract class Console_Library extends ConsoleAbstract
{
	private String jobName = null;
	private String expName = null;
	private List<String> jobArgs = new ArrayList<>();
	String ID = "";

	protected RunContext context = RunContextSimple.createDummy("Console");

	// context of job to get parameter
	// protected List<JobLibrary> LibList = new ArrayList<>();
	JobLibrary Library = null;
	protected List<String> StoreList = new ArrayList<>();
	ResourceFileXml Destinyfile = new ResourceFileXml();
	String Run = "";
	List<ResourceAbstract> rootList = null;

	public Console_Library(JobLibrary library)
	{
		super(null, "ConsoleLibrary", "result");
		Library = library;
		getEventHandler().subscribe(new LogConsole());
	}

	public List<String> getExperimentList()
	{
		return new ArrayList<>();
	}

	abstract protected Experiment_Console createExperiment(String name, List<String> args);

	void parseParameter(String[] args)
	{
		if ((args == null) || (args.length == 0))
		{
			bShowHelp = true;
			return;
		}

		int counter = 0;
		String last = null;
		// parse all arguments for software
		for (; (counter < args.length); counter++)
		{
			if ((jobName != null) || (expName != null))
			{
				jobArgs.add(args[counter]);
			} else if (args[counter].startsWith("-"))
			{
				String arg = args[counter];
				if (!!!parseArg(arg))
					last = arg;
				continue;
			} else if ((last == null) && (args[counter].startsWith("Job")))
			{
				jobName = args[counter];
			} else if ((last == null) && (args[counter].startsWith("exp")))
			{
				expName = args[counter];
			}
		}
	}

	@Override
	void printVersion()
	{
		System.out.println(Library.getLibraryName() + ": " + Library.getLibaryVersion());
	}

	@Override
	void printList()
	{
		printVersion();
		List<Class<? extends Job>> jobList = Library.getJobList();
		for (Class<? extends Job> jobClass : jobList)
		{
			Job job = Library.createJob(jobClass.getSimpleName(), null, null);
			if (job == null)
				continue;
			System.out.println(job.getJobName() + ": " + job.getJobVersion());
		}
		for (String exp : getExperimentList())
			if (exp != null)
				System.out.println("Experiment: " + exp);
	}

	protected List<String> createHelp(List<String> helpList)
	{
		if (helpList == null)
			helpList = new ArrayList<>();
		helpList.add("-h / --help: this help text");
		helpList.add("-v / --version: version of library");
		helpList.add("-l / --list: list of jobs and experiments in library");
		helpList.add("-destiny: file to store results");
		helpList.add("-store: reference to values to store");
		helpList.add("experiment_name -run: create + run experiment");
		helpList.add("job_name --help: help text of job");
		// helpList.add("--verbose: amount of output on console
		// (0=nothing,1=normal,2=debug)");
		return helpList;
	}

	@Override
	public void printHelp()
	{
		for (String line : createHelp(null))
		{
			System.out.println(line);
		}
		System.out.println();
	}

	boolean runExperiment(String expName)
	{
		// expName = ResourceFile.getFilenameOnly(expName);
		if (expName == null)
			return false;
		Experiment_Console exp = createExperiment(expName, jobArgs);
		if (exp == null)
		{
			System.out.println("Error: Experiment was not created");
			return false;
		}
		exp.run(jobArgs.toArray(new String[] {}));
		return true;
	}

	void clean(String[] args)
	{
		for (int i = 0; i < args.length; i++)
		{
			String val = args[i];
			if (val.startsWith("\n"))
			{
				val = val.substring(1);
				args[i] = val;
			}
		}
	}

	public void run(String[] args)
	{
		clean(args);
		// parseParameter(args);

		String[] jobArgs = parseArguments(args);
		if (runSimple())
			// exclusively print information or run job
			return;
		if (expName != null)
		{
			if (runExperiment(expName))
				return;
			if (verbose > 1)
				System.out.println("Error with experiment " + expName);
			printHelp();
			return;
		} else if (jobName == null)
		{
			bShowHelp = true;
			runSimple();
			return;
		}

		// run job
		Console_Job2 jobCon = new Console_Job2(getEventHandler(), "LibraryJob", getFolderText());
		jobCon.setVerbose(verbose);
		jobCon.addLibrary(Library);
		if (verbose >= 2)
		{
			System.out.print("Call " + jobName);
			for (String s : jobArgs)
				System.out.print(" " + s);
			System.out.print("\n");
		}
		jobCon.run(jobArgs);
		storeResult(jobCon);
	}

	private List<ResourceAbstract> loadResultFile()
	{
		rootList = null;
		NodeXmlHandler_Resource handler = new NodeXmlHandler_Resource();
		Destinyfile.setHandler(handler);
		rootList = null;
		if (Destinyfile.readFile())
			rootList = handler.getRootList();
		if (rootList == null)
			rootList = new ArrayList<>();
		return rootList;
	}

	private void storeResult(Console_Job2 conJob)
	{
		Destinyfile.openFile(false);
		String run = Run;
		if (!!!ID.isEmpty())
			run += "/" + ID;
		ResourceAbstract jobRes = getJobRes(run);
		if (jobRes == null)
			return;
		// jobRes.addAttr("id", id);

		for (String store : StoreList)
		{
			ResourceInterface res = conJob.getResult(store);
			if (res instanceof ResourceAbstract)
				jobRes.addNext((ResourceAbstract) res);
		}
		NodeXmlHandler_Resource handler = new NodeXmlHandler_Resource();
		handler.setRootList(rootList);
		Destinyfile.setHandler(handler);
		Destinyfile.writeFile();
	}

	private ResourceAbstract getJobRes(String run)
	{
		if (rootList == null)
			loadResultFile();
		if ((run == null) || (run.isEmpty()))
			return new ResourceString(ID);
		String[] path = run.split("/");

		// get root element
		ResourceAbstract res = null;
		for (ResourceInterface r : rootList)
		{
			if (path[0].equals(r.getName()))
			{
				res = (ResourceAbstract) r;
				break;
			}
		}
		if (res == null)
		{
			res = new ResourceString();
			res.setName(path[0]);
			rootList.add(res);
		}

		// get existing path
		int i = 1;
		for (; i < path.length; i++)
		{
			ResourceAbstract n = (ResourceAbstract) res.getNextByName(path[i]);
			if (n != null)
				res = n;
			else
				break;
		}
		// create rest of path
		for (; i < path.length; i++)
		{
			ResourceAbstract next2 = new ResourceString();
			next2.setName(path[i]);
			next2.setParent(res);
			res.addNext(next2);
			res = next2;
		}
		return res;
	}

	private String[] parseArguments(String[] args)
	{
		// todo: parse -lib
		int j = 0;
		String opt = null;
		for (; j < args.length; j++)
		{
			String val = args[j];
			if (val.startsWith("-"))
			{
				if (parseArg(val))
					continue;
				opt = val;
				continue;
			}
			if (opt != null)
			{
				if ("-store".equals(opt))
					StoreList.add(val);
				else if ("-lib".equals(opt))
					loadLibrary(val);
				else if ("-destiny".equals(opt))
					Destinyfile.setData(val);
				else if ("-run".equals(opt))
					Run = val;
				else if ("-id".equals(opt))
					ID = val;
				opt = null;
			} else if (val.startsWith("Job"))
			{
				jobName = val;
				break;
			} else if (val.startsWith("experiment"))
			{
				expName = val;
				break;
			}
		}

		List<String> argList = new ArrayList<>();
		for (; j < args.length; j++)
		{
			String arg = args[j];
			String argVal = loadValue(arg);
			argList.add(argVal);
		}
		return argList.toArray(new String[] {});
	}

	private String loadValue(String arg)
	{
		int end = arg.indexOf("}");
		int start = arg.lastIndexOf("{", end);
		if ((end < 0) || (start < 0))
			return arg;
		int last = arg.lastIndexOf("/", end);
		String name = "";
		String path = "";
		if (last >= 0)
		{
			path = arg.substring(start + 1, last);
			name = arg.substring(last + 1, end);
		} else
			name = arg.substring(start, end);

		ResourceAbstract jobRes = getJobRes(path);
		ResourceInterface next = jobRes;
		while (next != null)
		{
			if (next.getName().equals(name) && (next instanceof ResourceAbstract))
				return ((ResourceAbstract) next).getData();
			next = next.getNext();
		}
		return null;
	}

	private void loadLibrary(String libPath)
	{
		// todo: implement
		context.logEventStatus(JobEvent.WARNING, "Load of an JobLibrary is not yet implemented.");
	}

}
