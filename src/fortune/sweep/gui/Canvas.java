package fortune.sweep.gui;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import fortune.sweep.Delaunay;
import fortune.sweep.EventPoint;
import fortune.sweep.EventQueue;
import fortune.sweep.Voronoi;
import fortune.sweep.arc.ArcTree;
import fortune.sweep.geometry.Point;

public class Canvas extends java.awt.Canvas implements MouseListener
{

	private static final long serialVersionUID = 461591430129084653L;

	Graphics offScreenGraphics;
	Image offScreenImage;
	int xPos;
	Voronoi voronoi;
	Delaunay delaunay;
	boolean drawCircles, drawBeach, drawVoronoiLines, drawDelaunay;
	EventQueue events;
	ArcTree arcs;

	public Canvas(int i, int j, int k)
	{
		drawCircles = false;
		drawBeach = true;
		drawVoronoiLines = true;
		drawDelaunay = false;
		addMouseListener(this);
		voronoi = new Voronoi(i, j, k);
		init();
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

	public void mouseClicked(MouseEvent mouseevent)
	{
	}

	public void mouseReleased(MouseEvent mouseevent)
	{
	}

	public void mouseEntered(MouseEvent mouseevent)
	{
	}

	public void mouseExited(MouseEvent mouseevent)
	{
	}

	public synchronized void mousePressed(MouseEvent mouseevent)
	{
		Point point = new Point(mouseevent.getPoint());
		if (point.x > (double) xPos) {
			voronoi.addSite(point);
			voronoi.checkDegenerate();
			events.insert(new EventPoint(point));
			repaint();
		}
	}

	public synchronized void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		g.setColor(Color.blue);
		voronoi.paint(g, drawVoronoiLines);
		g.setColor(Color.red);
		g.drawLine(xPos, 0, xPos, getBounds().height);
		if (events != null && arcs != null) {
			g.setColor(Color.black);
			events.paint(g, drawCircles);
			arcs.paint(g, xPos, drawVoronoiLines, drawBeach);
		}
		if (drawDelaunay) {
			g.setColor(Color.gray);
			delaunay.paint(g);
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
		if (events.events == null || (double) xPos < events.events.x)
			xPos++;

		while (events.events != null && (double) xPos >= events.events.x) {
			EventPoint eventpoint = events.pop();
			xPos = Math.max(xPos, (int) eventpoint.x);
			eventpoint.action(this);
			arcs.checkBounds(this, xPos);
		}

		if (xPos > getBounds().width && events.events == null)
			arcs.checkBounds(this, xPos);

		repaint();
		return events.events != null || xPos < 1000 + getBounds().width;
	}

	public synchronized void step()
	{
		EventPoint eventpoint = events.pop();
		if (eventpoint != null) {
			xPos = Math.max(xPos, (int) eventpoint.x);
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
