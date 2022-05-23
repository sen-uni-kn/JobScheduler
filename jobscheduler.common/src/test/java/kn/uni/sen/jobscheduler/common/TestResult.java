/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common;

import kn.uni.sen.jobscheduler.common.model.Job;

public interface TestResult
{
	boolean checkResult(Job job);
}
