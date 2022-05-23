/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common;

import org.junit.Test;

import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;

public abstract class Test_ResourceAbstract<T extends ResourceAbstract>
{
	T res;

	/**
	 * @return new job class of actual test
	 */
	protected abstract T createResource();

	@Test()
	public void checkData(String data)
	{
		res = createResource();
		res.setData(data);
		String ret = res.getData();
		boolean eq = ret.compareTo(data) == 0;
		assert (eq);
	}
}
