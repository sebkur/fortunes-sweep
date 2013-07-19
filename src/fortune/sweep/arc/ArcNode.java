package fortune.sweep.arc;

import java.awt.Rectangle;

import fortune.sweep.EventQueue;
import fortune.sweep.geometry.Edge;
import fortune.sweep.geometry.Point;
import fortune.sweep.gui.Canvas;

public class ArcNode extends ParabolaPoint
{
	private ArcNode next, prev;
	private CirclePoint circlePoint;
	private Point startOfTrace;

	public ArcNode(Point point)
	{
		super(point);
	}

	public ArcNode getNext()
	{
		return next;
	}

	public ArcNode getPrevious()
	{
		return prev;
	}

	public void setPrevious(ArcNode prev)
	{
		this.prev = prev;
	}

	public void setNext(ArcNode next)
	{
		this.next = next;
	}

	public Point getStartOfTrace()
	{
		return startOfTrace;
	}

	public CirclePoint getCirclePoint()
	{
		return circlePoint;
	}

	public void setCirclePoint(CirclePoint circlePoint)
	{
		this.circlePoint = circlePoint;
	}

	public void setStartOfTrace(Point startOfTrace)
	{
		this.startOfTrace = startOfTrace;
	}

	public void checkCircle(EventQueue eventqueue)
	{
		if (prev != null && next != null) {
			circlePoint = calculateCenter(next, this, prev);
			if (circlePoint != null)
				eventqueue.insert(circlePoint);
		}
	}

	public void removeCircle(EventQueue eventqueue)
	{
		if (circlePoint != null) {
			eventqueue.remove(circlePoint);
			circlePoint = null;
		}
	}

	public void completeTrace(Canvas canvas, Point point)
	{
		if (startOfTrace != null) {
			canvas.getVoronoi().addLine(new Edge(startOfTrace, point));
			canvas.getDelaunay().add(new Edge(this, next));
			startOfTrace = null;
		}
	}

	public void checkBounds(Canvas canvas, double d)
	{
		if (next != null) {
			next.init(d);
			if (d > next.getX() && d > getX() && startOfTrace != null) {
				try {
					double ad[] = solveQuadratic(getA() - next.getA(), getB()
							- next.getB(), getC() - next.getC());
					double d1 = ad[0];
					double d2 = d - f(d1);
					Rectangle rectangle = canvas.getBounds();
					if (d2 < startOfTrace.getX() && d2 < 0.0D || d1 < 0.0D
							|| d2 >= (double) rectangle.width
							|| d1 >= (double) rectangle.height)
						completeTrace(canvas, new Point(d2, d1));
				} catch (Throwable _ex) {
					System.out.println("*** exception");
				}
			}
			next.checkBounds(canvas, d);
		}
	}

	public void insert(ParabolaPoint parabolapoint, double sline,
			EventQueue eventqueue) throws Throwable
	{
		boolean split = true;
		if (next != null) {
			next.init(sline);
			if (sline > next.getX() && sline > getX()) {
				double xs[] = solveQuadratic(getA() - next.getA(), getB()
						- next.getB(), getC() - next.getC());
				if (xs[0] <= parabolapoint.realX() && xs[0] != xs[1])
					split = false;
			} else {
				split = false;
			}
		}

		if (split) {
			removeCircle(eventqueue);

			ArcNode arcnode = new ArcNode(parabolapoint);
			arcnode.next = new ArcNode(this);
			arcnode.prev = this;
			arcnode.next.next = next;
			arcnode.next.prev = arcnode;

			if (next != null)
				next.prev = arcnode.next;

			next = arcnode;

			checkCircle(eventqueue);
			next.next.checkCircle(eventqueue);

			next.next.startOfTrace = startOfTrace;
			startOfTrace = new Point(sline - f(parabolapoint.getY()),
					parabolapoint.getY());
			next.startOfTrace = new Point(sline - f(parabolapoint.getY()),
					parabolapoint.getY());
		} else {
			next.insert(parabolapoint, sline, eventqueue);
		}
	}

}
