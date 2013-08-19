package fortune.sweep.gui.swing;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import fortune.sweep.Algorithm;

public class Controls extends Panel implements ActionListener
{

	private static final long serialVersionUID = -8452143409724541737L;

	private SwingFortune fortune;

	private Algorithm algorithm;

	private JButton buttons[];

	private static String TEXT_PLAY = "Play";
	private static String TEXT_PAUSE = "Pause";
	private static String TEXT_NEXT_EVENT = "Next event";
	private static String TEXT_PREV_PIXEL = "Previous pixel";
	private static String TEXT_NEXT_PIXEL = "Next pixel";
	private static String TEXT_CLEAR = "Clear";
	private static String TEXT_RESTART = "Restart";

	public Controls(SwingFortune fortune, Algorithm algorithm)
	{
		this.fortune = fortune;
		this.algorithm = algorithm;
		String as[] = { TEXT_PLAY, TEXT_NEXT_EVENT, TEXT_PREV_PIXEL,
				TEXT_NEXT_PIXEL, TEXT_CLEAR, TEXT_RESTART };
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
		int i = 0;
		if (e.getSource() == buttons[i++]) {
			boolean running = fortune.toggleRunning();
			threadRunning(running);
		} else if (e.getSource() == buttons[i++]) {
			algorithm.nextEvent();
			return;
		} else if (e.getSource() == buttons[i++]) {
			algorithm.previousPixel();
			return;
		} else if (e.getSource() == buttons[i++]) {
			algorithm.nextPixel();
			return;
		} else if (e.getSource() == buttons[i++]) {
			fortune.stopRunning();
			threadRunning(false);
			algorithm.clear();
			return;
		} else if (e.getSource() == buttons[i++]) {
			algorithm.restart();
		}
	}

	public void threadRunning(boolean running)
	{
		if (running) {
			buttons[0].setText(TEXT_PAUSE);
			buttons[2].setEnabled(false);
			buttons[3].setEnabled(false);
		} else {
			buttons[0].setText(TEXT_PLAY);
			buttons[2].setEnabled(true);
			buttons[3].setEnabled(true);
		}
		buttons[0].invalidate();
		invalidate();
		validate();
	}
}
