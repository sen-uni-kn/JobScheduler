/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import kn.uni.sen.jobscheduler.common.impl.NodeXmlHandler;
import kn.uni.sen.jobscheduler.common.model.ResourceInterface;

public class NodeXmlHandler_Description extends NodeXmlHandler<ResourceDescription>
{
	String originalName = "";
	List<ResourceDescription> resourceList = new LinkedList<>();

	@Override
	public NodeXmlHandler<?> beginElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException
	{
		if (qName.compareTo("Resource") == 0)
		{
			ResourceDescription newResource = parseResourceDescription(qName, attributes);
			if (newResource != null)
				resourceList.add(newResource);
			return this;
		}
		return null;
	}

	@Override
	public NodeXmlHandler<?> finishElement(String uri, String localName, String qName) throws SAXException
	{
		// original name is closed -> close xmlHandler
		if (qName.compareTo(originalName) == 0)
			return null;
		if (qName.compareTo("Resource") == 0)
		{
			resourceList.remove(resourceList.size() - 1);
			if (!resourceList.isEmpty())
				return this;
		}
		return this;
	}

	void parseTags(ResourceDescription descr, String tags)
	{
		String[] tagList = tags.replace(" ", "").split(",");
		for (String tagStr : tagList)
		{
			tagStr = tagStr.toUpperCase();
			ResourceTag tag = ResourceTag.valueOf(tagStr);
			if (tag == null)
				tag = ResourceTag.UNDEFINED;

			if (tag != ResourceTag.UNDEFINED)
				descr.addTag(tag);
		}
	}

	// parses the resource according to the specifications
	public ResourceDescription parseResourceDescription(String qName, Attributes attributes)
	{
		ResourceDescription res = createResourceNode(qName, attributes);
		if (res == null)
			return null;

		// String refID = attributes.getValue("ref");
		// if ((refID != null) && (!!!refID.isEmpty()))
		// res.setReference(refID);
		String tags = attributes.getValue("tag");
		if ((tags != null) && (!!!tags.isEmpty()))
			parseTags(res, tags);
		return res;
	}

	public static Element createSaxElement(ResourceDescription descr)
	{
		Element element = new Element("resource");
		ResourceInterface res = descr.getChild();

		if ((descr != null) && (descr.getType() != null))
		{
			element.setAttribute("type", descr.getType().name());
		} else if (descr.getType() != null)
			element.setAttribute("type", descr.getType().name());

		if(res!=null && (res instanceof ResourceAbstract))
		{
			String val = ((ResourceAbstract) res).getData();
			if(val!=null)
				element.setAttribute("value", val);
		}
		return element;
	}

	@Override
	public Element createSaxRoot()
	{
		return new Element("root");
	}

	@Override
	public void addSaxContentList(Element ele)
	{
		for (ResourceDescription res : resourceList)
			ele.addContent(createSaxElement(res));
	}

	public ResourceDescription createResourceNode(String qName, Attributes attributes)
	{
		String name = attributes.getValue("name");
		if (name == null)
			return null;

		String typeText = attributes.getValue("type");
		ResourceType type = null;
		if (typeText != null)
		{
			String resType = typeText.toUpperCase();
			type = ResourceType.valueOf(resType);
		}

		ResourceDescription descr = new ResourceDescription(name, type);
		String data = attributes.getValue("default");
		if (data != null)
		{
			ResourceAbstract def = ResourceAbstract.createResource(name, type);
			def.setData(data);
			descr.setDefault(def);
		}

		return descr;

		// String id = attributes.getValue("id");
		// if (id == null)
		// id = attributes.getValue("ID");

		/*
		 * originalName = qName; switch (type) { case FILE: ResourceFile resf =
		 * null; resf = new ResourceFile(); return resf; case FILE_XML:
		 * ResourceFileXml resfx = null; resfx = new ResourceFileXml(); return
		 * resfx; case FOLDER: ResourceFolder resfo = null; resfo = new
		 * ResourceFolder(); return resfo; case STRING: ResourceString ress =
		 * null; ress = new ResourceString(); return ress; case URL: ResourceUrl
		 * resu = null; resu = new ResourceUrl(); return resu; case BOOL:
		 * ResourceBool resb = null; resb = new ResourceBool(); return resb;
		 * case INTEGER: ResourceInteger resi = null; resi = new
		 * ResourceInteger(); return resi; case DOUBLE: ResourceDouble resfl =
		 * null; resfl = new ResourceDouble(); return resfl; case POINTER:
		 * ResourcePointer resp = null; resp = new ResourcePointer(); return
		 * resp; case ENUM: ResourceEnum rese = null; rese = new ResourceEnum();
		 * return rese; case LIST: ResourceList resl = null; resl = new
		 * ResourceList(); return resl; case VECTOR: ResourceVector resv = null;
		 * resv = new ResourceVector(); return resv; default: return null; }
		 */
	}

	@Override
	public List<ResourceDescription> getRootList()
	{
		return resourceList;
	}

	public void setRootList(List<ResourceDescription> rootList)
	{
		resourceList = rootList;
	}

	@Override
	public void setRoot(ResourceDescription root)
	{
		resourceList.clear();
		resourceList.add(root);
	}
}
