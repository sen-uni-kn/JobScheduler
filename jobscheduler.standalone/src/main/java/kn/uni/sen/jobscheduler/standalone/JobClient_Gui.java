/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

//import org.w3c.dom.Document;

//import com.mxgraph.io.mxCodec;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.view.mxGraph;

import kn.uni.sen.jobscheduler.common.model.JobEvent;
import kn.uni.sen.jobscheduler.common.model.JobLibrary;
import kn.uni.sen.jobscheduler.common.model.JobState;
import kn.uni.sen.jobscheduler.common.resource.ResourceFileXml;
import kn.uni.sen.jobscheduler.console.JobServer_Console;
import kn.uni.sen.jobscheduler.core.impl.JobClientAbstract;
import kn.uni.sen.jobscheduler.core.impl.JobGraph_File;
import kn.uni.sen.jobscheduler.core.model.JobGraph;
import kn.uni.sen.jobscheduler.core.model.JobServer;
import kn.uni.sen.jobscheduler.core.model.JobRun;

/**
 * JobGraphAbstract This class creates a GUI and gives possibility to input data
 * into the graph, running the graph, showing progress during the run and
 * afterwards gives possibility to see results.
 * 
 * @author Martin Koelbl
 */
public class JobClient_Gui extends JobClientAbstract
{
	JobGraph graph = null;
	ResourceFileXml graphFile;
	mxGraph theGraph = null; // just a test
	JobServer jobServer = new JobServer_Console("Gui", "result");
	JobRun_Gui run;
	Timer timerUpdate;

	public JobClient_Gui(String[] args)
	{
		super(null, "JobGui", "result");
		if (args.length <= 0)
			return;

		graphFile = new ResourceFileXml();
		graphFile.setData(args[0]);
		graph = new JobGraph_File("", graphFile);

		theGraph = xmlToMx(graph); // just a test
	}

	void useNiceElements()
	{
		try
		{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e)
		{
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
			logEventStatus(JobEvent.WARNING, "Swing Look and Feel Nimbus not available.");
		}
	}

	void createWindow()
	{
		// Create and set up the window
		JFrame frame = new JFrame("JobScheduler");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(500, 500));
		// button for starting the scheduler
		JButton buttonRun = new JButton("Start");
		buttonRun.setPreferredSize(new Dimension(200, 40));
		// button for starting the test process
		// adds actionListener to the ButtonRun and does the job inside the code
		buttonRun.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				runScheduler();
			}
		});

		JButton buttonTest = new JButton("Test");
		buttonTest.setPreferredSize(new Dimension(200, 40));
		// adds actionListener to the ButtonTest and does the job inside the
		// code
		buttonTest.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				testScheduler();
			}
		});

		// new panel for placing the buttons to the screen
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.add(buttonTest);
		ButtonPanel.add(buttonRun);
		ButtonPanel.setOpaque(true);

		// adding Button panel to the south of border Layout
		frame.add(ButtonPanel, BorderLayout.SOUTH);

		JPanel graphContainer = new GraphContainerPanel(graph);
		frame.getContentPane().add(graphContainer, SwingConstants.CENTER);

		// Display the window
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);

	}

	// added
	public mxGraph xmlToMx(JobGraph graph)
	{

		mxGraph lv_theGraph = new mxGraph();
		try
		{
			// Document document =
			// mxXmlUtils.parseXml(mxUtils.readFile(graphFile.getData()));
			// mxCodec codec = new mxCodec(document);
			// codec.decode(document.getDocumentElement(), theGraph.getModel());
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return lv_theGraph;
	}

	void createLayout(mxGraph jgraphx)
	{
		// define layout
		mxHierarchicalLayout layout = new mxHierarchicalLayout(jgraphx);
		layout.execute(jgraphx.getDefaultParent());
	}

	void run()
	{
		if (graph == null)
		{
			logEventStatus(JobEvent.ERROR, "MissingJobGraph");
			return;
		}
		graph.load();

		// define layout
		// createLayout(theGraph);

		useNiceElements();
		createWindow();

	}

	// this function prints some output to show that go button is working
	// because
	// it will be activated once the button is clicked and working properly
	void runScheduler()
	{
		if (graphFile == null)
		{
			System.out.println("GraphFile is missing");
			return;
		}
		// 1. Check if already an instance is running(jobscheduler)
		if (run != null)
			return;

		System.out.print("Start scheduler\n");

		// 2. Save current JobGraph in a folder
		saveGraph();

		// 3. Call JobScheduler
		// String name = graphFile.getFileName(), user = "";

		run = (JobRun_Gui) jobServer.createSession(null);

		for (JobLibrary library : libraryList)
			run.addLibrary(library);
		run.setJobGraph(graphFile);
		run.start("run");

		timerUpdate = new Timer();
		timerUpdate.schedule(new RunTask(run), 0, 1000);
	}

	void testScheduler()
	{
		if (graphFile == null)
		{
			System.out.println("GraphFile is missing");
			return;
		}
		// 1. Check if already an instance is running(jobscheduler)
		if (run != null)
			return;

		System.out.print("Start scheduler\n");

		// 2. Save current JobGraph in a folder
		saveGraph();

		// 3. Call JobScheduler
		JobRun run2 = jobServer.createRun(null);
		if (!!!(run2 instanceof JobRun_Gui))
			return;
		JobRun_Gui run = (JobRun_Gui) run2;

		for (JobLibrary library : libraryList)
			run.addLibrary(library);
		run.setJobGraph(graphFile);
		run.start("simulate");

		timerUpdate = new Timer();
		timerUpdate.schedule(new RunTask(run), 0, 1000);
	}

	private class RunTask extends TimerTask
	{
		JobRun run;

		public RunTask(JobRun run)
		{
			this.run = run;
		}

		@Override
		public void run()
		{
			updateRun(run);
		}
	}

	void updateRun(JobRun run)
	{
		JobState state = run.getStatus();

		// todo: update surface

		// free running session if finished
		if ((state == JobState.FINISHED) || (state == JobState.UNDEFINED))
		{
			System.out.println("End scheduler. Result:" + run.getResult());
			this.run = null;
			timerUpdate.cancel();
			timerUpdate = null;
		}
	}

	void saveGraph()
	{

	}

	public static void main(String[] args)
	{
		JobClient_Gui gui = new JobClient_Gui(args);
		gui.run();
	}
}
