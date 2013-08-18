package fortune.sweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fortune.sweep.arc.ArcNode;
import fortune.sweep.arc.ArcTree;
import fortune.sweep.arc.CirclePoint;
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
	private EventQueue events;
	private ArcTree arcs;

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

	public EventQueue getEventQueue()
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
		events = new EventQueue();
		voronoi.clear();
		delaunay = new Delaunay();
		for (Point point : sites) {
			voronoi.addSite(point);
			events.insert(new SitePoint(point));
		}
	}

	public synchronized boolean singlestep()
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

	public synchronized boolean isFinshed()
	{
		return !(events.size() != 0 || xPos < PLAY_N_PIXELS_BEYOND_SCREEN
				+ maxX);
	}

	public synchronized void step()
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
		if (eventPoint instanceof CirclePoint) {
			CirclePoint circlePoint = (CirclePoint) eventPoint;
			ArcNode arc = circlePoint.getArc();
			ArcNode arcnode = arc.getPrevious();
			ArcNode arcnode1 = arc.getNext();
			Point point = new Point(
					eventPoint.getX() - circlePoint.getRadius(),
					eventPoint.getY());
			arc.completeTrace(this, point);
			arcnode.completeTrace(this, point);
			arcnode.setStartOfTrace(point);
			arcnode.setNext(arcnode1);
			arcnode1.setPrevious(arcnode);
			if (arcnode.getCirclePoint() != null) {
				getEventQueue().remove(arcnode.getCirclePoint());
				arcnode.setCirclePoint(null);
			}
			if (arcnode1.getCirclePoint() != null) {
				getEventQueue().remove(arcnode1.getCirclePoint());
				arcnode1.setCirclePoint(null);
			}
			arcnode.checkCircle(getEventQueue());
			arcnode1.checkCircle(getEventQueue());
		} else {
			getArcs().insert(eventPoint, getSweepX(), getEventQueue());
		}
	}

}
