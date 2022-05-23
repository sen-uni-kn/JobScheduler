/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.model;

/**
 * Represent a job graph with all job that have to
 */
public interface JobGraph extends JobDescription
{
	/**
	 * Save JobGraph with all its data into a file.
	 */
	public void save();

	/**
	 * Loads JobGraph from a file.
	 */
	public void load();

	public String getFileName(String filename);
}
