package fortune.sweep.gui;

import fortune.sweep.Delaunay;
import fortune.sweep.EventQueue;
import fortune.sweep.Voronoi;
import fortune.sweep.arc.ArcNode;
import fortune.sweep.arc.CirclePoint;
import fortune.sweep.geometry.Edge;
import fortune.sweep.geometry.Point;

public interface Painter
{
	public void paint(Point p);

	public void paint(Edge e);

	public void paint(Delaunay d);

	public void paint(CirclePoint p);

	public void paint(ArcNode a, double d, double d1, boolean drawVoronoiLines,
			boolean drawBeach);

	public void paint(EventQueue queue, boolean drawCircles);

	public void paint(Voronoi v, boolean drawVoronoiLines);
}
