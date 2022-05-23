/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;

/**
 * Each result, input, monitor or config parameter inside a job is derived from
 * this resource. It contains the data of an parameter and gives some function
 * to work on.
 * 
 * This must be an class because interface could not save the parameter values.
 */
// todo: add generic type to class Resource<T>
// generic type is type of resource example (Integer, Float, String, ...)
public abstract class ResourceAbstract extends ResourceImpl implements ResourceInterface
{
	String OwnerName;

	// means resource was already calculated
	// necessary for check as during the check result are not really calculated
	// so other job cannot check if they have all their inputs
	protected boolean availableCalculated;

	// list of observer to notify if resource is changed
	List<ObserverResource> observerList = new ArrayList<>();

	public ResourceAbstract()
	{
		this.Name = null;
		availableCalculated = false;
	}

	public ResourceAbstract(String name)
	{
		this.Name = name;
		availableCalculated = false;
	}

	public ResourceAbstract(ResourceAbstract res)
	{
		this.Name = res.Name;
		// ID = new String(res.ID);
		availableCalculated = res.availableCalculated;
		setData(res.getData());

		List<ResourceTag> tagList = res.getTagListResource();
		if (tagList != null)
			for (ResourceTag tag : tagList)
				addTag(tag);

		Class<?> c1 = res.getClass();
		Class<?> c2 = this.getClass();
		if (c1.getName().compareTo(c2.getName()) != 0)
			System.out.println("Warning: A Resource.clone seems to be wrongly overwritten!");
	}

	public boolean hasName(String name)
	{
		if (Name == null)
			return false;
		if (Name.compareTo(name) == 0)
			return true;
		return false;
	}

	@Override
	public abstract ResourceType getType();

	public List<ResourceTag> getTagList()
	{
		return TagList;
	}

	// return if resource is available means having data
	public boolean isAvailable()
	{
		if (availableCalculated)
			return true;
		return checkAvailable();
	}

	// set or unset if resource is actually available because the result is
	// given
	public void setAvailableCalculated(boolean avail)
	{
		availableCalculated = avail;
	}
	
	/**
	 * @param return
	 *            data as string
	 */
	public synchronized String getData()
	{
		return getPrivateData();
	}

	// check if resource is actually available
	public boolean checkAvailable()
	{
		if (availableCalculated)
			return true;
		if (getData() != null)
			return true;
		return false;
	}

	// return a list with all possible tags of a resource
	public List<ResourceTag> getPossibleTags()
	{
		// function is recursive so child create their lists by this function
		// and add another possible tags
		List<ResourceTag> tagList = new LinkedList<ResourceTag>();
		tagList.add(ResourceTag.NECESSARY);
		tagList.add(ResourceTag.HIDE);
		return tagList;
	}

	public static ResourceAbstract createResource(String name, ResourceType type)
	{
		// idea: a list would here be a much better solution
		ResourceAbstract res = null;
		switch (type)
		{
		case UNKNOWN:
			res = new ResourceStub(name);
			break;
		case DESCRIPTION:
			res = new ResourceDescription(name);
			break;
		case FILE:
			res = new ResourceFile();
			break;
		case FILE_XML:
			res = new ResourceFileXml();
			break;
		case FOLDER:
			res = new ResourceFolder();
			break;
		case STRING:
			res = new ResourceString();
			break;
		case URL:
			res = new ResourceUrl();
			break;
		case BOOL:
			res = new ResourceBool();
			break;
		case INTEGER:
			res = new ResourceInteger();
			break;
		case DOUBLE:
			res = new ResourceDouble();
			break;
		case ENUM:
			res = new ResourceEnum();
			break;
		case VECTOR:
			res = new ResourceVector();
			break;
		case JOB:
			res = new ResourceJob();
			break;
		case DATE:
			res = new ResourceDate();
			break;
		default:
			res = null;
		}
		if (res == null)
			return res;
		if (name != null)
			res.setName(name);
		return res;
	}

	public List<ResourceTag> getTagListResource()
	{
		return TagList;
	}

	public void setOwnerName(String name)
	{
		OwnerName = name;
	}

	/**
	 * @return name of job owning this resource
	 */
	public String getOwnerName()
	{
		return OwnerName;
	}

	/**
	 * @return Deep clone of actual resource class
	 */
	public abstract ResourceAbstract clone();

	/**
	 * Set data as string and convert it in right format
	 * 
	 * @param data
	 */
	public synchronized void setData(String data)
	{
		setPrivateData(data);
		if (data != null)
			availableCalculated = true;
		List<ObserverResource> list2 = new ArrayList<>();
		for (ObserverResource o : observerList)
			list2.add(o);

		for (ObserverResource o : list2)
			o.fresh(this);
	}

	protected abstract void setPrivateData(String data);

	protected abstract String getPrivateData();
}
