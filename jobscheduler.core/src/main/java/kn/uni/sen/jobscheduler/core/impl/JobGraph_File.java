/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import java.util.LinkedList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;
import kn.uni.sen.jobscheduler.core.model.JobDescription;

public class JobGraph_File extends JobGraphAbstract
{
	ResourceFileXml fileXml;

	public JobGraph_File(String id, ResourceFileXml file)
	{
		super(id);
		this.fileXml = file;
	}

	JobDescriptionStruct createJobGraph()
	{
		JobDescriptionStruct jobStruct = new JobDescriptionStruct("root");
		ResourceInterface j = jobList;
		while (j != null)
		{
			if (j instanceof JobDescription)
				jobStruct.addJob((JobDescription) j);
			j = j.getNext();
		}
		return jobStruct;
	}

	public void save()
	{
		fileXml.setHandler(handler);

		List<JobDescriptionStruct> list = new LinkedList<>();
		if (rootNodeGraph != null)
		{
			list.add(rootNodeGraph);
			handler.setRootList(list);
		} else
		{
			ResourceInterface j = jobList;
			while (j != null)
			{
				if (j instanceof JobDescription)
					handler.addChildRoot((JobDescription) j);
				j = j.getNext();
			}
		}

		fileXml.writeFile();
		// fileXml.closeFile();
	}

	public void saveTo(ResourceFileXml fileTo)
	{
		fileTo.setHandler(handler);

		List<JobDescriptionStruct> list = new LinkedList<>();
		list.add(rootNodeGraph);
		handler.setRootList(list);

		fileTo.writeFile();
	}

	public void load()
	{
		fileXml.setHandler(handler);
		fileXml.readFile();
	}
}
