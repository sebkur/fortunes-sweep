package fortune.sweep;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class MyCanvas extends Canvas implements MouseListener
{

	private static final long serialVersionUID = 461591430129084653L;

	Graphics offScreenGraphics;
	Image offScreenImage;
	int xPos;
	VoronoiClass voronoi;
	DelaunayClass delaunay;
	boolean drawCircles, drawBeach, drawVoronoiLines, drawDelaunay;
	EventQueue events;
	ArcTree arcs;

	public MyCanvas(int i, int j, int k)
	{
		drawCircles = false;
		drawBeach = true;
		drawVoronoiLines = true;
		drawDelaunay = false;
		addMouseListener(this);
		voronoi = new VoronoiClass(i, j, k);
		init();
	}

	public synchronized void init()
	{
		xPos = 0;
		arcs = new ArcTree();
		events = new EventQueue();
		voronoi.clear();
		delaunay = new DelaunayClass();
		for (int i = 0; i < voronoi.size(); i++) {
			events.insert(new EventPoint((MyPoint) voronoi.elementAt(i)));
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
		MyPoint mypoint = new MyPoint(mouseevent.getPoint());
		if (mypoint.x > (double) xPos) {
			voronoi.addElement(mypoint);
			voronoi.checkDegenerate();
			events.insert(new EventPoint(mypoint));
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
		voronoi = new VoronoiClass(getBounds().width, getBounds().height, 0);
		restart();
	}

	public synchronized void restart()
	{
		init();
		initGraphics();
		repaint();
	}

}
