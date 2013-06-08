package fortune.sweep.geometry;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;

import fortune.sweep.Paintable;

public class Point implements Paintable
{

	public volatile double x, y;

	public Point(double d, double d1)
	{
		x = d;
		y = d1;
	}

	public Point(Point point)
	{
		x = point.x;
		y = point.y;
	}

	public Point(java.awt.Point point)
	{
		x = point.x;
		y = point.y;
	}

	public void paint(Graphics g)
	{
		g.fillOval((int) (x - 3.0), (int) (y - 3.0), 7, 7);
	}

	public double distance(Point point)
	{
		double d = point.x - x;
		double d1 = point.y - y;
		return Math.sqrt(d * d + d1 * d1);
	}

}
