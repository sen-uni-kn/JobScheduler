/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

public class ResourceDate extends ResourceAbstract
{
	String date;

	public ResourceDate()
	{
	}

	public ResourceDate(ResourceDate resourceDate)
	{
		date = resourceDate.getData();
	}

	@Override
	public ResourceType getType()
	{
		return ResourceType.DATE;
	}

	@Override
	public ResourceAbstract clone()
	{
		return new ResourceDate(this);
	}

	@Override
	protected void setPrivateData(String date)
	{
		this.date = date;
	}

	@Override
	protected String getPrivateData()
	{
		return this.date;
	}
}
