/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import kn.uni.sen.jobscheduler.common.model.ResourceInterface;

public class ResourceList extends ResourceAbstract implements OwnerResource
{
	ResourceType type = ResourceType.UNDEFINED;

	public ResourceList(String name)
	{
		this(name, ResourceType.UNKNOWN);
	}

	public ResourceList(String name, ResourceType type)
	{
		setName(name);
		this.type = type;
	}

	@Override
	public ResourceType getType()
	{
		return ResourceType.LIST;
	}

	@Override
	public void addChild(ResourceInterface r)
	{
		if ((type == ResourceType.UNKNOWN) || (r.getType() == type))
			super.addChild(r);
	}

	@Override
	public ResourceAbstract clone()
	{
		// todo: implement
		return null;
	}

	@Override
	protected void setPrivateData(String data)
	{
	}

	@Override
	protected String getPrivateData()
	{
		return null;
	}

	public void addResource(ResourceInterface d)
	{
		addChild(d);
	}

	@Override
	public boolean setResource(String name, ResourceInterface res)
	{
		ResourceInterface r = getChildByName(name);
		if (r != null)
			removeChild(r);
		res.setName(name);
		addChild(res);
		return true;
	}

	@Override
	public ResourceInterface getResource(String name, boolean out)
	{
		return getChildByName(name);
	}
}
