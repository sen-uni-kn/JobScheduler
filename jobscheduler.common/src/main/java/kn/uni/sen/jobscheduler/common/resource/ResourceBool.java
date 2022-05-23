/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import kn.uni.sen.jobscheduler.common.resource.ResourceBool;

/**
 * Boolean resource can only save true or false.
 */
public class ResourceBool extends ResourceAbstract
{
	boolean value = false;

	public ResourceBool()
	{
		super();
	}

	public ResourceBool(ResourceBool res)
	{
		super(res);
		value = res.value;
	}

	public ResourceType getType()
	{
		return ResourceType.BOOL;
	}

	@Override
	protected void setPrivateData(String data)
	{
		value = Boolean.parseBoolean(data);
	}

	@Override
	public String getPrivateData()
	{
		return "" + value;
	}

	public ResourceAbstract clone()
	{
		return new ResourceBool(this);
	}

	public void setDataValue(boolean val)
	{
		value = val;
	}
	
	public boolean getDataValue()
	{
		return value;
	}
}
