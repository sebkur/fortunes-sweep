package fortune.sweep.gui;

import java.util.List;

import fortune.sweep.Algorithm;
import fortune.sweep.Delaunay;
import fortune.sweep.EventPoint;
import fortune.sweep.EventQueue;
import fortune.sweep.Voronoi;
import fortune.sweep.arc.ArcNode;
import fortune.sweep.arc.CirclePoint;
import fortune.sweep.arc.ParabolaPoint;
import fortune.sweep.geometry.Edge;
import fortune.sweep.geometry.Point;

public class AlgorithmPainter
{

	private Algorithm algorithm;
	private Config config;
	private Painter painter;

	private int width, height;

	public AlgorithmPainter(Algorithm algorithm, Config config, Painter painter)
	{
		this.algorithm = algorithm;
		this.config = config;
		this.painter = painter;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public void setPainter(Painter painter)
	{
		this.painter = painter;
	}

	private int colorBackground = 0xffffff;
	private int colorSweepline = 0xff0000;

	private int colorSites = 0x000000;
	private int colorSitesVisited = 0x666666;
	private int colorCircleEventPoints = 0x00ff00;
	private int colorBeachlineIntersections = 0x00ff00;
	private int colorSpikes = 0x000000;
	private int colorSpikeIntersections = 0x0000ff;

	private int colorVornoiSegments = 0x0000ff;
	private int colorArcs = 0x000000;
	private int colorCircles = 0x000000;
	private int colorDelaunay = 0x999999;

	public void paint()
	{
		painter.setColor(new Color(colorBackground));
		painter.fillRect(0, 0, width, height);

		paintSitesAndEdges(algorithm.getVoronoi());

		painter.setColor(new Color(colorSweepline));
		painter.drawLine(algorithm.getSweepX(), 0, algorithm.getSweepX(),
				height);

		if (algorithm.getEventQueue() != null && algorithm.getArcs() != null) {
			paintEventQueue(algorithm.getEventQueue(), config.isDrawCircles());
			paintArcs(algorithm.getArcs().getArcs(), algorithm.getSweepX());
		}

		if (config.isDrawDelaunay()) {
			paintDelaunay(algorithm.getDelaunay());
		}
	}

	private void paintDelaunay(Delaunay d)
	{
		painter.setColor(new Color(colorDelaunay));
		for (Edge e : d) {
			painter.paint(e);
		}
	}

	private void paintSitesAndEdges(Voronoi v)
	{
		List<Point> sites = v.getSites();
		List<Edge> edges = v.getEdges();

		painter.setColor(new Color(colorSitesVisited));
		for (int i = 0; i < sites.size(); i++) {
			painter.paint(sites.get(i));
		}

		painter.setColor(new Color(colorVornoiSegments));
		if (config.isDrawVoronoiLines()) {
			for (int i = 0; i < edges.size(); i++) {
				painter.paint(edges.get(i));
			}
		}
	}

	private void paintEventQueue(EventQueue queue, boolean drawCircles)
	{
		for (EventPoint eventpoint = queue.getEvents(); eventpoint != null; eventpoint = eventpoint
				.getNext()) {
			if (drawCircles || !(eventpoint instanceof CirclePoint)) {
				if (eventpoint instanceof CirclePoint) {
					CirclePoint cp = (CirclePoint) eventpoint;

					painter.setColor(new Color(colorCircles));
					painter.drawCircle(cp.getX() - cp.getRadius(), cp.getY(),
							cp.getRadius());

					painter.setColor(new Color(colorCircleEventPoints));
					painter.paint(eventpoint);
				} else {
					painter.setColor(new Color(colorSites));
					painter.paint(eventpoint);
				}
			}
		}
	}

	private void paintArcs(ArcNode arcNode, double sweepX)
	{
		for (ArcNode current = arcNode; current != null; current = current
				.getNext()) {
			current.init(sweepX);
		}

		double y1 = 0.0;
		double y2 = height;

		for (ArcNode current = arcNode; current != null; current = current
				.getNext()) {
			ArcNode next = current.getNext();

			if (sweepX == current.getX()) {
				// spikes on site events
				if (config.isDrawBeach()) {
					paintSpike(sweepX, current, next);
				}
				y2 = current.getY();
			} else {
				if (next == null) {
					y2 = height;
				} else {
					if (sweepX == next.getX()) {
						y2 = next.getY();
					} else {
						try {
							double ad[] = ParabolaPoint.solveQuadratic(
									current.getA() - next.getA(),
									current.getB() - next.getB(),
									current.getC() - next.getC());
							y2 = ad[0];
						} catch (Exception e) {
							y2 = y1;
							System.out
									.println("*** error: No parabola intersection while painting arc - SLine: "
											+ sweepX
											+ ", "
											+ current.toString()
											+ " "
											+ next.toString());
						}
					}
				}

				if (config.isDrawBeach()) {
					paintBeachline(y1, y2, current, sweepX);
				}

				if (config.isDrawVoronoiLines()) {
					paintTraces(y2, current, sweepX);
				}

				if (config.isDrawBeach() || config.isDrawVoronoiLines()) {
					paintBeachlineIntersections(y2, current, sweepX);
				}
			}

			y1 = Math.max(0.0D, y2);
		}
	}

	private void paintSpike(double sweepX, ArcNode current, ArcNode next)
	{
		double beachlineX = next != null ? sweepX - next.f(current.getY())
				: 0.0D;
		painter.setColor(new Color(colorSpikes));
		painter.drawLine((int) beachlineX, (int) current.getY(), (int) sweepX,
				(int) current.getY());

		// snip debug: red dot where spike meets beachline
		painter.setColor(new Color(colorSpikeIntersections));
		painter.fillCircle((int) beachlineX, (int) current.getY(), 2.5);
		// snap debug
	}

	private void paintBeachline(double y1, double y2, ArcNode current,
			double sweepX)
	{
		painter.setColor(new Color(colorArcs));
		int i = 1;
		double d4 = 0.0D;
		for (double d5 = y1; d5 < Math.min(Math.max(0.0D, y2), height); d5 += i) {
			double d6 = sweepX - current.f(d5);
			if (d5 > y1 && (d4 >= 0.0D || d6 >= 0.0D)) {
				painter.drawLine((int) d4, (int) (d5 - (double) i), (int) d6,
						(int) d5);
			}
			d4 = d6;
		}
	}

	private void paintTraces(double y2, ArcNode current, double sweepX)
	{
		Point startOfTrace = current.getStartOfTrace();
		if (startOfTrace != null) {
			double beachX = sweepX - current.f(y2);
			double beachY = y2;
			painter.setColor(new Color(colorVornoiSegments));
			painter.drawLine((int) startOfTrace.getX(),
					(int) startOfTrace.getY(), (int) beachX, (int) beachY);
		}
	}

	private void paintBeachlineIntersections(double y2, ArcNode current,
			double sweepX)
	{
		Point startOfTrace = current.getStartOfTrace();
		if (startOfTrace != null) {
			double beachX = sweepX - current.f(y2);
			double beachY = y2;
			// snip debug: green dots where neighboring beachline arcs
			// intersect
			painter.setColor(new Color(colorBeachlineIntersections));
			painter.fillCircle((int) beachX, (int) beachY, 2.5);
			// snap debug
		}
	}

}
