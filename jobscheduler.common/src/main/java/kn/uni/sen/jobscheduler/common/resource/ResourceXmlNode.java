/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import java.util.LinkedList;
import java.util.List;
import org.jdom2.Element;

import kn.uni.sen.jobscheduler.common.resource.ResourceXmlNode;

/**
 * Handles XMI node to parse an element from tree or write it todo: check if not
 * deprecated
 */
public class ResourceXmlNode extends ResourceAbstract
{
	Element saxElement = null;
	String data = null;

	public class XmlAttribute
	{
		public XmlAttribute(String name, String value)
		{
			Name = name;
			Value = value;
		}

		public String Name = "";
		public String Value = "";
	}

	// list with attributes of this node
	protected List<XmlAttribute> AttributeList = new LinkedList<XmlAttribute>();
	// list with children of this node
	protected List<ResourceXmlNode> ChildList;

	public ResourceXmlNode(String name)
	{
		super(name);
	}

	public ResourceXmlNode(ResourceXmlNode node)
	{
		super(node);
	}

	@Override
	public String getPrivateData()
	{
		if (data != null)
			return data;
		return null;
	}

	public String getNodeText()
	{
		String text = "<" + this.Name;
		for (XmlAttribute attr : AttributeList)
		{
			String name = attr.Name;
			String value = attr.Value;
			if ((name == null) || (value == null))
				continue;
			text += " " + name + "='" + value + "'";
		}

		text += ">";
		String d = getData();
		if (d != null)
			text += d;

		if (ChildList != null)
		{
			for (ResourceXmlNode node : ChildList)
			{
				if (node == null)
					continue;
				String ct = node.getNodeText();
				if (ct == null)
					continue;
				text += "\n" + ct;
			}
			text += "\n";
		}
		text += "</" + this.Name + ">";
		return text;
	}

	@Override
	protected void setPrivateData(String data)
	{
		this.data = data;
	}

	public ResourceType getType()
	{
		return ResourceType.XML_NODE;
	}

	public void addAttribute(String name, String value)
	{
		XmlAttribute ele = new XmlAttribute(name, value);
		AttributeList.add(ele);
	}

	public String getAttribute(String name)
	{
		for (XmlAttribute attr : AttributeList)
		{
			if (attr.Name.compareTo(name) == 0)
				return attr.Value;
		}
		return null;
	}

	public void addChild(ResourceXmlNode child)
	{
		if (ChildList == null)
			ChildList = new LinkedList<>();
		ChildList.add(child);
	}

	public List<ResourceXmlNode> getChildren(String name)
	{
		if (name.compareTo("*") == 0)
			return ChildList;

		List<ResourceXmlNode> list = new LinkedList<ResourceXmlNode>();
		for (ResourceXmlNode node : ChildList)
			if (node.getName().compareTo(name) == 0)
				list.add(node);
		return list;
	}

	public List<ResourceXmlNode> getChildren()
	{
		if (ChildList == null)
			return new LinkedList<ResourceXmlNode>();
		return ChildList;
	}

	public Element getSaxElement()
	{
		return null;
	}

	public ResourceAbstract clone()
	{
		return new ResourceXmlNode(this);
	}

	public List<XmlAttribute> getAttributeList()
	{
		return this.AttributeList;
	}
}
