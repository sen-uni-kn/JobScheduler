/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import kn.uni.sen.jobscheduler.common.impl.NodeXmlHandler;

import java.util.List;
import java.util.Stack;

import org.jdom2.Element;
import org.xml.sax.Attributes;

/**
 * this is a general handler for xml files, more specific handlers can be
 * derived from it (e.g. deviations for jobs and resources) this handler reads
 * in xml files, and parses them
 * 
 * @author Sirui Liu
 *
 */
public abstract class NodeXmlHandler<T> extends DefaultHandler
{
	protected Stack<NodeXmlHandler<?>> parserList = new Stack<NodeXmlHandler<?>>();

	public abstract List<T> getRootList();
	
	/** set multiple root elements
	 * 
	 * @param root
	 */
	public abstract void setRootList(List<T> rootList);

	/** set single root element
	 * 
	 * @param root
	 */
	public abstract void setRoot(T root);

	private NodeXmlHandler<?> getCurrentParser()
	{
		return parserList.isEmpty() ? null : parserList.lastElement();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		NodeXmlHandler<?> currentParser = getCurrentParser();
		NodeXmlHandler<?> newParser = null;
		if (currentParser != null)
			newParser = currentParser.beginElement(uri, localName, qName, attributes);
		else
			newParser = beginElement(uri, localName, qName, attributes);
		if ((newParser != null) && (currentParser != newParser))
		{
			parserList.push(currentParser);			
			currentParser = newParser;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		NodeXmlHandler<?> currentParser = getCurrentParser();
		if (currentParser == null)
		{
			finishElement(uri, localName, qName);
			return;
		}
		NodeXmlHandler<?> newParser = currentParser.finishElement(uri, localName, qName);
		if ((newParser != currentParser) || (newParser == null))
			parserList.pop();
	}

	public abstract NodeXmlHandler<?> beginElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException;

	public abstract NodeXmlHandler<?> finishElement(String uri, String localName, String qName) throws SAXException;

	/** Creates list of root elements (usually xml/xmi)
	 * 
	 * @return list of elements that are contained by the current node, but only
	 *         different nodes (by name) are allowed
	 */
	public abstract Element createSaxRoot();

	/** Add all content of current node to the node ele
	 * 
	 * @param ele parent node
	 */
	protected abstract void addSaxContentList(Element ele);
}
