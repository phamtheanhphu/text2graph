package com.nlp.text2graph.main;

import javax.swing.JOptionPane;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

public class AppGraphClick {

	protected static String generalStyleSheet = "node {shape: box;fill-color: red;"
			+ "text-size: 25px;size:30px;stroke-color: yellow;stroke-mode: plain;}"
			+ " node#ROOT {fill-color: green; size: 30px;} " + " node.marked {fill-color: red; } "
			+ " edge {fill-color: black;text-size: 18px;size: 1.5px;text-color:blue;}";
	private static Graph graph = new SingleGraph("Tutorial 1");

	public static void main(String args[]) {

		// System.setProperty("gs.ui.renderer",
		// "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		
		graph.addNode("A").setAttribute("label", "A");		
		graph.addNode("B").setAttribute("label", "B");
		graph.addNode("C").setAttribute("label", "C");
		
		graph.addEdge("AB", "A", "B", true).setAttribute("label", "AB");
		graph.addEdge("BC", "B", "C", true).setAttribute("label", "BC");
		graph.addEdge("CA", "C", "A", true).setAttribute("label", "CA");

		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		graph.addAttribute("ui.stylesheet", generalStyleSheet);

		new GraphNodeClick(graph);

	}

	public static class GraphNodeClick implements ViewerListener {

		// private JFrame mainFrame;
		private Viewer viewer;
		private boolean loopPump = true;

		public GraphNodeClick(Graph graph) {

			// TODO Auto-generated constructor stub
			this.viewer = graph.display();
			// this.view = this.viewer.addDefaultView(false);
			ViewerPipe viewerPipe = viewer.newViewerPipe();
			viewerPipe.addViewerListener(this);
			viewerPipe.addSink(graph);

			while (loopPump) {
				viewerPipe.pump();
			}

		}

		@Override
		public void buttonPushed(String nodeId) {
			// TODO Auto-generated method stub
			JOptionPane.showMessageDialog(this.viewer.getDefaultView(), "Bạn vừa click vào đỉnh [" + nodeId + "]",
					"Graph's clicking", JOptionPane.INFORMATION_MESSAGE, null);
		}

		@Override
		public void buttonReleased(String arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void viewClosed(String arg0) {
			// TODO Auto-generated method stub
			loopPump = false;

		}

	}

}
