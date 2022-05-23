/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common;

import java.util.TimerTask;

import kn.uni.sen.jobscheduler.common.model.Job;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ObserverResource;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceTag;

public class Test_ResultOnline extends TimerTask implements ObserverResource
{
	Job job;

	Test_ResultOnline(Job job)
	{
		this.job = job;
	}

	@Override
	public void run()
	{
		ResourceInterface d = job.getResultDescription();
		while (d != null)
		{
			if (d.hasTag(ResourceTag.ONLINE))
			{
				ResourceInterface res = d.getChild();
				if ((res != null) && (res instanceof ResourceAbstract))
					System.out.println(((ResourceAbstract) res).getData());
			}
			d = d.getNext();
		}
	}

	@Override
	public void fresh(ResourceAbstract res)
	{
		String data = res.getData();
		System.out.println("Result" + res.getName() + "Online: " + data);
	}
}
