/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import java.util.LinkedList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.model.ResourceInterface;

public abstract class ResourceImpl implements ResourceInterface
{
	String Name;
	List<ResourceTag> TagList = new LinkedList<ResourceTag>();
	// Map<String, String> AttrMap = null;
	
	ResourceInterface next = null;
	ResourceInterface before = null;
	ResourceInterface child = null;
	protected ResourceInterface parent = null;

	@Override
	public void addTag(ResourceTag tag)
	{
		TagList.add(tag);
	}
	
	@Override
	public boolean hasTag(ResourceTag tag)
	{
		for (ResourceTag tagEle : TagList)
			if (tag == tagEle)
				return true;
		return false;
	}

	@Override
	public void setName(String name)
	{
		Name = name;
	}

	@Override
	public String getName()
	{
		return Name;
	}

	
	@Override
	public void setParent(ResourceInterface parent)
	{
		// if (parent != null)
		// System.out.println("Setting parent resource " + parent.getName());
		this.parent = parent;
	}
	
	@Override
	public ResourceInterface getParent()
	{
		return parent;
	}

	@Override
	public void setNext(ResourceInterface value)
	{
		if (next != null)
		{
			ResourceInterface n = next.getBefore();
			if (n != null)
				next.setBefore(null);
		}
		next = value;
	}

	@Override
	public void addNext(ResourceInterface value)
	{
		ResourceInterface f = this;
		while (f.getNext() != null)
			f = f.getNext();

		if (f instanceof ResourceAbstract)
			((ResourceAbstract) f).setNext(value);
	}

	@Override
	public ResourceInterface getNext()
	{
		return next;
	}

	@Override
	public void setBefore(ResourceInterface value)
	{
		if (before != null)
		{
			ResourceInterface n = before.getNext();
			if (n != null)
				before.setNext(null);
		}
		next = value;
	}

	@Override
	public ResourceInterface getBefore()
	{
		return before;
	}

	@Override
	public void addChild(ResourceInterface value)
	{
		if (child == null)
		{
			child = value;
			return;
		}
		value.setParent(this);
		child.addNext(value);
	}

	@Override
	public ResourceInterface getChild()
	{
		return child;
	}

	@Override
	public ResourceInterface getChildByName(String name)
	{
		if (child == null)
			return null;
		return child.getNextByName(name);
	}

	@Override
	public ResourceInterface getNextByName(String name)
	{
		if (name == null)
			return null;
		ResourceInterface n = this;
		while (n != null)
		{
			if (name.equals(n.getName()))
				return n;
			n = n.getNext();
		}
		return null;
	}

	@Override
	public void removeNext(ResourceInterface r)
	{
		ResourceInterface res = this;
		while (res != null)
		{
			if (res == r)
			{
				ResourceInterface b = res.getBefore();
				if (b != null)
					b.setNext(next);
				return;
			}
			res = res.getNext();
		}
	}

	@Override
	public void removeChild(ResourceInterface r)
	{
		ResourceInterface res = this.getChild();
		if (res != null)
			res.removeNext(r);
	}

	public <T extends ResourceInterface> T getNextByType()
	{
		ResourceInterface res = getNext();
		while (res != null)
		{
			try
			{
				@SuppressWarnings("unchecked")
				T r = (T) res;
				return r;
			} catch (Exception ex)
			{
			}
			res = res.getNext();
		}
		return null;
	}
	
	@Override
	public void notify(ResourceInterface res)
	{
	}

	public static ResourceInterface addList(ResourceInterface list, ResourceInterface ele)
	{
		if (list == null)
			return ele;
		list.addNext(ele);
		return list;
	}
}
