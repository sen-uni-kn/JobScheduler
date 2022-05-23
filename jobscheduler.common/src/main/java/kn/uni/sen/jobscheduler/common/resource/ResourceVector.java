/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import java.util.List;
import java.util.Vector;

import kn.uni.sen.jobscheduler.common.resource.ResourceVector;

/**
 * Resource is set of resources with order.
 */
public class ResourceVector extends ResourceAbstract
{
	List<ResourceAbstract> ResourceList = new Vector<ResourceAbstract>();

	public ResourceVector()
	{
		super();
	}

	public ResourceVector(ResourceVector list)
	{
		super(list);
		for (ResourceAbstract res : list.ResourceList)
			addResource(res.clone());
	}

	public void addResource(ResourceAbstract res)
	{
		if (res == null)
			return;
		ResourceList.add(res);
	}

	public ResourceType getType()
	{
		return ResourceType.VECTOR;
	}

	@Override
	protected void setPrivateData(String data)
	{
	}

	@Override
	public String getPrivateData()
	{
		return null;
	}

	public ResourceAbstract clone()
	{
		return new ResourceVector(this);
	}
}
