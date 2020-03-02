package newbug;

import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.view.Viewer;

import bug.MouseClicker;

public class Bug {
	
	static Graph g;
	static Node currentLocation;
	static Node goal;
	
	public static void main(String args[])
	{
		Scanner s = new Scanner(System.in);
		g = genGrid();
		Viewer viewer = g.display(false);
		ProxyPipe pipe = viewer.newViewerPipe();
		pipe.addAttributeSink(g);
		viewer.getDefaultView().setMouseManager(new MouseClicker());
		System.out.println("it worked");
		System.out.println("bug1 or bug2?");
		if(s.next().matches("bug1"))
		{
			pipe.pump();
			bug1();
		}
		else
		{
			pipe.pump();
			bug2();
		}
	}
	
	static void bug1()
	{
		System.out.println("bug1");
		System.out.println("Start:" + g.getAttribute("start"));
		currentLocation = g.getAttribute("start");
		goal = g.getAttribute("goal");
		while(currentLocation != goal)
		{
			moveTowardsGoal();
		}
		System.out.println(g.getAttribute("start").toString());
	}
	
	static void bug2()
	{
		System.out.println("bug2");
	}
	
	static String moveTowardsGoal()
	{
		double x,y,tempDistance,distance = Double.MAX_VALUE;
		Node next = currentLocation;
		Node temp;
		String[] str = currentLocation.toString().split("_");
		x = Double.parseDouble(str[0]);
		y = Double.parseDouble(str[1]);
		System.out.println("CL:" + currentLocation.getOutDegree());
		Iterator<Node> i = g.getNode(currentLocation.toString()).getNeighborNodeIterator();
		while(i.hasNext())
		{
			temp = i.next();
			tempDistance = distance(temp,goal);
			if(tempDistance < distance)
			{
				distance = tempDistance;
				next = temp;
			}
		}
		System.out.println(currentLocation.toString() + " --> " +
		next.toString());
		if(next != goal)
		{
			next.addAttribute("ui.style","fill-color: blue;");
		}
		currentLocation = next;
		return null;
	}
	
	static double distance(Node n,Node goal)
	{
		double x = (Double)goal.getAttribute("x") - (Double)n.getAttribute("x");
		double y = (Double)goal.getAttribute("y") - (Double)n.getAttribute("y");
		return Math.sqrt(x * x + y * y);
	}
	
	public static Graph genGrid()
	{
		Graph graph = new SingleGraph("grid");
		Generator gen = new GridGenerator(true,false,true);
		gen.addSink(graph);
		gen.begin();

		for(int i=0; i<20; i++) {
			gen.nextEvents();
		}


		// Nodes already have a position.
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        Collection<Node> c = graph.getNodeSet();
		for(int i = 0;i < graph.getNodeCount();i++)
		{
			graph.getNode(i).addAttribute("ui.style", "shape: box;size: 20px,20px;");
			graph.getNode(i).addAttribute("passable", "yes");
			String[] str = graph.getNode(i).toString().split("_");
			graph.getNode(i).addAttribute("x",Double.parseDouble(str[0]));
			graph.getNode(i).addAttribute("y",Double.parseDouble(str[1]));
		}
		return graph;
	}

}
