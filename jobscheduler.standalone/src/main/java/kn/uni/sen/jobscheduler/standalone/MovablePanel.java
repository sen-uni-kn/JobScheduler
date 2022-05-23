/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.JPanel;

public class MovablePanel extends JPanel
{
	protected JPanel Container;
	protected MouseDragListener MouseDragListener;

	private static final long serialVersionUID = 1046066535113598422L;

	private List<UpdateListener> updateListeners = new ArrayList<UpdateListener>();

	public MovablePanel(JPanel cont)
	{
		MouseDragListener = new MouseDragListener();
		addMouseListener(MouseDragListener);
		addMouseMotionListener(MouseDragListener);

		addMouseMotionListener(new MouseAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				for (UpdateListener updateListener : updateListeners)
					updateListener.update();
			}
		});

		Container = cont;
	}

	public void addUpdateListener(UpdateListener listener)
	{
		updateListeners.add(listener);
	}
}
