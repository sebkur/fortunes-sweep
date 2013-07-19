package fortune.sweep.arc;

import fortune.sweep.Algorithm;
import fortune.sweep.EventPoint;
import fortune.sweep.geometry.Point;

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

	public void action(Algorithm algorithm)
	{
		ArcNode arcnode = arc.getPrevious();
		ArcNode arcnode1 = arc.getNext();
		Point point = new Point(getX() - radius, getY());
		arc.completeTrace(algorithm, point);
		arcnode.completeTrace(algorithm, point);
		arcnode.setStartOfTrace(point);
		arcnode.setNext(arcnode1);
		arcnode1.setPrevious(arcnode);
		if (arcnode.getCirclePoint() != null) {
			algorithm.getEventQueue().remove(arcnode.getCirclePoint());
			arcnode.setCirclePoint(null);
		}
		if (arcnode1.getCirclePoint() != null) {
			algorithm.getEventQueue().remove(arcnode1.getCirclePoint());
			arcnode1.setCirclePoint(null);
		}
		arcnode.checkCircle(algorithm.getEventQueue());
		arcnode1.checkCircle(algorithm.getEventQueue());
	}

}
