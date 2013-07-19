package fortune.sweep.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import fortune.sweep.Delaunay;
import fortune.sweep.EventPoint;
import fortune.sweep.EventQueue;
import fortune.sweep.Voronoi;
import fortune.sweep.arc.ArcTree;
import fortune.sweep.geometry.Point;
import fortune.sweep.paint.AwtPainter;

public class Canvas extends JPanel
{

	private static final long serialVersionUID = 461591430129084653L;

	private Graphics offScreenGraphics;
	private Image offScreenImage;
	private int xPos;
	private Voronoi voronoi;
	private Delaunay delaunay;
	private boolean drawCircles, drawBeach, drawVoronoiLines, drawDelaunay;

	private EventQueue events;
	private ArcTree arcs;

	public Canvas(int i, int j, int k)
	{
		drawCircles = false;
		drawBeach = true;
		drawVoronoiLines = true;
		drawDelaunay = false;

		voronoi = new Voronoi(i, j, k);
		init();

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e)
			{
				Point point = new Point(e.getPoint().x, e.getPoint().y);
				if (point.getX() > (double) xPos) {
					voronoi.addSite(point);
					voronoi.checkDegenerate();
					events.insert(new EventPoint(point));
					repaint();
				}
			}

		});
	}

	public boolean isDrawCircles()
	{
		return drawCircles;
	}

	public void setDrawCircles(boolean drawCircles)
	{
		this.drawCircles = drawCircles;
	}

	public boolean isDrawBeach()
	{
		return drawBeach;
	}

	public void setDrawBeach(boolean drawBeach)
	{
		this.drawBeach = drawBeach;
	}

	public boolean isDrawVoronoiLines()
	{
		return drawVoronoiLines;
	}

	public void setDrawVoronoiLines(boolean drawVoronoiLines)
	{
		this.drawVoronoiLines = drawVoronoiLines;
	}

	public boolean isDrawDelaunay()
	{
		return drawDelaunay;
	}

	public void setDrawDelaunay(boolean drawDelaunay)
	{
		this.drawDelaunay = drawDelaunay;
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

	public synchronized void initGraphics()
	{
		offScreenImage = createImage(getBounds().width, getBounds().height);
		offScreenGraphics = offScreenImage.getGraphics();
	}

	public synchronized void paint(Graphics g)
	{
		AwtPainter drawer = new AwtPainter(g);

		g.setColor(Color.white);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		g.setColor(Color.blue);
		drawer.paint(voronoi, drawVoronoiLines);
		g.setColor(Color.red);
		g.drawLine(xPos, 0, xPos, getBounds().height);
		if (events != null && arcs != null) {
			g.setColor(Color.black);
			drawer.paint(events, drawCircles);
			if (arcs.getArcs() != null) {
				arcs.getArcs().init(xPos);
				drawer.paint(arcs.getArcs(), xPos, 0.0D, drawVoronoiLines,
						drawBeach);
			}
		}
		if (drawDelaunay) {
			g.setColor(Color.gray);
			drawer.paint(delaunay);
		}
	}

	public void update(Graphics g)
	{
		ensureGraphics();
		offScreenGraphics.setClip(g.getClipBounds());
		paint(offScreenGraphics);
		g.drawImage(offScreenImage, 0, 0, this);
		// paint(g);
	}

	private void ensureGraphics()
	{
		if (offScreenImage == null) {
			initGraphics();
		}
	}

	public synchronized boolean singlestep()
	{
		if (events.getEvents() == null
				|| (double) xPos < events.getEvents().getX())
			xPos++;

		while (events.getEvents() != null
				&& (double) xPos >= events.getEvents().getX()) {
			EventPoint eventpoint = events.pop();
			xPos = Math.max(xPos, (int) eventpoint.getX());
			eventpoint.action(this);
			arcs.checkBounds(this, xPos);
		}

		if (xPos > getBounds().width && events.getEvents() == null)
			arcs.checkBounds(this, xPos);

		repaint();
		return events.getEvents() != null || xPos < 1000 + getBounds().width;
	}

	public synchronized void step()
	{
		EventPoint eventpoint = events.pop();
		if (eventpoint != null) {
			xPos = Math.max(xPos, (int) eventpoint.getX());
			eventpoint.action(this);
		} else if (xPos < getBounds().width) {
			xPos = getBounds().width;
		} else {
			init();
			initGraphics();
		}
		arcs.checkBounds(this, xPos);
		repaint();
	}

	public synchronized void clear()
	{
		voronoi = new Voronoi(getBounds().width, getBounds().height, 0);
		restart();
	}

	public synchronized void restart()
	{
		init();
		initGraphics();
		repaint();
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

	public ArcTree getArcs()
	{
		return arcs;
	}

	public int getXPos()
	{
		return xPos;
	}
}
