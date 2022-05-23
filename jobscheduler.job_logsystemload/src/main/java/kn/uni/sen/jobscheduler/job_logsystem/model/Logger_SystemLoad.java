/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.job_logsystem.model;

import kn.uni.sen.jobscheduler.common.model.JobState;

/**
 * Regularly save all added resources(ram, cpu) of the machine a process is
 * running on.
 */
public interface Logger_SystemLoad
{
	/**
	 * Send new command to the logger what to do.
	 * 
	 * @param cmd
	 */
	public boolean sendCommand(JobState request);

	/**
	 * regularly called to check if datas have to be saved log data
	 */
	public abstract void checkLogData();

	/**
	 * called to save actual state to
	 */
	public abstract void logData();

	/**
	 * Add new element to the list of elements to be logged
	 * 
	 * @param logElement
	 *            the new element
	 */
	public abstract void addElement(SystemResource logElement);

	/**
	 * set name of file where datas are logged to
	 * 
	 * @param fileName
	 */
	public void setLogFile(String fileName);

	/**
	 * @return name of file where datas are logged to
	 */
	public String getLogFile();
}
