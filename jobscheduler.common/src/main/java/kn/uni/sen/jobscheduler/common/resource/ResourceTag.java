/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

/**
 * Give some information to a resource how it is used
 * 
 * UNDEFINED: not defined (like null-pointer) HIDE: Resource or Job is not shown
 * to the user NECESSARY: Resource has to be calculated to end JobGraph with
 * success FIXED: value cannot be changed and handled like constant READONLY:
 * value/file can only be read POSSIBLE: resource/job can be available but must
 * not IGNORE: resource is not used also if available
 */
public enum ResourceTag
{
	UNDEFINED, HIDE, NECESSARY, FIXED, READONLY, POSSIBLE, IGNORE, ONLINE, UNIQUE, LIST
}
