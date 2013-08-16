package fortune.sweep.arc;

import fortune.sweep.EventPoint;

public class CirclePoint extends EventPoint
{

	private double radius;
	private ArcNode arc;

	public CirclePoint(double d, double d1, ArcNode arcnode)
	{
		super(d, d1);
		arc = arcnode;
		radius = distance(arcnode);
		setX(getX() + radius);
	}

	public double getRadius()
	{
		return radius;
	}

	public ArcNode getArc()
	{
		return arc;
	}

}
