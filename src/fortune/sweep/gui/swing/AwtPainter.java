package fortune.sweep.gui.swing;

import java.awt.Graphics;

import fortune.sweep.geometry.Edge;
import fortune.sweep.geometry.Point;
import fortune.sweep.gui.Color;
import fortune.sweep.gui.Painter;

public class AwtPainter implements Painter
{

	private Graphics g;

	public AwtPainter(Graphics g)
	{
		this.g = g;
	}

	public void setGraphics(Graphics g)
	{
		this.g = g;
	}

	@Override
	public void setColor(Color color)
	{
		g.setColor(new java.awt.Color(color.getRGB()));
	}

	@Override
	public void fillRect(int x, int y, int width, int height)
	{
		g.fillRect(x, y, width, height);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2)
	{
		g.drawLine(x1, y1, x2, y2);
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
	public void drawCircle(double x, double y, double radius)
	{
		double diam = radius * 2;
		int d = (int) Math.round(diam);
		int px = (int) Math.round(x - radius);
		int py = (int) Math.round(y - radius);
		g.drawOval(px, py, d, d);
	}

	@Override
	public void fillCircle(double x, double y, double radius)
	{
		double diam = radius * 2;
		int d = (int) Math.round(diam);
		int px = (int) Math.round(x - radius);
		int py = (int) Math.round(y - radius);
		g.fillOval(px, py, d, d);
	}
}
