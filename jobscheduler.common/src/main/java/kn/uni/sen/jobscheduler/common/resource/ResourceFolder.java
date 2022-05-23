/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import kn.uni.sen.jobscheduler.common.helper.Helper;
import kn.uni.sen.jobscheduler.common.helper.Helper.OpSys;
import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;

/**
 * Resource saves a folder from filesytem
 */
public class ResourceFolder extends ResourceAbstract
{
	String path = "";

	public ResourceFolder()
	{
		super();
	}

	public ResourceFolder(ResourceFolder res)
	{
		super(res);
		setData(res.getData());
	}

	public ResourceFolder(String path)
	{
		super();
		this.path = path;
	}

	public ResourceFolder(String name, String path)
	{
		super(name);
		this.path = path;
	}

	public ResourceType getType()
	{
		return ResourceType.FOLDER;
	}

	@Override
	protected void setPrivateData(String data)
	{
		if (data == null)
			data = "";
		path = new String(data);
	}

	@Override
	public String getPrivateData()
	{
		return path;
	}

	public static boolean createFolder(String folder)
	{
		if (folder == null)
			return false;
		if (folder.isEmpty())
			return true;
		File jobGraphFolder = new File(folder);
		if (!!!jobGraphFolder.exists())
			jobGraphFolder.mkdirs();
		if (!!!jobGraphFolder.exists())
			return false;
		return true;
	}

	public ResourceAbstract clone()
	{
		return new ResourceFolder(this);
	}

	public boolean createFolder()
	{
		if (hasTag(ResourceTag.UNIQUE))
		{
			String f = getUniqueFolderDateTime(getData());
			setData(f);
		}

		return ResourceFolder.createFolder(getData());
	}

	public void appendFolder(String subFolder)
	{
		setData(appendFolder(getData(), subFolder));
	}

	public static String appendFolder(String folder, String subFolder)
	{
		if ((subFolder == null) || (subFolder.isEmpty()))
			return folder;
		if ((folder == null) || (folder.isEmpty()))
			folder = subFolder;
		else
			folder += getSplitSign() + subFolder;
		return folder;
	}

	static String opSign = null;

	public static String getSplitSign()
	{
		if (opSign == null)
			if (Helper.getOperatingSystem() == OpSys.WINDOWS)
				opSign = "\\";
			else
				opSign = "/";
		return opSign;
	}

	public boolean exists()
	{
		File f = new File(getData());
		if (!!!f.isDirectory())
			return false;
		return f.exists();
	}

	public boolean isFolder()
	{
		File f = new File(getData());
		return f.isDirectory();
	}

	/**
	 * add date and time to foldername
	 * 
	 * @param folder
	 *            where file shall be created
	 * @param fileName
	 *            fileName that should be used
	 * @return filename with date and time
	 */
	public static String getUniqueFolderDateTime(String folder)
	{
		// "ExampleFile'16Nov11_11:11:11'.txt"
		// the part between '' is the part automatically created

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date(System.currentTimeMillis());
		String unique = folder.concat("_" + dateFormat.format(date));
		return getUniqueFolder(unique);
	}

	public static String getUniqueFolder(String folder)
	{
		// todo: check if folder is unique else append number
		return folder;
	}

	public static boolean isPathAbsolute(String path)
	{
		if (path.startsWith("/"))
			return true;
		if (path.startsWith("\\"))
			return true;
		return false;
	}

	public static void remove(String folder)
	{
		File fol = new File(folder);
		if (!!!fol.exists())
			return;
		if (fol.isDirectory())
		{
			String[] children = fol.list();
			for (int i = 0; i < children.length; i++)
			{
				remove(folder + ResourceFolder.getSplitSign() + children[i]);
			}
		}
		fol.delete();
	}

	public static String removeLastPathElement(String fol)
	{
		if (fol == null)
			return null;
		String sign = getSplitSign();
		int index = fol.lastIndexOf(sign);
		if (index <= 2)
			return "";
		return fol.substring(0, index);
	}

	public static List<String> getFiles(String path, String end)
	{
		String endText = end;
		if ((end == null) || (end.compareTo("*") == 0))
			endText = null;

		List<String> textFiles = new ArrayList<String>();
		File dir = new File(path);
		for (File file : dir.listFiles())
		{
			if ((endText == null) || file.getName().endsWith(endText))
			{
				textFiles.add(file.getName());
			}
		}
		return textFiles;
	}

	public static String getCurrentPath()
	{
		String current;
		try
		{
			current = new java.io.File(".").getCanonicalPath();
			return current;
		} catch (IOException e)
		{
		}
		return null;
	}

	public static String getParentFolder(String path)
	{
		if (path == null)
			return null;
		String parent = null;
		int index = path.lastIndexOf(getSplitSign());
		if (index >= 0)
		{
			parent = path.substring(0, index);
		}
		return parent;
	}
}
