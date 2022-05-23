/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.experiment;

import java.util.ArrayList;
import java.util.List;

public class ExpResource
{
	enum ResType
	{
		NONE, RESOURCE, INPUT, OUTPUT
	};

	ResType Type;
	String ID;
	String Name;
	String Ref;
	String Value;
	String Src;
	boolean Store = false;
	ExpResource Father = null;

	List<ExpResource> resList = new ArrayList<>();

	public ExpResource(ExpResource father, ResType type, String id, String value, String src)
	{
		Type = type;
		ID = id;
		Value = value;
		Father = father;
		Src = src;
	}

	public ExpResource(ResType type, String id, String name, String ref, String value, String src, String store)
	{
		this(null, type, id, value, src);
		this.Name = name;
		this.Ref = ref;
		setStore(store);
	}

	public ExpResource(ExpResource r)
	{
		this.Type = r.getType();
		this.ID = r.getID();
		this.Name = r.getName();
		this.Ref = r.getRef();
		this.Value = r.getValue();
		this.Src = r.getSrc();
		this.Father = null;
		this.Store = r.isStore();

		for (ExpResource c : resList)
			this.resList.add(new ExpResource(c));
	}

	private void setStore(String store)
	{
		if (store == null)
			return;
		this.Store = !!!(store.toLowerCase().equals("false"));
	}

	private ResType getType()
	{
		return Type;
	}

	public List<ExpResource> getResourceList()
	{
		return resList;
	}

	public String getName()
	{
		return Name;
	}

	public void addResource(ExpResource res)
	{
		resList.add(res);
	}

	public String getID()
	{
		return ID;
	}

	public String getSrc()
	{
		return Src;
	}

	public boolean isStore()
	{
		return Store;
	}

	public String getValue()
	{
		return Value;
	}

	public String getRef()
	{
		return Ref;
	}

	public ExpResource getFather()
	{
		return Father;
	}

	public void setValue(String value)
	{
		Value = value;
	}
}
