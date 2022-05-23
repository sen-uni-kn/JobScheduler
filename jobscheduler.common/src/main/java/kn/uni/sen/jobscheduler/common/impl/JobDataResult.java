/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import java.util.HashMap;
import java.util.Map;

import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.OwnerResource;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;

public class JobDataResult extends JobDataInput implements OwnerResource
{
	protected Map<String, ResourceInterface> resultList = new HashMap<>();

	public void storeResults(Job job)
	{
		ResourceInterface d = job.getResultDescription();
		d = d.getChild();
		while (d != null)
		{
			if (d instanceof ResourceDescription)
			{
				ResourceInterface res = d.getChild();
				String name = getUniqueName(d.getName());
				if ((name != null) && !!!name.isEmpty())
					resultList.put(name, res);
			}
		}
	}
}
