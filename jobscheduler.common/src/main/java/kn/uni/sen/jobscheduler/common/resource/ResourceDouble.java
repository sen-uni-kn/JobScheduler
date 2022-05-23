/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import kn.uni.sen.jobscheduler.common.resource.ResourceDouble;

/**
 * Resource saves math data from type floating point
 */
public class ResourceDouble extends ResourceAbstract
{
	Double value = 0.0d;

	public ResourceDouble()
	{
		super();
	}

	public ResourceDouble(Double value)
	{
		super();
		this.value = value;
	}

	public ResourceDouble(ResourceDouble res)
	{
		super(res);
		value = res.getDataValue();
	}

	@Override
	public String getPrivateData()
	{
		return value.toString();
	}

	public Double getDataValue()
	{
		return value;
	}

	public void setDataValue(Double value)
	{
		this.value = value;
	}

	public void setDataValue(String value)
	{
		this.value = parseStringDouble(value);
	}

	protected void setPrivateData(String data)
	{
		value = Double.valueOf(data);
	}

	public ResourceType getType()
	{
		return ResourceType.DOUBLE;
	}

	public ResourceAbstract clone()
	{
		return new ResourceDouble(this);
	}

	public static String checkStringDouble(String val)
	{
		try
		{
			Double.parseDouble(val);
			return val;
		} catch (Exception e)
		{
		}
		return null;
	}

	public static double parseStringDouble(String val)
	{
		try
		{
			return Double.parseDouble(val);
		} catch (Exception e)
		{
		}
		return Double.NaN;
	}
}
