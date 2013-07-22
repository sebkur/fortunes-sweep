package fortune.sweep.gui.swing;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fortune.sweep.Algorithm;
import fortune.sweep.gui.Config;

public class SwingFortune extends JPanel implements Runnable
{

	private static final long serialVersionUID = 3917389635770683885L;

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Fortune's sweep");

		final SwingFortune fortune = new SwingFortune();
		fortune.init();
		frame.add(fortune);

		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private Algorithm algorithm;
	private Canvas canvas;
	private Controls controls;
	private Thread thread;

	public void init()
	{
		algorithm = new Algorithm();
		Config config = new Config();

		setLayout(new BorderLayout());
		canvas = new Canvas(algorithm, config, getWidth(), getHeight() - 50);
		add(new Settings(canvas, config), BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		add(controls = new Controls(this, algorithm), BorderLayout.SOUTH);

		algorithm.addWatcher(canvas);

		canvas.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e)
			{
				algorithm.setMaxX(canvas.getWidth());
				algorithm.setHeight(canvas.getHeight());
			}

		});
	}

	private boolean running = false;

	public boolean toggleRunning()
	{
		if (!running) {
			if (thread == null) {
				thread = new Thread(this);
				running = true;
				thread.start();
			} else {
				running = true;
				thread.resume();
			}
		} else {
			running = false;
			thread.suspend();
		}
		return running;
	}

	public void run()
	{
		if (thread != null) {
			do {
				while (algorithm.singlestep()) {
					try {
						Thread.sleep(25L);
					} catch (InterruptedException _ex) {
					}
				}
				controls.threadRunning(false);
			} while (true);
		} else {
			return;
		}
	}

}
