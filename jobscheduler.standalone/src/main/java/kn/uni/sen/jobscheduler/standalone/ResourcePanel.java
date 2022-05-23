/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import kn.uni.sen.jobscheduler.common.resource.ResourceDescription;

public class ResourcePanel extends MovablePanel
{
	public enum ResourceType
	{
		IN, OUT
	}

	private static final long serialVersionUID = 2981117914444289601L;

	private ResourceDescription descr = null;
	ResourceType type;
	private int angle;

	public ResourcePanel(JPanel cont, String name, ResourceDescription descr, ResourceType type)
	{
		super(cont);

		setSize(new Dimension(40, 40));
		setBackground(new Color(0, 0, 0, 0));

		JLabel nameLabel = new JLabel(name != null ? name : descr.getName());
		nameLabel.setLocation(getLocation().x + 40, getLocation().y);
		nameLabel.setSize(100, getSize().width);
		cont.add(nameLabel);

		MouseDragListener.addLinkedComponent(nameLabel);

		this.descr = descr;
		this.type = type;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g.setColor(new Color(214, 217, 223));

		// Fill the inside of the circle with the background color of the
		// GraphContainerPanel so that the connection lines leading to the
		// center are not visible since the rest of the background has to be
		// transparent so that the connective lines cover the corner areas as
		// well
		g.fillOval(10, 10, getWidth() - 20 - 1, getHeight() - 20 - 1);

		g.setColor(Color.BLACK);

		// Draw input/output arc/circle afterwards to avoid overlap errors
		// -1 required to work around problem with the right-most and
		// bottom-most pixels being outside of the panel
		g.drawArc(10, 10, getWidth() - 20 - 1, getHeight() - 20 - 1, 0, 360);

		// TODO: Draw input arc if the resource is input for a job
	}

	public void setInputArcAngle(int angle)
	{
		this.angle = angle;
	}

	public ResourceDescription getResource()
	{
		return descr;
	}

	public int getAngle()
	{
		return angle;
	}
}
