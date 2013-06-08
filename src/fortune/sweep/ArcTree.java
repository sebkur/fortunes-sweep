package fortune.sweep;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;

import fortune.sweep.geometry.Point;
import fortune.sweep.gui.Canvas;

public class ArcTree
{

	ArcNode arcs;

	public void insert(Point point, double d, EventQueue eventqueue)
	{
		if (arcs == null) {
			arcs = new ArcNode(point);
			return;
		}
		try {
			ParabolaPoint parabolapoint = new ParabolaPoint(point);
			parabolapoint.init(d);
			arcs.init(d);
			arcs.insert(parabolapoint, d, eventqueue);
			return;
		} catch (Throwable _ex) {
			System.out
					.println("*** error: No parabola intersection during ArcTree.insert()");
		}
	}

	public void checkBounds(Canvas canvas, double d)
	{
		if (arcs != null) {
			arcs.init(d);
			arcs.checkBounds(canvas, d);
		}
	}

	public void paint(Graphics g, double d, boolean flag, boolean drawBeach)
	{
		if (arcs != null) {
			arcs.init(d);
			arcs.paint(g, d, 0.0D, flag, drawBeach);
		}
	}

}
