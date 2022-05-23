/******************************************************************************
 * © Martin Koelbl, Mozilla Public License Version 2.0
 ******************************************************************************/

package kn.uni.sen.jobscheduler.standalone;

//import org.eclipse.gef4.layout;

import java.util.*;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgraph.JGraph;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import com.jgraph.layout.*;

import org.jgrapht.Graph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;

/**
 * Eine Klasse die einen einfachen DAG mit zwei Knoten implementiert
 * 
 * @author Kinan
 */

public class DagDraw extends JApplet
{
	private static final long serialVersionUID = 3256444702936019250L;
	private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");

	private JGraphModelAdapter<String, DefaultEdge> jgAdapter;
	static Graph<String, DefaultEdge> dag;
	static String v1 = "v1";
	static String v2 = "v2";
	static String v3 = "v3";
	static String v4 = "v4";
	static String v5 = "v5";
	static String v6 = "v6";

	private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

	public static void main(String[] args)
	{

		DagDraw applet = new DagDraw();
		applet.init();

		JFrame frame = new JFrame();
		frame.getContentPane().add(applet);
		frame.setTitle("JGraphT Adapter to JGraph Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init()
	{
		// create a JGraphT graph..
		DirectedAcyclicGraph<String, DefaultEdge> dag = new DirectedAcyclicGraph<>(DefaultEdge.class);

		// ..with the following vertices
		dag.addVertex(v1); // Ressource == Kreis
		dag.addVertex(v2); // Job == Rechteck
		dag.addVertex(v3);
		dag.addVertex(v4);
		dag.addVertex(v5);
		dag.addVertex(v6);

		// ...and the following edges
		dag.addEdge(v1, v2);
		dag.addEdge(v1, v3);
		dag.addEdge(v4, v2);
		dag.addEdge(v5, v2);
		dag.addEdge(v6, v3);

		// create a visualization using JGraph, via an adapter
		jgAdapter = new JGraphModelAdapter<>(dag);

		new JGraphXAdapter<>(dag);

		JGraph jgraph = new JGraph(jgAdapter);

		adjustDisplaySettings(jgraph);
		getContentPane().add(jgraph);
		resize(DEFAULT_SIZE);

		layout(dag, jgAdapter, jgraph); // layouts the hierarchical layout via
										// jg

		// layout2(dag, jgxAdapter, jgraph); // layouts the hierarchical layout
		// via jgx

	}

	private void adjustDisplaySettings(JGraph jg)
	{
		jg.setPreferredSize(DEFAULT_SIZE);

		Color c = DEFAULT_BG_COLOR;
		String colorStr = null;

		try
		{
			colorStr = getParameter("bgcolor");
		} catch (Exception e)
		{
		}

		if (colorStr != null)
		{
			c = Color.decode(colorStr);
		}

		jg.setBackground(c);
	}

	/*
	 * @SuppressWarnings("unchecked") private void positionVertexAt(Object
	 * vertex, int x, int y) { DefaultGraphCell cell =
	 * jgAdapter.getVertexCell(vertex); AttributeMap attr =
	 * cell.getAttributes();
	 * 
	 * Rectangle2D bounds = GraphConstants.getBounds(attr);
	 * 
	 * Rectangle2D newBounds = new Rectangle2D.Double(x, y, bounds.getWidth(),
	 * bounds.getHeight());
	 * 
	 * GraphConstants.setBounds(attr, newBounds);
	 * 
	 * AttributeMap cellAttr = new AttributeMap(); cellAttr.put(cell, attr);
	 * jgAdapter.edit(cellAttr, null, null, null); }
	 */

	/**
	 * A listenable directed multigraph that allows loops and parallel edges.
	 */
	/*
	 * private static class ListenableDirectedMultigraph<V, E> extends
	 * DefaultListenableGraph<V, E> { private static final long serialVersionUID
	 * = 1L;
	 * 
	 * ListenableDirectedMultigraph(Class<E> edgeClass) { super(new
	 * DirectedMultigraph<>(edgeClass)); } }
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void layout( DirectedAcyclicGraph graphModelDag, JGraphModelAdapter graphAdapter, JGraph graph)
	{
		List roots = new ArrayList();
		Iterator vertexIter = graphModelDag.vertexSet().iterator();
		while (vertexIter.hasNext())
		{
			Object vertex = vertexIter.next();
			if (graphModelDag.inDegreeOf(vertex) == 0)
			{
				roots.add(graphAdapter.getVertexCell(vertex));
			}
		}

		JGraphFacade jgf = new JGraphFacade(graph);
		final JGraphHierarchicalLayout layout = new JGraphHierarchicalLayout();
		layout.run(jgf);

		final Map nestedMap = jgf.createNestedMap(true, true);
		graph.getGraphLayoutCache().edit(nestedMap);
	}

	/*
	 * private void layout2( DirectedAcyclicGraph graphModelDag, JGraphXAdapter
	 * graphXAdapter, JGraph graph) { List roots = new ArrayList(); Iterator
	 * vertexIter = graphModelDag.vertexSet().iterator(); while
	 * (vertexIter.hasNext()) { Object vertex = vertexIter.next(); if
	 * (graphModelDag.inDegreeOf(vertex) == 0) {
	 * roots.add(graphXAdapter.getVertexToCellMap()); } }
	 * 
	 * JGraphFacade jgf = new JGraphFacade(graph); final
	 * JGraphHierarchicalLayout layout = new JGraphHierarchicalLayout();
	 * layout.run(jgf);
	 * 
	 * final Map nestedMap = jgf.createNestedMap(true, true);
	 * graph.getGraphLayoutCache().edit(nestedMap); }
	 */
}
