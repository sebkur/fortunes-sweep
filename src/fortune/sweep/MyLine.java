package fortune.sweep;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;

class MyLine implements Paintable
{

	MyPoint p1, p2;

	MyLine(MyPoint mypoint, MyPoint mypoint1)
	{
		p1 = mypoint;
		p2 = mypoint1;
	}

	public void paint(Graphics g)
	{
		g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
	}

}
