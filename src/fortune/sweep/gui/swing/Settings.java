package fortune.sweep.gui.swing;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import fortune.sweep.gui.Config;

public class Settings extends JPanel implements ItemListener
{

	private static final long serialVersionUID = -6537449209660520005L;

	private Canvas canvas;
	private Config config;

	private JCheckBox[] boxes;

	private static final String TEXT_CIRCLES = "Circles";
	private static final String TEXT_BEACHLINE = "Beachline";
	private static final String TEXT_VORONOI = "Voronoi diagram";
	private static final String TEXT_DELAUNAY = "Delaunay triangulation";

	public Settings(Canvas canvas, Config config)
	{
		this.canvas = canvas;
		this.config = config;
		String as[] = { TEXT_CIRCLES, TEXT_BEACHLINE, TEXT_VORONOI,
				TEXT_DELAUNAY };

		boxes = new JCheckBox[as.length];
		for (int i = 0; i < as.length; i++) {
			boxes[i] = new JCheckBox(as[i]);
			boxes[i].addItemListener(this);
			add(boxes[i]);
		}

		boxes[0].setSelected(config.isDrawCircles());
		boxes[1].setSelected(config.isDrawBeach());
		boxes[2].setSelected(config.isDrawVoronoiLines());
		boxes[3].setSelected(config.isDrawDelaunay());
	}

	public void itemStateChanged(ItemEvent e)
	{
		JCheckBox box = (JCheckBox) e.getItem();
		String s = box.getText();
		boolean flag = box.isSelected();
		if (s.equals(TEXT_CIRCLES)) {
			config.setDrawCircles(flag);
		} else if (s.equals(TEXT_BEACHLINE)) {
			config.setDrawBeach(flag);
		} else if (s.equals(TEXT_VORONOI)) {
			config.setDrawVoronoiLines(flag);
		} else if (s.equals(TEXT_DELAUNAY)) {
			config.setDrawDelaunay(flag);
		}
		canvas.repaint();
	}

}
