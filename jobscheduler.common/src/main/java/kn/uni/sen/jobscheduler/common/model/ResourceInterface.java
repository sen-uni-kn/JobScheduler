/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

import kn.uni.sen.jobscheduler.common.resource.ResourceTag;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

/**
 * interface for all resource values to implement
 */
public interface ResourceInterface
{
	ResourceType getType();

	public void addTag(ResourceTag tag);

	public void setName(String name);

	String getName();
	
	void notify(ResourceInterface res);

	/**
	 * @return true if tag is set
	 */
	boolean hasTag(ResourceTag tag);
	
	ResourceInterface getNext();

	ResourceInterface getNextByName(String name);

	void setNext(ResourceInterface r);

	void addNext(ResourceInterface r);

	void removeNext(ResourceInterface r);

	ResourceInterface getBefore();

	void setBefore(ResourceInterface r);

	ResourceInterface getChild();

	ResourceInterface getChildByName(String name);

	void addChild(ResourceInterface r);

	void removeChild(ResourceInterface r);

	ResourceInterface getParent();

	void setParent(ResourceInterface par);
	
	public <T extends ResourceInterface> T getNextByType();
}
