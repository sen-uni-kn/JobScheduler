/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.experiment;

import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;

public class ExpJob
{
	String Name;
	String ID;
	List<ExpResource> inputList = new ArrayList<>();
	List<ExpResource> resultList = new ArrayList<>();

	static int count = 1;

	public ExpJob(String name)
	{
		this(name, "job" + (++count));
		Name = name;
	}

	public ExpJob(String name, String id)
	{
		Name = name;
		ID = id;
	}

	public void addInput(ExpResource res)
	{
		inputList.add(res);
	}

	public void addResult(ExpResource res)
	{
		resultList.add(res);
	}

	public String getName()
	{
		return Name;
	}

	public String getID()
	{
		return ID;
	}

	private ExpResource getResource(String id, List<ExpResource> list)
	{
		if (id == null)
			return null;
		for (ExpResource res : list)
			if (res.getID().compareTo(id) == 0)
				return res;
		return null;
	}

	public List<ExpResource> getStoreList()
	{
		List<ExpResource> list = new ArrayList<>();
		for (ExpResource res : inputList)
			if (res.isStore())
				list.add(res);
		for (ExpResource res : resultList)
			if (res.isStore())
				list.add(res);
		return list;
	}

	/**
	 * @param folder
	 * @param parList
	 * @return String with all parameters to call a job
	 */
	public String createParameter(String folder, List<ExpResource> parList)
	{
		// for (ExpResource res : parList)
		// System.out.print(" " + res.getID() + res.getValue());
		// System.out.print("\n");

		String text = "";
		for (ExpResource in : inputList)
		{
			// System.out.println(in.getName());
			ExpResource inValue = getResource(in.getRef(), parList);
			String value = null;
			if (inValue != null)
				value = inValue.getValue();
			if (value == null)
				value = in.getValue();
			if (value == null)
				continue;
			text += " -" + in.getName() + " " + value;
		}

		for (ExpResource res : resultList)
		{
			ExpResource inValue = getResource(res.getRef(), parList);
			String tmp = "";
			if ("file".equals(res.getSrc()))
			{
				if (res.getRef() == null)
					tmp = folder + ResourceFolder.getSplitSign();
				else if (inValue != null)
					setResultFolder(folder, inValue);
			}

			String value = null;
			if (inValue != null)
				value = inValue.getValue();
			if (value == null)
				value = res.getValue();
			if (value == null)
				continue;
			text += " -" + res.getName() + " " + tmp + value;
		}
		return text;
	}

	private void setResultFolder(String folder, ExpResource res)
	{
		String val = res.getValue();
		if (val == null)
			return;
		res.setValue(ResourceFolder.appendFolder(folder, val));
	}
}
