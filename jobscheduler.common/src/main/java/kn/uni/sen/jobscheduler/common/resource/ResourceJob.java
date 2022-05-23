/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

public class ResourceJob extends ResourceAbstract
{
	@Override
	public ResourceType getType()
	{
		return ResourceType.JOB;
	}

	@Override
	public ResourceAbstract clone()
	{
		return null;
	}

	@Override
	protected void setPrivateData(String data)
	{
	}

	@Override
	protected String getPrivateData()
	{
		return null;
	}
}
