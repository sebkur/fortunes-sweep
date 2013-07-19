package fortune.sweep.paint;

import java.awt.Graphics;
import java.util.List;

import fortune.sweep.Delaunay;
import fortune.sweep.EventPoint;
import fortune.sweep.EventQueue;
import fortune.sweep.Voronoi;
import fortune.sweep.arc.ArcNode;
import fortune.sweep.arc.CirclePoint;
import fortune.sweep.arc.ParabolaPoint;
import fortune.sweep.geometry.Edge;
import fortune.sweep.geometry.Point;

public class AwtPainter implements Painter
{

	private Graphics g;

	public AwtPainter(Graphics g)
	{
		this.g = g;
	}

	@Override
	public void paint(Point p)
	{
		g.fillOval((int) (p.getX() - 3.0), (int) (p.getY() - 3.0), 7, 7);
	}

	@Override
	public void paint(Edge e)
	{
		Point p1 = e.getStart();
		Point p2 = e.getEnd();
		g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(),
				(int) p2.getY());
	}

	@Override
	public void paint(Delaunay d)
	{
		for (Edge e : d) {
			paint(e);
		}
	}

	@Override
	public void paint(CirclePoint p)
	{
		paint((Point) p);
		double d = p.getRadius();
		g.drawOval((int) (p.getX() - 2D * d), (int) (p.getY() - d),
				(int) (2D * d), (int) (2D * d));
	}

	@Override
	public void paint(ArcNode a, double d, double d1, boolean drawVoronoiLines,
			boolean drawBeach)
	{
		double d2 = g.getClipBounds().height;
		ArcNode arcnode = a.getNext();
		if (arcnode != null) {
			arcnode.init(d);
		}
		if (d == a.getX()) {
			double d3 = arcnode != null ? d - arcnode.f(a.getY()) : 0.0D;
			if (drawBeach)
				g.drawLine((int) d3, (int) a.getY(), (int) d, (int) a.getY());
			d2 = a.getY();
		} else {
			if (arcnode != null) {
				if (d == arcnode.getX()) {
					d2 = arcnode.getY();
				} else {
					try {
						double ad[] = ParabolaPoint.solveQuadratic(a.getA()
								- arcnode.getA(), a.getB() - arcnode.getB(),
								a.getC() - arcnode.getC());
						d2 = ad[0];
					} catch (Throwable _ex) {
						d2 = d1;
						System.out
								.println("*** error: No parabola intersection during ArcNode.paint() - SLine: "
										+ d
										+ ", "
										+ toString()
										+ " "
										+ arcnode.toString());
					}
				}
			}

			if (drawBeach) {
				int i = 1;
				double d4 = 0.0D;
				for (double d5 = d1; d5 < Math.min(Math.max(0.0D, d2),
						g.getClipBounds().height); d5 += i) {
					double d6 = d - a.f(d5);
					if (d5 > d1 && (d4 >= 0.0D || d6 >= 0.0D)) {
						g.drawLine((int) d4, (int) (d5 - (double) i), (int) d6,
								(int) d5);
					}
					d4 = d6;
				}
			}

			Point startOfTrace = a.getStartOfTrace();
			if (drawVoronoiLines && startOfTrace != null) {
				double d7 = d - a.f(d2);
				double d8 = d2;
				g.getClipBounds();
				g.getClipBounds();
				g.drawLine((int) startOfTrace.getX(),
						(int) startOfTrace.getY(), (int) d7, (int) d8);
			}
		}

		if (a.getNext() != null) {
			paint(a.getNext(), d, Math.max(0.0D, d2), drawVoronoiLines,
					drawBeach);
		}
	}

	@Override
	public void paint(EventQueue queue, boolean drawCircles)
	{
		for (EventPoint eventpoint = queue.getEvents(); eventpoint != null; eventpoint = eventpoint
				.getNext()) {
			if (drawCircles || !(eventpoint instanceof CirclePoint)) {
				if (eventpoint instanceof CirclePoint) {
					paint((CirclePoint) eventpoint);
				} else {
					paint(eventpoint);
				}
			}
		}
	}

	@Override
	public void paint(Voronoi v, boolean drawVoronoiLines)
	{
		List<Point> sites = v.getSites();
		List<Edge> edges = v.getEdges();

		for (int i = 0; i < sites.size(); i++) {
			paint(sites.get(i));
		}
		if (drawVoronoiLines) {
			for (int i = 0; i < edges.size(); i++) {
				paint(edges.get(i));
			}
		}
	}

}
