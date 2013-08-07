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

	private boolean running = false;
	private Thread thread;
	private Object wait = new Object();

	public void init()
	{
		algorithm = new Algorithm();
		Config config = new Config();

		config.setDrawCircles(true);
		config.setDrawBeach(true);
		config.setDrawVoronoiLines(true);
		config.setDrawDelaunay(false);

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

		thread = new Thread(this);
		thread.start();
	}

	public boolean toggleRunning()
	{
		if (running) {
			running = false;
		} else {
			if (!algorithm.isFinshed()) {
				running = true;
				synchronized (wait) {
					wait.notify();
				}
			}
		}
		return running;
	}

	public void stopRunning()
	{
		if (!running) {
			return;
		}
		running = false;
	}

	public void run()
	{
		while (true) {
			if (running) {
				boolean eventsLeft = algorithm.singlestep();
				if (!eventsLeft) {
					setPaused();
				}
				try {
					Thread.sleep(25L);
				} catch (InterruptedException ex) {
					// ignore
				}
			} else {
				setPaused();
			}
		}
	}

	private void setPaused()
	{
		running = false;
		controls.threadRunning(false);
		while (true) {
			try {
				synchronized (wait) {
					wait.wait();
				}
				controls.threadRunning(true);
				break;
			} catch (InterruptedException e) {
				continue;
			}
		}
	}

}
