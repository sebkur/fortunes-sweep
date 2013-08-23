package fortune.sweep.events;

import java.util.ArrayList;
import java.util.List;

import fortune.sweep.Algorithm;
import fortune.sweep.events.EventQueueModification.Type;

public class HistoryEventQueue extends EventQueue
{

	private List<EventQueueModification> modifications = new ArrayList<EventQueueModification>();

	private Algorithm algorithm;

	public HistoryEventQueue(Algorithm algorithm)
	{
		this.algorithm = algorithm;
	}

	public synchronized boolean insertEvent(EventPoint eventPoint)
	{
		EventQueueModification modification = new EventQueueModification(
				algorithm.getSweepX(), Type.ADD, eventPoint);
		if (eventPoint instanceof CirclePoint) {
			// Circle event should always get through
			modifications.add(modification);
			insert(eventPoint);
			return true;
		} else if (eventPoint instanceof SitePoint) {
			// Site events should only be allowed if they will be inserted at
			// the correct position. That's only if there's no event yet or if
			// the latest modification is the insertion of a site event, too.
			if (modifications.size() == 0) {
				modifications.add(modification);
				insert(eventPoint);
				return true;
			} else {
				EventQueueModification latest = getLatestModification();
				if (latest.getType() == Type.ADD
						&& latest.getEventPoint() instanceof SitePoint) {
					modifications.add(modification);
					insert(eventPoint);
					return true;
				}
			}
			// TODO: now we cannot add any sites after the sweep began. This is
			// a little restrictive. To relax this, we would have to make sure
			// the eventsQueue and the modification list will be sane after late
			// insertion.
		}
		return false;
	}

	@Override
	public synchronized boolean remove(EventPoint eventPoint)
	{
		boolean remove = super.remove(eventPoint);
		if (remove) {
			modifications.add(new EventQueueModification(algorithm.getSweepX(),
					Type.REMOVE, eventPoint));
		}
		return remove;
	}

	@Override
	public synchronized EventPoint pop()
	{
		EventPoint eventPoint = top();
		modifications.add(new EventQueueModification(eventPoint.getX(),
				Type.REMOVE, eventPoint));
		return super.pop();
	}

	public synchronized boolean hasModification()
	{
		return modifications.size() > 0;
	}

	public synchronized EventQueueModification getLatestModification()
	{
		if (modifications.size() == 0) {
			return null;
		}
		return modifications.get(modifications.size() - 1);
	}

	public synchronized EventQueueModification revertModification()
	{
		if (modifications.size() == 0) {
			return null;
		}
		EventQueueModification modification = modifications
				.remove(modifications.size() - 1);
		// Reverse EventQueue modification
		if (modification.getType() == Type.ADD) {
			// Remove if the event was added
			super.remove(modification.getEventPoint());
		} else if (modification.getType() == Type.REMOVE) {
			// Insert if the event was removed
			insert(modification.getEventPoint());
			// Revert pointers of arcs to their circle events.
			if (modification.getEventPoint() instanceof CirclePoint) {
				CirclePoint circlePoint = (CirclePoint) modification
						.getEventPoint();
				circlePoint.getArc().setCirclePoint(circlePoint);
			}
		}

		return modification;
	}

}
