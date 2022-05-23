/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.experiment;

import java.util.ArrayList;
import java.util.List;

public class Experiment
{
	List<ExpResource> ResList = new ArrayList<>();
	List<ExpJob> JobList = new ArrayList<>();

	String Name;
	String AnalysisName;
	String Destiny;

	public void addJob(ExpJob job)
	{
		JobList.add(job);
	}

	public List<ExpJob> getJobList()
	{
		return JobList;
	}

	public void addResource(ExpResource res)
	{
		ResList.add(res);
	}

	public List<ExpResource> getResourceList()
	{
		return ResList;
	}

	public List<List<ExpResource>> createParameterCombinationList()
	{
		List<ExperimentInput> combinationList = new ArrayList<>();
		combinationList.add(new ExperimentInput());

		// add all resource of depht 1
		PriorityQueueExplicit<ExpResource> queue = new PriorityQueueExplicit<>();
		for (ExpResource r : ResList)
			queue.add(r, 1);

		while (!!!queue.isEmpty())
		{
			PriorityQueueExplicit<ExpResource>.Pair pair = queue.poll();
			ExpResource res = pair.res;
			int depth = pair.depth;
			// System.out.println(res.getID() + " " + depth);
			// add resource of resource to queue
			for (ExpResource r : res.getResourceList())
				queue.add(r, depth + 1);

			// add every
			List<ExperimentInput> orgList = cloneList(combinationList);
			for (ExperimentInput inList : orgList)
			{
				ExpResource father = res.getFather();
				if ((father != null) && (inList.getResource(father) == null))
					// ignore since father not contained
					continue;

				int d = inList.getResourceDepth(res.getID());
				if (depth < d)
					// should not happen
					continue;
				else if ((d == depth) && (depth == 1))
				{
					// ExpResource er = inList.getResource(res.getID());
					// same name and depth clone list and replace parameter
					inList = inList.Clone();
					inList.addParameter(res, depth);
					if (!!!contains(inList, combinationList))
						combinationList.add(inList);
					continue;
				}
				// add or replace parameter
				inList.addParameter(res, depth);
			}
		}

		// create list with combinations
		List<List<ExpResource>> combListList = new ArrayList<>();
		for (ExperimentInput inList : combinationList)
			combListList.add(inList.getResourceList());

		return combListList;
	}

	boolean compareResource(ExpResource r1, ExpResource r2)
	{
		if (r1 == null)
			return false;
		String val1 = r1.getValue();
		if ((val1 == null) || !!!val1.equals(r2.getValue()))
			return false;
		for (ExpResource c1 : r1.getResourceList())
		{
			boolean f = false;
			for (ExpResource c2 : r2.getResourceList())
			{
				if (compareResource(c2, c1))
				{
					f = true;
					break;
				}
			}
			if (!!!f)
				return false;
		}
		for (ExpResource c2 : r2.getResourceList())
		{
			boolean f = false;
			for (ExpResource c1 : r1.getResourceList())
			{
				if (compareResource(c2, c1))
				{
					f = true;
					break;
				}
			}
			if (!!!f)
				return false;
		}
		return true;
	}

	private boolean contains(ExperimentInput inList, List<ExperimentInput> combinationList)
	{
		for (ExperimentInput in : combinationList)
		{
			boolean equiv = true;
			// subset comparison
			for (ExpResource res : inList.getResourceList())
			{
				ExpResource r = inList.getResource(res.getID());
				if (!!!compareResource(res, r))
				{
					equiv = false;
					break;
				}
			}
			// subset comparison other direction
			for (ExpResource res : in.getResourceList())
			{
				ExpResource r = inList.getResource(res.getID());
				if (!!!compareResource(res, r))
				{
					equiv = false;
					break;
				}
			}
			if (equiv)
				return true;
		}
		return false;
	}

	private List<ExperimentInput> cloneList(List<ExperimentInput> list)
	{
		List<ExperimentInput> dupList = new ArrayList<>();
		for (ExperimentInput in : list)
			dupList.add(in);
		return dupList;
	}

	public void setName(String name)
	{
		this.Name = name;
	}

	public String getName()
	{
		return Name;
	}

	public void setAnalysisName(String name)
	{
		this.AnalysisName = name;
	}
	
	public void setDestiny(String destiny)
	{
		Destiny = destiny;
	}

	public String getDestiny()
	{
		return Destiny;
	}

	public String getAnalysisName()
	{
		return AnalysisName;
	}
}
