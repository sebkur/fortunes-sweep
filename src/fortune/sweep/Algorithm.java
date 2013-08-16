package fortune.sweep;

import java.util.ArrayList;
import java.util.List;

import fortune.sweep.arc.ArcTree;
import fortune.sweep.geometry.Point;

public class Algorithm
{

	private Voronoi voronoi;
	private Delaunay delaunay;

	private int xPos;
	private int maxX;
	private int height;

	private EventQueue events;
	private ArcTree arcs;

	private List<AlgorithmWatcher> watchers = new ArrayList<AlgorithmWatcher>();

	public Algorithm()
	{
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

	public int getSweepX()
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

	public int getxPos()
	{
		return xPos;
	}

	public EventQueue getEventQueue()
	{
		return events;
	}

	public ArcTree getArcs()
	{
		return arcs;
	}

	public void addSite(Point point)
	{
		voronoi.addSite(point);
		voronoi.checkDegenerate();
		events.insert(new EventPoint(point));
	}

	public void addWatcher(AlgorithmWatcher watcher)
	{
		watchers.add(watcher);
	}

	public void removeWatcher(AlgorithmWatcher watcher)
	{
		watchers.remove(watcher);
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
		for (int i = 0; i < voronoi.getNumberOfSites(); i++) {
			events.insert(new EventPoint(voronoi.getSite(i)));
		}
	}

	public synchronized boolean singlestep()
	{
		if (events.getEvents() == null
				|| (double) xPos < events.getEvents().getX()) {
			xPos++;
		}

		while (events.getEvents() != null
				&& (double) xPos >= events.getEvents().getX()) {
			EventPoint eventPoint = events.pop();
			xPos = Math.max(xPos, (int) eventPoint.getX());
			eventPoint.action(this);
		}

		notifyWatchers();
		return !isFinshed();
	}

	public synchronized boolean isFinshed()
	{
		return !(events.getEvents() != null || xPos < 1000 + maxX);
	}

	public synchronized void step()
	{
		EventPoint eventPoint = events.pop();
		if (eventPoint != null) {
			xPos = Math.max(xPos, (int) eventPoint.getX());
			eventPoint.action(this);
		} else if (xPos < maxX) {
			xPos = maxX;
		}
		notifyWatchers();
	}

	public synchronized void clear()
	{
		voronoi = new Voronoi();
		restart();
	}

	public synchronized void restart()
	{
		init();
		notifyWatchers();
	}

}
