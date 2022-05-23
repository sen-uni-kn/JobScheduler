/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import kn.uni.sen.jobscheduler.common.resource.ResourceInteger;

/**
 * Resource saves math value from type integral numbers
 */
public class ResourceInteger extends ResourceAbstract
{
	Integer Value = Integer.valueOf(0);

	public ResourceInteger()
	{
		super();
	}

	public ResourceInteger(ResourceInteger res)
	{
		super(res);
		Value = Integer.valueOf(res.Value);
	}

	public ResourceInteger(int value)
	{
		super();
		Value = value;
	}

	@Override
	public String getPrivateData()
	{
		return Value.toString();
	}

	@Override
	protected void setPrivateData(String data)
	{
		Value = Integer.parseInt(data);
	}

	public ResourceType getType()
	{
		return ResourceType.INTEGER;
	}

	public ResourceType getDataType()
	{
		return getType();
	}

	public ResourceAbstract clone()
	{
		return new ResourceInteger(this);
	}

	public int getDataValue()
	{
		return Integer.parseInt(getData());
	}
}
