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

	public void remove(EventPoint eventpoint)
	{
		if (eventpoint.getNext() != null) {
			eventpoint.getNext().setPrevious(eventpoint.getPrevious());
		}

		if (eventpoint.getPrevious() != null) {
			eventpoint.getPrevious().setNext(eventpoint.getNext());
		} else {
			events = eventpoint.getNext();
		}
	}

	public EventPoint pop()
	{
		EventPoint eventpoint = events;
		if (eventpoint != null) {
			events = events.getNext();
			if (events != null) {
				events.setPrevious(null);
			}
		}
		return eventpoint;
	}

}
