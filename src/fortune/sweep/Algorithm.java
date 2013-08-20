package fortune.sweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.Stack;

import fortune.sweep.arc.ArcNode;
import fortune.sweep.arc.ArcTree;
import fortune.sweep.events.CirclePoint;
import fortune.sweep.events.EventPoint;
import fortune.sweep.events.EventQueueModification;
import fortune.sweep.events.EventQueueModification.Type;
import fortune.sweep.events.HistoryEventQueue;
import fortune.sweep.events.SitePoint;
import fortune.sweep.geometry.Point;

public class Algorithm
{
	private static final int PLAY_N_PIXELS_BEYOND_SCREEN = 1000;

	private Voronoi voronoi;
	private Delaunay delaunay;

	private double xPos;
	private int maxX;
	private int height;

	private List<Point> sites;
	private EventPoint currentEvent;
	private HistoryEventQueue events;
	private ArcTree arcs;

	private Stack<EventPoint> executedEvents;

	private List<AlgorithmWatcher> watchers = new ArrayList<AlgorithmWatcher>();

	public Algorithm()
	{
		sites = new ArrayList<Point>();
		voronoi = new Voronoi();
		init();
	}

	public int getMaxX()
	{
		return maxX;
	}

	public void setMaxX(int maxX)
	{
		this.maxX = maxX;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public double getSweepX()
	{
		return xPos;
	}

	public Voronoi getVoronoi()
	{
		return voronoi;
	}

	public Delaunay getDelaunay()
	{
		return delaunay;
	}

	public HistoryEventQueue getEventQueue()
	{
		return events;
	}

	public EventPoint getCurrentEvent()
	{
		return currentEvent;
	}

	public ArcTree getArcs()
	{
		return arcs;
	}

	public void addSite(Point point)
	{
		sites.add(point);
		voronoi.addSite(point);
		voronoi.checkDegenerate();
		events.insert(new SitePoint(point));
	}

	public void addWatcher(AlgorithmWatcher watcher)
	{
		watchers.add(watcher);
	}

	public void removeWatcher(AlgorithmWatcher watcher)
	{
		watchers.remove(watcher);
	}

	public List<Point> getSites()
	{
		return Collections.unmodifiableList(sites);
	}

	public void setSites(List<Point> sites)
	{
		this.sites = sites;
		voronoi = new Voronoi();
		for (Point point : sites) {
			voronoi.addSite(point);
		}
		restart();
	}

	private void notifyWatchers()
	{
		for (AlgorithmWatcher watcher : watchers) {
			watcher.update();
		}
	}

	public synchronized void init()
	{
		xPos = 0;
		arcs = new ArcTree();
		events = new HistoryEventQueue(this);
		executedEvents = new Stack<EventPoint>();
		currentEvent = null;
		voronoi.clear();
		delaunay = new Delaunay();
		for (Point point : sites) {
			voronoi.addSite(point);
			events.insert(new SitePoint(point));
		}
	}

	public synchronized boolean nextPixel()
	{
		if (events.size() == 0 || xPos < events.top().getX()) {
			xPos++;
			currentEvent = null;
		}

		double xPosOld = xPos;
		while (events.size() != 0 && xPosOld >= events.top().getX()) {
			EventPoint eventPoint = events.pop();
			xPos = eventPoint.getX();
			process(eventPoint);
			currentEvent = eventPoint;
		}
		xPos = xPosOld;

		notifyWatchers();
		return !isFinshed();
	}

	public synchronized boolean previousPixel()
	{
		if (xPos <= 0) {
			return false;
		}
		double xPosBefore = xPos;
		xPos--;
		currentEvent = null;

		/*
		 * Restore event queue
		 */
		while (events.hasModification()) {
			EventQueueModification mod = events.getLatestModification();
			EventPoint event = events.getLatestModification().getEventPoint();
			if (event instanceof SitePoint) {
				if (!(event.getX() >= xPos && event.getX() <= xPosBefore)) {
					break;
				}
			} else if (event instanceof CirclePoint) {
				if (mod.getType() == Type.REMOVE) {
					if (!(mod.getX() >= xPos && mod.getX() <= xPosBefore)) {
						break;
					}
				} else if (mod.getType() == Type.ADD) {
					if (!(mod.getX() >= xPos && mod.getX() <= xPosBefore)) {
						break;
					}
				}
			}
			events.revertModification();
		}

		/*
		 * Go through executed events and revert everything within the interval
		 */
		while (executedEvents.size() > 0) {
			EventPoint lastEvent = executedEvents.top();
			if (!(lastEvent.getX() >= xPos && lastEvent.getX() <= xPosBefore)) {
				break;
			}
			executedEvents.pop();
			if (lastEvent instanceof SitePoint) {
				SitePoint sitePoint = (SitePoint) lastEvent;
				revert(sitePoint);
			} else if (lastEvent instanceof CirclePoint) {
				CirclePoint circlePoint = (CirclePoint) lastEvent;
				revert(circlePoint);
			}
		}

		notifyWatchers();
		return xPos > 0;
	}

	public synchronized boolean isFinshed()
	{
		return !(events.size() != 0 || xPos < PLAY_N_PIXELS_BEYOND_SCREEN
				+ maxX);
	}

	public synchronized void nextEvent()
	{
		if (events.size() > 0) {
			EventPoint eventPoint = events.pop();
			xPos = eventPoint.getX();
			process(eventPoint);
			currentEvent = eventPoint;
		} else if (xPos < maxX) {
			xPos = maxX;
			currentEvent = null;
		}
		notifyWatchers();
	}

	public synchronized void clear()
	{
		sites = new ArrayList<Point>();
		voronoi = new Voronoi();
		restart();
	}

	public synchronized void restart()
	{
		init();
		notifyWatchers();
	}

	private void process(EventPoint eventPoint)
	{
		executedEvents.push(eventPoint);
		if (eventPoint instanceof CirclePoint) {
			CirclePoint circlePoint = (CirclePoint) eventPoint;
			process(circlePoint);
		} else {
			SitePoint sitePoint = (SitePoint) eventPoint;
			process(sitePoint);
		}
	}

	private void process(CirclePoint circlePoint)
	{
		// arc is the disappearing arc
		ArcNode arc = circlePoint.getArc();
		// prev and next are the new neighbors on the beachline
		ArcNode prev = arc.getPrevious();
		ArcNode next = arc.getNext();
		// point is the position of the new voronoi vertex
		Point point = new Point(circlePoint.getX() - circlePoint.getRadius(),
				circlePoint.getY());
		// add two new voronoi edges
		prev.completeTrace(this, point);
		arc.completeTrace(this, point);
		// add a new trace
		prev.setStartOfTrace(point);
		// change arc pointers
		prev.setNext(next);
		next.setPrevious(prev);
		// dismiss now invalid circle events
		if (prev.getCirclePoint() != null) {
			getEventQueue().remove(prev.getCirclePoint());
			prev.setCirclePoint(null);
		}
		if (next.getCirclePoint() != null) {
			getEventQueue().remove(next.getCirclePoint());
			next.setCirclePoint(null);
		}
		// check for new circle events
		prev.checkCircle(getEventQueue());
		next.checkCircle(getEventQueue());
	}

	private void revert(CirclePoint circlePoint)
	{
		// reinsert arc between previous and next
		ArcNode arc = circlePoint.getArc();
		arc.getNext().setPrevious(arc);
		arc.getPrevious().setNext(arc);
		// restore trace starting at removed voronoi vertex
		Point point = new Point(circlePoint.getX() - circlePoint.getRadius(),
				circlePoint.getY());
		arc.uncompleteTrace();
		arc.getPrevious().uncompleteTrace();
		// remove vertex/edges from vornoi diagram
		voronoi.removeLinesFromVertex(point);
	}

	private void process(SitePoint sitePoint)
	{
		getArcs().insert(sitePoint, getSweepX(), getEventQueue());
	}

	private void revert(SitePoint sitePoint)
	{
		getArcs().remove(sitePoint);
	}

}
