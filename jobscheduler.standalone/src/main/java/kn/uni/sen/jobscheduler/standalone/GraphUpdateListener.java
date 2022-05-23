/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import java.awt.Component;

public class GraphUpdateListener implements UpdateListener
{
	private Component component;

	public GraphUpdateListener(Component comp)
	{
		component = comp;
	}

	public void update()
	{
		component.repaint();
	}
}
