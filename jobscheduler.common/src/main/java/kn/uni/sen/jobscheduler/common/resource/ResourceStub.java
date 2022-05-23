/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

/** Resource Dummy with data
 * 
 * @author Martin Koelbl
 */
public class ResourceStub extends ResourceAbstract
{
	String Data = "";

	public ResourceStub(String name)
	{
		super(name);
	}

	@Override
	public ResourceType getType()
	{
		return ResourceType.UNKNOWN;
	}

	@Override
	public ResourceAbstract clone()
	{
		ResourceAbstract resDup = new ResourceStub(Name);
		resDup.setPrivateData(this.getPrivateData());
		return resDup;
	}

	@Override
	protected void setPrivateData(String data)
	{
		Data = data;
	}

	@Override
	protected String getPrivateData()
	{
		return Data;
	}
}
