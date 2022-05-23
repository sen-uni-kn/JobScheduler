/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

/**
 * Result of Job if JobState reached FINISHED is reached.
 * 
 * UNKOWN nothing is set.
 * 
 * OK Results are calculated and okay (results itself can be right or wrong).
 * 
 * ERROR happened during calculation.
 * 
 * BREAKED was sent during calculation.
 */
public enum JobResult
{
	UNDEFINED, OK, ERROR, BREAKED
}
