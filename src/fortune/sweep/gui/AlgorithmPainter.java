package fortune.sweep.gui;

import java.awt.Color;

import fortune.sweep.Algorithm;

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

	public void paint()
	{
		painter.setColor(Color.white);
		painter.fillRect(0, 0, width, height);
		painter.setColor(Color.blue);
		painter.paint(algorithm.getVoronoi(), config.isDrawVoronoiLines());
		painter.setColor(Color.red);
		painter.drawLine(algorithm.getPosX(), 0, algorithm.getPosX(), height);
		if (algorithm.getEventQueue() != null && algorithm.getArcs() != null) {
			painter.setColor(Color.black);
			painter.paint(algorithm.getEventQueue(), config.isDrawCircles());
			if (algorithm.getArcs().getArcs() != null) {
				algorithm.getArcs().getArcs().init(algorithm.getPosX());
				painter.paint(algorithm.getArcs().getArcs(),
						algorithm.getPosX(), 0.0D, config.isDrawVoronoiLines(),
						config.isDrawBeach());
			}
		}
		if (config.isDrawDelaunay()) {
			painter.setColor(Color.gray);
			painter.paint(algorithm.getDelaunay());
		}
	}

}
