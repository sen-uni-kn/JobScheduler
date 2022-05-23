/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.model;

import java.net.JarURLConnection;

/**
 * Contains current version number of JobScheduler
 * 
 * @author Martin Koelbl
 */
public class Version
{
	final static String version = "1.1.0";
	
	public static String getVersion()
	{
		return version;
	}

	public static Long getCompileTime(Class<?> cl)
	{
		try
		{
			String rn = cl.getName().replace('.', '/') + ".class";
			JarURLConnection j = (JarURLConnection) ClassLoader.getSystemResource(rn).openConnection();
			return j.getJarFile().getEntry("META-INF/MANIFEST.MF").getTime();
		} catch (Exception e)
		{
			return null;
		}
	}
}
