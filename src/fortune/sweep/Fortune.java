package fortune.sweep;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Fortune extends JPanel implements Runnable
{

	private static final long serialVersionUID = 3917389635770683885L;

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();

		final Fortune fortune = new Fortune();
		fortune.init();
		frame.add(fortune);

		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fortune.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e)
			{
				// fortune.start();
			}

			@Override
			public void componentResized(ComponentEvent e)
			{

			}

			@Override
			public void componentMoved(ComponentEvent e)
			{

			}

			@Override
			public void componentHidden(ComponentEvent e)
			{

			}
		});
	}

	MyCanvas canvas;
	MyControls controls;
	Thread thread;

	public void init()
	{
		setLayout(new BorderLayout());
		canvas = new MyCanvas(getSize().width, getSize().height - 50, 85);
		add("North", new MySettings(canvas));
		add("Center", canvas);
		add("South", controls = new MyControls(this, canvas));
	}

	public void start()
	{
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		} else {
			thread.resume();
		}
	}

	public void run()
	{
		if (thread != null) {
			do {
				canvas.init();
				while (canvas.singlestep()) {
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

	public void suspend()
	{
		thread.suspend();
	}

	public void resume()
	{
		thread.resume();
	}
}
