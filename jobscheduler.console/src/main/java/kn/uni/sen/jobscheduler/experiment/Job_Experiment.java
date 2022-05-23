/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.experiment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.helper.Helper;
import kn.uni.sen.jobscheduler.common.helper.HelperConsole;
import kn.uni.sen.jobscheduler.common.impl.JobAbstract;
import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.JobResult;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.model.RunContext;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceFile;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;
import kn.uni.sen.jobscheduler.common.resource.ResourceTag;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

public class Job_Experiment extends JobAbstract
{
	public static String EXPERIMENT = "Experiment";
	public static String LIBRARY = "Library";
	public static String PRE = "Pre";
	public static String RUN = "Run";

	String Pre = "";
	ResourceFolder Folder = null;

	public Job_Experiment(RunContext parent)
	{
		super(parent);
		this.version = "1.1.0";

		createInputDescr(EXPERIMENT, ResourceType.FILE).addTag(ResourceTag.NECESSARY);
		createInputDescr(LIBRARY, ResourceType.FILE).addTag(ResourceTag.NECESSARY);
		createInputDescr(PRE, ResourceType.STRING);
		createInputDescr(RUN, ResourceType.STRING);
	}

	@Override
	public JobState task()
	{
		Folder = (ResourceFolder) getResource(FOLDER, false);
		ResourceAbstract file = getResourceWithType(EXPERIMENT, false);
		if ((file == null) || (file.getType() != ResourceType.FILE))
			return endError("Missing Input File");

		ResourceAbstract res = getResourceWithType(LIBRARY, false);
		if ((res == null) || (res.getType() != ResourceType.FILE))
			return endError("Missing Library File");
		ResourceFile lib = (ResourceFile) res;

		res = getResourceWithType(PRE, false);
		if (res != null)
			Pre = res.getData();

		logEventStatus(JobEvent.DEBUG, "-- Parse Experiment --");
		Experiment exp = (new ExperimentParser()).parseFile(file.getData());
		if (exp == null)
			return endError("Missing experiment file.");

		logEventStatus(JobEvent.DEBUG, "-- Create Experiment --");
		ResourceFile bash = new ResourceFile();
		bash.setData(file.getData());
		bash.setFolder(Folder.getData());
		bash.setExtension(".sh");
		createExperiment(exp, bash, lib);

		res = getResourceWithType(RUN, false);
		if (res != null)
		{
			logEventStatus(JobEvent.DEBUG, "-- Run Experiment --");
			runExperiment(bash.getFileName());
			logEventStatus(JobEvent.DEBUG, "-- End Experiment --");
		} else
			logEventStatus(JobEvent.DEBUG, "-- End Experiment --");
		return end(JobResult.OK);
	}

	ResourceFolder createSubFolder(ResourceFolder folder, String subFolder)
	{
		ResourceFolder anaFolder = null;
		if (folder == null)
			anaFolder = new ResourceFolder(subFolder);
		else
		{
			anaFolder = new ResourceFolder(folder.getData());
			anaFolder.appendFolder(subFolder);
		}
		anaFolder.createFolder();
		return anaFolder;
	}

	String getLibPath()
	{
		if (Helper.getOperatingSystem() == Helper.OpSys.LINUX)
		{
			return "export LD_LIBRARY_PATH=\"/lib:/usr/lib:/usr/local/lib:./lib\"\n";
		}
		return "";
	}

	private String parsePattern(String name, int index, List<ExpResource> parList)
	{
		if (name == null)
			return null;
		String nameNew = "";
		int last = 0;
		int start = name.indexOf("{");
		while (start >= 0)
		{
			int e = name.indexOf("}", start);
			if (e < 0)
				break;
			String bet = name.substring(last, start);
			nameNew += bet;
			last = e + 1;
			bet = name.substring(start + 1, last - 1);
			boolean ref = bet.startsWith("$");
			if (ref)
				bet = bet.substring(1);
			if ("index".equals(bet))
				nameNew += index;
			else
				for (ExpResource r : parList)
				{
					String n = r.getName();
					if (ref)
						n = r.getID();
					if (ref && bet.equals(n))
					{
						String val = r.getValue();
						val = val.replace(".xmi", "");
						val = val.replace(".xml", "");
						val = val.replace("/", "_");
						val = val.replace("\\", "_");
						nameNew += val;
						break;
					}
				}

			start = name.indexOf("{", last);
		}

		nameNew += name.substring(last);
		return nameNew;
	}

