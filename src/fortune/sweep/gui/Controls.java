package fortune.sweep.gui;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import fortune.sweep.Fortune;

public class Controls extends Panel implements ActionListener
{

	private static final long serialVersionUID = -8452143409724541737L;

	private Fortune fortune;

	private Canvas canvas;

	private JButton buttons[];

	private static String TEXT_PLAY = "Play";
	private static String TEXT_PAUSE = "Pause";
	private static String TEXT_NEXT_EVENT = "Next event";
	private static String TEXT_NEXT_PIXEL = "Next pixel";
	private static String TEXT_CLEAR = "Clear";
	private static String TEXT_RESTART = "Restart";

	public Controls(Fortune fortune, Canvas canvas)
	{
		this.fortune = fortune;
		this.canvas = canvas;
		String as[] = { TEXT_PLAY, TEXT_NEXT_EVENT, TEXT_NEXT_PIXEL,
				TEXT_CLEAR, TEXT_RESTART };
		buttons = new JButton[as.length];
		for (int i = 0; i < as.length; i++) {
			buttons[i] = new JButton(as[i]);
			buttons[i].addActionListener(this);
			add(buttons[i]);
		}

		threadRunning(false);
	}

	public void actionPerformed(ActionEvent e)
	{
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
			buttons[0].setText(TEXT_PAUSE);
			buttons[2].setEnabled(false);
		} else {
			buttons[0].setText(TEXT_PLAY);
			buttons[2].setEnabled(true);
		}
		buttons[0].invalidate();
		invalidate();
		validate();
	}
}