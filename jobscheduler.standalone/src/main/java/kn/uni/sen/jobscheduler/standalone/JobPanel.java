/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kn.uni.sen.jobscheduler.core.model.JobDescription;

public class JobPanel extends MovablePanel
{
	private static final long serialVersionUID = -3405672436207347844L;

	private JobDescription Job;

	JobPanel(JPanel cont, JobDescription job)
	{
		super(cont);

		setSize(new Dimension(100, 100));

		// Set the background color explicitly to remove any transparency which
		// would make the inner lines visible
		setBackground(new Color(214, 217, 223)); //grey as background
		setBackground(new Color(214, 230, 223));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JLabel label = new JLabel(job.getJobName());
		add(label);

		Job = job;
	}

	public JobDescription getJob()
	{
		return Job;
	}
}
