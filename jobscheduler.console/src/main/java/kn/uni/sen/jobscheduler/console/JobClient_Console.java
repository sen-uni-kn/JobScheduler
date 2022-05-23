/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.console;

import java.util.LinkedList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.JobLibrary;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.core.impl.JobClientAbstract;
import kn.uni.sen.jobscheduler.core.impl.JobDescriptionStruct;
import kn.uni.sen.jobscheduler.core.impl.JobGraph_File;
import kn.uni.sen.jobscheduler.core.impl.JobServerAbstract;
import kn.uni.sen.jobscheduler.core.model.JobDescription;
import kn.uni.sen.jobscheduler.core.model.JobRun;

public class JobClient_Console extends JobClientAbstract
{
	String JobName;
	String JobLibraryName;

	protected JobGraph_File JobGraph;
	ResourceFileXml XmlFile;

	protected JobDescription Job;

	List<String> searchPathList = new LinkedList<>();

	public JobClient_Console(EventHandler father, String name, String folder)
	{
		super(father, name, folder);
	}

	protected boolean checkParameters()
	{
		if ((JobGraph == null) && (Job == null))
		{
			System.out.println("No Job or JobGraph is given!");
			return false;
		}
		return true;
	}

	protected void addJob(String jobName2)
	{
		Job = new JobDescriptionStruct(jobName2);

		if (JobGraph == null)
		{
			XmlFile = new ResourceFileXml();
			XmlFile.setFilePath(getFolderText() + "/JobGraph.xmi");
			JobGraph = new JobGraph_File("42", XmlFile);
		}
		JobGraph.addJob(Job);
	}

	private void addSearchPath(String arg)
	{
		if ((arg == null) || (arg.isEmpty()))
			return;
		searchPathList.add(arg);
	}

	private void addJobGraph(String input)
	{
		// JobGraph = new ResourceFileXml(input);
	}

	protected void addResultFolder(String folder)
	{
		setFolder(new ResourceFolder(folder));
	}

	private void addInput(String input)
	{
		/*
		 * if (Job != null) { // todo: parse input String type = ""; String name
		 * = ""; String value = ""; // job.addInput(res, name); }
		 */
	}

	@Override
	public JobLibrary loadJobLibrary(String pathJar, String libClassName)
	{
		JobLibrary lib = null;
		try
		{
			lib = super.loadJobLibrary(".", libClassName);
		} catch (Exception ex)
		{

		}
		for (String path : searchPathList)
		{
			if (lib != null)
				break;
			try
			{
				lib = super.loadJobLibrary(path, libClassName);
			} catch (Exception ex)
			{

			}
		}
		if (lib == null)
		{
			System.out.println("Warning: Library " + libClassName + " could not be loaded!");
		}
		return lib;
	}

	protected void run()
	{
		if (JobGraph != null)
		{
			JobGraph.save();
		}

		JobServerAbstract server = new JobServer_Console("Console", getFolderText());

		// JobSession session = server.createSession("");

		JobRun run = server.createRun(null);

		for (JobLibrary lib : libraryList)
			run.addLibrary(lib);

		// 4. run graph by JobScheduler
		run.setJobGraph(XmlFile);
		if (run instanceof JobRun_Console)
			((JobRun_Console) run).setJobGraph(JobGraph);
		run.start(null);

		// 5. wait until JobScheduler finished
	}

	public static void main(String[] args)
	{
		JobClient_Console client = new JobClient_Console(null, "JobClient", "result");
		// 1. parse arguments (e.g. library, jobName, input arguments for job)
		String par = "";
		for (String arg : args)
		{
			if (arg.startsWith("-"))
			{
				par = arg;
				continue;
			}
			String par2 = par;
			par = "";
			if (par2.isEmpty())
				continue;

			if (par2.startsWith("-p"))
			{
				client.addSearchPath(arg);
			}

			if (par2.startsWith("-l"))
			{
				JobLibrary jobLibrary = client.loadJobLibrary(".", arg);
				client.addJobLibrary(jobLibrary);
			}

			if (par2.startsWith("-j"))
			{
				client.addJob(arg);
			}

			if (par2.startsWith("-i"))
			{
				client.addInput(arg);
			}

			if (par2.startsWith("-g"))
			{
				client.addJobGraph(arg);
			}

			if (arg.startsWith("-f "))
			{
				client.addResultFolder(arg);
			}
		}

		if (!!!client.checkParameters())
			return;

		client.run();
	}
}
