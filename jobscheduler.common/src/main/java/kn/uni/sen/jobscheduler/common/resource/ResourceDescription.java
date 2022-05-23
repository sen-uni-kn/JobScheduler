/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import kn.uni.sen.jobscheduler.common.model.ResourceInterface;

public class ResourceDescription extends ResourceAbstract
{
	ResourceType Type = ResourceType.UNKNOWN;

	// handle of resource owner
	OwnerResource Owner = null;
	ResourceInterface Default;
	ResourceAbstract Value;
	boolean result = false;

	public ResourceDescription(String name)
	{
		super(name);
	}

	public ResourceDescription(String name, String defaultValue)
	{
		super(name);
		ResourceAbstract def = ResourceAbstract.createResource(name, Type);
		def.setData(defaultValue);
		Default = def;
	}

	public ResourceDescription(String name, ResourceType type)
	{
		super(name);
		this.Type = type;
	}

	public ResourceDescription(String name, ResourceType type, String defaultValue)
	{
		this(name, defaultValue);
		this.Type = type;
	}

	public void setResult(boolean result)
	{
		this.result = result;
	}

	/**
	 * @return true if description describes a job result
	 */
	public boolean getResult()
	{
		return result;
	}

	public ResourceDescription(ResourceDescription dup)
	{
		this(dup.getName(), dup.getType());
		Default = dup.Default;
	}

	public void setDefault(ResourceInterface defaultValue)
	{
		Default = defaultValue;
	}

	public void setOwner(OwnerResource owner)
	{
		// System.out.print(getName() + " " + owner.getClass().getSimpleName() +
		// "\n");
		this.Owner = owner;
	}

	@Override
	public ResourceInterface getChild()
	{
		return getResource();
	}

	public ResourceInterface getResource()
	{
		ResourceInterface res = null;
		if ((Value != null) && (result || Value.checkAvailable()))
			res = Value;
		if ((res == null) && (child != null))
			res = child;
		if (Owner != null)
			res = Owner.getResource(getName(), result);
		if ((res == null) && (Default != null))
			res = Default;

		if ((res == null) || !!!(res instanceof ResourceAbstract) || (res.getType() == Type))
			return res;
		// todo: Bug forgets to parse other options if set
		ResourceAbstract res2 = null;
		while (res != null)
		{
			ResourceAbstract res3 = ResourceAbstract.createResource(Name, Type);
			res3.setData(((ResourceAbstract) res).getData());
			if (res2 == null)
				res2 = res3;
			else
				res2.addNext(res3);
			res = res.getNext();
		}
		return res2;
	}

	public OwnerResource getOwner()
	{
		return Owner;
	}

	public ResourceInterface getDefault()
	{
		return Default;
	}

	public void setData(ResourceAbstract in)
	{
		// if (in.getType() != Type)
		// return;

		ResourceAbstract res = ResourceAbstract.createResource(Name, Type);
		res.setData(in.getData());
		if (hasTag(ResourceTag.LIST))
		{
			if (Value != null)
				res.setNext(Value);
		}
		Value = res;
	}

	@Override
	public ResourceType getType()
	{
		return ResourceType.DESCRIPTION;
	}

	public ResourceType getDescrType()
	{
		return Type;
	}

	@Override
	public ResourceAbstract clone()
	{
		return new ResourceDescription(this);
	}

	@Override
	protected void setPrivateData(String data)
	{
		ResourceStub res = new ResourceStub(Name);
		res.setData(data);
		Value = res;
	}

	@Override
	protected String getPrivateData()
	{
		if (Value == null)
			return null;
		return Value.getData();
	}

	@SuppressWarnings("unchecked")
	public <T extends ResourceInterface> T getResourceWithType()
	{
		ResourceInterface res = getChild();
		if ((res == null) || (res.getType() != getType()))
			return null;
		return (T) res;
	}

	public static void setOwner(ResourceInterface r, OwnerResource owner)
	{
		while (r != null)
		{
			if (r instanceof ResourceDescription)
				((ResourceDescription) r).setOwner(owner);
			r = r.getNext();
		}
	}
}
