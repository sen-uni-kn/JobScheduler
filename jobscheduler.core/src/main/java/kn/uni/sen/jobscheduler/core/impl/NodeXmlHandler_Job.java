/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import kn.uni.sen.jobscheduler.common.impl.NodeXmlHandler;
import kn.uni.sen.jobscheduler.common.impl.NodeXmlHandler_XMI;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.NodeXmlHandler_Description;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.core.model.JobDescription;

//the inner class that extends the xml handler to override start and end
// element
// a better solution must be found how to handle the tree structure problem
// because the current node and the root node must be located outside of
// this class
class NodeXmlHandler_Job extends NodeXmlHandler<JobDescriptionStruct>
{
	JobDescriptionStruct rootNode = new JobDescriptionStruct("RootJob");
	JobDescriptionStruct currentNode = rootNode;

	public NodeXmlHandler_Job(JobDescriptionStruct jobGraphAbstract)
	{
		currentNode = jobGraphAbstract;
	}

	public NodeXmlHandler<?> beginElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException
	{
		// if the starting element is a job
		if (qName.equals("job"))
		{
			// create a tree structure here
			JobDescriptionStruct newNode = extractNodeXml(currentNode, qName, attributes);
			currentNode.addJob(newNode);
			currentNode = newNode;
			return this;
		}

		// if the starting element is a resource
		if (qName.equals("input") || qName.equals("result") || qName.equals("monitor"))
		{
			// the extract function for resources is located in the
			// Resource class
			NodeXmlHandler_Description resourceParser = new NodeXmlHandler_Description();
			ResourceDescription newNode = resourceParser.parseResourceDescription(qName, attributes);
			if (newNode == null)
				return null;

			// add the different resources to the specific list in the
			// JobDescriptionStruct: InputList, ResultList, MonitorList
			if (qName.equals("input"))
				currentNode.addInput(newNode);
			if (qName.equals("result"))
				currentNode.addResult(newNode);
			return resourceParser;
		}
		return null;
	}

	public JobDescriptionStruct extractNodeXml(JobDescriptionStruct parent, String qName, Attributes attributes)
	{
		/////////////////// debug purpose
		System.out.println("********************************");
		String jobid = attributes.getValue("id");
		System.out.println("Job ID : " + jobid);
		// jobDescription.add("Job ID : " + jobid);
		String jobname = attributes.getValue("jobname");
		System.out.println("Job Name : " + jobname);
		// jobDescription.add("Job Name : " + jobname);
		String jobtag = attributes.getValue("tag");
		System.out.println("Job Tag : " + jobtag);
		// jobDescription.add("Job Name : " + jobtag);

		// currentNodeGlobal.SetParentJob(parent);
		JobDescriptionStruct parentJob = currentNode;
		if (parentJob == null)
		{
			System.out.println("This is Root, no parent");
		} else
		{
			System.out.println(
					"Parent: " + parentJob.getJobName() + " " + parentJob.getJobId() + " " + parentJob.getJobTag());
		}
		//////////////////// debug end
		JobDescriptionStruct jobDescription = new JobDescriptionStruct(jobname);
		jobDescription.setJobTag(jobtag);
		jobDescription.setJobId(jobid);
		jobDescription.setParentJob(parent);
		// currentNodeGlobal = currentNode;
		// rootNodeGlobal = xmlTreeRoot;

		return jobDescription;
	}

	public static void addChild(JobDescriptionStruct currentNode, JobDescriptionStruct newNode)
	{
		currentNode.addJob(newNode);
	}

	public void addChildRoot(JobDescription newNode)
	{
		rootNode.addJob(newNode);
	}

	public NodeXmlHandler<?> finishElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName.equals("job") && (currentNode != null))
		{
			currentNode = currentNode.getParentResource();
		}
		return this;
	}

	@Override
	public List<JobDescriptionStruct> getRootList()
	{
		List<JobDescriptionStruct> list = new LinkedList<JobDescriptionStruct>();
		list.add(rootNode);
		return list;
	}

	@Override
	public Element createSaxRoot()
	{
		Element root = new NodeXmlHandler_XMI<JobDescriptionStruct>(this).createSaxRoot();
		// if (!eleList.isEmpty())
		// addSaxContentList(eleList.get(0));
		return root;
	}

	@Override
	public void addSaxContentList(Element ele)
	{
		Element root = createElement_Job(rootNode);
		for (Element child : root.getChildren())
		{
			child.detach();
			ele.addContent(child);
		}
	}

	public static Element createElement_Job(JobDescription job)
	{
		Element ele = new Element("Job");

		String tmp = job.getJobName();
		if ((tmp != null) && (!tmp.isEmpty()))
			ele.setAttribute("JobName", tmp);

		tmp = job.getName();
		if ((tmp != null) && (!tmp.isEmpty()))
			ele.setAttribute("Name", tmp);

		// todo: add tags

		ResourceInterface d = job.getInputList();
		while (d != null)
		{
			if (d instanceof ResourceDescription)
				ele.addContent(createElement_Resource("input", (ResourceDescription) d));
			d = d.getNext();
		}

		d = job.getResultList();
		while (d != null)
		{
			if (d instanceof ResourceDescription)
				ele.addContent(createElement_Resource("result", (ResourceDescription) d));
			d = d.getNext();
		}

		d = job.getJobList();
		while (d != null)
		{
			if (d instanceof JobDescription)
				ele.addContent(createElement_Job((JobDescription) d));
			d = d.getNext();
		}
		return ele;
	}

	public static Element createElement_Resource(String eleName, ResourceDescription descr)
	{
		Element ele = NodeXmlHandler_Description.createSaxElement(descr);
		String name = descr.getName();
		if (name != null)
			ele.setAttribute("name", name);
		name = descr.getOwnerName();
		if (name == null)
			descr.setOwnerName(name);
		return ele;
	}

	@Override
	public void setRootList(List<JobDescriptionStruct> rootList)
	{
		rootNode = new JobDescriptionStruct("RootJob");
		for (JobDescription job : rootList)
			rootNode.addJob(job);
	}

	@Override
	public void setRoot(JobDescriptionStruct root)
	{
		rootNode = new JobDescriptionStruct("RootJob");
		rootNode.addJob(root);
	}
}
