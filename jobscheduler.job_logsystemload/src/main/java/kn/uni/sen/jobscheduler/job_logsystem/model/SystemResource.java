/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.job_logsystem.model;

/**
 * @class represents an element that can be regularly logged by LogSystemLoad to
 *        save single hardware resource of the running machine.
 */
public interface SystemResource
{
	/**
	 * @return String with name of the resource to log (e.g. cpu, ram, ...)
	 */
	public abstract String getName();

	/**
	 * regularly called to check if datas have to be saved log data
	 * 
	 * @return String withdata
	 */
	public abstract String checkLogData();

	/**
	 * called to save actual state to
	 * 
	 * @return string with data
	 */
	public abstract String logData();
}
