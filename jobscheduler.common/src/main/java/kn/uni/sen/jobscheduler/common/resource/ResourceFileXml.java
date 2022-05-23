/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import kn.uni.sen.jobscheduler.common.impl.NodeXmlHandler;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;

/**
 * Resource give possibility to read and write a file in XML language
 */
public class ResourceFileXml extends ResourceFile
{
	// document to write content to
	Document XmlDocument;
	// handler for parsing xml file
	// datas are inside handler
	NodeXmlHandler<?> handler;

	public ResourceFileXml()
	{
		super();
	}

	public ResourceFileXml(String filePath, NodeXmlHandler<?> handler)
	{
		super(filePath);
		this.handler = handler;
	}

	public ResourceFileXml(ResourceFileXml xml)
	{
		super(xml);
	}

	@Override
	public ResourceType getType()
	{
		return ResourceType.FILE_XML;
	}

	public void setHandler(NodeXmlHandler<?> handler)
	{
		this.handler = handler;
	}

	public NodeXmlHandler<?> getHandler()
	{
		return handler;
	}

	@Override
	public boolean readFile()
	{
		openFile(getFilePath(), false);
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(fileHandle, handler);
		} catch (Exception e)
		{
			return false;
		}
		return true;
	}

	public void deleteFile()
	{
		File file = new File(getFilePath());
		file.delete();
	}

	@Override
	public boolean writeFile()
	{
		try
		{
			if (!createFile(getFilePath(), false))
				return false;

			if (handler == null)
				return false;

			Element tree = handler.createSaxRoot();
			if (tree == null)
			{
				writer.close();
				return false;
			}

			// create xml document if not done yet
			if (XmlDocument == null)
				XmlDocument = new Document();
			XmlDocument.setContent(tree);

			// write document XMLOutputter
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			if (writer == null)
			{
				createFile(false);
				if (writer == null)
					return false;
			}

			// outputs the event inside a log file
			outputter.output(XmlDocument, writer);

			// close file
			writer.close();
			writer = null;
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * function shall write xml nodes to a xquery file in xml format
	 */
	public ResourceXmlNode getNodeData()
	{
		// no events yet
		return null;
	}

	/**
	 * function parses xml nodes from a xquery file
	 */
	public ResourceXmlNode parseNodeData(Element rootNode)
	{
		return new ResourceXmlNode("");
	}

	public ResourceAbstract clone()
	{
		return new ResourceFileXml(this);
	}
}
