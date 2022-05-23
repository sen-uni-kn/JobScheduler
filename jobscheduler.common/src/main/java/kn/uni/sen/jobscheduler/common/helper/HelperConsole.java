/*********************************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 *********************************************************************************************/

package kn.uni.sen.jobscheduler.common.helper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kn.uni.sen.jobscheduler.common.resource.ResourceFolder;

public class HelperConsole
{
	public static int result = -2;

	public static String runCommand(String cmd, String folderPath, boolean bText)
	{
		return runCommand(cmd, folderPath, bText, -1);
	}

	public static String runCommand(String cmd, String folderPath, boolean bText, int timeout)
	{
		return runCommand(cmd, folderPath, bText, true, timeout);
	}

	private static String getInput(BufferedInputStream inputStream, boolean out)
	{
		// CharBuffer buffer = CharBuffer.allocate(1024);
		String text = "";
		try
		{
			int length = 1;
			long last = System.currentTimeMillis();
			byte[] b = new byte[1024];
			while (length >= 0)
			{
				if (inputStream.available() > 0)
				{
					length = inputStream.read(b, 0, 1024);
					String t = new String(b);
					System.out.print(t);
					text += t;
					if (length != 0)
						last = System.currentTimeMillis();
					length = 0;
				}
				long now = System.currentTimeMillis();
				if (now - last > 2000)
					break;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return text;
	}

	public static String runCommandList(String cmd, List<String> cmdList, String folderPath)
	{
		// Console con = System.console();
		result = -2;
		try
		{
			Runtime r = Runtime.getRuntime();
			File folder = new File(folderPath);
			Process p = r.exec(cmd, null, folder);
			OutputStreamWriter writer = new OutputStreamWriter(p.getOutputStream());
			StringBuffer buffer = new StringBuffer();
			BufferedInputStream error = new BufferedInputStream(p.getErrorStream());
			BufferedInputStream input = new BufferedInputStream(p.getInputStream());
			BufferedReader err1 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			TimeUnit.SECONDS.sleep(1);
			buffer.append(getInput(input, true));
			for (String s : cmdList)
			{
				writer.write(s);
				writer.flush();
				TimeUnit.SECONDS.sleep(1);
				buffer.append(getInput(input, true));
				buffer.append(getInput(error, true));
			}
			input.close();
			err1.close();
			p.waitFor();
			return buffer.toString();
		} catch (Exception ex)
		{
			System.out.print(ex);
		}
		return null;
	}

	public static String runCommand(String cmd, String folderPath, boolean bText, boolean writeError, int timeout)
	{
		result = -2;
		try
		{
			Runtime r = Runtime.getRuntime();
			// Process p = r.exec(cmdList);
			Process p = null;
			if (folderPath != null)
			{
				File folder = new File(folderPath);

				// TaskMem task = TaskMem.createTask(Runtime.getRuntime());
				// Timer timer = new Timer();
				// timer.schedule(task, 100);

				if (folder != null)
					cmd = "." + ResourceFolder.getSplitSign() + cmd;
				p = r.exec(cmd, null, folder);
			} else
				p = r.exec(cmd, null);
			boolean res = true;
			if (timeout < 0)
				p.waitFor();
			else
				p.waitFor(timeout, TimeUnit.SECONDS);
			if (!!!res)
				return "";
			try
			{
				result = p.exitValue();
			} catch (Exception ex)
			{
				return null;
			}
			// timer.cancel();
			// task.run();
			// double mem2 = task.maxMem / 1024.0 / 1024.0;
			// we only check the current process but not the called one by exec
			// System.out.println("Memory: " + mem2);
			StringBuffer buffer = new StringBuffer();
			if (bText)
			{
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader err1 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line;
				while ((line = input.readLine()) != null)
				{
					buffer.append(line);
					buffer.append("\n");
				}
				input.close();
				while ((line = err1.readLine()) != null)
				{
					buffer.append(line);
					buffer.append("\n");
				}
				err1.close();
			}
			return buffer.toString();
		} catch (Exception err)
		{
			if (writeError)
				err.printStackTrace();
		}
		return null;
	}
}
