package fortune.sweep.export;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fortune.sweep.Algorithm;

public class GraphicsExporter
{

	public static void exportPNG(File file, Algorithm algorithm, int width,
			int height) throws IOException
	{
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);

		// TODO implement this

		ImageIO.write(image, "png", file);
	}

}
