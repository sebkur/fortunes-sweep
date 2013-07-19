package fortune.sweep;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import fortune.sweep.geometry.Edge;
import fortune.sweep.geometry.Point;

public class Voronoi
{
	private List<Point> sites = new ArrayList<Point>();
	private List<Edge> edges = new ArrayList<Edge>();

	public Voronoi(int width, int height, int npoints)
	{
		if (npoints > 0) {
			boolean flag = false;
			for (int x = 30; x < width; x += width / 8) {
				int start_y = (flag ^= true) ? 0 : width / 16;
				// for(int y = 30 + start_y; y < height; y += width/8)
				for (int y = 30 + start_y; y < height; y += height / 2)
					sites.add(new Point(x, y));
			}

			// addElement(new MyPoint(10D, height / 2));
		}

		checkDegenerate();
	}

	public void checkDegenerate()
	{
		if (sites.size() > 1) {
			Point min = sites.get(0), next = min;
			for (int i = 1; i < sites.size(); i++) {
				Point element = sites.get(i);
				if (element.getX() <= min.getX()) {
					next = min;
					min = element;
				} else if (element.getX() <= min.getX()) {
					next = element;
				}
			}

			if (min.getX() == next.getX() && min != next) {
				min.setX(min.getX() - 1);
				System.out.println("Moved point: " + next.getX() + " -> "
						+ min.getX());
			}
		}
	}

	public void paint(Graphics g, boolean flag)
	{
		for (int i = 0; i < sites.size(); i++) {
			sites.get(i).paint(g);
		}
		if (flag) {
			for (int i = 0; i < edges.size(); i++) {
				edges.get(i).paint(g);
			}
		}
	}

	public void clear()
	{
		edges.clear();
	}

	public void addSite(Point site)
	{
		sites.add(site);
	}

	public Point getSite(int i)
	{
		return sites.get(i);
	}

	public int getNumberOfSites()
	{
		return sites.size();
	}

	public void addLine(Edge edge)
	{
		edges.add(edge);
	}
}
