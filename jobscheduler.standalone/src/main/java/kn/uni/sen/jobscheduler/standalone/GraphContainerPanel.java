/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import java.awt.Component;
import java.awt.Graphics;
import java.util.*;

import javax.swing.*;

import kn.uni.sen.jobscheduler.common.model.ResourceInterface;
import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;
import kn.uni.sen.jobscheduler.core.model.JobDescription;
import kn.uni.sen.jobscheduler.core.model.JobGraph;

public class GraphContainerPanel extends JPanel
{
	private static final long serialVersionUID = 5923105400001093832L;

	private List<ResourcePanel> resourcePanels = new ArrayList<ResourcePanel>();
	private List<JobPanel> jobPanels = new ArrayList<JobPanel>();

	private JobDescription jobGraph;

	public GraphContainerPanel(JobGraph graph)
	{
		jobGraph = graph;

		addNewResourcePanels(jobGraph.getInputList(), ResourcePanel.ResourceType.IN);
		addNewResourcePanels(jobGraph.getResultList(), ResourcePanel.ResourceType.OUT);

		ResourceInterface j = jobGraph.getJobList();
		while (j != null)
		{
			if (j instanceof JobDescription)
			{
				JobDescription job = (JobDescription) j;
				addJobPanel(job);
				addNewResourcePanels(job.getInputList(), ResourcePanel.ResourceType.IN);
				addNewResourcePanels(job.getResultList(), ResourcePanel.ResourceType.OUT);
			}
			j = j.getNext();
		}

		// Place components where ever we want to
		// Warning: requires components to define size
		setLayout(null);
	}

	@Override
	public void paintComponent(Graphics g)
	{

		// g.translate(250, 250); // centering

		super.paintComponent(g);

		// Draw lines from jobs to related resources
		for (JobPanel jobPanel : jobPanels)
		{
			JobDescription job = jobPanel.getJob();

			ResourceInterface d = job.getInputList();
			while (d != null)
			{
				for (ResourcePanel resourcePanel : resourcePanels)
				{
					ResourceDescription descr2 = resourcePanel.getResource();

					if (d.getName().compareTo(descr2.getName()) == 0)
						drawLines(jobPanel, resourcePanel, g);
				}
				d = d.getNext();
			}

			d = job.getResultList();
			while (d != null)
			{
				for (ResourcePanel resourcePanel : resourcePanels)
				{
					ResourceDescription descr2 = resourcePanel.getResource();

					if (d.getName().compareTo(descr2.getName()) == 0)
						drawLines(jobPanel, resourcePanel, g);
				}
				d = d.getNext();
			}
		}
	}

	protected void drawLines(JPanel panelA, JPanel panelB, Graphics g)
	{
		g.drawLine(panelA.getX() + panelA.getWidth() / 2, panelA.getY() + panelA.getHeight() / 2,
				panelB.getX() + panelB.getWidth() / 2, panelB.getY() + panelB.getWidth() / 2);
	}

	protected void addJobPanel(JobDescription job)
	{
		add(new JobPanel(this, job));
	}

	protected void addNewResourcePanel(String name, ResourceDescription descr, ResourcePanel.ResourceType type)
	{
		for (ResourcePanel resourcePanel : resourcePanels)
		{
			ResourceDescription descr2 = resourcePanel.getResource();
			if (resourcePanel.getResource().getName().compareTo(descr2.getName()) == 0)
				return;
		}

		add(new ResourcePanel(this, name, descr, type));
	}

	protected void addNewResourcePanels(ResourceInterface dList, ResourcePanel.ResourceType type)
	{
		while (dList != null)
		{
			if (dList instanceof ResourceDescription)
				addNewResourcePanel(dList.getName(), (ResourceDescription) dList, type);
			dList = dList.getNext();
		}
	}

	@Override
	public Component add(Component comp)
	{
		// Add listener to repaint on move (for both JobPanel and ResourcePanel)
		if (comp instanceof MovablePanel)
			((MovablePanel) comp).addUpdateListener(new GraphUpdateListener(this));

		if (comp instanceof JobPanel)
			jobPanels.add((JobPanel) comp);

		if (comp instanceof ResourcePanel)
			resourcePanels.add((ResourcePanel) comp);

		return super.add(comp);
	}
}
