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

public class MyControls extends Panel implements ActionListener
{

	private static final long serialVersionUID = -8452143409724541737L;

	private Fortune fortune;

	private Canvas canvas;
	private Button buttons[];

	public MyControls(Fortune fortune, Canvas canvas)
	{
		this.fortune = fortune;
		this.canvas = canvas;
		String as[] = { "Start", "Suspend", "Resume", "Next event",
				"Next pixel", "Clear", "Restart" };
		buttons = new Button[as.length];
		for (int i = 0; i < as.length; i++) {
			buttons[i] = new Button(as[i]);
			buttons[i].addActionListener(this);
			add(buttons[i]);
		}

		threadRunning(false);
	}

	public void actionPerformed(ActionEvent actionevent)
	{
		String s = actionevent.getActionCommand();
		if (s == "Start") {
			fortune.start();
			threadRunning(true);
		}
		if (s == "Suspend") {
			fortune.suspend();
			threadRunning(false);
			return;
		}
		if (s == "Resume") {
			fortune.resume();
			threadRunning(true);
			return;
		}
		if (s == "Next event") {
			canvas.step();
			return;
		}
		if (s == "Next pixel") {
			canvas.singlestep();
			return;
		}
		if (s == "Clear") {
			threadRunning(false);
			canvas.clear();
			return;
		}
		if (s == "Restart") {
			canvas.restart();
		}
	}

	public void threadRunning(boolean running)
	{
		if (running) {
			buttons[0].setEnabled(false);
			buttons[1].setEnabled(true);
			buttons[2].setEnabled(false);
			buttons[4].setEnabled(false);
		} else {
			buttons[0].setEnabled(true);
			buttons[1].setEnabled(false);
			buttons[2].setEnabled(true);
			buttons[4].setEnabled(true);
		}
	}
}
