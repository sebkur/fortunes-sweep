package fortune.sweep.arc;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;

import fortune.sweep.EventPoint;
import fortune.sweep.geometry.Point;
import fortune.sweep.gui.Canvas;

public class CirclePoint extends EventPoint
{

	double radius;
	ArcNode arc;

	public CirclePoint(double d, double d1, ArcNode arcnode)
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

	public void action(Canvas canvas)
	{
		ArcNode arcnode = arc.prev;
		ArcNode arcnode1 = arc.next;
		Point point = new Point(x - radius, y);
		arc.completeTrace(canvas, point);
		arcnode.completeTrace(canvas, point);
		arcnode.startOfTrace = point;
		arcnode.next = arcnode1;
		arcnode1.prev = arcnode;
		if (arcnode.circlePoint != null) {
			canvas.getEventQueue().remove(arcnode.circlePoint);
			arcnode.circlePoint = null;
		}
		if (arcnode1.circlePoint != null) {
			canvas.getEventQueue().remove(arcnode1.circlePoint);
			arcnode1.circlePoint = null;
		}
		arcnode.checkCircle(canvas.getEventQueue());
		arcnode1.checkCircle(canvas.getEventQueue());
	}

}
