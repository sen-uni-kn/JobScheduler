/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.common.resource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import kn.uni.sen.jobscheduler.common.resource.ResourceFile;

/**
 * Resource give possibility to read and write a file.
 */
public class ResourceFile extends ResourceAbstract
{
	public enum Permission
	{
		NONE, READ, WRITE, EXECUTE
	};

	// handle of open file
	protected File fileHandle;
	// protected FileOutputStream outputStream;
	protected Writer writer;

	// filename of the file with data
	String fileName = "";
	// folder with file location
	String fileFolder = "";
	// Pattern how filename has to look like.
	String filePattern = "*";

	public ResourceFile()
	{
		super();
	}

	public ResourceFile(ResourceFile file)
	{
		super(file);
		fileHandle = null;
		fileFolder = new String(file.fileFolder);
		fileName = new String(file.fileName);
	}

	public ResourceFile(String filePath)
	{
		super();
		setFilePath(filePath);
	}

	public void setFolder(String folder)
	{
		if (folder == null)
			folder = "";
		while (folder.endsWith("/") || folder.endsWith("/"))
			folder = fileFolder.substring(0, folder.length() - 1);
		fileFolder = folder;
	}

	public String getFolder()
	{
		if (fileFolder.endsWith("/") || fileFolder.endsWith("/"))
			return fileFolder.substring(0, fileFolder.length() - 1);
		return fileFolder;
	}

	public static String getFolder(String folderfile)
	{
		if (folderfile == null)
			folderfile = "";
		// search the filename part
		int index = folderfile.lastIndexOf('/');
		if (index == -1)
			return "";
		return folderfile.substring(0, index);
	}

