package fortune.sweep.geometry;

import java.awt.Graphics;

import fortune.sweep.Paintable;

public class Point implements Paintable
{

	protected double x, y;

	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Point(Point point)
	{
		x = point.x;
		y = point.y;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}

	public void paint(Graphics g)
	{
		g.fillOval((int) (x - 3.0), (int) (y - 3.0), 7, 7);
	}

	public double distance(Point point)
	{
		double d = point.x - x;
		double d1 = point.y - y;
		return Math.sqrt(d * d + d1 * d1);
	}

}
