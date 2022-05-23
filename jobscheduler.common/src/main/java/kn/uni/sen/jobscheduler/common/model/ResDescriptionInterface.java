/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

import kn.uni.sen.jobscheduler.common.resource.OwnerResource;
import kn.uni.sen.jobscheduler.common.resource.ResourceTag;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

public interface ResDescriptionInterface
{
	/**
	 * @return Name of resource description
	 */
	String getName();
	
	ResourceType getType();
	
	String getData();
	
	void setOwner(OwnerResource descr);

	ResourceInterface getResource();

	boolean hasTag(ResourceTag tag);
}
