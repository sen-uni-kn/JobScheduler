/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MouseInputAdapter;

public class MouseDragListener extends MouseInputAdapter
{
	private List<Component> LinkedComponents = new ArrayList<Component>();

	private Point location;
	private MouseEvent pressed;

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		pressed = e;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		moveComponent(e.getComponent(), e);

		for (Component comp : LinkedComponents)
			moveComponent(comp, e);
	}

	public void addLinkedComponent(Component component)
	{
		LinkedComponents.add(component);
	}

	private void moveComponent(Component component, MouseEvent e)
	{
		location = component.getLocation(location);

		int x = location.x - pressed.getX() + e.getX();
		int y = location.y - pressed.getY() + e.getY();

		if (x < 0)
			x = 0;

		if (y < 0)
			y = 0;

		Dimension size = component.getParent().getSize();

		if (x + component.getWidth() > size.width)
			x = size.width - component.getWidth();

		if (y + component.getHeight() > size.height)
			y = size.height - component.getHeight();

		// Improved rounding (over-complicated but fast)
		component.setLocation(x + (x % 10 > 5 ? 10 - x % 10 : -(x % 10)), y + (y % 10 > 5 ? 10 - y % 10 : -(y % 10)));
	}
}
