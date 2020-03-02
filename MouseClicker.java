package newbug;

import java.awt.event.MouseEvent;

import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.MouseManager;

public class MouseClicker implements MouseManager {
	
	protected View view; 
	
    protected Graph graph;
    
    protected double calx;
    protected double caly;

    protected Element element; 
    
    protected int click = -1;
    protected Element temp;

	@Override
	public void mouseClicked(MouseEvent e) {
		if(click == -1)
		{
			System.out.println("cally");
			calibrate(e);
			return;
		}
        element = view.findNodeOrSpriteAt(e.getX(), e.getY());
        if(element != null){
        	System.out.println(element.toString());
        	if(click == 0)
        	{
        		temp = element;
        		element.setAttribute("ui.style", "fill-color: purple;");
        		click++;
        	}
        	else if(click == 1)
        	{
        		fillArea(temp,element);
        		click--;
        	}
        	else
        	{
        		if(graph.getAttribute("start") == null)
        		{
        			graph.addAttribute("start",element);
        			System.out.println(graph.getAttribute("start").toString());
        			element.setAttribute("ui.style", "fill-color: green;");
        		}
        		else
        		{
        			graph.addAttribute("goal",element);
        			System.out.println(graph.getAttribute("goal").toString());
        			element.setAttribute("ui.style", "fill-color: red;");
        			click++;
        		}
        	}
        }
	}
	
	void calibrate(MouseEvent e)
	{
		System.out.println(e.getX() + " " + e.getY());
		System.out.println("click the center of top-left");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void init(Graph graph, View view) {
        this.graph = graph;
        this.view = view;
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
        view.removeMouseListener(this);
        view.removeMouseMotionListener(this);
		
	}
	
	void fillArea(Element a,Element b)
	{
		double ax,ay,bx,by,xx,xy;
		String[] str = a.toString().split("_");
		ax = Double.parseDouble(str[0]);
		ay = Double.parseDouble(str[1]);
		str = b.toString().split("_");
		bx = Double.parseDouble(str[0]);
		by = Double.parseDouble(str[1]);
		if(ax > bx)
		{
			xx = ax;
			ax = bx;
			bx = xx;
		}
		if(ay > by)
		{
			xx = ay;
			ay = by;
			by = xx;
		}
		for(Node x:graph.getNodeSet())
		{
			str = x.toString().split("_");
			xx = Double.parseDouble(str[0]);
			xy = Double.parseDouble(str[1]);
			if( xx >= ax && xx <= bx && xy >= ay && xy <= by)
			{
				x.setAttribute("ui.style", "fill-color: grey;");
				x.setAttribute("passable", "no");
			}
		}
	}
	
	double distance(Node n,Node goal)
	{
		double x = (Double)goal.getAttribute("x") - (Double)n.getAttribute("x");
		double y = (Double)goal.getAttribute("y") - (Double)n.getAttribute("y");
		return Math.sqrt(x * x + y * y);
	}

	@Override
	public void init(GraphicGraph arg0, View arg1) {
		// TODO Auto-generated method stub
		
	}
	

}