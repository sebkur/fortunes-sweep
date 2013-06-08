package fortune.sweep;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;
import java.awt.Rectangle;

class ArcNode extends ParabolaPoint
{
	ArcNode next, prev;
	CirclePoint circlePoint;
	MyPoint startOfTrace;

	public ArcNode(MyPoint mypoint)
	{
		super(mypoint);
	}

	public void checkCircle(EventQueue eventqueue)
	{
		if (prev != null && next != null) {
			circlePoint = calculateCenter(next, this, prev);
			if (circlePoint != null)
				eventqueue.insert(circlePoint);
		}
	}

	public void removeCircle(EventQueue eventqueue)
	{
		if (circlePoint != null) {
			eventqueue.remove(circlePoint);
			circlePoint = null;
		}
	}

	public void completeTrace(MyCanvas mycanvas, MyPoint mypoint)
	{
		if (startOfTrace != null) {
			mycanvas.voronoi.addElement(new MyLine(startOfTrace, mypoint));
			mycanvas.delaunay.addElement(new MyLine(this, next));
			startOfTrace = null;
		}
	}

	public void checkBounds(MyCanvas mycanvas, double d)
	{
		if (next != null) {
			next.init(d);
			if (d > next.x && d > x && startOfTrace != null) {
				try {
					double ad[] = solveQuadratic(a - next.a, b - next.b, c
							- next.c);
					double d1 = ad[0];
					double d2 = d - F(d1);
					Rectangle rectangle = mycanvas.getBounds();
					if (d2 < startOfTrace.x && d2 < 0.0D || d1 < 0.0D
							|| d2 >= (double) rectangle.width
							|| d1 >= (double) rectangle.height)
						completeTrace(mycanvas, new MyPoint(d2, d1));
				} catch (Throwable _ex) {
					System.out.println("*** exception");
				}
			}
			next.checkBounds(mycanvas, d);
		}
	}

	public void insert(ParabolaPoint parabolapoint, double sline,
			EventQueue eventqueue) throws Throwable
	{
		boolean split = true;
		if (next != null) {
			next.init(sline);
			if (sline > next.x && sline > x) {
				double xs[] = solveQuadratic(a - next.a, b - next.b, c - next.c);
				if (xs[0] <= parabolapoint.realX() && xs[0] != xs[1])
					split = false;
			} else {
				split = false;
			}
		}

		if (split) {
			removeCircle(eventqueue);

			ArcNode arcnode = new ArcNode(parabolapoint);
			arcnode.next = new ArcNode(this);
			arcnode.prev = this;
			arcnode.next.next = next;
			arcnode.next.prev = arcnode;

			if (next != null)
				next.prev = arcnode.next;

			next = arcnode;

			checkCircle(eventqueue);
			next.next.checkCircle(eventqueue);

			next.next.startOfTrace = startOfTrace;
			startOfTrace = new MyPoint(sline - F(parabolapoint.y),
					parabolapoint.y);
			next.startOfTrace = new MyPoint(sline - F(parabolapoint.y),
					parabolapoint.y);
		} else {
			next.insert(parabolapoint, sline, eventqueue);
		}
	}

	public void paint(Graphics g, double d, double d1, boolean flag,
			boolean drawBeach)
	{
		double d2 = g.getClipBounds().height;
		ArcNode arcnode = next;
		if (arcnode != null) {
			arcnode.init(d);
		}
		if (d == x) {
			double d3 = arcnode != null ? d - arcnode.F(y) : 0.0D;
			if (drawBeach)
				g.drawLine((int) d3, (int) y, (int) d, (int) y);
			d2 = y;
		} else {
			if (arcnode != null) {
				if (d == arcnode.x) {
					d2 = arcnode.y;
				} else {
					try {
						double ad[] = solveQuadratic(a - arcnode.a, b
								- arcnode.b, c - arcnode.c);
						d2 = ad[0];
					} catch (Throwable _ex) {
						d2 = d1;
						System.out
								.println("*** error: No parabola intersection during ArcNode.paint() - SLine: "
										+ d
										+ ", "
										+ toString()
										+ " "
										+ arcnode.toString());
					}
				}
			}

			if (drawBeach) {
				int i = 1;
				double d4 = 0.0D;
				for (double d5 = d1; d5 < Math.min(Math.max(0.0D, d2),
						g.getClipBounds().height); d5 += i) {
					double d6 = d - F(d5);
					if (d5 > d1 && (d4 >= 0.0D || d6 >= 0.0D)) {
						g.drawLine((int) d4, (int) (d5 - (double) i), (int) d6,
								(int) d5);
					}
					d4 = d6;
				}
			}

			if (flag && startOfTrace != null) {
				double d7 = d - F(d2);
				double d8 = d2;
				g.getClipBounds();
				g.getClipBounds();
				g.drawLine((int) startOfTrace.x, (int) startOfTrace.y,
						(int) d7, (int) d8);
			}
		}

		if (next != null)
			next.paint(g, d, Math.max(0.0D, d2), flag, drawBeach);
	}

}
