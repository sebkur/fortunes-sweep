package fortune.sweep.events;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;


public class EventQueue
{

	private SortedSet<EventPoint> points;

	public EventQueue()
	{
		points = new TreeSet<EventPoint>(new Comparator<EventPoint>() {

			@Override
			public int compare(EventPoint e1, EventPoint e2)
			{
				if (e1.getX() != e2.getX()) {
					if (e1.getX() < e2.getX()) {
						return -1;
					} else if (e1.getX() > e2.getX()) {
						return 1;
					}
				}
				// e1.getX() == e2.getX()
				if (e1.getY() < e2.getY()) {
					return -1;
				} else if (e1.getY() > e2.getY()) {
					return 1;
				}
				// e1.getY() == e2.getY()
				boolean c1 = e1 instanceof CirclePoint;
				boolean c2 = e1 instanceof CirclePoint;
				if (c1 && !c2) {
					return -1;
				}
				if (!c1 && c2) {
					return 1;
				}
				// c1 == c2
				return 0;
			}
		});
	}

	public synchronized int size()
	{
		return points.size();
	}

	public synchronized void insert(EventPoint p)
	{
		points.add(p);
	}

	public synchronized void remove(EventPoint eventPoint)
	{
		points.remove(eventPoint);
	}

	public synchronized EventPoint top()
	{
		return points.first();
	}

	public synchronized EventPoint pop()
	{
		EventPoint point = points.first();
		points.remove(point);
		return point;
	}

	public Iterator<EventPoint> iterator()
	{
		return points.iterator();
	}
	
	public synchronized EventQueue getCopy()
	{
		EventQueue copy = new EventQueue();
		for (EventPoint point : points) {
			copy.insert(point);
		}
		return copy;
	}

}
