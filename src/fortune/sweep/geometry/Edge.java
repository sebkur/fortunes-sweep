package fortune.sweep.geometry;

import java.awt.Graphics;

import fortune.sweep.Paintable;

public class Edge implements Paintable
{

	protected Point p1, p2;

	public Edge(Point p1, Point p2)
	{
		this.p1 = p1;
		this.p2 = p2;
	}

	public Point getStart()
	{
		return p1;
	}

	public Point getEnd()
	{
		return p2;
	}

	public void paint(Graphics g)
	{
		g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
	}

}
