/*********************************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 *********************************************************************************************/

package kn.uni.sen.jobscheduler.common.helper;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;

import kn.uni.sen.jobscheduler.common.resource.ResourceFile;

public class Helper
{
	public static class Pair<T1, T2>
	{
		public T1 name;
		public T2 value;

		public Pair(T1 name, T2 value)
		{
			this.name = name;
			this.value = value;
		}
	}

	public enum OpSys
	{
		UNKOWN, WINDOWS, LINUX, MAC
	};

	private static String OS = null;

	public static OpSys getOperatingSystem()
	{
		if (OS == null)
		{
			OS = System.getProperty("os.name");
		}
		if (OS.startsWith("Windows"))
			return OpSys.WINDOWS;
		return OpSys.LINUX;
	}

	public static int getJavaVersion()
	{
		String version = System.getProperty("java.version");
		if (version.startsWith("1."))
		{
			version = version.substring(2, 3);
		} else
		{
			int dot = version.indexOf(".");
			if (dot != -1)
			{
				version = version.substring(0, dot);
			}
		}
		return Integer.parseInt(version);
	}

	public static void addLoadLib(String path)
	{
		String val = System.getProperty("java.library.path");
		String sep = System.getProperty("path.separator");
		val += sep + path;
		System.setProperty("java.library.path", val);
		try
		{
			Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param fileUrl
	 *            of file
	 * @return filepath as encoded as UTF-8
	 */
	public static String getPath(URL fileUrl)
	{
		if (fileUrl == null)
			return "";
		String val = fileUrl.getPath();
		// System.out.println("url: " + val);
		int index = val.indexOf('%');

		if (index == -1)
			return fileUrl.getPath();

		try
		{
			return URLDecoder.decode(fileUrl.getPath(), "UTF-8");
		} catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}

	public static String loadFilePath(String fileName)
	{
		ClassLoader classLoader = Helper.class.getClassLoader();
		URL res = classLoader.getResource(fileName);
		if (res == null)
			return null;
		return getPath(res);
	}

	public static ResourceFile loadFile(String fileName)
	{
		String val = loadFilePath(fileName);
		return val != null ? new ResourceFile(val) : null;
	}

	public static String storeUrlFile(String nameFile, String folder)
	{
		if (ResourceFile.exists(nameFile))
			return nameFile;

		ClassLoader classLoader = Helper.class.getClassLoader();
		URL fileUrl = classLoader.getResource(nameFile);
		ResourceFile file = loadFile(nameFile);
		if (file == null)
			return null;
		if (nameFile == null)
		{
			System.out.println("File is missing!");
			return null;
		}
		if (file.exists())
			return file.getData();

		file.setFolder(folder);
		String path = file.getFilePath();
		ResourceFile.writeURL2File(fileUrl, path);
		return path;
	}
}
