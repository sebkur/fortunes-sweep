package fortune.sweep.gui;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fortune.sweep.Fortune;

public class Controls extends Panel implements ActionListener
{

	private static final long serialVersionUID = -8452143409724541737L;

	private Fortune fortune;

	private Canvas canvas;
	private Button buttons[];

	private static String PLAY = "Play";
	private static String PAUSE = "Pause";

	public Controls(Fortune fortune, Canvas canvas)
	{
		this.fortune = fortune;
		this.canvas = canvas;
		String as[] = { PLAY, "Next event", "Next pixel", "Clear", "Restart" };
		buttons = new Button[as.length];
		for (int i = 0; i < as.length; i++) {
			buttons[i] = new Button(as[i]);
			buttons[i].addActionListener(this);
			add(buttons[i]);
		}

		threadRunning(false);
	}

	public void actionPerformed(ActionEvent e)
	{
		String s = e.getActionCommand();
		if (e.getSource() == buttons[0]) {
			boolean running = fortune.toggleRunning();
			threadRunning(running);
		} else if (e.getSource() == buttons[1]) {
			canvas.step();
			return;
		} else if (e.getSource() == buttons[2]) {
			canvas.singlestep();
			return;
		} else if (e.getSource() == buttons[3]) {
			threadRunning(false);
			canvas.clear();
			return;
		} else if (e.getSource() == buttons[4]) {
			canvas.restart();
		}
	}

	public void threadRunning(boolean running)
	{
		if (running) {
			buttons[0].setLabel(PAUSE);
			buttons[2].setEnabled(false);
		} else {
			buttons[0].setLabel(PLAY);
			buttons[2].setEnabled(true);
		}
		buttons[0].invalidate();
		invalidate();
		validate();
	}
}
