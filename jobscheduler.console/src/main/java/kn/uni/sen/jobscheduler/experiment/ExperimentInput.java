/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.experiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExperimentInput
{
	Map<ExpResource, Integer> DepthMap = new HashMap<>();
	List<ExpResource> ResList = new ArrayList<>();

	public ExperimentInput Clone()
	{
		ExperimentInput dup = new ExperimentInput();
		for (ExpResource res : ResList)
		{
			int depth = DepthMap.get(res);
			dup.addParameter(res, depth);
		}
		return dup;
	}

	public int getResourceDepth(String id)
	{
		ExpResource res = getResource(id);
		Integer val = DepthMap.get(res);
		if (val == null)
			return -1;
		return val;
	}

	public void addParameter(ExpResource res, int depth)
	{
		// remove parameter with same name
		removeResource(res.getID());
		// add parameter
		ResList.add(res);
		DepthMap.put(res, depth);
	}

	ExpResource getResource(String id)
	{
		for (ExpResource r : ResList)
			if (r.getID().compareTo(id) == 0)
			{
				return r;
			}
		return null;
	}

	void removeResource(String id)
	{
		ExpResource r = getResource(id);
		while (r != null)
		{
			ResList.remove(r);
			DepthMap.remove(r);
			r = getResource(id);
		}
	}

	List<ExpResource> getResourceList()
	{
		return ResList;
	}

	public ExpResource getResource(Object res)
	{
		for (ExpResource r : ResList)
		{
			if (r == res)
				return r;
		}
		return null;
	}
}
