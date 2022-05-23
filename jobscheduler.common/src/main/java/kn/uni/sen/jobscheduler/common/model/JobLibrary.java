/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

import java.util.List;

/**
 * library is an abstraction of a library that can be called when a library is
 * used all jobs are read by this interface and then can be called
 */
public interface JobLibrary
{
	/**
	 * @return version of this library
	 */
	public String getLibaryVersion();

	/**
	 * @return name of this library
	 * 
	 */
	public String getLibraryName();

	/**
	 * @return with all jobs contains in this library
	 */
	public List<Class<? extends Job>> getJobList();

	/**
	 * @return with all jobs contains in this library
	 */
	public List<Class<? extends ResourceInterface>> getResourceList();

	/**
	 * @param JobName
	 *            name of job class to instantiate.
	 * @param Version
	 *            of job class to instantiate or empty.
	 * @return single job from this library
	 */
	public Job createJob(String JobName, String Version, RunContext handle);

	/**
	 * @param ResourceClassName
	 *            name of resource class to instantiate.
	 * @param Version
	 *            of resource class to instantiate or empty.
	 * @return resource from this library
	 */
	public ResourceInterface createResource(String ResourceClassName, String Version);

}
