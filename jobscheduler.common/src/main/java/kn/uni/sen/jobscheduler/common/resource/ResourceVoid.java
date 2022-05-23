/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import kn.uni.sen.jobscheduler.common.resource.ResourceVoid;

public class ResourceVoid extends ResourceAbstract
{
	public ResourceVoid(String name)
	{
		super(name);
	}

	public ResourceVoid(ResourceVoid res)
	{
		super(res);
	}

	@Override
	public ResourceAbstract clone()
	{
		return new ResourceVoid(this);
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

	public ResourceType getType()
	{
		return ResourceType.VOID;
	}
}
