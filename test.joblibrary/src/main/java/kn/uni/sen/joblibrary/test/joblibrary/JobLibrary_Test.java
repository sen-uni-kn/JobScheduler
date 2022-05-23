/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.joblibrary.test.joblibrary;

import kn.uni.sen.joblibrary.test.job_bye.JobBye;
import kn.uni.sen.joblibrary.test.job_hello.JobHello;
import kn.uni.sen.jobscheduler.common.impl.JobLibraryAbstract;

/** Test library for jobscheduler to test
 */
public class JobLibrary_Test extends JobLibraryAbstract
{
	public JobLibrary_Test()
	{
		JobList.add(JobHello.class);
		JobList.add(JobBye.class);
	}

	public String getLibaryVersion()
	{
		return "1.0";
	}
}
