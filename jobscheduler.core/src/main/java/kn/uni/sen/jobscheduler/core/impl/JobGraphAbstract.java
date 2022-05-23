/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.core.model.JobGraph;

public class JobGraphAbstract extends JobDescriptionStruct implements JobGraph
{
	JobDescriptionStruct rootNodeGraph;
	NodeXmlHandler_Job handler;

	//List<ResourceDescription> listResource = new ArrayList<>();

	public JobGraphAbstract(String id)
	{
		super(id);
		handler = new NodeXmlHandler_Job(this);
	}

	/**
	 * Normalize the graph so that each Job has direct references - Example: no
	 * child resource is directly referencing of sibling's child resource
	 */
	public void normalizeGraphReferences()
	{
	}

	@Override
	public String getFileName(String filename)
	{
		return filename;
	}

	public void addResource(ResourceAbstract res, String name)
	{
		ResourceDescription descr = new ResourceDescription(name, res.getType());
		descr.setDefault(res);
		addInput(descr);
	}

	@Override
	public void save()
	{
	}

	@Override
	public void load()
	{
	}
}
