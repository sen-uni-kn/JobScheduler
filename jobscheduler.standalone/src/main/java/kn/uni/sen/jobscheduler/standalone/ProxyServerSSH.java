/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import com.jcraft.jsch.*;

/**
 * Connects to a JobScheduler by an ssh connection. To request and send data an
 * http connection is used inside of the ssh connection.
 * 
 * @author Martin Koelbl
 */
public class ProxyServerSSH extends ProxyServerHttp
{
	void startServer()
	{
		// todo: start server by ssh connection
	}

	void stopServer()
	{
		// todo: stop server by ssh connection
	}

	void connect()
	{
		// SSH server and credentials
		String host = "00.000.000.00";
		String user = "login";
		String password = "password";
		int port = 22;

		// The host I want to send HTTP requests to - the remote host
		String remoteHost = "test.com";
		int remotePort = 80;

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");

		try
		{
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();

			Channel channel = session.getStreamForwarder(remoteHost, remotePort);

			// The GET request
			String cmd = "GET /foo/foo HTTP/1.0\r\n\r\n";

			InputStream in = channel.getInputStream();
			OutputStream out = channel.getOutputStream();

			channel.connect(10000);

			byte[] bytes = cmd.getBytes();
			InputStream is = new ByteArrayInputStream(cmd.getBytes("UTF-8"));

			int numRead;

			while ((numRead = is.read(bytes)) >= 0)
				out.write(bytes, 0, numRead);

			out.flush();

			System.out.println("Request supposed to have been sent");

			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				for (String line; (line = reader.readLine()) != null;)
				{
					System.out.println(line);
				}
			} catch (java.io.IOException exc)
			{
				System.out.println(exc.toString());
			}

			channel.disconnect();
			session.disconnect();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
