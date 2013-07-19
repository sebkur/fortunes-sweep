package fortune.sweep;

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
				for (int y = 30 + start_y; y < height; y += height / 2)
					sites.add(new Point(x, y));
			}
		}

		checkDegenerate();
	}

	public List<Point> getSites()
	{
		return sites;
	}

	public List<Edge> getEdges()
	{
		return edges;
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
