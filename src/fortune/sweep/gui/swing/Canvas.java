package fortune.sweep.gui.swing;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import fortune.sweep.Algorithm;
import fortune.sweep.AlgorithmWatcher;
import fortune.sweep.geometry.Point;
import fortune.sweep.gui.AlgorithmPainter;
import fortune.sweep.gui.Config;

public class Canvas extends JPanel implements AlgorithmWatcher
{

	private static final long serialVersionUID = 461591430129084653L;

	private Algorithm algorithm;
	private AlgorithmPainter algorithmPainter;
	private AwtPainter painter;

	public Canvas(Algorithm algorithm, Config config, int width, int height)
	{
		this.algorithm = algorithm;

		painter = new AwtPainter(null);
		algorithmPainter = new AlgorithmPainter(algorithm, config, painter);

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e)
			{
				Point point = new Point(e.getPoint().x, e.getPoint().y);
				if (point.getX() > (double) Canvas.this.algorithm.getSweepX()) {
					Canvas.this.algorithm.addSite(point);
					repaint();
				}
			}

		});
	}

	@Override
	public void update()
	{
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		painter.setGraphics(g);
		algorithmPainter.setWidth(getWidth());
		algorithmPainter.setHeight(getHeight());
		algorithmPainter.paint();
	}

}
