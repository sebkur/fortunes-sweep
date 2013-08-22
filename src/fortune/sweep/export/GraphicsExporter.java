package fortune.sweep.export;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fortune.sweep.Algorithm;
import fortune.sweep.gui.AlgorithmPainter;
import fortune.sweep.gui.Config;
import fortune.sweep.gui.swing.AwtPainter;

public class GraphicsExporter
{

	public static void exportPNG(File file, Algorithm algorithm, int width,
			int height) throws IOException
	{
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D graphics = image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		AwtPainter painter = new AwtPainter(graphics);

		Config config = new Config();
		config.setDrawBeach(true);
		config.setDrawCircles(true);
		config.setDrawDelaunay(false);
		config.setDrawVoronoiLines(true);

		AlgorithmPainter algorithmPainter = new AlgorithmPainter(algorithm,
				config, painter);

		algorithmPainter.setWidth(width);
		algorithmPainter.setHeight(height);
		algorithmPainter.paint();

		ImageIO.write(image, "png", file);
	}

}
