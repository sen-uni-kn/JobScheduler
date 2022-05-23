/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExperimentParser extends DefaultHandler
{
	class ExperimentHandler extends DefaultHandler
	{
		Experiment Exp = new Experiment();
		ExpJob Job = null;
		Stack<ExpResource> ResStack = new Stack<>();

		public ExperimentHandler()
		{
		}

		@Override
		public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
		{
			if (qName.compareTo("experiment") == 0)
			{
				String name = atts.getValue("name");
				Exp = new Experiment();
				Exp.setName(name);
			} else if (qName.compareTo("parameter") == 0)
			{
			} else if (qName.compareTo("analysis") == 0)
			{
				String name = atts.getValue("name");
				String destiny = atts.getValue("destiny");
				Exp.setAnalysisName(name);
				Exp.setDestiny(destiny);
			} else if ((qName.compareTo("resource") == 0))
			{
				String id = atts.getValue("id");
				String value = atts.getValue("value");
				String src = atts.getValue("src");
				ExpResource resFather = null;
				if (!!!ResStack.isEmpty())
					resFather = ResStack.peek();
				ExpResource res = new ExpResource(resFather, ExpResource.ResType.RESOURCE, id, value, src);
				if (resFather != null)
					resFather.addResource(res);
				else
					Exp.addResource(res);
				ResStack.push(res);
			} else if (qName.compareTo("job") == 0)
			{
				String name = atts.getValue("name");
				String id = atts.getValue("id");
				Job = new ExpJob(name, id);
				Exp.addJob(Job);
			} else if ((qName.compareTo("result") == 0) && (Job != null))
			{
				ExpResource res = parseResource(atts);
				if (res != null)
					Job.addResult(res);
			} else if ((qName.compareTo("input") == 0) && (Job != null))
			{
				ExpResource res = parseResource(atts);
				if (res != null)
					Job.addInput(res);
			}
		}

		private ExpResource parseResource(Attributes atts)
		{
			String id = atts.getValue("id");
			String name = atts.getValue("name");
			String ref = atts.getValue("ref");
			String value = atts.getValue("value");
			String src = atts.getValue("src");
			String store = atts.getValue("store");
			return new ExpResource(ExpResource.ResType.INPUT, id, name, ref, value, src, store);
		}

		public void endElement(String namespaceURI, String localName, String qName)
		{
			if (qName.compareTo("experiment") == 0)
			{
			} else if (qName.compareTo("parameter") == 0)
			{
			} else if ((qName.compareTo("input") == 0))
			{
			} else if ((qName.compareTo("resource") == 0))
			{
				ResStack.pop();
			} else if (qName.compareTo("job") == 0)
			{
				Job = null;
			} else if ((qName.compareTo("input") == 0))
			{
			} else if ((qName.compareTo("result") == 0))
			{
			}
		}

		public Experiment getExperiment()
		{
			return Exp;
		}
	}

	public Experiment parseFile(String file)
	{
		ExperimentHandler handler = new ExperimentHandler();
		if (!!!parseFile(file, handler))
			return null;
		return handler.getExperiment();
	}

	private static boolean parseFile(String file, DefaultHandler handler)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try
		{
			saxParser = factory.newSAXParser();
			saxParser.parse(new File(file), handler);
			return true;
		} catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		} catch (SAXException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			System.out.println("Error: File " + file + " not found.");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
