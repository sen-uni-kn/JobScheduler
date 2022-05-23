/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.console;

import kn.uni.sen.jobscheduler.common.impl.RunContextSimple;
import kn.uni.sen.jobscheduler.common.model.EventHandler;

public abstract class ConsoleAbstract extends RunContextSimple
{
	public ConsoleAbstract(EventHandler handler, String name, String folder)
	{
		super(handler, name, folder);
	}

	protected boolean bShowHelp = false;
	protected boolean bVersion = false;
	protected boolean bList = false;
	protected int verbose = 1; // 0 -> not prints, 1 -> normal, 2-> print debug
	// information

	protected boolean parseArg(String arg)
	{
		if ((arg.compareTo("-h") == 0) || (arg.compareToIgnoreCase("--help") == 0))
		{
			bShowHelp = true;
			return true;
		} else if ((arg.compareTo("-v") == 0) || (arg.compareToIgnoreCase("--version") == 0))
		{
			bVersion = true;
			return true;
		} else if ((arg.compareTo("-l") == 0) || (arg.compareToIgnoreCase("--list") == 0))
		{
			bList = true;
			return true;
		} else if ((arg.compareTo("-v") == 0) || (arg.compareToIgnoreCase("--verbose") == 0))
			verbose = 2;
		return false;
	}

	boolean runSimple()
	{
		if (bShowHelp)
		{
			printHelp();
			return true;
		}
		if (bVersion)
		{
			printVersion();
			return true;
		}
		if (bList)
		{
			printList();
			return true;
		}
		return false;
	}

	abstract void printList();

	abstract void printVersion();

	abstract void printHelp();
}
