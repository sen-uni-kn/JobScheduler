/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.OwnerResource;

public class JobDataInput implements OwnerResource
{
	List<String> nameList = new ArrayList<>();
	Map<String, ResourceInterface> inList = new HashMap<>();

	public void add(String name, ResourceInterface res)
	{
		name = getUniqueName(name);
		if (name == null)
			return;
		inList.put(name, res);
	}

	@Override
	public boolean setResource(String name, ResourceInterface res)
	{
		add(name, res);
		return true;
	}

	protected String getUniqueName(String name)
	{
		if (name == null)
			return null;
		for (String n : nameList)
			if (name.equals(n))
				return n;
		nameList.add(name);
		return name;
	}

	@Override
	public ResourceInterface getResource(String name, boolean out)
	{
		name = getUniqueName(name);
		if (name == null)
			return null;
		return inList.get(name);
	}
}
