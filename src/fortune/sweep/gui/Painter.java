package fortune.sweep.gui;

import java.awt.Color;

import fortune.sweep.Delaunay;
import fortune.sweep.EventQueue;
import fortune.sweep.Voronoi;
import fortune.sweep.arc.ArcNode;
import fortune.sweep.arc.CirclePoint;
import fortune.sweep.geometry.Edge;
import fortune.sweep.geometry.Point;

public interface Painter
{
	public void setColor(Color color);

	public void fillRect(int x, int y, int width, int height);

	public void drawLine(int x1, int y1, int x2, int y2);

	public void paint(Point p);

	public void paint(Edge e);

	public void paint(Delaunay d);

	public void paint(CirclePoint p);

	public void paint(ArcNode a, double d, double d1, boolean drawVoronoiLines,
			boolean drawBeach);

	public void paint(EventQueue queue, boolean drawCircles);

	public void paint(Voronoi v, boolean drawVoronoiLines);

}
