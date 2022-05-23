/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import org.xml.sax.Attributes;

import kn.uni.sen.jobscheduler.common.resource.ResourceString;

/**
 * Resource that contains a string.
 */
public class ResourceString extends ResourceAbstract
{
	String value;

	public ResourceString()
	{
		super();
	}

	public ResourceString(ResourceString node)
	{
		super(node);
		if (node.value != null)
			value = new String(node.value);
	}

	public ResourceString(String value)
	{
		super();
		setPrivateData(value);
	}

	@Override
	public String getPrivateData()
	{
		return value;
	}

	@Override
	protected void setPrivateData(String data)
	{
		value = null;
		if (data != null)
			value = new String(data);
	}

	public ResourceType getType()
	{
		return ResourceType.STRING;
	}

	@Override
	public ResourceAbstract clone()
	{
		return new ResourceString(this);
	}

	// parses the resource according to the specifications
	public static ResourceAbstract parseResource(String qName, Attributes attributes)
	{
		System.out.println("Parse ResourceString");
		ResourceString res = new ResourceString(qName);
		res.setData(qName);
		return res;
	}
	
	public static int countOccurrences(String z3Code, String word)
	{
		String[] list = z3Code.split(word);
		return list.length - 1;
	}
}
