package fortune.sweep;

import fortune.sweep.arc.CirclePoint;
import fortune.sweep.geometry.Point;

public class EventPoint extends Point
{

	private EventPoint prev, next;

	public EventPoint(Point point)
	{
		super(point);
	}

	public EventPoint(double x, double y)
	{
		super(x, y);
	}

	public EventPoint getNext()
	{
		return next;
	}

	public EventPoint getPrevious()
	{
		return prev;
	}

	public void setPrevious(EventPoint prev)
	{
		this.prev = prev;
	}

	public void setNext(EventPoint next)
	{
		this.next = next;
	}

	public void insert(EventPoint eventPoint)
	{
		if (eventPoint.getX() > getX() || eventPoint.getX() == getX()
				&& eventPoint.getY() > getY()) {
			if (next != null) {
				next.insert(eventPoint);
				return;
			} else {
				next = eventPoint;
				eventPoint.prev = this;
				return;
			}
		}
		if (eventPoint.getX() != getX() || eventPoint.getY() != getY()
				|| (eventPoint instanceof CirclePoint)) {
			eventPoint.prev = prev;
			eventPoint.next = this;
			if (prev != null) {
				prev.next = eventPoint;
			}
			prev = eventPoint;
			return;
		} else {
			eventPoint.prev = eventPoint;
			System.out
					.println("Double point ignored: " + eventPoint.toString());
			return;
		}
	}

	public void action(Algorithm algorithm)
	{
		algorithm.getArcs().insert(this, algorithm.getSweepX(),
				algorithm.getEventQueue());
	}

}
