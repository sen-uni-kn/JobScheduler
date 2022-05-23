/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import kn.uni.sen.jobscheduler.common.resource.ResourceAbstract;
import kn.uni.sen.jobscheduler.core.model.ProxyServer;

/**
 * Connects to an JobScheduler by an http connection.
 * 
 * @author Martin Koelbl
 */
public class ProxyServerHttp implements ProxyServer
{
	@Override
	public boolean login(String user, String group, String password)
	{
		return false;
	}

	@Override
	public String getSessionID()
	{
		return null;
	}

	@Override
	public String[] getSchedulerList(boolean active)
	{
		return null;
	}

	@Override
	public boolean logout(String sessionID)
	{
		return false;
	}

	@Override
	public boolean sendData(String url2)
	{
		// put
		// String search = "Teletubbies On Tour";
		// search = "p=" + URLEncoder.encode(search.trim(), "UTF-8");
		// URL u = new URL("http://de.search.yahoo.com/search?" + search);
		//
		// String r = new Scanner(u.openStream()).useDelimiter("\\Z").next();
		// System.out.println(r);

		// post
		try
		{
			String body = "param1=" + URLEncoder.encode("value1", "UTF-8") + "&" + "param2="
					+ URLEncoder.encode("value2", "UTF-8");

			URL url = new URL("http://li.la.lu.lo/post/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", String.valueOf(body.length()));

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(body);
			writer.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			for (String line; (line = reader.readLine()) != null;)
			{
				System.out.println(line);
			}

			writer.close();
			reader.close();
		} catch (Exception ex)
		{
		}
		return false;
	}

	@Override
	public String sendResult(String url)
	{
		return null;
	}

	@Override
	public boolean sendRequest(String url)
	{
		return false;
	}

	@Override
	public ResourceAbstract requestResult(String url)
	{
		return null;
	}

	@Override
	public ConnectionState getConnectionState()
	{
		return null;
	}
}
