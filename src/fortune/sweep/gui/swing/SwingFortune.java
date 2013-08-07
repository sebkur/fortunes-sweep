package fortune.sweep.gui.swing;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import fortune.sweep.Algorithm;
import fortune.sweep.gui.Config;

public class SwingFortune extends JFrame implements Runnable
{

	private static final long serialVersionUID = 3917389635770683885L;

	public static void main(String[] args)
	{
		new SwingFortune();
	}

	private Algorithm algorithm;
	private Canvas canvas;
	private Controls controls;

	private JPanel main;
	private JMenuBar menu;
	private Settings settings;

	private boolean running = false;
	private Thread thread;
	private Object wait = new Object();

	public SwingFortune()
	{
		super("Fortune's sweep");

		init();

		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init()
	{
		menu = new JMenuBar();
		
		JMenu menuFile = new JMenu("File");
		menu.add(menuFile);
		JMenuItem quit = new JMenuItem("Quit");
		menuFile.add(quit);
		
		JMenu menuHelp = new JMenu("Help");
		menu.add(menuHelp);
		JMenuItem about = new JMenuItem("About");
		menuHelp.add(about);

		setJMenuBar(menu);

		main = new JPanel();
		setContentPane(main);
		main.setLayout(new BorderLayout());

		/*
		 * canvas
		 */

		algorithm = new Algorithm();
		Config config = new Config();

		config.setDrawCircles(true);
		config.setDrawBeach(true);
		config.setDrawVoronoiLines(true);
		config.setDrawDelaunay(false);

		canvas = new Canvas(algorithm, config, getWidth(), getHeight() - 50);
		controls = new Controls(this, algorithm);
		settings = new Settings(canvas, config);
		
		main.add(settings, BorderLayout.NORTH);
		main.add(canvas, BorderLayout.CENTER);
		main.add(controls, BorderLayout.SOUTH);

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
