/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import java.util.List;

import org.jdom2.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NodeXmlHandler_XMI<T> extends NodeXmlHandler<T>
{
	NodeXmlHandler<T> handlerChild;

	public NodeXmlHandler_XMI(NodeXmlHandler<T> nodeXmlHandler_Job)
	{
		this.handlerChild = nodeXmlHandler_Job;
	}

	@Override
	public List<T> getRootList()
	{
		return null;
	}

	@Override
	public void setRootList(List<T> rootList)
	{
		handlerChild.setRootList(rootList);
	}

	@Override
	public void setRoot(T root)
	{
		handlerChild.setRoot(root);
	}

	@Override
	public NodeXmlHandler<?> beginElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException
	{
		// todo: parse documentation
		// todo: parse xmi
		if (qName.compareTo("xmi:XMI") == 0)
			return handlerChild;
		return null;
	}

	@Override
	public NodeXmlHandler<?> finishElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName.compareTo("xmi:XMI") == 0)
			return null;
		return this;
	}

	@Override
	public Element createSaxRoot()
	{
		Element ele = new Element("xmi");
		addSaxContentList(ele);
		return ele;
	}

	@Override
	public void addSaxContentList(Element ele)
	{
		handlerChild.addSaxContentList(ele);
	}
}
