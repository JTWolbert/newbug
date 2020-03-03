package newbug;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.view.Viewer;

import newbug.MouseClicker;

public class Bug {
	
	static Graph g;
	static Node currentLocation;
	static Node goal;
	static Stack<String> stack = new Stack<String>();
	static ArrayList<String> mline = new ArrayList<String>();
	static String mode;
	
	public static void main(String args[])
	{
		Scanner s = new Scanner(System.in);
		g = genGrid();
		Viewer viewer = g.display(false);
		ProxyPipe pipe = viewer.newViewerPipe();
		pipe.addAttributeSink(g);
		viewer.getDefaultView().setMouseManager(new MouseClicker());
		for(int i = 0;i < g.getNodeCount();i++)
		{

			if(g.getNode(i).getDegree() < 8)
			{
				//g.getNode(i).addAttribute("ui.style", "fill-color: grey;");
			}
		}
		System.out.println("it worked");
		System.out.println("build mline? y/n");
		if(s.next().matches("y"))
		{
			pipe.pump();
			buildmline();
		}
		for(String x:mline)
		{
			System.out.println(x);
		}
		System.out.println("bug1 or bug2?");
		mode = s.next();
		pipe.pump();
		bug();
	}
	
	static void bug()
	{
		System.out.println(mode);
		currentLocation = g.getAttribute("start");
		System.out.println("Start:" + currentLocation);
		goal = g.getAttribute("goal");
		System.out.println("Goal:" + goal);
		while(!currentLocation.toString().matches(goal.toString()))
		{
			moveTowardsGoal();
			if(!currentLocation.toString().matches(goal.toString()))
			{
				currentLocation.addAttribute("ui.style","fill-color: blue;");
			}
		}
	}
	
	static String moveTowardsGoal()
	{
		double x,y,tempDistance,distance = Double.MAX_VALUE;
		Node next = currentLocation;
		Node temp;
		String[] str = currentLocation.toString().split("_");
		x = Double.parseDouble(str[0]);
		y = Double.parseDouble(str[1]);
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
		if(next.getAttribute("ui.style").toString().matches("fill-color: grey;"))
		{
			if(mode.matches("bug1"))
			{
				bug1encircle();
			}
			else
			{
				bug2encircle();
			}
		}
		else
		{
			System.out.println(currentLocation.toString() + " --> " +
			next.toString());
			currentLocation = next;
		}
		return null;
	}
	
	static String bug1encircle()
	{
		System.out.println("bug1encircle");
		Node closest = currentLocation;
		Node circleStart = currentLocation;
		double minDist = Double.MAX_VALUE;
		circle();
		while(!currentLocation.toString().matches(circleStart.toString()))
		{
			circle();
			if(distance(currentLocation,goal) < minDist)
			{
				closest = currentLocation;
				minDist = distance(currentLocation,goal);
			}
		}
		while(!currentLocation.toString().matches(closest.toString()))
		{
			circle();
		}
		return null;
	}
	
	static String bug2encircle()
	{
		System.out.println("bug2encircle");
		double entryDist = distance(currentLocation,goal);
		circle();
		while(true)
		{
			if(mline.contains(currentLocation.toString()))
			{
				if(distance(currentLocation,goal) < entryDist)
				{
					break;
				}
			}
			circle();
		}
		return null;
	}
	
	static String circle()
	{
		System.out.println("circleStep");
		String[] str = currentLocation.toString().split("_");
		int x = (int)Double.parseDouble(str[0]);
		int y = (int)Double.parseDouble(str[1]);
		int ready = 0;//clockwise ordering
		Node first;
		stack.push(x - 1 + "_" + (y + 1));//top-left
		first = g.getNode(stack.peek());
		stack.push(x + "_" + (y + 1));
		stack.push(x + 1 + "_" + (y + 1));//top-right
		stack.push(x + 1 + "_" + y);
		stack.push(x + 1 + "_" + (y - 1));//bottom-right
		stack.push(x + "_" + (y - 1));
		stack.push(x - 1 + "_" + (y - 1));
		stack.push(x - 1 + "_" + y);
		Node temp;
		if(first.getAttribute("ui.style").toString().matches("fill-color: grey;"))
		{
			ready = 1;
		}
		while(!stack.isEmpty())
		{
			temp = g.getNode(stack.pop());
			if(temp.getAttribute("ui.style").toString().matches("fill-color: grey;"))
			{
				ready = 1;
			}
			else
			{
				if(ready == 1)
				{
					stack.empty();
					temp.addAttribute("ui.style","fill-color: blue;");
					System.out.println(currentLocation.toString() + " --> " +
					temp.toString());
					currentLocation = temp;
					return null;
				}
			}
		}
		temp = g.getNode(x - 1 + "_" + y);
		temp.addAttribute("ui.style","fill-color: blue;");
		System.out.println(currentLocation.toString() + " --> " +
		temp.toString());
		currentLocation = temp;
		return null;
	}
	
	static double distance(Node n,Node goal)
	{
		double x = (Double)goal.getAttribute("x") - (Double)n.getAttribute("x");
		double y = (Double)goal.getAttribute("y") - (Double)n.getAttribute("y");
		return Math.sqrt(x * x + y * y);
	}
	
	static void buildmline()
	{
		currentLocation = g.getAttribute("start");
		goal = g.getAttribute("goal");
		stack.push(currentLocation.toString());
		while(!currentLocation.toString().matches(goal.toString()))
		{
			moveTowardsGoal();
			stack.push(currentLocation.toString());
		}
		while(!stack.isEmpty())
		{
			mline.add(stack.pop());
		}
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