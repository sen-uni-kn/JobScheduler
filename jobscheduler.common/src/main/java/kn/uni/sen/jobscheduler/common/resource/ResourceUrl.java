/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import kn.uni.sen.jobscheduler.common.resource.ResourceUrl;

/** Resource contains an URL in URL standard syntax.
 */
public class ResourceUrl extends ResourceAbstract
{
	String Url = "";
	
	public ResourceUrl()
	{
		super();
	}

	public ResourceUrl(ResourceUrl node)
	{
		super(node);
		Url = new String(node.Url);
	}

	@Override
	public String getPrivateData()
	{
		return Url;
	}

	@Override
	protected void setPrivateData(String data)
	{
		Url = new String(data);
	}

	public ResourceType getType()
	{
		return ResourceType.URL;
	}
	
	public ResourceAbstract clone()
	{
		return new ResourceUrl(this);
	}
}
