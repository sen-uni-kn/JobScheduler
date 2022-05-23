/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.impl;

public class JobEventStatus extends JobEventAbstract
{
	String name = "";

	public JobEventStatus(String tag, String text)
	{
		this(tag, "", text);
	}

	public JobEventStatus(String tag, String name, String text)
	{
		super(text);
		addTag(tag);
		this.name = name;
	}

	@Override
	public String getText()
	{
		if ((name == null) || (name.isEmpty()))
			return eventText;
		return name + ":" + eventText;
	}
}
