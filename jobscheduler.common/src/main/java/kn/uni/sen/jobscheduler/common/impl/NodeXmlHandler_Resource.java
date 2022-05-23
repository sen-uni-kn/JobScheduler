/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.jdom2.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.common.resource.ResourceTag;
import kn.uni.sen.jobscheduler.common.resource.ResourceType;

public class NodeXmlHandler_Resource extends NodeXmlHandler<ResourceAbstract>
{
	List<ResourceAbstract> rootList = new ArrayList<>();
	ResourceAbstract curRes = null;
	Stack<ResourceAbstract> stack = new Stack<>();

	@Override
	public List<ResourceAbstract> getRootList()
	{
		return rootList;
	}

	@Override
	public void setRootList(List<ResourceAbstract> rootList)
	{
		this.rootList = rootList;
	}

	@Override
	public void setRoot(ResourceAbstract root)
	{
	}

	@Override
	public NodeXmlHandler<?> beginElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException
	{
		if (qName == null)
			return this;
		if (qName.equals("resource"))
		{
			String type = attributes.getValue("type");
			String name = attributes.getValue("name");
			String value = attributes.getValue("value");
			ResourceAbstract father = curRes;
			if (curRes != null)
				stack.add(curRes);

			ResourceType type2 = ResourceType.UNKNOWN;
			try
			{
				type2 = ResourceType.valueOf(type);
			} catch (Exception ex)
			{
				System.out.println("Error: parse resource type " + type);
			}

			curRes = ResourceAbstract.createResource(name, type2);
			curRes.setData(value);
			if (father == null)
				rootList.add(curRes);
			
			father.setNext(curRes);
		}
		return this;
	}

	@Override
	public NodeXmlHandler<?> finishElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName == null)
			return this;
		if (qName.equals("resource"))
		{
			if (!!!stack.isEmpty())
				curRes = stack.pop();
			else
				curRes = null;
		}
		return this;
	}

	private Element createSaxElement(ResourceAbstract res)
	{
		if (res == null)
			return null;

		Element element = new Element("resource");
		element.setAttribute("type", res.getType().name());

		String val = res.getName();
		if (val != null)
			element.setAttribute("name", val);

		val = res.getData();
		if (val != null)
			element.setAttribute("value", val);

		//todo: check if important
		//val = res.getAttr("ID");
		//if (val != null)
		//	element.setAttribute("ID", val);

		if (res.hasTag(ResourceTag.LIST))
		{
			ResourceInterface next = res.getNext();
			if ((next != null) && (next instanceof ResourceAbstract))
			{
				Element n = createSaxElement((ResourceAbstract) next);
				element.setContent(n);
			}
		}
		return element;
	}

	@Override
	public Element createSaxRoot()
	{
		Element root = new Element("root");
		for (ResourceAbstract res : rootList)
		{
			Element ele = createSaxElement(res);
			if (ele != null)
				root.addContent(ele);
		}
		return root;
	}

	@Override
	protected void addSaxContentList(Element ele)
	{
	}
}
