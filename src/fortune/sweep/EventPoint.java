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

	public EventPoint(double d, double d1)
	{
		super(d, d1);
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

	public void insert(EventPoint eventpoint)
	{
		if (eventpoint.getX() > getX() || eventpoint.getX() == getX()
				&& eventpoint.getY() > getY()) {
			if (next != null) {
				next.insert(eventpoint);
				return;
			} else {
				next = eventpoint;
				eventpoint.prev = this;
				return;
			}
		}
		if (eventpoint.getX() != getX() || eventpoint.getY() != getY()
				|| (eventpoint instanceof CirclePoint)) {
			eventpoint.prev = prev;
			eventpoint.next = this;
			if (prev != null) {
				prev.next = eventpoint;
			}
			prev = eventpoint;
			return;
		} else {
			eventpoint.prev = eventpoint;
			System.out
					.println("Double point ignored: " + eventpoint.toString());
			return;
		}
	}

	public void action(Algorithm algorithm)
	{
		algorithm.getArcs().insert(this, algorithm.getPosX(),
				algorithm.getEventQueue());
	}

}
