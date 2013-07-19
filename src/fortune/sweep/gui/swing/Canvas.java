package fortune.sweep.gui.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import fortune.sweep.Algorithm;
import fortune.sweep.AlgorithmWatcher;
import fortune.sweep.geometry.Point;
import fortune.sweep.gui.Painter;

public class Canvas extends JPanel implements AlgorithmWatcher
{

	private static final long serialVersionUID = 461591430129084653L;

	private Algorithm algorithm;

	private boolean drawCircles, drawBeach, drawVoronoiLines, drawDelaunay;

	public Canvas(Algorithm algorithm, int width, int height)
	{
		this.algorithm = algorithm;

		drawCircles = false;
		drawBeach = true;
		drawVoronoiLines = true;
		drawDelaunay = false;

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e)
			{
				Point point = new Point(e.getPoint().x, e.getPoint().y);
				if (point.getX() > (double) Canvas.this.algorithm.getPosX()) {
					Canvas.this.algorithm.addSite(point);
					repaint();
				}
			}

		});
	}

	public boolean isDrawCircles()
	{
		return drawCircles;
	}

	public void setDrawCircles(boolean drawCircles)
	{
		this.drawCircles = drawCircles;
	}

	public boolean isDrawBeach()
	{
		return drawBeach;
	}

	public void setDrawBeach(boolean drawBeach)
	{
		this.drawBeach = drawBeach;
	}

	public boolean isDrawVoronoiLines()
	{
		return drawVoronoiLines;
	}

	public void setDrawVoronoiLines(boolean drawVoronoiLines)
	{
		this.drawVoronoiLines = drawVoronoiLines;
	}

	public boolean isDrawDelaunay()
	{
		return drawDelaunay;
	}

	public void setDrawDelaunay(boolean drawDelaunay)
	{
		this.drawDelaunay = drawDelaunay;
	}

	public synchronized void paintComponent(Graphics g)
	{
		Painter drawer = new AwtPainter(g);

		g.setColor(Color.white);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		g.setColor(Color.blue);
		drawer.paint(algorithm.getVoronoi(), drawVoronoiLines);
		g.setColor(Color.red);
		g.drawLine(algorithm.getPosX(), 0, algorithm.getPosX(),
				getBounds().height);
		if (algorithm.getEventQueue() != null && algorithm.getArcs() != null) {
			g.setColor(Color.black);
			drawer.paint(algorithm.getEventQueue(), drawCircles);
			if (algorithm.getArcs().getArcs() != null) {
				algorithm.getArcs().getArcs().init(algorithm.getPosX());
				drawer.paint(algorithm.getArcs().getArcs(),
						algorithm.getPosX(), 0.0D, drawVoronoiLines, drawBeach);
			}
		}
		if (drawDelaunay) {
			g.setColor(Color.gray);
			drawer.paint(algorithm.getDelaunay());
		}
	}

	@Override
	public void update()
	{
		repaint();
	}

}