	public void setPattern(String ext)
	{
		if (ext == null)
			ext = "";
		filePattern = ext;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileName()
	{
		return fileName;
	}

	public String getFilenameOnly()
	{
		return getFilenameOnly(getData());
	}

	public String getExtension()
	{
		int index = fileName.lastIndexOf('.');
		if (index < 0)
			return null;
		return fileName.substring(index).trim();
	}

	public void setFilePath(String folderfile)
	{
		if (folderfile == null)
			folderfile = "";
		// search the filename part
		int sub = folderfile.lastIndexOf('/');
		String val = "";
		if (sub != -1)
		{
			val = folderfile.substring(sub).trim();
			fileName = val.substring(1, val.length());
			val = folderfile.substring(0, folderfile.length() - fileName.length() - 1);
		} else
			fileName = new String(folderfile);
		setFolder(val);
	}

	public String getFilePath()
	{
		String folder = "";
		if ((fileFolder != null) && (!fileFolder.isEmpty()))
		{
			folder = fileFolder;
			if (!fileFolder.endsWith("/"))
				folder += "/";
		}

		return folder + fileName;
	}

	/**
	 * opens file for reading
	 * 
	 * @param fileName
	 * @param readOnly
	 * @return true if file could be opened
	 */
	public boolean openFile(String fileName, boolean readOnly)
	{
		setFilePath(fileName);
		try
		{
			fileHandle = new File(fileName);
			fileHandle.setWritable(!readOnly);
			return true;
		} catch (Exception ex)
		{
			System.out.println("failure set file");
		}
		return false;
	}

	/**
	 * opens file for reading
	 * 
	 * @param fileName
	 * @param readOnly
	 * @return true if file could be opened
	 */
	public boolean openFile(boolean readOnly)
	{
		if (fileHandle != null)
			return true;
		String fileName = getData();
		try
		{
			fileHandle = new File(fileName);
			fileHandle.setWritable(!readOnly);
			return true;
		} catch (Exception ex)
		{
			System.out.println("failure set file");
		}
		return false;
	}

	/**
	 * opens file for writting
	 * 
	 * @param fileName
	 * @param readOnly
	 * @return true if file could be opened
	 */
	public static boolean createFile(String fileName, boolean errorExist)
	{
		File file = new File(fileName);
		try
		{
			if (!!!ResourceFolder.createFolder(ResourceFile.getFolder(fileName)))
				return false;

			if (file.exists() && errorExist)
				return false;
			if (!file.createNewFile() && errorExist)
				return false;
		} catch (IOException ex)
		{
			if (errorExist)
				System.out.println(ex);
			return false;
		}
		return true;
	}

	/**
	 * opens file for writting
	 * 
	 * @param fileName
	 * @param readOnly
	 * @return true if file could be opened
	 */
	public boolean createFile(boolean errorExis)
	{
		createFile(getFilePath(), errorExis);
		try
		{
			FileOutputStream outputStream = new FileOutputStream(this.getFilePath());
			writer = new OutputStreamWriter(outputStream, "UTF8");
		} catch (IOException ex)
		{
			if (errorExis)
			{
				System.out.println(ex);
				return false;
			}
		}
		return true;
	}

	public void closeFile()
	{
		if (writer != null)
		{
			try
			{
				writer.flush();
				writer.close();
				writer = null;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public boolean readFile()
	{
		return false;
	}

	public boolean writeFile()
	{
		return false;
	}

	// data means here the filename of the file
	@Override
	public String getPrivateData()
	{
		return getFilePath();
	}

	// data means the filename of the file with maybe folder/filename.extension
	@Override
	protected void setPrivateData(String data)
	{
		setFilePath(data);
	}

	/**
	 * @return Content of file as string.
	 */
	@SuppressWarnings("resource")
	public String getContent()
	{
		Scanner scanner;
		String contents = null;
		try
		{
			scanner = new Scanner(new File(getFilePath())).useDelimiter("\\Z");
			contents = scanner.next();
			scanner.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return contents;
	}

	public InputStream getInputStream()
	{
		if ((fileHandle == null) && !openFile(getFilePath(), true))
			return null;

		try
		{
			return new FileInputStream(fileHandle);
		} catch (FileNotFoundException e)
		{
			// e.printStackTrace();
		}
		return null;
	}

	public ResourceType getType()
	{
		return ResourceType.FILE;
	}

	// check if resource is actually available
	@Override
	public boolean checkAvailable()
	{
		return exists();
	}

	public boolean exists()
	{
		String filePath = getData();
		File file = new File(filePath);
		if (file.exists())
			return true;
		return false;
	}

	public List<ResourceTag> getPossibleTags()
	{
		List<ResourceTag> list = super.getPossibleTags();
		list.add(ResourceTag.READONLY);
		return list;
	}

	/**
	 * add date and time to filename
	 * 
	 * @param folder
	 *            where file shall be created
	 * @param fileName
	 *            fileName that should be used
	 * @return filename with date and time
	 */
	public static String getFileWithDateTime(String folder, String fileName)
	{
		// TODO: create a fileName with actual date and time
		// "ExampleFile'16Nov11_11:11:11'.txt"
		// the part between '' is the part automatically created
		return fileName;
	}

	/**
	 * if file already exists incremental number is added
	 * 
	 * @param folder
	 *            where file shall be created
	 * @param fileName
	 *            fileName that should be used
	 * @return unique filename
	 */
	public static String getUniqueFile(String folder, String fileName)
	{
		// TODO: create a unique fileName by checking if the file already exists
		// "ExampleFile'_2'.txt"
		// the part between '' is the part automatically created
		return fileName;
	}

	public static String getFilename(String fileUrl)
	{
		String sign = ResourceFolder.getSplitSign();
		int index = fileUrl.lastIndexOf(sign);
		if (index == -1)
		{
			index = fileUrl.lastIndexOf("/");
			if (index == -1)
				return fileUrl;
		}
		return fileUrl.substring(index + 1);
	}

	/**
	 * @param fileUrl
	 * @return file name without folder and extension
	 */
	public static String getFilenameOnly(String fileUrl)
	{
		String name = getFilename(fileUrl);
		int index = name.lastIndexOf(".");
		if (index == -1)
			return name;
		return name.substring(0, index);
	}

	public ResourceAbstract clone()
	{
		return new ResourceFile(this);
	}

	public boolean compareContent(ResourceFile otherFile)
	{
		String content = getContent();
		String otherContent = otherFile.getContent();

		if ((content == null) || (otherContent == null))
			return false;

		return content.compareTo(otherContent) == 0;
	}

	// parses the resource according to the specifications
	/*
	 * public static Resource ParseResource(String qName, Attributes attributes)
	 * { System.out.println("Parse ResourceFile"); ResourceFile res = new
	 * ResourceFile(); return res; }
	 */

	public static boolean copyFile(String original, String file)
	{
		if (equivalentPath(original, file))
			return true;
		
		if(ResourceFile.exists(file))
			ResourceFile.removeFile(file);

		// Files.copy(original, file, StandardCopyOption.REPLACE_EXISTING);
		InputStream inStream = null;
		OutputStream outStream = null;
		try
		{
			inStream = new FileInputStream(new File(original));
			outStream = new FileOutputStream(new File(file));

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0)
			{
				outStream.write(buffer, 0, length);
			}
			inStream.close();
			outStream.close();
			return true;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean equivalentPath(String original, String file)
	{
		String p1 = getAbsolutPath(original);
		String p2 = getAbsolutPath(file);
		if (p1 == null)
			return false;
		return p1.equals(p2);
	}

	public void setExtension(String ext)
	{
		String name = getFilenameOnly(this.fileName);
		if (ext != null)
			name += ext;
		setFileName(name);
	}

	public static boolean appendText2File(String file, String text)
	{
		if (text == null)
			return true;
		try (FileWriter fw = new FileWriter(file, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw))
		{
			out.print(text);
			return true;
		} catch (IOException e)
		{
			System.out.println("File " + file + " does not exist");
		}
		return false;
	}

	public void appendText(String text)
	{
		String file = getData();
		appendText2File(file, text);
	}

	public void removeFile()
	{
		removeFile(getData());
	}

	public static void removeFile(String filePath)
	{
		File file = new File(filePath);
		if (file.exists())
		{
			file.delete();
		}
	}

	public void setPermission(Permission per)
	{
		setPermission(getData(), per);
	}

	public static boolean setPermission(String filePath, Permission per)
	{
		File file = new File(filePath);
		boolean res = false;
		if (per == Permission.EXECUTE)
			res = file.setExecutable(true);
		else if (per == Permission.READ)
			res = file.setReadable(true);
		else if (per == Permission.WRITE)
			res = file.setWritable(true);
		return res;
	}

	public static boolean exists(String path)
	{
		if (path == null)
			return false;
		File file = new File(path);
		return file.exists();
	}

	public static String writeURL2File(URL urlFile, String filePath)
	{
		if (urlFile == null)
			return null;

		String org = urlFile.getFile();
		if (org != null)
		{
			if (ResourceFile.exists(org) && filePath.equals(org))
				return org;
			else if (ResourceFile.isFolder(filePath))
			{
				String file = ResourceFile.getFilename(org);
				filePath += ResourceFolder.getSplitSign() + file;
			}
		}

		// create folder if missing
		ResourceFile.createFolder(filePath);
		if (ResourceFile.exists(filePath))
			ResourceFile.removeFile(filePath);

		// resources loaded by maven are not a file but a stream
		InputStream input;
		try
		{
			input = urlFile.openStream();
			File targetFile = new File(filePath);
			OutputStream outStream = new FileOutputStream(targetFile);
			byte[] buffer = new byte[10 * 1024];
			for (int length; (length = input.read(buffer)) != -1;)
			{
				outStream.write(buffer, 0, length);
			}
			outStream.close();
			// IOUtils.copy(input, outStream);
			return filePath;
		} catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}

	private static void createFolder(String filePath)
	{
		if (filePath == null)
			return;
		String fol = ResourceFile.getFolder(filePath);
		ResourceFolder.createFolder(fol);
	}

	private static boolean isFolder(String filePath)
	{
		return (new File(filePath)).isDirectory();
	}

	public boolean isAbsolutePath()
	{
		return isAbsolutePath(getData());
	}
	
	public static boolean isAbsolutePath(String p)
	{
		if(p.startsWith("/"))
			return true;
		return false;
	}

	public static String getAbsolutPath(String curLib)
	{
		if (curLib == null)
			return null;
		File file = new File(curLib);
		return file.getAbsolutePath();
	}
	
	public boolean writeText2File(String content)
	{
		removeFile();
		String t = getData();
		return writeText2File(t, content);
	}

	public static boolean writeText2File(String fileDestiny, String text)
	{
		// Files.copy(original, file, StandardCopyOption.REPLACE_EXISTING);
		OutputStream outStream = null;
		try
		{
			outStream = new FileOutputStream(new File(fileDestiny));
			outStream.write(text.getBytes(), 0, text.length());
			outStream.close();
			return true;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static String parseFile(String fileName)
	{
		try
		{
			return new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
