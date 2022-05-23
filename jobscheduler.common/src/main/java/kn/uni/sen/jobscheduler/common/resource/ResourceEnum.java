/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import java.util.List;

import kn.uni.sen.jobscheduler.common.resource.ResourceEnum;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Enum has a string list and the value can only be from this string list
 */
public class ResourceEnum extends ResourceString
{
	List<String> possibleEnumList = new ArrayList<String>();

	public ResourceEnum()
	{
		super();
		possibleEnumList = new LinkedList<String>();
	}

	public ResourceEnum(ResourceEnum res)
	{
		super(res);
		for (String pos : res.getPossibleEnum())
			possibleEnumList.add(pos);
	}

	public ResourceEnum(Enum<?> res)
	{
		super();
		setPossibleData(res.name());
		setData(res.name());
	}

	public ResourceEnum(String data)
	{
		super();
		setPossibleData(data);
		setData(data);
	}

	@Override
	public ResourceType getType()
	{
		return ResourceType.ENUM;
	}

	public static ResourceEnum createEnum(String val)
	{
		ResourceEnum en = new ResourceEnum();
		en.setPossibleData(val);
		en.setData(val);
		return en;
	}

	@Override
	protected void setPrivateData(String data)
	{
		boolean found = false;
		for (String val : possibleEnumList)
			if (val.compareTo(data) == 0)
			{
				found = true;
				super.setPrivateData(val);
			}
		if (!!!found)
		{
			setPossibleData(data);
			setPrivateData(data);
		}
	}

	public void setPossibleData(String val)
	{
		possibleEnumList.add(new String(val));
	}

	public List<String> getPossibleEnum()
	{
		return possibleEnumList;
	}

	public void setValue(Enum<?> res)
	{
		setData(res.name());
	}
}