	private String getStoreText(String destiny, String run, ExpJob job)
	{
		String text = "";
		if ((destiny != null) && !!!destiny.isEmpty())
			text += "-destiny " + destiny + " ";
		if (run != null)
			text += "-run " + run + " ";
		if (job.getID() != null)
			text += "-id " + job.getID() + " ";

		List<ExpResource> list = job.getStoreList();
		for (ExpResource res : list)
			text += "-store $" + res.getName() + " ";

		if (!!!text.isEmpty())
			text += "\\\n";
		return text;
	}

	private void createExperiment(Experiment exp, ResourceFile bashFile, ResourceFile libFile)
	{
		bashFile.removeFile();
		String library = libFile.getData();
		if (!!!ResourceFolder.isPathAbsolute(library))
		// adjust relative path of library to bash file
		{
			String f = Folder.getData();

			int length = 1;
			String sign = ResourceFolder.getSplitSign();
			int index = f.indexOf(sign);
			while (index != -1)
			{
				index = f.indexOf(sign, index + 1);
				length++;
			}
			for (int i = 0; i < length; i++)
				library = ".." + ResourceFolder.getSplitSign() + library;
		}

		String call = "-cp";
		if ((Pre == null) || Pre.isEmpty())
			call = "-jar";
		else
			Pre = Pre + " ";

		String callJobLibrary = "java -Xmx200g " + call + " " + library + " " + Pre;

		String cmd = getLibPath() + "\n";

		int counter = 1;
		for (List<ExpResource> parList : exp.createParameterCombinationList())
		{
			parList = cloneResList(parList);
			int index = counter++;
			String runName = parsePattern(exp.getAnalysisName(), index, parList);

			String destiny = parsePattern(exp.getDestiny(), index, parList);
			ResourceFolder anaPath = createSubFolder(Folder, runName);

			copyFiles(anaPath, runName, parList);

			if (exp.getAnalysisName() != null)
				cmd += "\n# " + runName + "\n";
			for (ExpJob job : exp.getJobList())
			{
				String jobFolder = runName + ResourceFolder.getSplitSign() + job.getName();
				// ResourceFolder jobPath = createSubFolder(anaPath,
				// job.getName());
				String callJob = callJobLibrary + " \\\n";
				callJob += getStoreText(destiny, runName, job);
				callJob += job.getName();
				callJob += " -Folder " + jobFolder;
				// create parameter arguments for job
				callJob += job.createParameter(jobFolder, parList);
				cmd += callJob + "\n\n";
			}
			cmd += "\n";
		}
		bashFile.appendText(cmd);
		bashFile.appendText("\n echo \"Experiment terminated!\"");
		bashFile.writeFile();
		bashFile.setPermission(ResourceFile.Permission.EXECUTE);
	}

	private List<ExpResource> cloneResList(List<ExpResource> parList)
	{
		List<ExpResource> dupList = new ArrayList<>();
		for (ExpResource r : parList)
			dupList.add(new ExpResource(r));
		return dupList;
	}

	private void copyFiles(ResourceFolder anaPath, String anaFolder, List<ExpResource> parList)
	{
		for (ExpResource res : parList)
		{
			if (!!!"file".equals(res.getSrc()))
				continue;

			ClassLoader classLoader = getClass().getClassLoader();
			URL url = classLoader.getResource(res.getValue());
			if (url == null)
				continue;

			String aimPath = ResourceFolder.appendFolder(anaPath.getData(), res.getValue());
			anaPath.createFolder();
			ResourceFile.writeURL2File(url, aimPath);
			String anaFile = ResourceFolder.appendFolder(anaFolder, res.getValue());
			res.setValue(anaFile);
		}
	}

	private void runExperiment(String bashFile)
	{
		String text = HelperConsole.runCommand(bashFile, Folder.getData(), true);
		if ((text != null) && (!!!text.isEmpty()))
			// output text written to console
			System.out.println(text);
	}
}
