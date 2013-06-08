package fortune.sweep;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;

class CirclePoint extends EventPoint
{

	double radius;
	ArcNode arc;

	CirclePoint(double d, double d1, ArcNode arcnode)
	{
		super(d, d1);
		arc = arcnode;
		radius = distance(arcnode);
		x += radius;
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		double d = radius;
		g.drawOval((int) (x - 2D * d), (int) (y - d), (int) (2D * d),
				(int) (2D * d));
	}

	public void action(MyCanvas mycanvas)
	{
		ArcNode arcnode = arc.prev;
		ArcNode arcnode1 = arc.next;
		MyPoint mypoint = new MyPoint(x - radius, y);
		arc.completeTrace(mycanvas, mypoint);
		arcnode.completeTrace(mycanvas, mypoint);
		arcnode.startOfTrace = mypoint;
		arcnode.next = arcnode1;
		arcnode1.prev = arcnode;
		if (arcnode.circlePoint != null) {
			mycanvas.events.remove(arcnode.circlePoint);
			arcnode.circlePoint = null;
		}
		if (arcnode1.circlePoint != null) {
			mycanvas.events.remove(arcnode1.circlePoint);
			arcnode1.circlePoint = null;
		}
		arcnode.checkCircle(mycanvas.events);
		arcnode1.checkCircle(mycanvas.events);
	}

}
