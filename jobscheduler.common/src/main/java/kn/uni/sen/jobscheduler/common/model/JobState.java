/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

/**
 * Different state that a job can have during being processed.
 * 
 * UNDEFINED means to state set (equals null)
 * 
 * START ready to check input
 * 
 * CHECKING Checks if all input variable are avaiable
 * 
 * IDLE means nothing actually to do during run.
 * 
 * RUNNING Job is running its job.
 * 
 * PAUSE Job was paused and is doing nothing at the moment.
 * 
 * RESUMING Job goes from pause back to running and currently restore all data
 * 
 * ENDING Job was ended and is doing a clean shutdown.
 * 
 * FINISHED Job has ended.
 * 
 * RESET Job is reseted and will soon be at start again.
 */
public enum JobState
{
	UNDEFINED, START, CHECKING, IDLE, RUNNING, RESUMING, PAUSE, ENDING, FINISHED, RESET
}
