/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import kn.uni.sen.jobscheduler.common.model.ResourceInterface;

public interface OwnerResource
{
	boolean setResource(String name, ResourceInterface res);

	ResourceInterface getResource(String name, boolean out);
}
