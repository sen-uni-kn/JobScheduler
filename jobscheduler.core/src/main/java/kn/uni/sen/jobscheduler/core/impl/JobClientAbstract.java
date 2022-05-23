/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.core.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.impl.RunContextSimple;
import kn.uni.sen.jobscheduler.common.model.EventHandler;
import kn.uni.sen.jobscheduler.common.model.JobLibrary;
import kn.uni.sen.jobscheduler.core.model.JobClient;
import kn.uni.sen.jobscheduler.core.model.JobSession;

/**
 * @author Martin Koelbl
 */
public class JobClientAbstract extends RunContextSimple implements JobClient
{
	protected List<JobLibrary> libraryList = new ArrayList<>();

	/**
	 * Current user of session
	 */
	String user = "";
	/**
	 * Current group of session
	 */
	String group = "";

	public JobClientAbstract(EventHandler father, String name, String folder)
	{
		super(father, name, folder);
		//if (user != null)
		//	this.user = user;
		//if (group != null)
		//	this.group = group;
	}

	public void addJobLibrary(JobLibrary library)
	{
		libraryList.add(library);
	}

	public JobLibrary loadJobLibrary(String pathJar, String libClassName)
	{
		if (pathJar == null)
			return null;

		ClassLoader classLoader = getClass().getClassLoader();
		URL res = classLoader.getResource(pathJar);

		URL[] url;
		try
		{
			url = new URL[] { res };
			URLClassLoader child = new URLClassLoader(url, this.getClass().getClassLoader());

			Class<?> classToLoad = Class.forName(libClassName, true, child);
			Method method = classToLoad.getDeclaredMethod("myMethod");
			Object instance = classToLoad.newInstance();
			Object result = method.invoke(instance);
			if (result instanceof JobLibrary)
				return (JobLibrary) result;
		} catch (ClassNotFoundException e)
		{
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (NullPointerException ex)
		{
		}
		System.out.println("Library could not be loaded.");
		return null;
	}

	public void removeJobLibrary(JobLibrary library)
	{
		libraryList.remove(library);
	}

	@Override
	public void notify(JobSession session, String event)
	{
	}
	
	protected String getUser()
	{
		return user;
	}

	protected String getGroup()
	{
		return group;
	}
}
