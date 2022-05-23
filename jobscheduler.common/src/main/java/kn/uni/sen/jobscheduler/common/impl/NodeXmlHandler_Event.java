/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import java.util.List;

import org.jdom2.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import kn.uni.sen.jobscheduler.common.model.JobEvent;

public class NodeXmlHandler_Event extends NodeXmlHandler<JobEvent>
{
	JobEvent rootEvent;

	public NodeXmlHandler_Event(JobEvent event)
	{
		rootEvent = event;
	}

	@Override
	public List<JobEvent> getRootList()
	{
		return null;
	}

	@Override
	public void setRootList(List<JobEvent> rootList)
	{
	}

	@Override
	public void setRoot(JobEvent root)
	{
		rootEvent = root;
	}

	@Override
	public NodeXmlHandler<?> beginElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException
	{
		// todo: parse documentation
		// todo: parse xmi
		return null;
	}

	@Override
	public NodeXmlHandler<?> finishElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName.compareTo("event") == 0)
			return null;
		return this;
	}

	@Override
	public Element createSaxRoot()
	{
		// Element ele = new Element("event");
		// addSaxContentList(ele);
		return createElement(rootEvent);
	}

	protected Element createElement(JobEvent event)
	{
		Element ele = new Element("event");
		if (event == null)
			return ele;
		if (event.getText() != null)
			ele.setAttribute("description", event.getText());

		List<String> tags = event.getTagList();
		if ((tags != null) && (!!!tags.isEmpty()))
		{
			String text = "";
			for (String str : tags)
			{
				if (str.isEmpty())
					continue;
				if (!!!text.isEmpty())
					tags.add(",");
				text += str;
			}
			ele.setAttribute("tags", text);
		}
		// todo: for (ResourceInterface data : event.getData())
		// {
		// ele.addContent(createElement(data));
		// }
		return ele;
	}

	@Override
	public void addSaxContentList(Element ele)
	{
	}
}
