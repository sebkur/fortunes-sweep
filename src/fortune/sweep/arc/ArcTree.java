package fortune.sweep.arc;

import fortune.sweep.Algorithm;
import fortune.sweep.EventQueue;
import fortune.sweep.geometry.Point;

public class ArcTree
{

	private ArcNode arcs;

	public void insert(Point point, double d, EventQueue eventqueue)
	{
		if (arcs == null) {
			arcs = new ArcNode(point);
			return;
		}
		try {
			ParabolaPoint parabolapoint = new ParabolaPoint(point);
			parabolapoint.init(d);
			arcs.init(d);
			arcs.insert(parabolapoint, d, eventqueue);
			return;
		} catch (Throwable _ex) {
			System.out
					.println("*** error: No parabola intersection during ArcTree.insert()");
		}
	}

	public ArcNode getArcs()
	{
		return arcs;
	}

	public void checkBounds(Algorithm algorithm, double x)
	{
		if (arcs != null) {
			arcs.init(x);
			arcs.checkBounds(algorithm, x);
		}
	}

}
