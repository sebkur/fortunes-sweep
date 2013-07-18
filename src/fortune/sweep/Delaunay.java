package fortune.sweep;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;
import java.util.Vector;

public class Delaunay extends Vector<Paintable>
{

	private static final long serialVersionUID = -1644395346085708102L;

	public void paint(Graphics g)
	{
		for (int i = 0; i < size(); i++) {
			elementAt(i).paint(g);
		}

	}

}
