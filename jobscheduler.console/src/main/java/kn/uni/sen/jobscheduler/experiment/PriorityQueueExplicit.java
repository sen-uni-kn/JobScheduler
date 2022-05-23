/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.experiment;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class PriorityQueueExplicit<T> implements Comparator<T>
{
	protected Map<T, Integer> DepthMap = new HashMap<>();
	PriorityQueue<T> queue = new PriorityQueue<T>(this);

	public class Pair
	{
		public Integer depth;
		public T res;

		Pair(Integer d, T t)
		{
			this.depth = d;
			this.res = t;
		}
	}

	public void add(T res, int depth)
	{
		DepthMap.put(res, depth);
		queue.add(res);
	}

	public Pair poll()
	{
		T t = queue.poll();
		Integer depth = DepthMap.get(t);
		DepthMap.remove(t);
		return new Pair(depth, t);
	}

	public int getDepth(T res)
	{
		Integer val = DepthMap.get(res);
		if (val == null)
			return -1;
		return val;
	}

	@Override
	public int compare(T o1, T o2)
	{
		int d1 = getDepth(o1);
		int d2 = getDepth(o2);
		return d1 - d2;
	}

	public boolean isEmpty()
	{
		return queue.isEmpty();
	}
}
