package fortune.sweep;

public class EventQueue
{

	private EventPoint events;

	public EventPoint getEvents()
	{
		return events;
	}

	public void insert(EventPoint p)
	{
		if (events != null) {
			events.insert(p);
		}

		if (p.getPrevious() == null) {
			events = p;
		}
	}

	public void remove(EventPoint eventPoint)
	{
		if (eventPoint.getNext() != null) {
			eventPoint.getNext().setPrevious(eventPoint.getPrevious());
		}

		if (eventPoint.getPrevious() != null) {
			eventPoint.getPrevious().setNext(eventPoint.getNext());
		} else {
			events = eventPoint.getNext();
		}
	}

	public EventPoint pop()
	{
		EventPoint eventPoint = events;
		if (eventPoint != null) {
			events = events.getNext();
			if (events != null) {
				events.setPrevious(null);
			}
		}
		return eventPoint;
	}

}
