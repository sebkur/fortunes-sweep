package fortune.sweep.geometry;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;

import fortune.sweep.Paintable;

public class Edge implements Paintable
{

	public Point p1, p2;

	public Edge(Point point, Point point1)
	{
		p1 = point;
		p2 = point1;
	}

	public void paint(Graphics g)
	{
		g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
	}

}
